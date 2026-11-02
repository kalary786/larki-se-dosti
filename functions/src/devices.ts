import { createHash } from "crypto";
import { DocumentSnapshot, FieldValue, Firestore, Transaction } from "firebase-admin/firestore";
import { HttpsError } from "firebase-functions/v2/https";
import { SIGNUP_BONUS, addCoins } from "./coins";
import { appSettingsRef, parseAppSettings } from "./settings";

export function deviceIdHash(deviceId: string): string {
  return createHash("sha256").update(deviceId).digest("hex");
}

export function normalizeDeviceId(value: unknown): string {
  if (typeof value !== "string") return "";
  const clean = value.trim().toLowerCase();
  return /^[0-9a-f]{8,64}$/.test(clean) ? clean : "";
}

function whole(value: unknown): number {
  return typeof value === "number" && Number.isInteger(value) && value >= 0 ? value : 0;
}

async function applyWelcome(
  db: Firestore,
  tx: Transaction,
  uid: string,
  firstOnDevice: boolean,
  userSnap: DocumentSnapshot,
  bonusSnap: DocumentSnapshot,
  settingsSnap: DocumentSnapshot,
): Promise<void> {
  if (!userSnap.exists || userSnap.get("onboardingCompleted") !== true) return;
  const settings = parseAppSettings(settingsSnap);
  if (!settings) throw new HttpsError("failed-precondition", "config_missing");
  const userRef = db.collection("users").doc(uid);
  if (!firstOnDevice) {
    const used = whole(userSnap.get("freeMessagesUsed"));
    if (used < settings.freeMessages) {
      tx.update(userRef, { freeMessagesUsed: settings.freeMessages });
    }
    return;
  }
  if (bonusSnap.exists) return;
  await addCoins(db, tx, uid, SIGNUP_BONUS, "signup_bonus", uid, "signup_bonus");
}

export async function claimDevice(db: Firestore, uid: string, deviceId: string): Promise<boolean> {
  const hash = deviceIdHash(deviceId);
  return db.runTransaction(async (tx) => {
    const deviceRef = db.collection("devices").doc(hash);
    const linkRef = db.collection("deviceLinks").doc(uid);
    const userRef = db.collection("users").doc(uid);
    const bonusRef = userRef.collection("coinLedger").doc("signup_bonus");
    const [deviceSnap, userSnap, bonusSnap, settingsSnap] = await Promise.all([
      tx.get(deviceRef),
      tx.get(userRef),
      tx.get(bonusRef),
      tx.get(appSettingsRef(db)),
    ]);
    const firstOnDevice = !deviceSnap.exists || deviceSnap.get("uid") === uid;
    const profileReady = userSnap.exists && userSnap.get("onboardingCompleted") === true;
    if (profileReady) {
      await applyWelcome(db, tx, uid, firstOnDevice, userSnap, bonusSnap, settingsSnap);
    }
    if (!deviceSnap.exists) {
      tx.set(deviceRef, { uid, createdAt: FieldValue.serverTimestamp() });
    }
    tx.set(
      linkRef,
      {
        deviceIdHash: hash,
        firstOnDevice,
        ...(profileReady ? { signupClosed: true } : {}),
      },
      { merge: true },
    );
    return firstOnDevice;
  });
}

export async function applyStoredDevicePolicy(db: Firestore, uid: string): Promise<void> {
  await db.runTransaction(async (tx) => {
    const linkRef = db.collection("deviceLinks").doc(uid);
    const userRef = db.collection("users").doc(uid);
    const bonusRef = userRef.collection("coinLedger").doc("signup_bonus");
    const [linkSnap, userSnap, bonusSnap, settingsSnap] = await Promise.all([
      tx.get(linkRef),
      tx.get(userRef),
      tx.get(bonusRef),
      tx.get(appSettingsRef(db)),
    ]);
    if (!linkSnap.exists || !userSnap.exists) return;
    const firstOnDevice = linkSnap.get("firstOnDevice") === true;
    await applyWelcome(db, tx, uid, firstOnDevice, userSnap, bonusSnap, settingsSnap);
    tx.set(linkRef, { signupClosed: true }, { merge: true });
  });
}
