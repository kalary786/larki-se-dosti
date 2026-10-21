import { useEffect, type ReactNode } from "react";
import { X } from "lucide-react";
import { IconCircleButton } from "./IconCircleButton";

type BottomSheetProps = {
  open: boolean;
  title: string;
  onClose: () => void;
  children: ReactNode;
};

export function BottomSheet({ open, title, onClose, children }: BottomSheetProps) {
  useEffect(() => {
    if (!open) return;
    const onKey = (event: KeyboardEvent) => {
      if (event.key === "Escape") onClose();
    };
    window.addEventListener("keydown", onKey);
    return () => window.removeEventListener("keydown", onKey);
  }, [open, onClose]);

  if (!open) return null;

  return (
    <div className="overlay overlay--sheet" onClick={onClose}>
      <div
        className="sheet"
        role="dialog"
        aria-modal="true"
        aria-label={title}
        onClick={(event) => event.stopPropagation()}
      >
        <div className="sheet__handle" />
        <div className="sheet__head">
          <h2 className="title">{title}</h2>
          <IconCircleButton icon={X} label="Close" variant="ghost" size="sm" onClick={onClose} />
        </div>
        <div className="sheet__body">{children}</div>
      </div>
    </div>
  );
}
