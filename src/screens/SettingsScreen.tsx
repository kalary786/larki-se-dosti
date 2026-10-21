import { ArrowLeft, Bell, Languages, LogOut, Shield, Sun } from "lucide-react";
import { GhostButton } from "../components/Buttons";
import { IconCircleButton } from "../components/IconCircleButton";
import { SettingsRow } from "../components/SettingsRow";

type SettingsScreenProps = {
  lightTheme: boolean;
  notifications: boolean;
  language: string;
  onBack: () => void;
  onTheme: (light: boolean) => void;
  onNotifications: (on: boolean) => void;
  onPrivacy: () => void;
  onLanguage: () => void;
  onLogout: () => void;
};

export function SettingsScreen({
  lightTheme,
  notifications,
  language,
  onBack,
  onTheme,
  onNotifications,
  onPrivacy,
  onLanguage,
  onLogout,
}: SettingsScreenProps) {
  return (
    <section className="screen">
      <div className="stack">
        <div className="back-row">
          <IconCircleButton icon={ArrowLeft} label="Back to profile" variant="ghost" onClick={onBack} />
          <h1 className="h2">Settings</h1>
        </div>
        <div className="surface-block">
          <SettingsRow
            icon={Sun}
            title="Light theme"
            subtitle={lightTheme ? "Soft paper background" : "Night lounge is on"}
            toggle={{ checked: lightTheme, onChange: onTheme, label: "Light theme" }}
          />
          <SettingsRow
            icon={Bell}
            title="Notifications"
            subtitle="New messages in the lounge"
            toggle={{ checked: notifications, onChange: onNotifications, label: "Notifications" }}
          />
          <SettingsRow icon={Shield} title="Privacy" subtitle="Who can see you" onClick={onPrivacy} />
          <SettingsRow icon={Languages} title="Language" subtitle={language} onClick={onLanguage} />
        </div>
        <GhostButton fullWidth tone="danger" icon={LogOut} onClick={onLogout}>
          Log out
        </GhostButton>
      </div>
    </section>
  );
}
