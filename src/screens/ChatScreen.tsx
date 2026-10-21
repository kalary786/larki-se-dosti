import { useState, type FormEvent } from "react";
import { ArrowLeft, Send } from "lucide-react";
import { Avatar } from "../components/Avatar";
import { IconCircleButton } from "../components/IconCircleButton";
import type { ChatMessage } from "../data/lounge";

type ChatScreenProps = {
  name: string;
  portrait: string;
  online: boolean;
  messages: ChatMessage[];
  onBack: () => void;
  onSend: (text: string) => void;
};

export function ChatScreen({ name, portrait, online, messages, onBack, onSend }: ChatScreenProps) {
  const [text, setText] = useState("");

  function send() {
    const next = text.trim();
    if (!next) return;
    onSend(next);
    setText("");
  }

  function submit(event: FormEvent) {
    event.preventDefault();
    send();
  }

  return (
    <section className="screen screen--stack">
      <div className="back-row">
        <IconCircleButton icon={ArrowLeft} label="Back to inbox" variant="ghost" onClick={onBack} />
        <Avatar portrait={portrait} name={name} size={40} online={online} />
        <div className="topbar__titles">
          <h1 className="title">{name}</h1>
          <p className={online ? "caption status-online" : "caption muted"}>{online ? "Online" : "Away"}</p>
        </div>
      </div>
      <div className="stack-scroll">
        {messages.length === 0 ? (
          <p className="caption muted">You opened this conversation. Write the first note.</p>
        ) : (
          <div className="chat-thread">
            {messages.map((message) => (
              <div key={message.id} className={`bubble bubble--${message.from === "me" ? "me" : "them"}`}>
                {message.text}
                <span className="bubble__time">{message.time}</span>
              </div>
            ))}
          </div>
        )}
      </div>
      <form className="composer" onSubmit={submit}>
        <label className="sr-only" htmlFor="chat-input">
          Message
        </label>
        <input
          id="chat-input"
          className="field"
          value={text}
          placeholder={`Message ${name.split(" ")[0]}`}
          onChange={(event) => setText(event.target.value)}
        />
        <IconCircleButton icon={Send} label="Send message" variant="accent" onClick={send} />
      </form>
    </section>
  );
}
