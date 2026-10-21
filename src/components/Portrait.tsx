type PortraitProps = {
  id: string;
  className?: string;
};

type Spec = {
  bg: string;
  panel: string;
  skin: string;
  hair: string;
  outfit: string;
  accent: string;
  motif: "arch" | "bands" | "books" | "plant" | "staff";
};

const portraits: Record<string, Spec> = {
  ayesha: {
    bg: "#1F1B3A",
    panel: "#14112B",
    skin: "#E4C2A4",
    hair: "#1A1428",
    outfit: "#FF7A59",
    accent: "#B7B3D1",
    motif: "arch",
  },
  meher: {
    bg: "#1A2340",
    panel: "#142033",
    skin: "#C98A62",
    hair: "#2A160F",
    outfit: "#2A2550",
    accent: "#3DDC97",
    motif: "bands",
  },
  hira: {
    bg: "#241E3E",
    panel: "#1B1730",
    skin: "#F0D2BE",
    hair: "#3A241C",
    outfit: "#F7F5FF",
    accent: "#FF7A59",
    motif: "books",
  },
  sana: {
    bg: "#1C2436",
    panel: "#152033",
    skin: "#D7A888",
    hair: "#24160F",
    outfit: "#3DDC97",
    accent: "#FF7A59",
    motif: "plant",
  },
  zoya: {
    bg: "#19162E",
    panel: "#121026",
    skin: "#E8CDB8",
    hair: "#140E18",
    outfit: "#B7B3D1",
    accent: "#FF7A59",
    motif: "staff",
  },
  you: {
    bg: "#2A2550",
    panel: "#1F1B3A",
    skin: "#E7C7A8",
    hair: "#1A1428",
    outfit: "#FF7A59",
    accent: "#F7F5FF",
    motif: "arch",
  },
};

function Motif({ spec }: { spec: Spec }) {
  if (spec.motif === "arch") {
    return (
      <path
        d="M48 250V92c0-40 264-40 264 0v158"
        fill="none"
        stroke={spec.accent}
        strokeWidth="1.75"
      />
    );
  }
  if (spec.motif === "bands") {
    return (
      <g fill={spec.accent}>
        <rect x="0" y="250" width="360" height="8" />
        <rect x="0" y="274" width="360" height="4" opacity="0.7" />
      </g>
    );
  }
  if (spec.motif === "books") {
    return (
      <g fill="none" stroke={spec.accent} strokeWidth="1.75">
        <rect x="36" y="78" width="46" height="64" rx="4" />
        <rect x="46" y="70" width="46" height="64" rx="4" />
        <rect x="278" y="86" width="42" height="58" rx="4" />
      </g>
    );
  }
  if (spec.motif === "plant") {
    return (
      <g fill="none" stroke={spec.accent} strokeWidth="1.75">
        <path d="M62 230c18-48 8-90-6-120" />
        <path d="M62 180c24-8 36-28 34-48" />
        <path d="M300 220c-10-52 8-96 24-124" />
        <circle cx="62" cy="108" r="10" />
      </g>
    );
  }
  return (
    <g fill="none" stroke={spec.accent} strokeWidth="1.75">
      <path d="M40 96h280M40 116h280M40 136h280" />
      <circle cx="292" cy="96" r="8" fill={spec.accent} stroke="none" />
      <path d="M300 96v48" />
    </g>
  );
}

export function Portrait({ id, className }: PortraitProps) {
  const spec = portraits[id] ?? portraits.you;
  return (
    <svg className={className} viewBox="0 0 360 440" preserveAspectRatio="xMidYMid slice" aria-hidden>
      <rect width="360" height="440" fill={spec.bg} />
      <rect y="250" width="360" height="190" fill={spec.panel} />
      <Motif spec={spec} />
      <ellipse cx="148" cy="210" rx="22" ry="62" fill={spec.hair} />
      <ellipse cx="214" cy="214" rx="22" ry="68" fill={spec.hair} />
      <circle cx="180" cy="132" r="62" fill={spec.hair} />
      <circle cx="180" cy="148" r="48" fill={spec.skin} />
      <ellipse cx="180" cy="112" rx="50" ry="24" fill={spec.hair} />
      <rect x="164" y="188" width="32" height="36" rx="10" fill={spec.skin} />
      <path d="M78 440c18-118 62-168 102-168s84 50 102 168" fill={spec.outfit} />
      <path d="M150 250h60l-8 28h-44z" fill={spec.bg} />
    </svg>
  );
}
