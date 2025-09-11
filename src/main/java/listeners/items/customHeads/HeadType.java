package listeners.items.customHeads;

import listeners.items.customHeads.fullMoonHeads.LeakyBag;
import listeners.items.customHeads.fullMoonHeads.PieceCheese;
import listeners.items.customHeads.fullMoonHeads.RadiantHelmet;
import listeners.items.customHeads.otherHeads.minerHelmet.MinerHelmet;
import listeners.items.customHeads.otherHeads.skinHead.SkinHead;
import listeners.items.customHeads.otherHeads.undergroundDepthHelmet.UndergroundDepthHelmet;

public enum HeadType {
    LEAKY_BAG(new LeakyBag()),
    PIECE_CHEESE(new PieceCheese()),
    RADIANT_HELMET(new RadiantHelmet()),
    SKIN_HEAD(new SkinHead()),
    MINER_HELMET(new MinerHelmet()),
    UNDERGROUND_DEPTH_HELMET(new UndergroundDepthHelmet());

    private final BaseHead baseHead;

    HeadType(BaseHead baseHead) {
        this.baseHead = baseHead;
        this.baseHead.setHeadType(this);
    }

    public BaseHead getBaseHead() {
        return baseHead;
    }
}