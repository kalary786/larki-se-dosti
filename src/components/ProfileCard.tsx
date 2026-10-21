import { BadgeCheck, Bookmark, MapPin, X } from "lucide-react";
import { FilterChip } from "./FilterChip";
import { Icon } from "./Icon";
import { Portrait } from "./Portrait";
import { PrimaryButton } from "./Buttons";
import { IconCircleButton } from "./IconCircleButton";

export type ProfileCardProps = {
  portrait: string;
  name: string;
  age: number;
  city: string;
  bio: string;
  tags: string[];
  online?: boolean;
  verified?: boolean;
  saved?: boolean;
  onPass: () => void;
  onConnect: () => void;
  onSave: () => void;
};

export function ProfileCard({
  portrait,
  name,
  age,
  city,
  bio,
  tags,
  online,
  verified,
  saved,
  onPass,
  onConnect,
  onSave,
}: ProfileCardProps) {
  return (
    <article className="profile-card">
      <div className="profile-card__art">
        <Portrait id={portrait} />
      </div>
      <div className="profile-card__body">
        <div>
          <div className="profile-card__name">
            <h2 className="h2">
              {name}, {age}
            </h2>
            {verified ? (
              <span className="verified">
                <Icon icon={BadgeCheck} size={18} />
                <span className="sr-only">Verified</span>
              </span>
            ) : null}
            {online ? (
              <span className="online-dot">
                <span className="sr-only">Online</span>
              </span>
            ) : null}
          </div>
          <p className="profile-card__meta caption">
            <Icon icon={MapPin} size={14} />
            {city}
          </p>
        </div>
        <p className="body muted">{bio}</p>
        <div className="profile-card__tags">
          {tags.map((tag) => (
            <FilterChip key={tag} label={tag} />
          ))}
        </div>
        <div className="profile-card__actions">
          <IconCircleButton icon={X} label={`Pass on ${name}`} onClick={onPass} />
          <PrimaryButton onClick={onConnect}>Say hello</PrimaryButton>
          <IconCircleButton
            icon={Bookmark}
            label={saved ? `Remove ${name} from saved` : `Save ${name}`}
            variant={saved ? "accent" : "surface"}
            onClick={onSave}
          />
        </div>
      </div>
    </article>
  );
}
