package me.xflyiwnl.civilizations.object.helper;

import me.xflyiwnl.civilizations.Civilizations;
import me.xflyiwnl.civilizations.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CivBar {

    private String text;
    private float progress;
    private BarColor color;
    private BarStyle style;

    private BossBar bossBar;
    private List<UUID> players = new ArrayList<>();

    public CivBar() {
    }

    public CivBar(String text, float progress, BarColor color, BarStyle style) {
        this.text = text;
        this.progress = progress;
        this.color = color;
        this.style = style;

        createBar();
    }

    public void createBar() {
        bossBar = Bukkit.createBossBar(
                TextUtil.colorize(text),
                color, style
        );
    }

    public void show(Player player) {
        if (!players.contains(player.getUniqueId()))
            players.add(player.getUniqueId());
        bossBar.addPlayer(player);
    }

    public void hide(Player player) {
        if (!players.contains(player.getUniqueId()))
            players.add(player.getUniqueId());
        bossBar.removePlayer(player);
    }

    public void remove() {
        Civilizations.getInstance().getCivBars().remove(this);
        bossBar.getPlayers().forEach(bossBar::removePlayer);
    }

    public void setText(String text) {
        this.text = text;

        bossBar.setTitle(TextUtil.colorize(text));
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;

        bossBar.setProgress(progress);
    }

    public void setColor(BarColor color) {
        this.color = color;

        bossBar.setColor(color);
    }

    public void setStyle(BarStyle style) {
        this.style = style;

        bossBar.setStyle(style);
    }

    public void setBossBar(BossBar bossBar) {
        this.bossBar = bossBar;
    }

    public List<UUID> getPlayers() {
        return players;
    }
}
