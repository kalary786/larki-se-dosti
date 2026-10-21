type AdSlotProps = {
  title?: string;
  body?: string;
};

export function AdSlot({
  title = "Evening tea, served quietly",
  body = "Sponsor placement. Nothing here is a real offer.",
}: AdSlotProps) {
  return (
    <aside className="ad-slot" aria-label="Advertisement">
      <span className="ad-slot__label">Sponsored</span>
      <p className="title">{title}</p>
      <p className="caption muted">{body}</p>
    </aside>
  );
}
