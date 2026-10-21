import type { LucideIcon } from "lucide-react";
import { ArrowLeft } from "lucide-react";
import { Avatar } from "../components/Avatar";
import { EmptyState } from "../components/EmptyState";
import { IconCircleButton } from "../components/IconCircleButton";
import type { Profile } from "../data/lounge";

type PeopleScreenProps = {
  title: string;
  people: Profile[];
  emptyIcon: LucideIcon;
  emptyTitle: string;
  emptyBody: string;
  actionLabel?: string;
  onAction?: () => void;
  onBack: () => void;
};

export function PeopleScreen({
  title,
  people,
  emptyIcon,
  emptyTitle,
  emptyBody,
  actionLabel,
  onAction,
  onBack,
}: PeopleScreenProps) {
  return (
    <section className="screen">
      <div className="stack">
        <div className="back-row">
          <IconCircleButton icon={ArrowLeft} label="Back" variant="ghost" onClick={onBack} />
          <h1 className="h2">{title}</h1>
        </div>
        {people.length === 0 ? (
          <EmptyState
            icon={emptyIcon}
            title={emptyTitle}
            body={emptyBody}
            actionLabel={actionLabel}
            onAction={onAction}
          />
        ) : (
          <div>
            {people.map((person) => (
              <div key={person.id} className="person-row">
                <Avatar portrait={person.id} name={person.name} online={person.online} />
                <span className="person-row__copy">
                  <span className="title">{person.name}</span>
                  <span className="caption muted">{person.city}</span>
                </span>
              </div>
            ))}
          </div>
        )}
      </div>
    </section>
  );
}
