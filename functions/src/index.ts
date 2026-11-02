import { initializeApp } from "firebase-admin/app";
import { FieldValue, getFirestore } from "firebase-admin/firestore";
import { setGlobalOptions } from "firebase-functions/v2";
import { onDocumentCreated } from "firebase-functions/v2/firestore";
import { HttpsError, onCall, onRequest } from "firebase-functions/v2/https";
import { handleAdmobReward } from "./admobSsv";
import { writeGiftCatalog } from "./catalog";
import { CALL_COST, addCoins } from "./coins";
import { applyStoredDevicePolicy, claimDevice, normalizeDeviceId } from "./devices";
import { appSettingsRef, parseAppSettings } from "./settings";
import { deleteTestUsers, seedTestUsers } from "./testUsers";

initializeApp();
const db = getFirestore();

const callable = { region: "asia-south1" as const, enforceAppCheck: true };

setGlobalOptions({ region: "asia-south1", maxInstances: 10, enforceAppCheck: true });

function requireUid(uid: string | undefined): string {
  if (!uid) throw new HttpsError("unauthenticated", "sign_in_required");
  return uid;
}

function text(value: unknown, max: number): string {
  if (typeof value !== "string") return "";
  const clean = value.trim();
  if (clean.length === 0 || clean.length > max) return "";
  return clean;
}

function chatIdFor(a: string, b: string): string {
  return [a, b].sort().join("_");
}

export const ensureGiftCatalog = onCall(callable, async (request) => {
  requireUid(request.auth?.uid);
  await writeGiftCatalog(db);
  return { ok: true };
});

export const sendGift = onCall(callable, async (request) => {
  const uid = requireUid(request.auth?.uid);
  const receiverUid = text(request.data?.receiverUid, 128);
  const giftId = text(request.data?.giftId, 40);
  const chatId = text(request.data?.chatId, 280);
  if (!receiverUid || !giftId || !chatId) {
    throw new HttpsError("invalid-argument", "bad_request");
  }
  if (uid === receiverUid) {
    throw new HttpsError("invalid-argument", "same_user");
  }
  if (chatId !== chatIdFor(uid, receiverUid)) {
    throw new HttpsError("invalid-argument", "bad_chat");
  }

  const balance = await db.runTransaction(async (tx) => {
    const senderRef = db.collection("users").doc(uid);
    const receiverRef = db.collection("users").doc(receiverUid);
    const giftRef = db.collection("gifts").doc(giftId);
    const blockedBySender = db.collection("users").doc(uid).collection("blocks").doc(receiverUid);
    const blockedByReceiver = db.collection("users").doc(receiverUid).collection("blocks").doc(uid);
    const rateRef = db.collection("users").doc(uid).collection("rateLimits").doc("gifts");

    const [giftSnap, receiverSnap, senderBlock, receiverBlock, rateSnap] = await Promise.all([
      tx.get(giftRef),
      tx.get(receiverRef),
      tx.get(blockedBySender),
      tx.get(blockedByReceiver),
      tx.get(rateRef),
    ]);

    if (!receiverSnap.exists) throw new HttpsError("not-found", "user_not_found");
    if (senderBlock.exists || receiverBlock.exists) {
      throw new HttpsError("permission-denied", "blocked");
    }
    if (!giftSnap.exists || giftSnap.get("active") !== true) {
      throw new HttpsError("not-found", "gift_not_found");
    }
    const price = giftSnap.get("price");
    const name = giftSnap.get("name");
    const emoji = giftSnap.get("emoji");
    if (typeof price !== "number" || !Number.isInteger(price) || price <= 0) {
      throw new HttpsError("failed-precondition", "gift_unavailable");
    }
    if (typeof name !== "string" || typeof emoji !== "string") {
      throw new HttpsError("failed-precondition", "gift_unavailable");
    }

    const now = Date.now();
    let windowStart = now;
    let count = 0;
    if (rateSnap.exists) {
      const start = rateSnap.get("windowStart");
      const previous = rateSnap.get("count");
      if (typeof start === "number" && typeof previous === "number" && now - start < 60_000) {
        windowStart = start;
        count = previous;
      }
    }
    if (count >= 20) throw new HttpsError("resource-exhausted", "rate_limited");

    const next = await addCoins(
      db,
      tx,
      uid,
      -price,
      "gift_sent",
      giftId,
      undefined,
      { giftsSentCount: FieldValue.increment(1) },
    );
    tx.update(receiverRef, { giftsReceivedCount: FieldValue.increment(1) });
    tx.set(rateRef, { windowStart, count: count + 1 });
    tx.set(
      db.collection("chats").doc(chatId),
      {
        members: [uid, receiverUid].sort(),
        updatedAt: FieldValue.serverTimestamp(),
      },
      { merge: true },
    );
    tx.set(db.collection("chats").doc(chatId).collection("messages").doc(), {
      type: "gift",
      giftId,
      emoji,
      name,
      price,
      senderUid: uid,
      receiverUid,
      createdAt: FieldValue.serverTimestamp(),
    });
    return next;
  });

  return { balance };
});

