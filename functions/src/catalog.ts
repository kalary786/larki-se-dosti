import { Firestore } from "firebase-admin/firestore";

export const GIFT_CATALOG = [
  { id: "rose", name: "Rose", emoji: "🌹", price: 5, sortOrder: 1 },
  { id: "heart", name: "Heart", emoji: "❤️", price: 10, sortOrder: 2 },
  { id: "star", name: "Star", emoji: "⭐", price: 15, sortOrder: 3 },
  { id: "bouquet", name: "Bouquet", emoji: "💐", price: 25, sortOrder: 4 },
  { id: "diamond", name: "Diamond", emoji: "💎", price: 50, sortOrder: 5 },
  { id: "crown", name: "Crown", emoji: "👑", price: 100, sortOrder: 6 },
] as const;

export async function writeGiftCatalog(db: Firestore): Promise<void> {
  const batch = db.batch();
  for (const gift of GIFT_CATALOG) {
    batch.set(
      db.collection("gifts").doc(gift.id),
      {
        name: gift.name,
        emoji: gift.emoji,
        price: gift.price,
        sortOrder: gift.sortOrder,
        active: true,
      },
      { merge: true },
    );
  }
  await batch.commit();
}
