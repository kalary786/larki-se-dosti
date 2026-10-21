import { DoorOpen, SlidersHorizontal } from "lucide-react";
import { AdSlot } from "../components/AdSlot";
import { CoinPill } from "../components/CoinPill";
import { EmptyState } from "../components/EmptyState";
import { FilterChip } from "../components/FilterChip";
import { IconCircleButton } from "../components/IconCircleButton";
import { ProfileCard } from "../components/ProfileCard";
import type { LoungeFilters, Profile, QuickFilter } from "../data/lounge";

type LoungeScreenProps = {
  filters: LoungeFilters;
  profile?: Profile;
  matchedCount: number;
  seenAll: boolean;
  saved: boolean;
  balance: number;
  onAddCoins: () => void;
  onOpenFilters: () => void;
  onToggleQuick: (value: QuickFilter | "all") => void;
  onPass: () => void;
  onConnect: () => void;
  onSave: () => void;
  onResetSeen: () => void;
};

const quick: { id: QuickFilter | "all"; label: string }[] = [
  { id: "all", label: "All" },
  { id: "online", label: "Online" },
  { id: "new", label: "New here" },
  { id: "verified", label: "Verified" },
];

export function LoungeScreen({
  filters,
  profile,
  matchedCount,
  seenAll,
  saved,
  balance,
  onAddCoins,
  onOpenFilters,
  onToggleQuick,
  onPass,
  onConnect,
  onSave,
  onResetSeen,
}: LoungeScreenProps) {
  return (
    <section className="screen">
      <div className="stack">
        <div className="topbar">
          <div className="topbar__titles">
            <p className="caption muted">Tonight</p>
            <h1 className="h2">The lounge</h1>
          </div>
          <div className="row-actions">
            <CoinPill balance={balance} onAdd={onAddCoins} />
            <IconCircleButton icon={SlidersHorizontal} label="Filters" onClick={onOpenFilters} />
          </div>
        </div>
        <div className="chip-row" role="toolbar" aria-label="Quick filters">
          {quick.map((chip) => {
            const selected = chip.id === "all" ? filters.quick.length === 0 : filters.quick.includes(chip.id);
            return (
              <FilterChip
                key={chip.id}
                label={chip.label}
                selected={selected}
                onClick={() => onToggleQuick(chip.id)}
              />
            );
          })}
        </div>
        <p className="caption muted">
          {matchedCount === 0
            ? "No one fits these filters"
            : `${matchedCount} ${matchedCount === 1 ? "person" : "people"} in reach`}
        </p>
        {profile ? (
          <ProfileCard
            portrait={profile.id}
            name={profile.name}
            age={profile.age}
            city={profile.city}
            bio={profile.bio}
            tags={profile.tags}
            online={profile.online}
            verified={profile.verified}
            saved={saved}
            onPass={onPass}
            onConnect={onConnect}
            onSave={onSave}
          />
        ) : (
          <EmptyState
            icon={DoorOpen}
            title={seenAll ? "You have seen this set" : "The lounge is quiet"}
            body={
              seenAll
                ? "Everyone who fits these filters has already been passed."
                : "Widen the filters to meet someone else tonight."
            }
            actionLabel={seenAll ? "See them again" : "Adjust filters"}
            onAction={seenAll ? onResetSeen : onOpenFilters}
          />
        )}
        <AdSlot />
      </div>
    </section>
  );
}
