package listeners.items.customHeads.otherHeads.minerHelmet;

import listeners.items.customHeads.BaseHead;

import java.util.List;

public class MinerHelmet extends BaseHead {

    public MinerHelmet() {
        super(
                "§eДворфийская лампа",
                List.of(
                        "",
                        "§7Накладывает эффект:",
                        "§9Ночное зрение"
                ),
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDc0Zjg5NDk0MGM4NGZhMjY3MTYwMWMzOGY5YmRlNTQ3Y2ZhMmQ3YzFhMGNhMzhkZjc5Y2RlMzc0MjJkNmMifX19"
        );
        setArmor(BASE_ARMOR_AMOUNT);
    }

}
