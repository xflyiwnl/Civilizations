package me.xflyiwnl.civilizations;

import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.api.event.player.PlayerLoadEvent;
import me.xflyiwnl.civilizations.command.AreaCommand;
import me.xflyiwnl.civilizations.command.EditorCommand;
import me.xflyiwnl.civilizations.command.MapCommand;
import me.xflyiwnl.civilizations.database.FlatDataSource;
import me.xflyiwnl.civilizations.listener.CivListener;
import me.xflyiwnl.civilizations.listener.PlayerListener;
import me.xflyiwnl.civilizations.object.CivMap;
import me.xflyiwnl.civilizations.object.CivPlayer;
import me.xflyiwnl.civilizations.object.area.Area;
import me.xflyiwnl.civilizations.object.helper.CivAction;
import me.xflyiwnl.civilizations.object.helper.CivBar;
import me.xflyiwnl.civilizations.object.helper.CivBoard;
import me.xflyiwnl.civilizations.object.helper.CivRunnable;
import me.xflyiwnl.civilizations.util.TextUtil;
import me.xflyiwnl.colorfulgui.ColorfulGUI;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class Civilizations extends JavaPlugin {

    private static Civilizations instance;

    private FlatDataSource dataSource = new FlatDataSource();

    private static final FileManager fileManager = new FileManager();
    private static ColorfulGUI guiApi;

    private NamespacedKey namespacedKey = new NamespacedKey(this, "civilizations");

    private List<CivBar> bars = new ArrayList<>();
    private List<CivBoard> boards = new ArrayList<>();

    @Override
    public void onEnable() {
        instance = this;
        guiApi = new ColorfulGUI(this);

        fileManager.generate();

        dataSource.load();

        registerListeners();
        registerCommands();

        TabAPI.getInstance().getEventBus().register(PlayerLoadEvent.class, event -> {
            TabPlayer tabPlayer = event.getPlayer();
            Player player = Bukkit.getPlayer(tabPlayer.getUniqueId());

            CivBoard board = Civilizations.getInstance().getCivBoards().stream()
                    .filter(b -> b.getPlayers().contains(player.getUniqueId()))
                    .findFirst().orElse(null);

            if (board == null) {
                TabAPI.getInstance().getScoreboardManager().resetScoreboard(tabPlayer);
            } else {
                board.show(tabPlayer);
            }

        });

        for (Player player : Bukkit.getOnlinePlayers()) {
            CivPlayer civPlayer = Civilizations.getInstance().getCivPlayer(player);

            if (civPlayer != null) return;

            civPlayer = new CivPlayer(player.getUniqueId());
            civPlayer.create(true);
        }

    }

    @Override
    public void onDisable() {
        dataSource.unload();
    }

    public void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new PlayerListener(), this);
        pm.registerEvents(new CivListener(), this);
    }

    public void registerCommands() {
        getCommand("map").setTabCompleter(new MapCommand());
        getCommand("map").setExecutor(new MapCommand());

        getCommand("area").setTabCompleter(new AreaCommand());
        getCommand("area").setExecutor(new AreaCommand());

        getCommand("editor").setTabCompleter(new EditorCommand());
        getCommand("editor").setExecutor(new EditorCommand());
    }

    public void sendTitle(Player player, String title, String subtitle) {
        if (player == null || !player.isOnline()) return;
        player.sendTitle(TextUtil.colorize(title), TextUtil.colorize(subtitle), 20, 20, 20);
    }

    public CivRunnable createRunnable(CivAction action, int time) {
        return new CivRunnable(action, time);
    }

    public CivBar createBar(String text, float progress, BarColor color, BarStyle style) {
        CivBar bar = new CivBar(text, progress, color, style);
        bars.add(bar);
        return bar;
    }

    public CivBar createBar() {
        CivBar bar = new CivBar();
        bars.add(bar);
        return bar;
    }

    public List<CivBar> getCivBars() {
        return bars;
    }

    public CivBoard createBoard(String title) {
        CivBoard board = new CivBoard(title);
        boards.add(board);
        return board;
    }

    public CivBoard createBoard() {
        CivBoard board = new CivBoard();
        boards.add(board);
        return board;
    }

    public List<CivBoard> getCivBoards() {
        return boards;
    }

    public CivMap getMap(String name) {
        return dataSource.map().get(name);
    }

    public List<CivMap> getMaps() {
        return dataSource.map().all();
    }

    public Area getArea(String name) {
        return dataSource.area().get(name);
    }

    public List<Area> getAreas() {
        return dataSource.area().all();
    }

    public CivMap getMapEditor(CivPlayer player) {
        return getMaps().stream()
                .filter(map -> map.getEditor() != null && map.getEditor().getPlayers().contains(player))
                .findFirst().orElse(null);
    }

    public CivPlayer getCivPlayer(Player player) {
        return getCivPlayer(player.getUniqueId());
    }

    public CivPlayer getCivPlayer(UUID uniqueId) {
        return dataSource.getCivPlayers().get(uniqueId);
    }

    public Map<UUID, CivPlayer> getCivPlayers() {
        return dataSource.getCivPlayers();
    }

    public FlatDataSource getDataSource() {
        return dataSource;
    }

    public static ColorfulGUI getGuiApi() {
        return guiApi;
    }

    public NamespacedKey getNamespacedKey() {
        return namespacedKey;
    }

    public static FileManager getFileManager() {
        return fileManager;
    }

    public static Civilizations getInstance() {
        return instance;
    }

}
