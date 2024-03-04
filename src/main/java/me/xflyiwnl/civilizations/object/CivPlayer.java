package me.xflyiwnl.civilizations.object;

import me.xflyiwnl.civilizations.Civilizations;
import me.xflyiwnl.civilizations.object.editor.Editor;
import me.xflyiwnl.civilizations.object.helper.CivReply;
import me.xflyiwnl.civilizations.object.helper.CivTool;
import me.xflyiwnl.civilizations.object.helper.tool.ToolType;
import me.xflyiwnl.civilizations.object.helper.tool.tools.AreaEditorTool;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CivPlayer extends CivObject implements Saveable {

    private CivTool tool;
    private CivReply reply;

    public CivPlayer() {
    }

    public CivPlayer(UUID uniqueId) {
        super(uniqueId);
    }

    public void sendMessage(String message) {
        if (isOnline())
            getPlayer().sendMessage(message);
    }

    public void changeTool(ToolType type) {
        if (!isOnline()) return;
        getPlayer().getInventory().clear();

        if (type == null) {
            tool = null;
            return;
        }

        switch (type) {
            case EDITOR_AREA -> {
                tool = new AreaEditorTool(this);
            }
        }

        if (tool == null) return;

        tool.getItems().forEach((slot, item) ->
                getPlayer().getInventory().setItem(slot, item.getItemStack()));

    }

    public boolean hasTool() {
        return getTool() != null;
    }

    public CivTool getTool() {
        return tool;
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

    public boolean hasReply() {
        return reply != null;
    }

    public CivReply getReply() {
        return reply;
    }

    public void setReply(CivReply reply) {
        this.reply = reply;
    }

}
