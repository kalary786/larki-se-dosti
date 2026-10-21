import type { LucideIcon } from "lucide-react";
import { Icon } from "./Icon";
import { PrimaryButton } from "./Buttons";

type EmptyStateProps = {
  icon: LucideIcon;
  title: string;
  body: string;
  actionLabel?: string;
  onAction?: () => void;
};

export function EmptyState({ icon, title, body, actionLabel, onAction }: EmptyStateProps) {
  return (
    <div className="empty">
      <div className="empty__icon">
        <Icon icon={icon} size={26} />
      </div>
      <h2 className="title">{title}</h2>
      <p className="body muted">{body}</p>
      {actionLabel && onAction ? (
        <div style={{ marginTop: 12 }}>
          <PrimaryButton onClick={onAction}>{actionLabel}</PrimaryButton>
        </div>
      ) : null}
    </div>
  );
}
