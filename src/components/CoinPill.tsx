import { Coins, Plus } from "lucide-react";
import { Icon } from "./Icon";

type CoinPillProps = {
  balance: number;
  onAdd: () => void;
};

export function CoinPill({ balance, onAdd }: CoinPillProps) {
  return (
    <div className="coin-pill">
      <span className="coin-pill__icon">
        <Icon icon={Coins} size={16} />
      </span>
      <span className="coin-pill__value">{balance.toLocaleString("en-US")}</span>
      <button type="button" className="coin-pill__add" aria-label="Add coins" onClick={onAdd}>
        <Icon icon={Plus} size={14} />
      </button>
    </div>
  );
}
