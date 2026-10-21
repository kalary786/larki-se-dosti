import type { LucideIcon } from "lucide-react";
import { Icon } from "./Icon";

type IconCircleButtonProps = {
  icon: LucideIcon;
  label: string;
  onClick?: () => void;
  variant?: "surface" | "accent" | "ghost";
  size?: "md" | "sm";
};

export function IconCircleButton({
  icon,
  label,
  onClick,
  variant = "surface",
  size = "md",
}: IconCircleButtonProps) {
  return (
    <button
      type="button"
      className={`icon-btn icon-btn--${variant}${size === "sm" ? " icon-btn--sm" : ""}`}
      aria-label={label}
      onClick={onClick}
    >
      <Icon icon={icon} size={size === "sm" ? 18 : 20} />
    </button>
  );
}
