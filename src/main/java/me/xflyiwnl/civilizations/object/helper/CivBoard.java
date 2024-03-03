package me.xflyiwnl.civilizations.object.helper;

import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.api.scoreboard.Line;
import me.neznamy.tab.api.scoreboard.Scoreboard;
import me.neznamy.tab.api.scoreboard.ScoreboardManager;
import me.xflyiwnl.civilizations.Civilizations;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CivBoard {

    private String title;

    private Scoreboard board;

    private final TabAPI instance = TabAPI.getInstance();
    private final ScoreboardManager manager = instance.getScoreboardManager();

    private List<UUID> players = new ArrayList<>();

    public CivBoard() {
    }

    public CivBoard(String title) {
        this.title = title;

        createBoard();
    }

    protected void createBoard() {
        board = manager.createScoreboard(
                "civ-" + UUID.randomUUID().toString().split("-")[0],
                title,
                List.of()
        );
    }

    public CivBoard addLine(String value) {
        board.addLine(value);
        return this;
    }
    
    public CivBoard setLines(List<String> lines) {
        if (board.getLines().isEmpty()) {
            lines.forEach(s -> {
                board.addLine(s);
            });
            return this;
        }

        if (lines.size() > board.getLines().size()) {
            for (int i = 0; i < lines.size(); i++) {
                String text = lines.get(i);

                if (i + 1 >= board.getLines().size()) {
                    board.addLine(text);
                    continue;
                }

                board.getLines().get(i).setText(lines.get(i));
            }
        } else {
            for (int i = 0; i < board.getLines().size(); i++) {
                Line line = board.getLines().get(i);

                if (i + 1 > lines.size()) {
                    line.setText("");
                    continue;
                }

                line.setText(lines.get(i));

            }
        }

        return this;
    }

    public void show(Player player) {
        if (board == null) return;
        if (!getPlayers().contains(player.getUniqueId()))
            getPlayers().add(player.getUniqueId());
        TabPlayer tabPlayer = instance.getPlayer(player.getUniqueId());
        manager.showScoreboard(tabPlayer, board);
    }

    public void show(TabPlayer player) {
        if (board == null) return;
        if (!getPlayers().contains(player.getUniqueId()))
            getPlayers().add(player.getUniqueId());
        manager.showScoreboard(player, board);
    }

    public void hide(Player player) {
        if (board == null) return;
        if (getPlayers().contains(player.getUniqueId()))
            getPlayers().remove(player.getUniqueId());
        TabPlayer tabPlayer = instance.getPlayer(player.getUniqueId());
        manager.resetScoreboard(tabPlayer);
    }

    public void hide(TabPlayer player) {
        if (board == null) return;
        if (getPlayers().contains(player.getUniqueId()))
            getPlayers().remove(player.getUniqueId());
        manager.resetScoreboard(player);
    }

    public void remove() {
        Civilizations.getInstance().getCivBoards().remove(this);
        board.unregister();
    }

    public List<UUID> getPlayers() {
        return players;
    }
}
