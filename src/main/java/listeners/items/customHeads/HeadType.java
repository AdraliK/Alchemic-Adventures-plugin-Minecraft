package listeners.items.customHeads;

import listeners.items.customHeads.fullMoonHeads.LeakyBag;
import listeners.items.customHeads.fullMoonHeads.PieceCheese;
import listeners.items.customHeads.fullMoonHeads.RadiantHelmet;

public enum HeadType {
    LEAKY_BAG(new LeakyBag()),
    PIECE_CHEESE(new PieceCheese()),
    RADIANT_HELMET(new RadiantHelmet());

    private final BaseHead baseHead;

    HeadType(BaseHead baseHead) {
        this.baseHead = baseHead;
        this.baseHead.setHeadType(this);
    }

    public BaseHead getBaseHead() {
        return baseHead;
    }
}