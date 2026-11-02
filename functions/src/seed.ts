import { initializeApp } from "firebase-admin/app";
import { getFirestore } from "firebase-admin/firestore";
import { writeGiftCatalog } from "./catalog";
import { APP_SETTINGS } from "./settings";

initializeApp({ projectId: "larki-se-dosti" });

async function main(): Promise<void> {
  const host = process.env.FIRESTORE_EMULATOR_HOST;
  console.log(host ? `Seeding the emulator at ${host}` : "Seeding the live larki-se-dosti database");
  const db = getFirestore();
  await writeGiftCatalog(db);
  await db.doc("config/app").set(APP_SETTINGS, { merge: true });
  console.log("Gift catalog is ready: rose 5, heart 10, star 15, bouquet 25, diamond 50, crown 100.");
  console.log("App settings: 10 free messages, then 2 coins each, and a video can return 3 free messages.");
}

main().catch((error: unknown) => {
  console.error(error);
  process.exit(1);
});
