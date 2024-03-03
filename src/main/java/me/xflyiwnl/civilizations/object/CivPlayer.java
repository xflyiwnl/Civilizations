package me.xflyiwnl.civilizations.object;

import me.xflyiwnl.civilizations.Civilizations;
import me.xflyiwnl.civilizations.object.editor.Editor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CivPlayer extends CivObject implements Saveable {

    public CivPlayer() {
    }

    public CivPlayer(UUID uniqueId) {
        super(uniqueId);
    }

    public void sendMessage(String message) {
        if (isOnline())
            getPlayer().sendMessage(message);
    }

    public boolean isOnline() {
        return getPlayer() != null && getPlayer().isOnline();
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(getUniqueId());
    }

    @Override
    public void create(boolean save) {
        Civilizations.getInstance().getCivPlayers().put(getUniqueId(), this);
    }

    @Override
    public void save() {

    }

    @Override
    public void remove() {
        Civilizations.getInstance().getCivPlayers().remove(getUniqueId());
    }

    @Override
    public String getName() {
        return Bukkit.getOfflinePlayer(getUniqueId()).getName();
    }

}
