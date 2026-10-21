import { useEffect, useMemo, useState, type FormEvent } from "react";
import { Bookmark, Coins, DoorOpen, Eye, MessageCircle, UserRound } from "lucide-react";
import { BottomSheet } from "./components/BottomSheet";
import { GhostButton, PrimaryButton } from "./components/Buttons";
import { FilterChip } from "./components/FilterChip";
import { ModalDialog } from "./components/ModalDialog";
import { Avatar } from "./components/Avatar";
import { TabBar, type TabId } from "./components/TabBar";
import {
  coinPacks,
  defaultFilters,
  gifts,
  profileMatches,
  profiles,
  starterChats,
  toggleValue,
  you,
  type AgeBand,
  type ChatThread,
  type DistanceChoice,
  type Intent,
  type LoungeFilters,
  type QuickFilter,
} from "./data/lounge";
import { ChatScreen } from "./screens/ChatScreen";
import { InboxScreen } from "./screens/InboxScreen";
import { LoungeScreen } from "./screens/LoungeScreen";
import { PeopleScreen } from "./screens/PeopleScreen";
import { ProfileScreen } from "./screens/ProfileScreen";
import { SettingsScreen } from "./screens/SettingsScreen";
import { WalletScreen } from "./screens/WalletScreen";
import { WelcomeScreen } from "./screens/WelcomeScreen";
import { theme, type ThemeName } from "./theme/theme";

type Route =
  | { name: "welcome" }
  | { name: "main" }
  | { name: "chat"; chatId: string }
  | { name: "settings" }
  | { name: "saved" }
  | { name: "visitors" };

type Dialog =
  | { type: "none" }
  | { type: "signin" }
  | { type: "match"; profileId: string }
  | { type: "coins" }
  | { type: "gift"; giftId: string }
  | { type: "edit" }
  | { type: "privacy" };

type Sheet = "none" | "filters" | "language";

const tabs = [
  { id: "lounge" as const, label: "Lounge", icon: DoorOpen },
  { id: "inbox" as const, label: "Inbox", icon: MessageCircle },
  { id: "wallet" as const, label: "Coins", icon: Coins, coins: true },
  { id: "profile" as const, label: "You", icon: UserRound },
];

const ageOptions: { id: AgeBand; label: string }[] = [
  { id: "21-25", label: "21–25" },
  { id: "26-30", label: "26–30" },
  { id: "31-35", label: "31–35" },
];

const distanceOptions: { id: DistanceChoice; label: string }[] = [
  { id: "nearby", label: "Nearby" },
  { id: "city", label: "In the city" },
  { id: "anywhere", label: "Anywhere" },
];

const intentOptions: { id: Intent; label: string }[] = [
  { id: "conversation", label: "Conversation" },
  { id: "friendship", label: "Friendship" },
  { id: "dating", label: "Dating" },
];

function initialTheme(): ThemeName {
  try {
    return localStorage.getItem("dosti-theme") === "light" ? "light" : "dark";
  } catch {
    return "dark";
  }
}

function clock() {
  return new Date().toLocaleTimeString("en-US", { hour: "numeric", minute: "2-digit" });
}

function uid() {
  return `${Date.now()}-${Math.random().toString(16).slice(2)}`;
}

