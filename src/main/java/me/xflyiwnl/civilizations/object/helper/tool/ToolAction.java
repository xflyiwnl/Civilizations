package me.xflyiwnl.civilizations.object.helper.tool;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public interface ToolAction {

    void execute(PlayerInteractEvent event);

}
