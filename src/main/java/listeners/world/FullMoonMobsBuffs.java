package listeners.world;

import adralik.vanillaPlus.Main;
import helpers.RandomLoot;
import listeners.items.customHeads.HeadType;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

import static adralik.vanillaPlus.Main.config;

public class FullMoonMobsBuffs implements Listener {

    private final String configPath = "full-moon-mob-buffs";

    private final double healthMultiplier = config.getDouble(configPath + ".health-multiplier");
    private final double damageMultiplier = config.getDouble(configPath + ".damage-multiplier");
    private final double experienceMultiplier = config.getDouble(configPath + ".experience-multiplier");;

    private final Random random = new Random();

    // Проверка фазы луны
    private boolean isFullMoon(World world) {
        long time = world.getFullTime();
        long days = time / 24000;
        int phase = (int) (days % 8); // 8 фаз луны, полнолуние - это 0

        // Проверка фазы луны (полнолуние) и того, что время между 13000 и 23000 (ночь)
        long currentTimeOfDay = time % 24000; // Текущее время в пределах одного дня
        boolean isNight = currentTimeOfDay >= 13000 && currentTimeOfDay <= 23000;

        return phase == 0 && isNight;
    }

    // Увеличение урона враждебных мобов
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        World world = event.getEntity().getWorld();

        if (shouldCancelEvent(event.getEntity(), world)) return;

        Entity damager = event.getDamager();

        // Проверяем ближний бой
        if (damager instanceof Monster) {
            // Увеличиваем урон всех враждебных мобов ближнего боя на 50%
            double originalDamage = event.getDamage();
            event.setDamage(originalDamage * damageMultiplier);
        }

        // Проверяем, если урон был от стрелы
        if (damager instanceof Arrow) {
            Arrow arrow = (Arrow) damager;
            if (arrow.getShooter() instanceof Skeleton) {
                // Увеличиваем урон стрел от скелета
                double originalDamage = event.getDamage();
                event.setDamage(originalDamage * damageMultiplier);
            }
        }
    }

    // Зарядка криперов и увеличение здоровья в полнолуние
    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        LivingEntity entity = event.getEntity();
        World world = entity.getWorld();

        if (shouldCancelEvent(entity, world)) return;

        // Увеличиваем здоровье всех враждебных мобов
        if (entity instanceof Monster) {
            AttributeInstance healthAttribute = entity.getAttribute(Attribute.MAX_HEALTH);
            if (healthAttribute != null) {
                double originalHealth = healthAttribute.getBaseValue();
                double newHealth = originalHealth * healthMultiplier; // Увеличиваем здоровье на 50%
                healthAttribute.setBaseValue(newHealth);
                entity.setHealth(newHealth); // Устанавливаем новое здоровье
            }
            spawnParticlesOnMobs(entity);
        }

        // Заряжаем крипера
        if (entity instanceof Creeper) {
            Creeper creeper = (Creeper) entity;
            creeper.setPowered(true); // Делаем крипера заряженным
        }
    }


    private void spawnParticlesOnMobs(LivingEntity mob) {
        // Запускаем задачу для отображения частиц
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!mob.isValid()) {
                    cancel(); // Останавливаем, если моб больше не существует
                    return;
                }
                // Отображаем частицы
                mob.getWorld().spawnParticle(
                        Particle.ENTITY_EFFECT,
                        mob.getLocation().add(0, 1, 0),
                        3, 0.2, 0.7, 0.2, 1,
                        Color.RED
                );
            }
        }.runTaskTimer(Main.javaPlugin, 0, 10); // Запускаем задачу с заданным интервалом
    }

    // Обработчик смерти моба
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        World world = entity.getWorld();

        if (entity.getKiller() == null) return;
        if (shouldCancelEvent(entity, world)) return;

        // Проверяем, является ли моб враждебным и идет ли сейчас полнолуние
        if (entity instanceof Monster) {
            ItemStack extraLoot = getRandomLoot(randomLootList);
            if (extraLoot != null) {
                event.getDrops().add(extraLoot); // Добавляем дополнительный предмет в лут
            }

            // Увеличиваем опыт при смерти моба
            int defaultExperience = event.getDroppedExp();
            int increasedExperience = (int) (defaultExperience * experienceMultiplier);
            event.setDroppedExp(increasedExperience); // Устанавливаем новое значение опыта
        }
    }

    private boolean shouldCancelEvent(Entity entity, World world) {
        if (!isFullMoon(world)) return true;
        if (entity instanceof Silverfish) return true;
        if (world.getEnvironment() != World.Environment.NORMAL) return true;
        return false;
    }

    List<RandomLoot> randomLootList = Arrays.asList(
            new RandomLoot(Material.REDSTONE, 0.35),
            new RandomLoot(Material.GLOWSTONE_DUST, 0.35),
            new RandomLoot(Material.LAPIS_LAZULI, 0.35),

            new RandomLoot(Material.IRON_NUGGET, 2, 4, 0.25),
            new RandomLoot(Material.GOLD_NUGGET, 2, 4, 0.25),
            new RandomLoot(Material.COPPER_INGOT, 0.25),

            new RandomLoot(Material.SLIME_BALL, 0.08),

            new RandomLoot(HeadType.LEAKY_BAG, 0.05),
            new RandomLoot(HeadType.PIECE_CHEESE, 0.03),
            new RandomLoot(HeadType.RADIANT_HELMET, 0.03)
    );

    private ItemStack getRandomLoot(List<RandomLoot> randomLootList) {
        if (isEmpty(randomLootList)) return null;

        List<RandomLoot> bestCandidates = findBestCandidates(randomLootList);

        if (isEmpty(bestCandidates)) return null;

        return selectRandomItem(bestCandidates);
    }

    private <T> boolean isEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }

    private List<RandomLoot> findBestCandidates(List<RandomLoot> randomLootList) {
        double chance = random.nextDouble();
        double minChance = 1.1;
        List<RandomLoot> bestCandidates = null;

        for (RandomLoot loot : randomLootList) {
            double lootChance = loot.getChance();

            if (lootChance < chance) continue;

            if (lootChance < minChance) {
                minChance = lootChance;
                bestCandidates = new ArrayList<>();
                bestCandidates.add(loot);
            } else if (lootChance == minChance) {
                bestCandidates.add(loot);
            }
        }

        return bestCandidates;
    }

    private ItemStack selectRandomItem(List<RandomLoot> candidates) {
        int index = random.nextInt(candidates.size());
        RandomLoot loot = candidates.get(index);

        return loot.getItem();
    }
}

