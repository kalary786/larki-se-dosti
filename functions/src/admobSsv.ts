import { createVerify } from "crypto";
import { Firestore } from "firebase-admin/firestore";
import { appSettingsRef, parseAppSettings } from "./settings";

type CallbackRequest = { url?: string; originalUrl?: string };

const KEYS_URL = "https://www.gstatic.com/admob/reward/verifier-keys.json";
const DAILY_REWARD_CAP = 5;

type KeyCache = { at: number; keys: Map<string, string> };
let keyCache: KeyCache | null = null;

function queryString(url: string): string {
  const mark = url.indexOf("?");
  return mark >= 0 ? url.slice(mark + 1) : "";
}

function callbackParts(query: string): { data: string; signature: string; keyId: string } | null {
  const signatureAt = query.lastIndexOf("&signature=");
  const keyAt = query.lastIndexOf("&key_id=");
  if (signatureAt < 0 || keyAt < signatureAt) return null;
  const data = query.slice(0, signatureAt);
  const signature = decodeURIComponent(query.slice(signatureAt + "&signature=".length, keyAt));
  const keyId = decodeURIComponent(query.slice(keyAt + "&key_id=".length));
  if (!data || !signature || !keyId) return null;
  return { data, signature, keyId };
}

async function publicKey(keyId: string): Promise<string | null> {
  if (!keyCache || Date.now() - keyCache.at > 60 * 60 * 1000) {
    const response = await fetch(KEYS_URL);
    if (!response.ok) return null;
    const body = (await response.json()) as { keys?: { keyId?: number; pem?: string }[] };
    const keys = new Map<string, string>();
    for (const key of body.keys ?? []) {
      if (key.keyId != null && key.pem) keys.set(String(key.keyId), key.pem);
    }
    keyCache = { at: Date.now(), keys };
  }
  return keyCache.keys.get(keyId) ?? null;
}

function signatureMatches(data: string, signature: string, pem: string): boolean {
  const verifier = createVerify("SHA256");
  verifier.update(data);
  const normalized = signature.replace(/-/g, "+").replace(/_/g, "/");
  try {
    return verifier.verify(pem, normalized, "base64");
  } catch {
    return false;
  }
}

function dayInKarachi(now: Date): string {
  return new Intl.DateTimeFormat("en-CA", {
    timeZone: "Asia/Karachi",
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
  }).format(now);
}

function parameter(query: string, name: string): string {
  const parts = query.split("&");
  const prefix = `${name}=`;
  const found = parts.find((part) => part.startsWith(prefix));
  return found ? decodeURIComponent(found.slice(prefix.length)) : "";
}

export async function handleAdmobReward(db: Firestore, request: CallbackRequest): Promise<number> {
  const query = queryString(request.originalUrl || request.url || "");
  const parts = callbackParts(query);
  if (!parts) return 403;
  const pem = await publicKey(parts.keyId);
  if (!pem || !signatureMatches(parts.data, parts.signature, pem)) return 403;

  const customData = parameter(query, "custom_data");
  const userId = parameter(query, "user_id");
  const transactionId = parameter(query, "transaction_id");
  if (!transactionId || transactionId.length > 128) return 400;
  if (customData !== "messages" || !userId || userId.length > 128) return 200;

  await db.runTransaction(async (tx) => {
    const rewardRef = db.collection("adRewards").doc(transactionId);
    const userRef = db.collection("users").doc(userId);
    const rateRef = userRef.collection("rateLimits").doc("messageRewards");
    const [rewardSnap, settingsSnap, userSnap, rateSnap] = await Promise.all([
      tx.get(rewardRef),
      tx.get(appSettingsRef(db)),
      tx.get(userRef),
      tx.get(rateRef),
    ]);
    if (rewardSnap.exists) return;
    const settings = parseAppSettings(settingsSnap);
    const today = dayInKarachi(new Date());
    let count = 0;
    if (rateSnap.exists && rateSnap.get("day") === today) {
      const previous = rateSnap.get("count");
      count = typeof previous === "number" && Number.isInteger(previous) ? previous : 0;
    }
    const capped = count >= DAILY_REWARD_CAP || !settings || !userSnap.exists;
    if (!capped && settings) {
      const usedRaw = userSnap.get("freeMessagesUsed");
      const used = typeof usedRaw === "number" && Number.isInteger(usedRaw) && usedRaw > 0 ? usedRaw : 0;
      const next = Math.max(0, used - settings.rewardAdFreeMessages);
      tx.update(userRef, { freeMessagesUsed: next });
      tx.set(rateRef, { day: today, count: count + 1 });
    }
    tx.set(rewardRef, {
      userId,
      customData,
      granted: !capped,
      createdAt: new Date(),
    });
  });
  return 200;
}
