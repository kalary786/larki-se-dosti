import { useEffect, type ReactNode } from "react";
import { X } from "lucide-react";
import { IconCircleButton } from "./IconCircleButton";
import { GhostButton, PrimaryButton } from "./Buttons";

type Action = {
  label: string;
  onClick: () => void;
};

type ModalDialogProps = {
  open: boolean;
  title: string;
  onClose: () => void;
  children: ReactNode;
  primaryAction?: Action;
  secondaryAction?: Action;
};

export function ModalDialog({
  open,
  title,
  onClose,
  children,
  primaryAction,
  secondaryAction,
}: ModalDialogProps) {
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
    <div className="overlay overlay--modal" onClick={onClose}>
      <div
        className="modal"
        role="dialog"
        aria-modal="true"
        aria-labelledby="modal-title"
        onClick={(event) => event.stopPropagation()}
      >
        <div className="modal__head">
          <h2 id="modal-title" className="title">
            {title}
          </h2>
          <IconCircleButton icon={X} label="Close" variant="ghost" size="sm" onClick={onClose} />
        </div>
        <div className="body">{children}</div>
        {primaryAction || secondaryAction ? (
          <div className="modal__actions">
            {primaryAction ? (
              <PrimaryButton fullWidth onClick={primaryAction.onClick}>
                {primaryAction.label}
              </PrimaryButton>
            ) : null}
            {secondaryAction ? (
              <GhostButton fullWidth onClick={secondaryAction.onClick}>
                {secondaryAction.label}
              </GhostButton>
            ) : null}
          </div>
        ) : null}
      </div>
    </div>
  );
}
