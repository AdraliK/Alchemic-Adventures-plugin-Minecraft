package listeners.items.customHeads.otherHeads.skinHead;

import listeners.items.customHeads.BaseHead;

import java.util.List;

public class SkinHead extends BaseHead {
    
    public SkinHead() {
        super(
                "§eШаблон головы",
                List.of(
                        "",
                        "§7Применяет облик игрока",
                        "",
                        "§8Использование: Shift + ПКМ"
                ),
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODlhOTk1OTI4MDkwZDg0MmQ0YWZkYjIyOTZmZmUyNGYyZTk0NDI3MjIwNWNlYmE4NDhlZTQwNDZlMDFmMzE2OCJ9fX0="
        );
        setArmor(BASE_ARMOR_AMOUNT);
    }

}
