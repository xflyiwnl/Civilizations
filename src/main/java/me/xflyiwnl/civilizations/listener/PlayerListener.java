package me.xflyiwnl.civilizations.listener;

import me.xflyiwnl.civilizations.Civilizations;
import me.xflyiwnl.civilizations.object.CivPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        CivPlayer civPlayer = Civilizations.getInstance().getCivPlayer(player);

        if (civPlayer != null) {
            civPlayer.changeTool(civPlayer.getTool() != null ? civPlayer.getTool().getType() : null);
            return;
        }

        civPlayer = new CivPlayer(player.getUniqueId());
        civPlayer.create(true);

    }


}
