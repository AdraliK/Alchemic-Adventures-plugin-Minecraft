package helpers;

import adralik.vanillaPlus.Main;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class CauldronData {

    private Location location;
    private List<ItemStack> potionList;
    private BukkitRunnable particleTask;

    public CauldronData (Location location, ItemStack potion){
        this.location = location;
        potionList = new ArrayList<>();
        potionList.add(potion);
    }

    public Location getLocation() {
        return location;
    }

    public List<ItemStack> getPotionList() {
        return potionList;
    }

    public PotionEffect getPotionEffectFromList(int index){
        PotionMeta potionMeta = (PotionMeta) potionList.get(index).getItemMeta();
        return potionMeta.getBasePotionType().getPotionEffects().getFirst();
    }

    public boolean addPotion(ItemStack potion){
        PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
        PotionEffect potionEffect = potionMeta.getBasePotionType().getPotionEffects().getFirst();

        if (potionList.stream().anyMatch(x -> ((PotionMeta) x.getItemMeta())
                .getBasePotionType()
                .getPotionEffects()
                .getFirst()
                .equals(potionEffect))){
            return false;
        }

        potionList.add(potion);

        return true;
    }

    public void startParticleEffect(Block block){
        if (particleTask != null && !particleTask.isCancelled()) {
            return; // Если задача уже запущена, не создаем новую
        }
        particleTask = new BukkitRunnable() {
            @Override
            public void run() {
                World world = block.getWorld();
                world.spawnParticle(Particle.SCULK_CHARGE_POP, block.getLocation().add(0.5, 1, 0.5), 10, 0.25, 0.2, 0.25, 0.03);
            }
        };

        particleTask.runTaskTimer(Main.javaPlugin, 0L, 7L);

    }

    public void stopParticleEffect() {
        if (particleTask != null) {
            particleTask.cancel();
            particleTask = null; // Обнуляем ссылку на задачу, чтобы можно было запустить её снова при необходимости
        }
    }
}
