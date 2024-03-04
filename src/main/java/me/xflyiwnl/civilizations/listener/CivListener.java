package me.xflyiwnl.civilizations.listener;

import me.xflyiwnl.civilizations.Civilizations;
import me.xflyiwnl.civilizations.object.CivPlayer;
import me.xflyiwnl.civilizations.object.helper.tool.ToolItem;
import me.xflyiwnl.civilizations.object.helper.tool.ToolType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;
import java.util.stream.Collectors;

public class CivListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        Civilizations.getInstance().getCivBars().forEach(civBar -> {
            if (civBar.getPlayers().contains(player.getUniqueId())) {
                civBar.show(player);
            }
        });

    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        if (event.getInventory() == null) return;
        if (event.getCurrentItem() == null) return;

        Player player = (Player) event.getWhoClicked();
        CivPlayer civPlayer = Civilizations.getInstance().getCivPlayer(player);

        if (!civPlayer.hasTool()) return;

        ItemStack itemStack = event.getCurrentItem();
        ItemMeta meta = itemStack.getItemMeta();

        if (meta == null) return;

        PersistentDataContainer container = meta.getPersistentDataContainer();

        if (!container.has(Civilizations.getInstance().getNamespacedKey(), PersistentDataType.STRING)) return;

        String formattedId = container.get(Civilizations.getInstance().getNamespacedKey(), PersistentDataType.STRING);
        UUID uniqueId = UUID.fromString(formattedId);

        ToolItem toolItem = civPlayer.getTool().getItems().values().stream()
                .filter(item -> item.getUniqueId() != uniqueId)
                .findFirst().orElse(null);

        if (toolItem == null) return;

        event.setResult(Event.Result.DENY);

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        if (event.getItem() == null) return;

        Player player = event.getPlayer();
        CivPlayer civPlayer = Civilizations.getInstance().getCivPlayer(player);

        if (!civPlayer.hasTool()) return;

        ItemStack itemStack = event.getItem();
        ItemMeta meta = itemStack.getItemMeta();

        if (meta == null) return;

        PersistentDataContainer container = meta.getPersistentDataContainer();

        if (!container.has(Civilizations.getInstance().getNamespacedKey(), PersistentDataType.STRING)) return;

        String formattedId = container.get(Civilizations.getInstance().getNamespacedKey(), PersistentDataType.STRING);
        UUID uniqueId = UUID.fromString(formattedId);

        ToolItem toolItem = civPlayer.getTool().getItems().values().stream()
                .filter(item -> item.getUniqueId().equals(uniqueId))
                .findFirst().orElse(null);

        if (toolItem == null) return;

        event.setCancelled(true);

        if (toolItem.getAction() == null) return;

        toolItem.getAction().execute(event);

    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {

        Player player = event.getPlayer();
        CivPlayer civPlayer = Civilizations.getInstance().getCivPlayer(player);

        if (!civPlayer.hasTool()) return;

        ItemStack itemStack = event.getItemDrop().getItemStack();
        ItemMeta meta = itemStack.getItemMeta();

        if (meta == null) return;

        PersistentDataContainer container = meta.getPersistentDataContainer();

        if (!container.has(Civilizations.getInstance().getNamespacedKey(), PersistentDataType.STRING)) return;

        String formattedId = container.get(Civilizations.getInstance().getNamespacedKey(), PersistentDataType.STRING);
        UUID uniqueId = UUID.fromString(formattedId);

        ToolItem toolItem = civPlayer.getTool().getItems().values().stream()
                .filter(item -> item.getUniqueId() != uniqueId)
                .findFirst().orElse(null);

        if (toolItem == null) return;

        event.setCancelled(true);

    }

    @EventHandler
    public void onChat(PlayerChatEvent event) {

        Player player = event.getPlayer();
        CivPlayer civPlayer = Civilizations.getInstance().getCivPlayer(player);

        if (!civPlayer.hasReply()) return;

        boolean result = civPlayer.getReply().getChatAction().execute(event);

        if (result) {
            civPlayer.setReply(null);
            event.setCancelled(true);
        } else {
            event.setCancelled(true);
        }

    }

}
