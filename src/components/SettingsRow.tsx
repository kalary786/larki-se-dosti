import type { LucideIcon } from "lucide-react";
import { ChevronRight } from "lucide-react";
import { Icon } from "./Icon";

type SettingsRowProps = {
  icon: LucideIcon;
  title: string;
  subtitle?: string;
  onClick?: () => void;
  toggle?: {
    checked: boolean;
    onChange: (next: boolean) => void;
    label: string;
  };
  destructive?: boolean;
};

export function SettingsRow({ icon, title, subtitle, onClick, toggle, destructive }: SettingsRowProps) {
  const copy = (
    <>
      <span className="icon-btn icon-btn--sm" aria-hidden>
        <Icon icon={icon} size={18} />
      </span>
      <span className="settings-row__copy">
        <span className="title">{title}</span>
        {subtitle ? <span className="caption muted">{subtitle}</span> : null}
      </span>
    </>
  );

  if (toggle) {
    return (
      <div className={`settings-row${destructive ? " settings-row--danger" : ""}`}>
        {copy}
        <button
          type="button"
          className={`switch${toggle.checked ? " switch--on" : ""}`}
          role="switch"
          aria-checked={toggle.checked}
          aria-label={toggle.label}
          onClick={() => toggle.onChange(!toggle.checked)}
        >
          <span className="switch__thumb" />
        </button>
      </div>
    );
  }

  return (
    <button
      type="button"
      className={`settings-row${destructive ? " settings-row--danger" : ""}`}
      onClick={onClick}
    >
      {copy}
      <span className="settings-row__chevron">
        <Icon icon={ChevronRight} size={18} />
      </span>
    </button>
  );
}
