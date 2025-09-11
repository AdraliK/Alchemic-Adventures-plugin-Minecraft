package listeners.items.customHeads.fullMoonHeads;

import helpers.DatapackUtils;
import listeners.items.customHeads.BaseHead;
import listeners.items.customHeads.CustomHead;
import listeners.items.customHeads.HeadType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class RadiantHelmet extends BaseHead implements Listener {

    private static final double DAMAGE_BONUS = 0.1;

    public RadiantHelmet() {
        super(
                "§eЛучезарный шлем",
                List.of(
                        "§7ऑ Полнолуние",
                        "",
                        "§7Усиливает урон:",
                        "§9+10% к урону по монстрам"
                ),
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTNlNGM2Y2FmMDUwOTJlYWM0NzAxY2RiNjYzNTg0YTMyOTM3YzAwZGQzNjE4ZGRmMmM1ZGI4ODY4MDM3ZDhjMSJ9fX0="
        );
        setArmor(BASE_ARMOR_AMOUNT);
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        if (!(event.getEntity() instanceof Monster)) return;

        ItemStack helmet = player.getInventory().getHelmet();
        if (!CustomHead.typeIs(helmet, HeadType.RADIANT_HELMET)) return;

        DatapackUtils.grantAdvancement(player, "use_radianthelmet");

        double originalDamage = event.getDamage();
        double bonus = originalDamage * DAMAGE_BONUS;
        event.setDamage(originalDamage + bonus);
    }
}
