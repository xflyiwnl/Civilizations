package me.xflyiwnl.civilizations.listener;

import me.xflyiwnl.civilizations.Civilizations;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

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

}
