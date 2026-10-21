import { Coins } from "lucide-react";
import { AdSlot } from "../components/AdSlot";
import { Icon } from "../components/Icon";
import { PrimaryButton } from "../components/Buttons";
import type { CoinPack, Gift } from "../data/lounge";

type WalletScreenProps = {
  balance: number;
  packs: CoinPack[];
  gifts: Gift[];
  selectedPackId: string;
  onSelectPack: (id: string) => void;
  onBuy: () => void;
  onGift: (id: string) => void;
};

export function WalletScreen({
  balance,
  packs,
  gifts,
  selectedPackId,
  onSelectPack,
  onBuy,
  onGift,
}: WalletScreenProps) {
  return (
    <section className="screen">
      <div className="stack">
        <div className="topbar__titles">
          <p className="caption muted">Wallet</p>
          <h1 className="h2">Coins</h1>
        </div>
        <div className="balance-hero">
          <div className="balance-hero__row">
            <span className="gold">
              <Icon icon={Coins} size={28} />
            </span>
            <h2 className="h1 gold">{balance.toLocaleString("en-US")}</h2>
          </div>
          <p className="body muted">Used for gifts. Highlights stay out of this preview.</p>
        </div>
        <div className="section-label">
          <p className="title">Add coins</p>
          <div className="packages">
            {packs.map((pack) => {
              const selected = pack.id === selectedPackId;
              return (
                <button
                  key={pack.id}
                  type="button"
                  className={`package${selected ? " package--selected" : ""}`}
                  aria-pressed={selected}
                  onClick={() => onSelectPack(pack.id)}
                >
                  <span className="group">
                    <span className="title">{pack.coins} coins</span>
                    <span className="caption muted">
                      {pack.label} · {pack.note}
                    </span>
                  </span>
                  <span className="gold">
                    <Icon icon={Coins} size={18} />
                  </span>
                </button>
              );
            })}
          </div>
          <PrimaryButton fullWidth onClick={onBuy}>
            Add selected coins
          </PrimaryButton>
        </div>
        <div className="section-label">
          <p className="title">Gifts</p>
          <div className="gifts">
            {gifts.map((gift) => (
              <button key={gift.id} type="button" className="gift" onClick={() => onGift(gift.id)}>
                <span className="gift__mark" aria-hidden>
                  {gift.mark}
                </span>
                <span className="title">{gift.name}</span>
                <span className="caption gold">{gift.coins} coins</span>
              </button>
            ))}
          </div>
        </div>
        <AdSlot title="A seat by the window" body="Sponsor placement. Nothing here is a real offer." />
      </div>
    </section>
  );
}
