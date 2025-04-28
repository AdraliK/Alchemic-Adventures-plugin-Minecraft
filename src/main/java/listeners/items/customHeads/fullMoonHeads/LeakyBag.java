package listeners.items.customHeads.fullMoonHeads;

import helpers.DatapackUtils;
import listeners.items.customHeads.BaseHead;
import listeners.items.customHeads.CustomHead;
import listeners.items.customHeads.HeadType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;

public class LeakyBag extends BaseHead implements Listener {

    private static final List<String> defaultLore = List.of(
            "§7☽ Полная луна",
            "",
            "§7Позволяет хранить",
            "§7уникальные предметы"
    );

    public LeakyBag() {
        super(
                "§eПрохудившийся мешок",
                defaultLore,
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGNiM2FjZGMxMWNhNzQ3YmY3MTBlNTlmNGM4ZTliM2Q5NDlmZGQzNjRjNjg2OTgzMWNhODc4ZjA3NjNkMTc4NyJ9fX0="
        );
    }

    public static final NamespacedKey HEAD_BAG_KEY = new NamespacedKey("vanillaplus", "bag_inventory");

    private final Map<UUID, ItemStack> openBags = new HashMap<>();
    private final Map<UUID, Inventory> openInventories = new HashMap<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.getAction().isRightClick() || !event.getPlayer().isSneaking()) return;

        ItemStack item = event.getItem();
        if (!HeadType.is(item, HeadType.LEAKY_BAG)) return;

        event.setCancelled(true);

        Player player = event.getPlayer();
        Inventory inventory = Bukkit.createInventory(null, 27, getName(item));

        ItemMeta meta = item.getItemMeta();
        if (meta != null && meta.getPersistentDataContainer().has(HEAD_BAG_KEY, PersistentDataType.STRING)) {
            String encoded = meta.getPersistentDataContainer().get(HEAD_BAG_KEY, PersistentDataType.STRING);
            ItemStack[] contents = decodeItems(encoded);
            inventory.setContents(contents);
        }

        DatapackUtils.grantAdvancement(player, "use_leakybag");

        openBags.put(player.getUniqueId(), item);
        openInventories.put(player.getUniqueId(), inventory);
        player.openInventory(inventory);
    }

    private String getName(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null && meta.hasDisplayName()) {
            Component displayName = meta.displayName();
            if (displayName != null) {
                return PlainTextComponentSerializer.plainText().serialize(displayName);
            }
        }
        return "Уникальный предмет";
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (!openInventories.containsKey(uuid)) return;

        Inventory inv = openInventories.remove(uuid);
        ItemStack bag = openBags.remove(uuid);
        ItemStack[] contents = inv.getContents();
        String encoded = encodeItems(contents);

        updateLore(bag, contents);

        ItemMeta meta = bag.getItemMeta();
        if (meta != null) {
            meta.getPersistentDataContainer().set(HEAD_BAG_KEY, PersistentDataType.STRING, encoded);
            bag.setItemMeta(meta);
        }
    }

    @EventHandler
    public void onCustomInventoryClick(InventoryClickEvent event) {
        cancelInvalidTransfer(
                event,
                this::isNotCustomHead,
                top -> openInventories.containsValue(top)
        );

        Player player = (Player) event.getWhoClicked();
        Inventory topInventory = event.getView().getTopInventory();

        if (!openInventories.containsValue(topInventory)) return;

        ItemStack openItem = openBags.get(player.getUniqueId());
        if (openItem == null) return;

        ItemStack clicked = event.getCurrentItem();
        ItemStack cursor = event.getCursor();
        int hotbarSlot = event.getHotbarButton();

        boolean movingSameItem = (clicked != null && clicked.equals(openItem))
                || (cursor != null && cursor.equals(openItem))
                || (hotbarSlot >= 0 && player.getInventory().getItem(hotbarSlot) != null
                        && player.getInventory().getItem(hotbarSlot).equals(openItem));

        if (movingSameItem) {
            event.setCancelled(true);
        }
    }

    private boolean isNotCustomHead(ItemStack item) {
        return !CustomHead.is(item) || HeadType.is(item, HeadType.LEAKY_BAG);
    }

    @EventHandler
    public void onShulkerClick(InventoryClickEvent event) {
        cancelInvalidTransfer(
                event,
                CustomHead::is,
                top -> top.getType() == InventoryType.SHULKER_BOX
        );
    }

    private void cancelInvalidTransfer(
            InventoryClickEvent event,
            Predicate<ItemStack> forbiddenPredicate,
            Predicate<Inventory> restrictedInventoryCheck
    ) {
        Inventory clicked = event.getClickedInventory();
        Inventory top = event.getView().getTopInventory();

        if (clicked == null || !restrictedInventoryCheck.test(top)) return;

        ClickType click = event.getClick();
        ItemStack current = event.getCurrentItem();
        ItemStack cursor = event.getCursor();

        boolean isShiftClick = click.isShiftClick();
        boolean isCursorEmpty = cursor == null || cursor.getType() == Material.AIR;

        if (clicked != top && isShiftClick && forbiddenPredicate.test(current)) {
            event.setCancelled(true);
            return;
        }

        if (clicked == top && !isCursorEmpty && forbiddenPredicate.test(cursor)) {
            event.setCancelled(true);
            return;
        }

        int hotbarButton = event.getHotbarButton();
        if (hotbarButton >= 0 && clicked == top) {
            ItemStack hotbarItem = event.getWhoClicked().getInventory().getItem(hotbarButton);
            if (forbiddenPredicate.test(hotbarItem)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onAnyInventoryDrag(InventoryDragEvent event) {
        Inventory top = event.getView().getTopInventory();
        InventoryType type = top.getType();
        ItemStack cursor = event.getOldCursor();

        boolean isDraggingCustomHeadIntoShulker = type == InventoryType.SHULKER_BOX && CustomHead.is(cursor);
        boolean isDraggingVanillaIntoCustomBag = openInventories.containsValue(top) && !CustomHead.is(cursor);

        if (!isDraggingCustomHeadIntoShulker && !isDraggingVanillaIntoCustomBag) return;

        for (int slot : event.getRawSlots()) {
            if (slot < top.getSize()) {
                event.setCancelled(true);
                break;
            }
        }
    }

    @EventHandler
    public void onBundlePlace(InventoryClickEvent event) {
        if (event.getClick().isRightClick()) return;
        
        ItemStack cursor = event.getCursor();
        ItemStack current = event.getCurrentItem();

        boolean currentIsBundle = current != null && current.getType() == Material.BUNDLE;
        boolean cursorIsBundle = cursor.getType() == Material.BUNDLE;

        if (CustomHead.is(cursor) && currentIsBundle) {
            event.setCancelled(true);
            return;
        }
        if (cursorIsBundle && CustomHead.is(current)) {
            event.setCancelled(true);
        }
    }

    private void updateLore(ItemStack bag, ItemStack[] contents) {
        ItemMeta itemMeta = bag.getItemMeta();
        List<String> lore = new ArrayList<>(defaultLore);
        lore.add("");

        List<ItemStack> nonEmptyItems = Arrays.stream(contents)
                .filter(item -> item != null && item.getType() != Material.AIR)
                .toList();

        if (nonEmptyItems.isEmpty()) {
            itemMeta.setLore(defaultLore);
            bag.setItemMeta(itemMeta);
            return;
        }

        for (int i = 0; i < Math.min(3, nonEmptyItems.size()); i++) {
            ItemStack item = nonEmptyItems.get(i);
            String name = getName(item);
            lore.add("§8" + name + " (" + item.getAmount() + "x)");
        }

        if (nonEmptyItems.size() > 3) {
            int remaining = nonEmptyItems.size() - 3;
            lore.add("§8и ещё " + remaining + " шт.");
        }

        itemMeta.setLore(lore);
        bag.setItemMeta(itemMeta);
    }

    private String encodeItems(ItemStack[] items) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(items.length);

            for (ItemStack item : items) {
                dataOutput.writeObject(item);
            }

            dataOutput.close();
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private ItemStack[] decodeItems(String data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            int size = dataInput.readInt();
            ItemStack[] items = new ItemStack[size];

            for (int i = 0; i < size; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }

            dataInput.close();
            return items;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ItemStack[27];
        }
    }
}
