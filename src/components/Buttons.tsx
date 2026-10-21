import type { ButtonHTMLAttributes, ReactNode } from "react";
import type { LucideIcon } from "lucide-react";
import { Icon } from "./Icon";

type ButtonProps = {
  children: ReactNode;
  icon?: LucideIcon;
  fullWidth?: boolean;
  variant: "primary" | "secondary" | "ghost";
  tone?: "default" | "danger";
} & ButtonHTMLAttributes<HTMLButtonElement>;

function Button({
  children,
  icon,
  fullWidth,
  variant,
  tone = "default",
  className,
  type = "button",
  ...rest
}: ButtonProps) {
  const classes = [
    "btn",
    `btn--${variant}`,
    fullWidth ? "btn--full" : "",
    tone === "danger" ? "btn--danger" : "",
    className ?? "",
  ]
    .filter(Boolean)
    .join(" ");

  return (
    <button type={type} className={classes} {...rest}>
      {icon ? <Icon icon={icon} size={18} /> : null}
      {children}
    </button>
  );
}

export function PrimaryButton(props: Omit<ButtonProps, "variant" | "tone">) {
  return <Button variant="primary" {...props} />;
}

export function SecondaryButton(props: Omit<ButtonProps, "variant" | "tone">) {
  return <Button variant="secondary" {...props} />;
}

export function GhostButton(props: Omit<ButtonProps, "variant">) {
  return <Button variant="ghost" {...props} />;
}
