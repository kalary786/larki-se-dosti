import { FieldValue, Firestore, Transaction } from "firebase-admin/firestore";
import { HttpsError } from "firebase-functions/v2/https";

export const COIN_TYPES = [
  "purchase",
  "gift_sent",
  "reward_ad",
  "call",
  "signup_bonus",
  "message",
  "admin",
] as const;

export type CoinType = (typeof COIN_TYPES)[number];

export const SIGNUP_BONUS = 50;
export const CALL_COST = 10;

/**
 * Changes a balance inside an existing transaction and writes the ledger row.
 * Every coin change in this project goes through here. The balance cannot go below 0.
 */
export async function addCoins(
  db: Firestore,
  tx: Transaction,
  uid: string,
  amount: number,
  type: CoinType,
  refId: string,
  entryId?: string,
  extra: Record<string, unknown> = {},
): Promise<number> {
  if (!Number.isInteger(amount) || amount === 0) {
    throw new HttpsError("invalid-argument", "bad_amount");
  }
  const userRef = db.collection("users").doc(uid);
  const snap = await tx.get(userRef);
  if (!snap.exists) {
    throw new HttpsError("not-found", "user_not_found");
  }
  const currentRaw = snap.get("coins");
  const current = typeof currentRaw === "number" ? currentRaw : 0;
  if (!Number.isInteger(current)) {
    throw new HttpsError("failed-precondition", "bad_balance");
  }
  const next = current + amount;
  if (next < 0) {
    throw new HttpsError("failed-precondition", "insufficient_coins");
  }
  const entryRef = entryId
    ? userRef.collection("coinLedger").doc(entryId)
    : userRef.collection("coinLedger").doc();
  tx.update(userRef, { coins: next, ...extra });
  tx.set(entryRef, {
    type,
    amount,
    balanceAfter: next,
    refId,
    createdAt: FieldValue.serverTimestamp(),
  });
  return next;
}
