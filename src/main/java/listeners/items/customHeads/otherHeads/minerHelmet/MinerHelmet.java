package listeners.items.customHeads.otherHeads.minerHelmet;

import listeners.items.customHeads.BaseHead;

import java.util.List;

public class MinerHelmet extends BaseHead {

    public MinerHelmet() {
        super(
                "§eКаска рудокопа",
                List.of(
                        "",
                        "§7Накладывает эффект",
                        "§9Ночное зрение"
                ),
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDllYzNlNTM3MTBlMDQ5ZTUxMWQyOTZmMDI1N2Q3ZDAzM2Y1OGY4NmRlY2VmODVjNDQ3YTY2YTA4OThjNjk2YyJ9fX0="
        );
        setArmor(BASE_ARMOR_AMOUNT);
    }

}
