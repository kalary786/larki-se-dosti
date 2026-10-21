type FilterChipProps = {
  label: string;
  selected?: boolean;
  onClick?: () => void;
};

export function FilterChip({ label, selected = false, onClick }: FilterChipProps) {
  const className = `chip${selected ? " chip--selected" : ""}${onClick ? "" : " chip--static"}`;

  if (!onClick) {
    return <span className={className}>{label}</span>;
  }

  return (
    <button type="button" className={className} aria-pressed={selected} onClick={onClick}>
      {label}
    </button>
  );
}
