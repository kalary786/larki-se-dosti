import type { LucideIcon } from "lucide-react";
import { Badge } from "./Badge";
import { Icon } from "./Icon";

export type TabId = "lounge" | "inbox" | "wallet" | "profile";

type TabItem = {
  id: TabId;
  label: string;
  icon: LucideIcon;
  coins?: boolean;
};

type TabBarProps = {
  active: TabId;
  inboxCount: number;
  items: TabItem[];
  onChange: (tab: TabId) => void;
};

export function TabBar({ active, inboxCount, items, onChange }: TabBarProps) {
  return (
    <nav className="tabbar" aria-label="Primary">
      {items.map((item) => {
        const isActive = item.id === active;
        return (
          <button
            key={item.id}
            type="button"
            className={`tab${isActive ? " tab--active" : ""}${item.coins ? " tab--coins" : ""}`}
            aria-current={isActive ? "page" : undefined}
            onClick={() => onChange(item.id)}
          >
            <Icon icon={item.icon} size={22} />
            {item.label}
            {item.id === "inbox" ? (
              <span className="tab__badge">
                <Badge count={inboxCount} />
              </span>
            ) : null}
          </button>
        );
      })}
    </nav>
  );
}
