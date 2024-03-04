package me.xflyiwnl.civilizations.task.editor;

import me.xflyiwnl.civilizations.Civilizations;
import me.xflyiwnl.civilizations.object.CivPlayer;
import me.xflyiwnl.civilizations.object.Point;
import me.xflyiwnl.civilizations.object.area.Area;
import me.xflyiwnl.civilizations.object.editor.EditorType;
import me.xflyiwnl.civilizations.task.CivTask;
import me.xflyiwnl.civilizations.util.Settinger;
import me.xflyiwnl.civilizations.util.Translator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class AreaLoadTask implements CivTask {

    private EditorType type = EditorType.AREA;
    private CivPlayer civPlayer;
    private Area area;

    public AreaLoadTask(CivPlayer civPlayer, Area area) {
        this.civPlayer = civPlayer;
        this.area = area;
    }

    @Override
    public void execute() {

        civPlayer.sendMessage(Translator.ofString("editor.editor-type")
                .replace("%type%", type.getName()));
        civPlayer.sendMessage(Translator.ofString("editor.load-area")
                .replace("%area%", area.getFormattedName()));

        Point startPoint = area.getMap().getStartPoint();
        Point endPoint = area.getMap().getEndPoint();

        List<Location> blocks = new ArrayList<>();

        int y = startPoint.getY();
        for (int x = startPoint.getX(); x <= endPoint.getX(); x++) {
            for (int z = startPoint.getZ(); z <= endPoint.getZ(); z++) {
                Location location = new Location(startPoint.getWorld(), x, y, z);
                Block block = location.getBlock();

                if (block.getType() != Material.BEDROCK) continue;

                blocks.add(location);
            }
        }

        if (blocks.isEmpty()) {
            civPlayer.sendMessage(Translator.ofString("editor.no-blocks"));
            return;
        }

        civPlayer.sendMessage(Translator.ofString("editor.blocks-found")
                .replace("%count%", String.valueOf(blocks.size())));
        civPlayer.sendMessage(Translator.ofString("editor.start-fill"));

        int all = blocks.size(); // количество блоков
        int canIterate = 5000; // сколько сможет итерировать сервер, мощность сервера

        int time = 1; // сколько времени будем итерировать
        if (blocks.size() > canIterate)
            time = blocks.size() / canIterate;
        int count = blocks.size() / time; // сколько будет сервер итерировать в итоге

        Civilizations.getInstance().createRunnable(() -> {
            if (blocks.isEmpty()) {
                AreaRegionsTask regionTask = new AreaRegionsTask(area, civPlayer);
                regionTask.execute();
                return true;
            }

            List<Location> deleteBlocks = new ArrayList<>();
            int iteratedCount = 0;
            for (int i = 0; i < blocks.size() && iteratedCount < count; i++) {
                Location location = blocks.get(i);
                Block block = location.getBlock();
                if (block.getType() == Material.WHITE_WOOL) continue;

                block.setType(Material.WHITE_WOOL);

                deleteBlocks.add(location);
                iteratedCount++;
            }

            blocks.removeAll(deleteBlocks);

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
