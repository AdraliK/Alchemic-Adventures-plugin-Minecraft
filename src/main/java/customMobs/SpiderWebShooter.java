package customMobs;

import adralik.vanillaPlus.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.Spider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static adralik.vanillaPlus.Main.config;

public class SpiderWebShooter implements Listener {

    private static final String SPIDER_WEB_METADATA = "SpiderWeb";
    private final long coolDown = config.getLong("custom-mobs.spider.cooldown");; //600L

    private final Map<UUID, Boolean> activeSpiders = new HashMap<>();

    @EventHandler
    public void onSpiderTarget(EntityTargetEvent event) {
        if (event.getEntity() instanceof Spider && event.getTarget() instanceof Player) {
            Spider spider = (Spider) event.getEntity();
            Player target = (Player) event.getTarget();

            if (spider instanceof CaveSpider) return;

            // Проверяем, если задача уже активна для этого паука
            if (activeSpiders.getOrDefault(spider.getUniqueId(), false)) {
                return; // Если задача уже выполняется, выходим
            }

            // Устанавливаем флаг, что задача для этого паука запущена
            activeSpiders.put(spider.getUniqueId(), true);

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (spider.isDead() || target.isDead() || spider.getTarget() == null) {
                        this.cancel();
                        activeSpiders.remove(spider.getUniqueId()); // Убираем паука из активных, если он больше не целится
                        return;
                    }
                    
                    // Направление к игроку
                    Location startLocation = spider.getLocation().add(0, 1, 0);
                    Location targetLocation = target.getLocation().add(0, 1, 0);
                    Vector direction = targetLocation.toVector().subtract(startLocation.toVector()).normalize();

                    // Создаем паутину как падающий блок
                    FallingBlock web = spider.getWorld().spawnFallingBlock(startLocation, Material.COBWEB.createBlockData());
                    web.setDropItem(false);

                    // Начальная скорость и гравитация
                    double speed = 1;
                    double gravity = -0.07; // Пониженная гравитация для более плавной траектории
                    Vector velocity = direction.multiply(speed);
                    velocity.setY(velocity.getY() + 0.3); // Добавляем начальное ускорение вверх

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (!web.isValid()) {
                                spawnCobweb(web.getLocation());
                                this.cancel();
                                return;
                            }

                            // Применяем гравитацию
                            velocity.setY(velocity.getY() + gravity);
                            web.setVelocity(velocity);

                            // Проверяем, попала ли паутина в игрока
                            if (web.getLocation().distance(target.getLocation()) < 1.5) {
                                Location webLocation = target.getLocation().getBlock().getLocation();
                                spawnCobweb(webLocation);

                                web.remove();
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(Main.javaPlugin, 1L, 1L); // Запускаем задачу для проверки столкновения
                }
            }.runTaskTimer(Main.javaPlugin, 50L, coolDown); // Запускаем задачу с интервалом в 6 секунд
        }
    }

    private void spawnCobweb(Location webLocation){
        Block webBlock = webLocation.getBlock();
        webBlock.setType(Material.COBWEB);
        webBlock.setMetadata(SPIDER_WEB_METADATA, new FixedMetadataValue(Main.javaPlugin, true));
        deleteCobweb(webBlock);
    }

    private void deleteCobweb(Block webBlock){
        // Запускаем задачу по удалению блока паутины через 10 секунд
        new BukkitRunnable() {
            @Override
            public void run() {
                // Проверяем, что блок всё ещё является паутиной
                if (webBlock.getType() == Material.COBWEB) {
                    webBlock.setType(Material.AIR); // Удаляем блок, превращая его в воздух
                }
            }
        }.runTaskLater(Main.javaPlugin, 600L); // Задача будет выполнена через 200 тиков (10 секунд)
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Block block = e.getBlock();

        // Проверяем, является ли блок паутиной и имеет ли метаданные от паука
        if (block.getType() == Material.COBWEB && block.hasMetadata(SPIDER_WEB_METADATA)) {

            e.setDropItems(false);

            // Можно также удалить метаданные после разрушения блока, если нужно
            block.removeMetadata(SPIDER_WEB_METADATA, Main.javaPlugin);
        }
    }
}

