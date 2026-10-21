export type Intent = "conversation" | "friendship" | "dating";
export type AgeBand = "21-25" | "26-30" | "31-35";
export type Distance = "near" | "city" | "far";

export type Profile = {
  id: string;
  name: string;
  age: number;
  city: string;
  bio: string;
  tags: string[];
  online: boolean;
  verified: boolean;
  isNew: boolean;
  intent: Intent;
  ageBand: AgeBand;
  distance: Distance;
};

export const profiles: Profile[] = [
  {
    id: "ayesha",
    name: "Ayesha Rahman",
    age: 27,
    city: "Lahore",
    bio: "I design houses by day and walk the canal after dark. Looking for an easy conversation, not a performance.",
    tags: ["Architecture", "Walks", "Poetry"],
    online: true,
    verified: true,
    isNew: false,
    intent: "conversation",
    ageBand: "26-30",
    distance: "near",
  },
  {
    id: "meher",
    name: "Meher Ali",
    age: 25,
    city: "Karachi",
    bio: "Cook, swim, take the long way home. If you know a quiet cafe, start there.",
    tags: ["Cooking", "Sea", "Film"],
    online: true,
    verified: false,
    isNew: true,
    intent: "friendship",
    ageBand: "21-25",
    distance: "far",
  },
  {
    id: "hira",
    name: "Hira Qureshi",
    age: 29,
    city: "Islamabad",
    bio: "Product designer. I like bookstores, strong coffee, and people who reply in full sentences.",
    tags: ["Design", "Coffee", "Books"],
    online: false,
    verified: true,
    isNew: false,
    intent: "dating",
    ageBand: "26-30",
    distance: "city",
  },
  {
    id: "sana",
    name: "Sana Iqbal",
    age: 26,
    city: "Faisalabad",
    bio: "I teach literature and raise too many plants. Weekends are for long drives.",
    tags: ["Teaching", "Plants", "Drives"],
    online: true,
    verified: false,
    isNew: true,
    intent: "friendship",
    ageBand: "26-30",
    distance: "far",
  },
  {
    id: "zoya",
    name: "Zoya Khan",
    age: 31,
    city: "Rawalpindi",
    bio: "Classical music, museums, and late dinners. Tell me what you are listening to.",
    tags: ["Music", "Museums", "Dinner"],
    online: true,
    verified: true,
    isNew: false,
    intent: "dating",
    ageBand: "31-35",
    distance: "near",
  },
];

export type ChatMessage = {
  id: string;
  from: "me" | "them";
  text: string;
  time: string;
};

export type ChatThread = {
  id: string;
  profileId: string;
  unread: number;
  messages: ChatMessage[];
};

export const starterChats: ChatThread[] = [
  {
    id: "chat-ayesha",
    profileId: "ayesha",
    unread: 1,
    messages: [
      {
        id: "m1",
        from: "them",
        text: "The canal is nice after nine, if it is not too cold.",
        time: "9:14",
      },
      { id: "m2", from: "me", text: "I will bring tea.", time: "9:21" },
    ],
  },
  {
    id: "chat-zoya",
    profileId: "zoya",
    unread: 0,
    messages: [
      { id: "m3", from: "them", text: "What are you listening to this week?", time: "Yesterday" },
    ],
  },
];

export type CoinPack = {
  id: string;
  coins: number;
  label: string;
  note: string;
};

export const coinPacks: CoinPack[] = [
  { id: "starter", coins: 80, label: "Starter", note: "A few hellos" },
  { id: "regular", coins: 200, label: "Regular", note: "Most evenings" },
  { id: "long", coins: 500, label: "Long evening", note: "Gifts included" },
];

export type Gift = {
  id: string;
  name: string;
  coins: number;
  mark: string;
};

export const gifts: Gift[] = [
  { id: "chai", name: "Chai", coins: 15, mark: "🍵" },
  { id: "jasmine", name: "Jasmine", coins: 40, mark: "🌸" },
  { id: "letter", name: "Letter", coins: 10, mark: "✉️" },
  { id: "record", name: "Record", coins: 80, mark: "💿" },
];

export const you = {
  id: "you",
  name: "Bilal Hassan",
  city: "Islamabad",
  age: 28,
};

export type QuickFilter = "online" | "new" | "verified";
export type DistanceChoice = "nearby" | "city" | "anywhere";

export type LoungeFilters = {
  quick: QuickFilter[];
  ages: AgeBand[];
  distance: DistanceChoice;
  intents: Intent[];
};

export const defaultFilters: LoungeFilters = {
  quick: [],
  ages: [],
  distance: "anywhere",
  intents: [],
};

export function toggleValue<T>(list: T[], value: T): T[] {
  return list.includes(value) ? list.filter((item) => item !== value) : [...list, value];
}

export function profileMatches(profile: Profile, filters: LoungeFilters) {
  if (filters.quick.includes("online") && !profile.online) return false;
  if (filters.quick.includes("new") && !profile.isNew) return false;
  if (filters.quick.includes("verified") && !profile.verified) return false;
  if (filters.ages.length > 0 && !filters.ages.includes(profile.ageBand)) return false;
  if (filters.intents.length > 0 && !filters.intents.includes(profile.intent)) return false;
  if (filters.distance === "nearby" && profile.distance === "far") return false;
  if (filters.distance === "city" && profile.distance !== "city") return false;
  return true;
}
