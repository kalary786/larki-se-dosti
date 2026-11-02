import { DocumentSnapshot, Firestore } from "firebase-admin/firestore";

export type AppSettings = {
  freeMessages: number;
  messageCost: number;
  rewardAdFreeMessages: number;
};

function positiveInteger(value: unknown): number | null {
  return typeof value === "number" && Number.isInteger(value) && value > 0 ? value : null;
}

export function parseAppSettings(snap: DocumentSnapshot): AppSettings | null {
  if (!snap.exists) return null;
  const freeMessages = positiveInteger(snap.get("freeMessages"));
  const messageCost = positiveInteger(snap.get("messageCost"));
  const rewardAdFreeMessages = positiveInteger(snap.get("rewardAdFreeMessages"));
  if (freeMessages == null || messageCost == null || rewardAdFreeMessages == null) return null;
  return { freeMessages, messageCost, rewardAdFreeMessages };
}

export function appSettingsRef(db: Firestore) {
  return db.collection("config").doc("app");
}

export const APP_SETTINGS = {
  freeMessages: 10,
  messageCost: 2,
  rewardAdFreeMessages: 3,
} as const;
