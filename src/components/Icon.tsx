import type { LucideIcon } from "lucide-react";
import { iconStroke } from "../theme/theme";

type IconProps = {
  icon: LucideIcon;
  size?: number;
};

export function Icon({ icon: Glyph, size = 22 }: IconProps) {
  return <Glyph size={size} strokeWidth={iconStroke} aria-hidden />;
}