function messageBody(value: unknown): string {
  if (typeof value !== "string") throw new HttpsError("invalid-argument", "bad_request");
  const clean = value.trim();
  if (clean.length === 0 || clean.length > 1000) {
    throw new HttpsError("invalid-argument", "bad_request");
  }
  return clean;
}

function wholeNumber(value: unknown): number {
  return typeof value === "number" && Number.isInteger(value) && value >= 0 ? value : 0;
}

export const sendMessage = onCall(callable, async (request) => {
  const uid = requireUid(request.auth?.uid);
  const receiverUid = text(request.data?.receiverUid, 128);
  const chatId = text(request.data?.chatId, 280);
  const messageId = text(request.data?.messageId, 40);
  const body = messageBody(request.data?.text);
  if (!receiverUid || !chatId || !/^[A-Za-z0-9]{16,40}$/.test(messageId)) {
    throw new HttpsError("invalid-argument", "bad_request");
  }
  if (uid === receiverUid) throw new HttpsError("invalid-argument", "same_user");
  if (chatId !== chatIdFor(uid, receiverUid)) throw new HttpsError("invalid-argument", "bad_chat");

  return db.runTransaction(async (tx) => {
    const senderRef = db.collection("users").doc(uid);
    const receiverRef = db.collection("users").doc(receiverUid);
    const chatRef = db.collection("chats").doc(chatId);
    const messageRef = chatRef.collection("messages").doc(messageId);
    const rateRef = senderRef.collection("rateLimits").doc("messages");
    const linkRef = db.collection("deviceLinks").doc(uid);
    const [settingsSnap, senderSnap, receiverSnap, senderBlock, receiverBlock, rateSnap, chatSnap, existing, linkSnap] =
      await Promise.all([
        tx.get(appSettingsRef(db)),
        tx.get(senderRef),
        tx.get(receiverRef),
        tx.get(senderRef.collection("blocks").doc(receiverUid)),
        tx.get(receiverRef.collection("blocks").doc(uid)),
        tx.get(rateRef),
        tx.get(chatRef),
        tx.get(messageRef),
        tx.get(linkRef),
      ]);

    const settings = parseAppSettings(settingsSnap);
    if (!settings) throw new HttpsError("failed-precondition", "config_missing");
    if (!senderSnap.exists) throw new HttpsError("not-found", "user_not_found");
    if (!receiverSnap.exists) throw new HttpsError("not-found", "user_not_found");
    if (senderBlock.exists || receiverBlock.exists) {
      throw new HttpsError("permission-denied", "blocked");
    }

    const used = wholeNumber(senderSnap.get("freeMessagesUsed"));
    const coinsNow = wholeNumber(senderSnap.get("coins"));
    const firstOnDevice = linkSnap.exists && linkSnap.get("firstOnDevice") === true;
    const allowanceReady = firstOnDevice || (linkSnap.exists && linkSnap.get("signupClosed") === true);
    const allowance = allowanceReady ? settings.freeMessages : 0;
    if (existing.exists) {
      if (existing.get("senderUid") !== uid) throw new HttpsError("permission-denied", "bad_message");
      return { freeMessagesLeft: Math.max(0, allowance - used), coins: coinsNow };
    }

    const now = Date.now();
    let windowStart = now;
    let count = 0;
    if (rateSnap.exists) {
      const start = rateSnap.get("windowStart");
      const previous = rateSnap.get("count");
      if (typeof start === "number" && typeof previous === "number" && now - start < 60_000) {
        windowStart = start;
        count = previous;
      }
    }
    if (count >= 30) throw new HttpsError("resource-exhausted", "rate_limited");

    let coins = coinsNow;
    if (used < allowance) {
      tx.update(senderRef, { freeMessagesUsed: used + 1 });
    } else if (coinsNow >= settings.messageCost) {
      coins = await addCoins(db, tx, uid, -settings.messageCost, "message", messageId);
    } else {
      throw new HttpsError("failed-precondition", "no_free_messages");
    }

    const unreadRaw = chatSnap.get("unread");
    const unread: Record<string, number> = {};
    if (unreadRaw && typeof unreadRaw === "object") {
      for (const [key, value] of Object.entries(unreadRaw as Record<string, unknown>)) {
        if (typeof value === "number" && Number.isInteger(value) && value >= 0) unread[key] = value;
      }
    }
    unread[receiverUid] = (unread[receiverUid] ?? 0) + 1;

    tx.set(rateRef, { windowStart, count: count + 1 });
    tx.set(messageRef, {
      type: "text",
      text: body,
      senderUid: uid,
      receiverUid,
      createdAt: FieldValue.serverTimestamp(),
    });
    tx.set(
      chatRef,
      {
        members: [uid, receiverUid].sort(),
        lastMessage: body,
        lastMessageAt: FieldValue.serverTimestamp(),
        updatedAt: FieldValue.serverTimestamp(),
        unread,
      },
      { merge: true },
    );

    const freeMessagesLeft = used < allowance
      ? allowance - (used + 1)
      : Math.max(0, allowance - used);
    return { freeMessagesLeft, coins };
  });
});

