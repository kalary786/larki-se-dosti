import { Coins, DoorOpen, Shield } from "lucide-react";
import { GhostButton, PrimaryButton } from "../components/Buttons";
import { Icon } from "../components/Icon";
import { LoungeMark } from "../components/LoungeMark";

type WelcomeScreenProps = {
  onEnter: () => void;
  onSignIn: () => void;
};

const features = [
  {
    icon: DoorOpen,
    title: "Unhurried meetings",
    body: "One person at a time. No noisy deck.",
    gold: false,
  },
  {
    icon: Coins,
    title: "Coins stay visible",
    body: "Gifts spend from the same wallet.",
    gold: true,
  },
  {
    icon: Shield,
    title: "You open the door",
    body: "Nothing is sent until you say hello.",
    gold: false,
  },
];

export function WelcomeScreen({ onEnter, onSignIn }: WelcomeScreenProps) {
  return (
    <section className="screen">
      <div className="welcome">
        <div className="welcome__brand">
          <LoungeMark />
          <p className="caption muted">Larki Se Dosti</p>
          <h1 className="h1">dosti</h1>
          <p className="body muted">A night lounge for people who would rather talk than perform.</p>
        </div>
        <div className="welcome__features">
          {features.map((feature) => (
            <div className="feature" key={feature.title}>
              <span className={`icon-btn icon-btn--sm${feature.gold ? " gold" : ""}`} aria-hidden>
                <Icon icon={feature.icon} size={18} />
              </span>
              <div className="group">
                <p className="title">{feature.title}</p>
                <p className="caption muted">{feature.body}</p>
              </div>
            </div>
          ))}
        </div>
        <div className="welcome__actions">
          <PrimaryButton fullWidth onClick={onEnter}>
            Enter the lounge
          </PrimaryButton>
          <GhostButton fullWidth onClick={onSignIn}>
            Sign in
          </GhostButton>
        </div>
      </div>
    </section>
  );
}
