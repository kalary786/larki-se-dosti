import { Firestore } from "firebase-admin/firestore";

type TestPerson = {
  id: string;
  username: string;
  displayName: string;
  gender: "Female" | "Male";
  country: string;
  dateOfBirth: string;
  online: boolean;
  hoursAgo: number;
};

const TEST_PEOPLE: TestPerson[] = [
  { id: "test-user-01", username: "test_sara", displayName: "Sara", gender: "Female", country: "Pakistan", dateOfBirth: "2002-04-12", online: true, hoursAgo: 0 },
  { id: "test-user-02", username: "test_hina", displayName: "Hina", gender: "Female", country: "Pakistan", dateOfBirth: "2001-09-03", online: false, hoursAgo: 2 },
  { id: "test-user-03", username: "test_ayesha", displayName: "Ayesha", gender: "Female", country: "United Arab Emirates", dateOfBirth: "2000-01-18", online: true, hoursAgo: 0 },
  { id: "test-user-04", username: "test_omar", displayName: "Omar", gender: "Male", country: "Pakistan", dateOfBirth: "1999-06-21", online: true, hoursAgo: 0 },
  { id: "test-user-05", username: "test_bilal", displayName: "Bilal", gender: "Male", country: "Saudi Arabia", dateOfBirth: "1998-11-09", online: false, hoursAgo: 5 },
  { id: "test-user-06", username: "test_maya", displayName: "Maya", gender: "Female", country: "United Kingdom", dateOfBirth: "2003-02-14", online: true, hoursAgo: 0 },
  { id: "test-user-07", username: "test_leo", displayName: "Leo", gender: "Male", country: "United States", dateOfBirth: "2001-07-30", online: false, hoursAgo: 26 },
  { id: "test-user-08", username: "test_noor", displayName: "Noor", gender: "Female", country: "Bangladesh", dateOfBirth: "2002-12-01", online: true, hoursAgo: 0 },
  { id: "test-user-09", username: "test_yusuf", displayName: "Yusuf", gender: "Male", country: "Turkey", dateOfBirth: "1997-03-08", online: false, hoursAgo: 1 },
  { id: "test-user-10", username: "test_zara", displayName: "Zara", gender: "Female", country: "Malaysia", dateOfBirth: "2004-05-19", online: true, hoursAgo: 0 },
];

export async function seedTestUsers(db: Firestore): Promise<number> {
  const now = Date.now();
  const batch = db.batch();
  TEST_PEOPLE.forEach((person, index) => {
    const ref = db.collection("users").doc(person.id);
    batch.set(ref, {
      uid: person.id,
      username: person.username,
      displayName: person.displayName,
      shortId: 9_000_001 + index,
      dateOfBirth: person.dateOfBirth,
      gender: person.gender,
      country: person.country,
      photos: [],
      coins: 0,
      giftsSentCount: 0,
      giftsReceivedCount: 0,
      freeMessagesUsed: 0,
      level: 1,
      isOnline: person.online,
      lastSeen: now - person.hoursAgo * 60 * 60 * 1000,
      createdAt: now,
      onboardingCompleted: true,
      isGoogleLinked: false,
      isTestUser: true,
    });
  });
  await batch.commit();
  return TEST_PEOPLE.length;
}

export async function deleteTestUsers(db: Firestore): Promise<number> {
  const refs = TEST_PEOPLE.map((person) => db.collection("users").doc(person.id));
  const snaps = await db.getAll(...refs);
  const batch = db.batch();
  let deleted = 0;
  snaps.forEach((snap) => {
    if (snap.exists && snap.get("isTestUser") === true) {
      batch.delete(snap.ref);
      deleted += 1;
    }
  });
  if (deleted > 0) await batch.commit();
  return deleted;
}
