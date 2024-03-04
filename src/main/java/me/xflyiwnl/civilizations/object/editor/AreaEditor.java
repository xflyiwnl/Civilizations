package me.xflyiwnl.civilizations.object.editor;

import me.xflyiwnl.civilizations.Civilizations;
import me.xflyiwnl.civilizations.object.CivMap;
import me.xflyiwnl.civilizations.object.CivPlayer;
import me.xflyiwnl.civilizations.object.area.Area;
import me.xflyiwnl.civilizations.object.area.AreaRegion;
import me.xflyiwnl.civilizations.object.helper.CivBar;
import me.xflyiwnl.civilizations.object.helper.CivBoard;
import me.xflyiwnl.civilizations.object.helper.CivRunnable;
import me.xflyiwnl.civilizations.object.helper.tool.ToolType;
import me.xflyiwnl.civilizations.util.Translator;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AreaEditor implements Editor {

    private Area area;
    private CivMap map;

    private CivBar bar;
    private CivBoard board;

    private CivRunnable runnable;

    private AreaRegion region;

    private List<CivPlayer> players = new ArrayList<>();

    public AreaEditor(Area area) {
        this.area = area;
        this.map = area.getMap();
    }

    @Override
    public void enable() {
        map.setEditor(this);

        bar = Civilizations.getInstance().createBar(
                "#FFF4D6Вы сейчас в режиме #6CE25Fредактора",
                1f,
                BarColor.YELLOW,
                BarStyle.SEGMENTED_6
        );

        board = Civilizations.getInstance().createBoard(
                "#FCD05CЗа Цивилизацию!")
                .setLines(Arrays.asList(
                        " ",
                        " #FFF4D6карта: #F1B823" + map.getName(),
                        " #FFF4D6терр-а: #6CE25F" + area.getName(),
                        " ",
                        " #FFF4D6регионы: #FCD05C" + (area.getRegions().isEmpty() ? "0" : area.getRegions().size()),
                        " #FFF4D6редактируется: #F1B823" + (region == null ? "Нет" : region.getFormattedName()),
                        " "
                ));

        runnable = Civilizations.getInstance().createRunnable(() -> {
            if (board == null) return false;
            board.setLines(Arrays.asList(
                    " ",
                    " #FFF4D6карта: #F1B823" + map.getName(),
                    " #FFF4D6терр-а: #6CE25F" + area.getName(),
                    " ",
                    " #FFF4D6регионы: #FCD05C" + (area.getRegions().isEmpty() ? "0" : area.getRegions().size()),
                    " #FFF4D6редактируется: #F1B823" + (region == null ? "Нет" : region.getFormattedName()),
                    " "
            ));
            return false;
        }, 1);

    }

    @Override
    public void disable() {
        runnable.cancel();

        board.remove();
        bar.remove();

        map.setEditor(null);

        getPlayers().forEach(player -> {
            player.changeTool(null);
            player.setReply(null);
        });

    }

    @Override
    public void addPlayer(CivPlayer player) {

        if (!players.contains(player))
            players.add(player);

        bar.show(player.getPlayer());
        board.show(player.getPlayer());

        if (map.getSpawn() != null)
            player.getPlayer().teleport(map.getSpawn());

        player.changeTool(ToolType.EDITOR_AREA);

        player.sendMessage(Translator.ofString("editor.enter"));

    }

    @Override
    public void removePlayer(CivPlayer player) {

        players.remove(player);

        bar.hide(player.getPlayer());
        board.hide(player.getPlayer());

        player.changeTool(null);
        player.setReply(null);

        player.sendMessage(Translator.ofString("editor.quit"));
    }

    @Override
    public EditorType getType() {
        return EditorType.AREA;
    }

    @Override
    public List<CivPlayer> getPlayers() {
        return players;
    }

    public Area getArea() {
        return area;
    }

    public CivMap getMap() {
        return map;
    }

    public CivBar getBar() {
        return bar;
    }

    public CivBoard getBoard() {
        return board;
    }

    public CivRunnable getRunnable() {
        return runnable;
    }

    public AreaRegion getRegion() {
        return region;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public void setMap(CivMap map) {
        this.map = map;
    }

    public void setBar(CivBar bar) {
        this.bar = bar;
    }

    public void setBoard(CivBoard board) {
        this.board = board;
    }

    public void setRunnable(CivRunnable runnable) {
        this.runnable = runnable;
    }

    public void setRegion(AreaRegion region) {
        this.region = region;
    }

    public void setPlayers(List<CivPlayer> players) {
        this.players = players;
    }
}