export function App() {
  const [themeName, setThemeName] = useState<ThemeName>(initialTheme);
  const [route, setRoute] = useState<Route>({ name: "welcome" });
  const [tab, setTab] = useState<TabId>("lounge");
  const [dialog, setDialog] = useState<Dialog>({ type: "none" });
  const [sheet, setSheet] = useState<Sheet>("none");
  const [filters, setFilters] = useState<LoungeFilters>(defaultFilters);
  const [draft, setDraft] = useState<LoungeFilters>(defaultFilters);
  const [passed, setPassed] = useState<string[]>([]);
  const [saved, setSaved] = useState<string[]>([]);
  const [balance, setBalance] = useState(120);
  const [packId, setPackId] = useState(coinPacks[1].id);
  const [chats, setChats] = useState<ChatThread[]>(starterChats);
  const [me, setMe] = useState(you);
  const [draftName, setDraftName] = useState(you.name);
  const [draftCity, setDraftCity] = useState(you.city);
  const [notifications, setNotifications] = useState(true);
  const [language, setLanguage] = useState("English");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [authError, setAuthError] = useState("");

  useEffect(() => {
    document.documentElement.dataset.theme = themeName;
    document.querySelector('meta[name="theme-color"]')?.setAttribute(
      "content",
      themeName === "light" ? theme.themes.light.background : theme.themes.dark.background,
    );
    try {
      localStorage.setItem("dosti-theme", themeName);
    } catch {
      /* Private mode can block storage. The theme still applies for this visit. */
    }
  }, [themeName]);

  const matched = useMemo(() => profiles.filter((profile) => profileMatches(profile, filters)), [filters]);
  const visible = matched.filter((profile) => !passed.includes(profile.id));
  const current = visible[0];
  const overlayOpen = dialog.type !== "none" || sheet !== "none";
  const activeChat = route.name === "chat" ? chats.find((chat) => chat.id === route.chatId) : undefined;
  const activeProfile = activeChat ? profiles.find((profile) => profile.id === activeChat.profileId) : undefined;
  const matchProfile = dialog.type === "match" ? profiles.find((profile) => profile.id === dialog.profileId) : undefined;
  const selectedPack = coinPacks.find((pack) => pack.id === packId) ?? coinPacks[0];
  const activeGift = dialog.type === "gift" ? gifts.find((gift) => gift.id === dialog.giftId) : undefined;
  const inboxCount = chats.reduce((sum, chat) => sum + chat.unread, 0);
  const savedPeople = profiles.filter((profile) => saved.includes(profile.id));

  function openDialog(next: Dialog) {
    setSheet("none");
    setDialog(next);
  }

  function goMain(next: TabId) {
    setTab(next);
    setRoute({ name: "main" });
    setDialog({ type: "none" });
    setSheet("none");
  }

  function toggleQuick(value: QuickFilter | "all") {
    setFilters((currentFilters) =>
      value === "all"
        ? { ...currentFilters, quick: [] }
        : { ...currentFilters, quick: toggleValue(currentFilters.quick, value) },
    );
  }

  function openChat(profileId: string) {
    const existing = chats.find((chat) => chat.profileId === profileId);
    const chatId = existing?.id ?? `chat-${profileId}`;
    setChats((prev) => {
      if (prev.some((chat) => chat.id === chatId)) {
        return prev.map((chat) => (chat.id === chatId ? { ...chat, unread: 0 } : chat));
      }
      return [{ id: chatId, profileId, unread: 0, messages: [] }, ...prev];
    });
    setDialog({ type: "none" });
    setRoute({ name: "chat", chatId });
  }

  function sendMessage(chatId: string, text: string) {
    setChats((prev) =>
      prev.map((chat) =>
        chat.id === chatId
          ? { ...chat, messages: [...chat.messages, { id: uid(), from: "me", text, time: clock() }] }
          : chat,
      ),
    );
  }

  function submitSignIn(event: FormEvent) {
    event.preventDefault();
    if (!email.includes("@") || !email.split("@")[1]?.includes(".")) {
      setAuthError("Enter a valid email to continue.");
      return;
    }
    if (password.length < 4) {
      setAuthError("Use at least 4 characters.");
      return;
    }
    setAuthError("");
    setPassword("");
    goMain("lounge");
  }

  return (
    <div className="app-shell">
      <div className={`phone${overlayOpen ? " phone--locked" : ""}`}>
        {route.name === "welcome" ? (
          <WelcomeScreen onEnter={() => goMain("lounge")} onSignIn={() => openDialog({ type: "signin" })} />
        ) : null}

        {route.name === "main" && tab === "lounge" ? (
          <LoungeScreen
            filters={filters}
            profile={current}
            matchedCount={matched.length}
            seenAll={matched.length > 0 && visible.length === 0}
            saved={current ? saved.includes(current.id) : false}
            balance={balance}
            onAddCoins={() => goMain("wallet")}
            onOpenFilters={() => {
              setDraft(filters);
              setSheet("filters");
            }}
            onToggleQuick={toggleQuick}
            onPass={() => {
              if (!current) return;
              setPassed((prev) => (prev.includes(current.id) ? prev : [...prev, current.id]));
            }}
            onConnect={() => {
              if (!current) return;
              openDialog({ type: "match", profileId: current.id });
            }}
            onSave={() => {
              if (!current) return;
              setSaved((prev) => toggleValue(prev, current.id));
            }}
            onResetSeen={() => setPassed([])}
          />
        ) : null}

        {route.name === "main" && tab === "inbox" ? (
          <InboxScreen
            items={chats.map((chat) => {
              const profile = profiles.find((item) => item.id === chat.profileId);
              const last = chat.messages[chat.messages.length - 1];
              return {
                id: chat.id,
                name: profile?.name ?? "Member",
                portrait: profile?.id ?? "you",
                online: profile?.online ?? false,
                preview: last?.text ?? "Say hello",
                time: last?.time ?? "",
                unread: chat.unread,
              };
            })}
            onOpen={(chatId) => {
              setChats((prev) => prev.map((chat) => (chat.id === chatId ? { ...chat, unread: 0 } : chat)));
              setRoute({ name: "chat", chatId });
            }}
          />
        ) : null}

        {route.name === "main" && tab === "wallet" ? (
          <WalletScreen
            balance={balance}
            packs={coinPacks}
            gifts={gifts}
            selectedPackId={packId}
            onSelectPack={setPackId}
            onBuy={() => openDialog({ type: "coins" })}
            onGift={(giftId) => openDialog({ type: "gift", giftId })}
          />
        ) : null}

        {route.name === "main" && tab === "profile" ? (
          <ProfileScreen
            name={me.name}
            city={me.city}
            chats={chats.length}
            saved={saved.length}
            onEdit={() => {
              setDraftName(me.name);
              setDraftCity(me.city);
              openDialog({ type: "edit" });
            }}
            onSaved={() => setRoute({ name: "saved" })}
            onVisitors={() => setRoute({ name: "visitors" })}
            onWallet={() => goMain("wallet")}
            onSettings={() => setRoute({ name: "settings" })}
          />
        ) : null}

        {route.name === "chat" && activeChat && activeProfile ? (
          <ChatScreen
            name={activeProfile.name}
            portrait={activeProfile.id}
            online={activeProfile.online}
            messages={activeChat.messages}
            onBack={() => goMain("inbox")}
            onSend={(text) => sendMessage(activeChat.id, text)}
          />
        ) : null}

        {route.name === "settings" ? (
          <SettingsScreen
            lightTheme={themeName === "light"}
            notifications={notifications}
            language={language}
            onBack={() => goMain("profile")}
            onTheme={(light) => setThemeName(light ? "light" : "dark")}
            onNotifications={setNotifications}
            onPrivacy={() => openDialog({ type: "privacy" })}
            onLanguage={() => setSheet("language")}
            onLogout={() => {
              setDialog({ type: "none" });
              setSheet("none");
              setRoute({ name: "welcome" });
            }}
          />
        ) : null}

        {route.name === "saved" ? (
          <PeopleScreen
            title="Saved"
            people={savedPeople}
            emptyIcon={Bookmark}
            emptyTitle="No one saved"
            emptyBody="Bookmark a profile in the lounge and they will wait here."
            actionLabel="Back to the lounge"
            onAction={() => goMain("lounge")}
            onBack={() => goMain("profile")}
          />
        ) : null}

        {route.name === "visitors" ? (
          <PeopleScreen
            title="Visitors"
            people={[]}
            emptyIcon={Eye}
            emptyTitle="No visitors yet"
            emptyBody="When someone opens your profile, they will show up here."
            onBack={() => goMain("profile")}
          />
        ) : null}

        {route.name === "main" ? <TabBar active={tab} inboxCount={inboxCount} items={tabs} onChange={goMain} /> : null}

        <BottomSheet open={sheet === "filters"} title="Filters" onClose={() => setSheet("none")}>
          <div className="section-label">
            <p className="title">Looking for</p>
            <div className="chip-wrap">
              {intentOptions.map((option) => (
                <FilterChip
                  key={option.id}
                  label={option.label}
                  selected={draft.intents.includes(option.id)}
                  onClick={() => setDraft((prev) => ({ ...prev, intents: toggleValue(prev.intents, option.id) }))}
                />
              ))}
            </div>
          </div>
          <div className="section-label">
            <p className="title">Age</p>
            <div className="chip-wrap">
              {ageOptions.map((option) => (
                <FilterChip
                  key={option.id}
                  label={option.label}
                  selected={draft.ages.includes(option.id)}
                  onClick={() => setDraft((prev) => ({ ...prev, ages: toggleValue(prev.ages, option.id) }))}
                />
              ))}
            </div>
          </div>
          <div className="section-label">
            <p className="title">Distance</p>
            <div className="chip-wrap">
              {distanceOptions.map((option) => (
                <FilterChip
                  key={option.id}
                  label={option.label}
                  selected={draft.distance === option.id}
                  onClick={() => setDraft((prev) => ({ ...prev, distance: option.id }))}
                />
              ))}
            </div>
          </div>
          <div className="group">
            <PrimaryButton
              fullWidth
              onClick={() => {
                setFilters(draft);
                setSheet("none");
              }}
            >
              Apply filters
            </PrimaryButton>
            <GhostButton fullWidth onClick={() => setDraft({ ...defaultFilters, quick: draft.quick })}>
              Reset
            </GhostButton>
          </div>
        </BottomSheet>

        <BottomSheet open={sheet === "language"} title="Language" onClose={() => setSheet("none")}>
          <p className="body muted">The interface stays in English in this preview. Your choice is kept here.</p>
          <div className="chip-wrap">
            {["English", "Urdu"].map((option) => (
              <FilterChip key={option} label={option} selected={language === option} onClick={() => setLanguage(option)} />
            ))}
          </div>
          <PrimaryButton fullWidth onClick={() => setSheet("none")}>
            Done
          </PrimaryButton>
        </BottomSheet>

        <ModalDialog open={dialog.type === "signin"} title="Sign in" onClose={() => setDialog({ type: "none" })}>
          <form className="form" onSubmit={submitSignIn}>
            <p className="caption muted">Preview only. Nothing leaves this device.</p>
            <div className="group">
              <label className="caption muted" htmlFor="email">
                Email
              </label>
              <input
                id="email"
                className={`field${authError ? " field--error" : ""}`}
                value={email}
                autoComplete="off"
                inputMode="email"
                onChange={(event) => {
                  setEmail(event.target.value);
                  setAuthError("");
                }}
              />
            </div>
            <div className="group">
              <label className="caption muted" htmlFor="password">
                Password
              </label>
              <input
                id="password"
                type="password"
                className={`field${authError ? " field--error" : ""}`}
                value={password}
                autoComplete="off"
                onChange={(event) => {
                  setPassword(event.target.value);
                  setAuthError("");
                }}
              />
            </div>
            {authError ? <p className="caption error-text">{authError}</p> : null}
            <PrimaryButton type="submit" fullWidth>
              Continue
            </PrimaryButton>
          </form>
        </ModalDialog>

        <ModalDialog
          open={dialog.type === "match" && Boolean(matchProfile)}
          title={matchProfile ? `Say hello to ${matchProfile.name.split(" ")[0]}` : "Say hello"}
          onClose={() => setDialog({ type: "none" })}
          primaryAction={matchProfile ? { label: "Open chat", onClick: () => openChat(matchProfile.id) } : undefined}
          secondaryAction={{ label: "Not now", onClick: () => setDialog({ type: "none" }) }}
        >
          {matchProfile ? (
            <>
              <div className="pair">
                <Avatar portrait="you" name={me.name} size={72} ring />
                <Avatar portrait={matchProfile.id} name={matchProfile.name} size={72} online={matchProfile.online} />
              </div>
              <p className="muted">Nothing is sent until you write the first note.</p>
            </>
          ) : null}
        </ModalDialog>

        <ModalDialog
          open={dialog.type === "coins"}
          title="Add coins"
          onClose={() => setDialog({ type: "none" })}
          primaryAction={{
            label: `Add ${selectedPack.coins}`,
            onClick: () => {
              setBalance((value) => value + selectedPack.coins);
              setDialog({ type: "none" });
            },
          }}
          secondaryAction={{ label: "Cancel", onClick: () => setDialog({ type: "none" }) }}
        >
          <p className="muted">
            {selectedPack.coins} coins will be added on this device. This preview does not take payment.
          </p>
        </ModalDialog>

        <ModalDialog
          open={dialog.type === "gift" && Boolean(activeGift)}
          title={activeGift ? activeGift.name : "Gift"}
          onClose={() => setDialog({ type: "none" })}
          primaryAction={
            activeGift
              ? activeGift.coins > balance
                ? { label: "Close", onClick: () => setDialog({ type: "none" }) }
                : {
                    label: `Send for ${activeGift.coins}`,
                    onClick: () => {
                      setBalance((value) => value - activeGift.coins);
                      setDialog({ type: "none" });
                    },
                  }
              : undefined
          }
        >
          {activeGift ? (
            <p className="muted">
              {activeGift.coins > balance
                ? `You need ${activeGift.coins} coins. The wallet has ${balance}.`
                : `${activeGift.name} is set aside for your next hello. ${activeGift.coins} coins leave the wallet.`}
            </p>
          ) : null}
        </ModalDialog>

        <ModalDialog
          open={dialog.type === "edit"}
          title="Edit profile"
          onClose={() => setDialog({ type: "none" })}
          primaryAction={{
            label: "Save",
            onClick: () => {
              if (!draftName.trim() || !draftCity.trim()) return;
              setMe((prev) => ({ ...prev, name: draftName.trim(), city: draftCity.trim() }));
              setDialog({ type: "none" });
            },
          }}
          secondaryAction={{ label: "Cancel", onClick: () => setDialog({ type: "none" }) }}
        >
          <div className="form">
            <div className="group">
              <label className="caption muted" htmlFor="profile-name">
                Name
              </label>
              <input id="profile-name" className="field" value={draftName} onChange={(event) => setDraftName(event.target.value)} />
            </div>
            <div className="group">
              <label className="caption muted" htmlFor="profile-city">
                City
              </label>
              <input id="profile-city" className="field" value={draftCity} onChange={(event) => setDraftCity(event.target.value)} />
            </div>
          </div>
        </ModalDialog>

        <ModalDialog
          open={dialog.type === "privacy"}
          title="Privacy"
          onClose={() => setDialog({ type: "none" })}
          primaryAction={{ label: "Done", onClick: () => setDialog({ type: "none" }) }}
        >
          <p className="muted">Your profile is visible in the lounge. You choose who receives a message.</p>
        </ModalDialog>
      </div>
    </div>
  );
}
