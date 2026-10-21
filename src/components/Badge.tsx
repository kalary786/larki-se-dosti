type BadgeProps = {
  count: number;
  tone?: "danger" | "accent";
};

export function Badge({ count, tone = "danger" }: BadgeProps) {
  if (count <= 0) return null;
  const label = count > 99 ? "99+" : String(count);
  return <span className={`badge${tone === "accent" ? " badge--accent" : ""}`}>{label}</span>;
}
