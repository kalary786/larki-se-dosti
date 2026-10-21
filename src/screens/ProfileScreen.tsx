import { Bookmark, Eye, Settings, UserRound, Wallet } from "lucide-react";
import { Avatar } from "../components/Avatar";
import { GhostButton } from "../components/Buttons";
import { SettingsRow } from "../components/SettingsRow";
import { StatTile } from "../components/StatTile";

type ProfileScreenProps = {
  name: string;
  city: string;
  chats: number;
  saved: number;
  onEdit: () => void;
  onSaved: () => void;
  onVisitors: () => void;
  onWallet: () => void;
  onSettings: () => void;
};

export function ProfileScreen({
  name,
  city,
  chats,
  saved,
  onEdit,
  onSaved,
  onVisitors,
  onWallet,
  onSettings,
}: ProfileScreenProps) {
  return (
    <section className="screen">
      <div className="stack">
        <div className="profile-hero">
          <Avatar portrait="you" name={name} size={96} online ring />
          <h1 className="h2">{name}</h1>
          <p className="caption muted">{city}</p>
          <GhostButton onClick={onEdit}>Edit profile</GhostButton>
        </div>
        <div className="stats">
          <StatTile value={String(chats)} label="Chats" />
          <StatTile value={String(saved)} label="Saved" />
          <StatTile value="0" label="Visits" hint="Quiet" />
        </div>
        <div className="surface-block">
          <SettingsRow icon={UserRound} title="Edit profile" subtitle="Name and city" onClick={onEdit} />
          <SettingsRow icon={Bookmark} title="Saved people" subtitle="Kept from the lounge" onClick={onSaved} />
          <SettingsRow icon={Eye} title="Visitors" subtitle="No one new" onClick={onVisitors} />
          <SettingsRow icon={Wallet} title="Wallet" subtitle="Coins and gifts" onClick={onWallet} />
          <SettingsRow icon={Settings} title="Settings" subtitle="Theme, privacy, language" onClick={onSettings} />
        </div>
      </div>
    </section>
  );
}
