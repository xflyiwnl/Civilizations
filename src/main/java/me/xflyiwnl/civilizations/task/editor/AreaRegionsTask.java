package me.xflyiwnl.civilizations.task.editor;

import me.xflyiwnl.civilizations.Civilizations;
import me.xflyiwnl.civilizations.object.CivPlayer;
import me.xflyiwnl.civilizations.object.Point;
import me.xflyiwnl.civilizations.object.area.Area;
import me.xflyiwnl.civilizations.object.area.AreaRegion;
import me.xflyiwnl.civilizations.object.editor.AreaEditor;
import me.xflyiwnl.civilizations.object.editor.Editor;
import me.xflyiwnl.civilizations.task.CivTask;
import me.xflyiwnl.civilizations.util.Settinger;
import me.xflyiwnl.civilizations.util.Translator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;

import java.util.*;

public class AreaRegionsTask implements CivTask {

    private Area area;
    private CivPlayer civPlayer;

    public AreaRegionsTask(Area area, CivPlayer player) {
        this.area = area;
        this.civPlayer = player;
    }

    @Override
    public void execute() {

        civPlayer.sendMessage(Translator.ofString("editor.load-regions"));

        Map<Point, AreaRegion> blocks = new HashMap<>();

        for (AreaRegion region : area.getRegions()) {
            for (Point block : region.getBlocks()) {
                blocks.put(block, region);
            }
        }

        int all = blocks.size(); // количество блоков
        int canIterate = 5000; // сколько сможет итерировать сервер, мощность сервера

        int time = 1; // сколько времени будем итерировать
        if (blocks.size() > canIterate)
            time = blocks.size() / canIterate;
        int count = blocks.size() / time; // сколько будет сервер итерировать в итоге

        Civilizations.getInstance().createRunnable(() -> {
            if (blocks.isEmpty()) {
                civPlayer.sendMessage(Translator.ofString("editor.loaded"));

                Editor editor = new AreaEditor(area);

                area.getMap().setEditor(editor);
                editor.enable();

                editor.addPlayer(civPlayer);

                return true;
            }

            Iterator<Map.Entry<Point, AreaRegion>> iterator = blocks.entrySet().iterator();
            int iteratedCount = 0;
            while (iterator.hasNext() && iteratedCount < count) {
                Map.Entry<Point, AreaRegion> entry = iterator.next();

                Point point = entry.getKey();
                AreaRegion region = entry.getValue();
                Location location = point.asLocation();
                Block block = location.getBlock();

                if (block.getType() != Material.WHITE_WOOL) {
                    region.getBlocks().remove(point);
                    iterator.remove();
                    continue;
                }

                block.setType(region.getMaterial());

                iterator.remove();
                iteratedCount++;
            }

            int percent = (int) (((double) blocks.size() / all) * 100);
            Civilizations.getInstance().sendTitle(civPlayer.getPlayer(),
                    Settinger.ofString("editor.title"),
                    Settinger.ofString("editor.subtitle")
                            .replace("%iterated%", String.valueOf(all - blocks.size()))
                            .replace("%all%", String.valueOf(all))
                            .replace("%percent%", String.valueOf(100 - percent))
            );
            if (civPlayer.isOnline())
                civPlayer.getPlayer().playSound(civPlayer.getPlayer().getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 1f, 1f);

            return false;
        }, 1);

    }

}
