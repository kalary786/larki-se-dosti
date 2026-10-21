/**
 * Dosti visual identity — "night lounge".
 * Flat surfaces, soft shadows, outline icons. Gold is reserved for coins.
 * CSS variables in src/styles/global.css mirror these tokens.
 */

export const iconStroke = 1.75;

export const spacing = {
  4: 4,
  8: 8,
  12: 12,
  16: 16,
  20: 20,
  24: 24,
  32: 32,
} as const;

export const radius = {
  card: 20,
  button: 14,
  chip: 999,
  input: 16,
} as const;

export const typography = {
  fontFamily: '"Plus Jakarta Sans", system-ui, sans-serif',
  h1: { fontSize: 28, fontWeight: 700, lineHeight: 1.15 },
  h2: { fontSize: 22, fontWeight: 600, lineHeight: 1.2 },
  title: { fontSize: 17, fontWeight: 600, lineHeight: 1.3 },
  body: { fontSize: 15, fontWeight: 400, lineHeight: 1.45 },
  caption: { fontSize: 12, fontWeight: 500, lineHeight: 1.35 },
} as const;

export const darkColors = {
  background: "#14112B",
  surface: "#1F1B3A",
  elevated: "#2A2550",
  accent: "#FF7A59",
  gold: "#FFC75F",
  success: "#3DDC97",
  danger: "#FF4D6D",
  textPrimary: "#FFFFFF",
  textSecondary: "#B7B3D1",
  border: "rgba(255,255,255,0.08)",
  onAccent: "#14112B",
  input: "#14112B",
  shadow: "0 8px 24px rgba(0,0,0,0.18)",
  scrim: "rgba(8,6,20,0.72)",
} as const;

export const lightColors = {
  background: "#F7F5FF",
  surface: "#FFFFFF",
  elevated: "#FFFFFF",
  accent: "#FF7A59",
  gold: "#FFC75F",
  success: "#3DDC97",
  danger: "#FF4D6D",
  textPrimary: "#14112B",
  textSecondary: "#5A5674",
  border: "rgba(20,17,43,0.08)",
  onAccent: "#14112B",
  input: "#F7F5FF",
  shadow: "0 8px 24px rgba(20,17,43,0.06)",
  scrim: "rgba(20,17,43,0.46)",
} as const;

export const themes = {
  dark: darkColors,
  light: lightColors,
} as const;

export type ThemeName = keyof typeof themes;
export type ThemeColors = (typeof themes)[ThemeName];

export const theme = {
  spacing,
  radius,
  typography,
  iconStroke,
  themes,
} as const;
