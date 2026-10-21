type StatTileProps = {
  value: string;
  label: string;
  hint?: string;
};

export function StatTile({ value, label, hint }: StatTileProps) {
  return (
    <article className="stat-tile">
      <span className="stat-tile__value">{value}</span>
      <span className="caption">{label}</span>
      {hint ? <span className="caption muted">{hint}</span> : null}
    </article>
  );
}
