import { useState } from "react";
import { Search } from "lucide-react";
import { Avatar } from "../components/Avatar";
import { Badge } from "../components/Badge";
import { EmptyState } from "../components/EmptyState";
import { Icon } from "../components/Icon";
import { MessageCircle } from "lucide-react";

export type InboxItem = {
  id: string;
  name: string;
  portrait: string;
  online: boolean;
  preview: string;
  time: string;
  unread: number;
};

type InboxScreenProps = {
  items: InboxItem[];
  onOpen: (id: string) => void;
};

export function InboxScreen({ items, onOpen }: InboxScreenProps) {
  const [query, setQuery] = useState("");
  const needle = query.trim().toLowerCase();
  const visible = needle
    ? items.filter((item) => item.name.toLowerCase().includes(needle) || item.preview.toLowerCase().includes(needle))
    : items;

  return (
    <section className="screen">
      <div className="stack">
        <div className="topbar__titles">
          <p className="caption muted">Conversations</p>
          <h1 className="h2">Inbox</h1>
        </div>
        <label className="sr-only" htmlFor="inbox-search">
          Search conversations
        </label>
        <div className="search">
          <span className="search__icon" aria-hidden>
            <Icon icon={Search} size={18} />
          </span>
          <input
            id="inbox-search"
            className="field"
            value={query}
            placeholder="Search names"
            onChange={(event) => setQuery(event.target.value)}
          />
        </div>
        {visible.length === 0 ? (
          <EmptyState
            icon={MessageCircle}
            title={needle ? "No conversations found" : "No messages yet"}
            body={needle ? "Try another name." : "Say hello in the lounge and the thread will land here."}
          />
        ) : (
          <div>
            {visible.map((item) => (
              <button key={item.id} type="button" className="person-row" onClick={() => onOpen(item.id)}>
                <Avatar portrait={item.portrait} name={item.name} online={item.online} />
                <span className="person-row__copy">
                  <span className="title">{item.name}</span>
                  <span className="caption muted">{item.preview}</span>
                </span>
                <span className="group align-end">
                  <span className="caption muted">{item.time}</span>
                  <Badge count={item.unread} />
                </span>
              </button>
            ))}
          </div>
        )}
      </div>
    </section>
  );
}
