package listeners.items.customHeads.otherHeads.undergroundDepthHelmet;

import helpers.DatapackUtils;
import listeners.items.customHeads.CustomHead;
import listeners.items.customHeads.HeadType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class UndergroundDepthHelmetListener implements Listener {

    @EventHandler
    public void onPotionEffect(EntityPotionEffectEvent e) {
        if (!(e.getEntity() instanceof Player player)) return;
        if (e.getAction() != EntityPotionEffectEvent.Action.ADDED) return;

        PotionEffect newEffect = e.getNewEffect();
        if (newEffect == null) return;

        PotionEffectType type = newEffect.getType();
        if (type != PotionEffectType.BLINDNESS && type != PotionEffectType.DARKNESS) return;

        if (!CustomHead.typeIs(
                player.getInventory().getHelmet(),
                HeadType.UNDERGROUND_DEPTH_HELMET
        )) return;

        DatapackUtils.grantAdvancement(player, "use_amethyst_head");

        e.setCancelled(true);
    }

}
