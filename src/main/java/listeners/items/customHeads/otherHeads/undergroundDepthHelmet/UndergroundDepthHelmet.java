package listeners.items.customHeads.otherHeads.undergroundDepthHelmet;

import listeners.items.customHeads.BaseHead;

import java.util.List;

public class UndergroundDepthHelmet extends BaseHead {

    public UndergroundDepthHelmet() {
        super(
                "§eАметистовая жеода",
                List.of(
                        "",
                        "§7Даёт иммунитет",
                        "§7к ослепляющим эффектам"
                ),
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTRmNDc2YjRlMTMwNGVjOThlYzI0OTE5NTE4OTY2Y2E4MzJjODQwM2EwODJlZjEyYjM1M2I1OTBmMzhkODE4ZCJ9fX0="
        );
        setArmor(BASE_ARMOR_AMOUNT);
    }

}