export const admobSsv = onRequest({ region: "asia-south1", invoker: "public" }, async (request, response) => {
  if (request.method !== "GET") {
    response.status(405).send("method");
    return;
  }
  const status = await handleAdmobReward(db, request);
  response.status(status).send(status === 200 ? "ok" : "rejected");
});

export const registerDevice = onCall(callable, async (request) => {
  const uid = requireUid(request.auth?.uid);
  const deviceId = normalizeDeviceId(request.data?.deviceId);
  if (!deviceId) throw new HttpsError("invalid-argument", "bad_device");
  const firstOnDevice = await claimDevice(db, uid, deviceId);
  return { firstOnDevice };
});

export const chargeCall = onCall(callable, async (request) => {
  const uid = requireUid(request.auth?.uid);
  const otherUid = text(request.data?.otherUid, 128);
  if (!otherUid || otherUid === uid) {
    throw new HttpsError("invalid-argument", "bad_call");
  }
  const balance = await db.runTransaction(async (tx) => {
    return addCoins(db, tx, uid, -CALL_COST, "call", otherUid);
  });
  return { balance };
});

export const seedDiscoverUsers = onCall(callable, async (request) => {
  requireUid(request.auth?.uid);
  const created = await seedTestUsers(db);
  return { created };
});

export const deleteDiscoverUsers = onCall(callable, async (request) => {
  requireUid(request.auth?.uid);
  const deleted = await deleteTestUsers(db);
  return { deleted };
});

export const grantSignupBonus = onDocumentCreated(
  { document: "users/{uid}", region: "asia-south1" },
  async (event) => {
    const created = event.data?.data();
    const uid = event.params.uid;
    if (!created || created.onboardingCompleted !== true || created.isTestUser === true || !uid) return;
    await applyStoredDevicePolicy(db, uid);
  },
);
