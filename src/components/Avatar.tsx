import { Portrait } from "./Portrait";

type AvatarProps = {
  portrait: string;
  name: string;
  size?: number;
  online?: boolean;
  ring?: boolean;
};

export function Avatar({ portrait, name, size = 48, online = false, ring = false }: AvatarProps) {
  return (
    <span
      className={`avatar${ring ? " avatar--ring" : ""}`}
      style={{ width: size, height: size }}
      role="img"
      aria-label={online ? `${name}, online` : name}
    >
      <Portrait id={portrait} className="avatar__art" />
      {online ? <span className="avatar__dot" /> : null}
    </span>
  );
}
