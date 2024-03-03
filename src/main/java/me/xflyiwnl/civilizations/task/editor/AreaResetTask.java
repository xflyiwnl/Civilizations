package me.xflyiwnl.civilizations.task.editor;

import me.xflyiwnl.civilizations.Civilizations;
import me.xflyiwnl.civilizations.object.CivMap;
import me.xflyiwnl.civilizations.object.CivPlayer;
import me.xflyiwnl.civilizations.object.Point;
import me.xflyiwnl.civilizations.object.area.Area;
import me.xflyiwnl.civilizations.object.editor.AreaEditor;
import me.xflyiwnl.civilizations.object.editor.Editor;
import me.xflyiwnl.civilizations.task.CivTask;
import me.xflyiwnl.civilizations.util.Settinger;
import me.xflyiwnl.civilizations.util.TextUtil;
import me.xflyiwnl.civilizations.util.Translator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AreaResetTask implements CivTask {

    private Player player;
    private CivPlayer civPlayer;
    private CivMap map;

    public AreaResetTask(Player player, CivPlayer civPlayer, CivMap map) {
        this.player = player;
        this.civPlayer = civPlayer;
        this.map = map;
    }

    @Override
    public void execute() {

        civPlayer.sendMessage(Translator.ofString("editor.reset-map")
                .replace("%map%", map.getFormattedName()));

        Point startPoint = map.getStartPoint();
        Point endPoint = map.getEndPoint();

        List<Location> blocks = new ArrayList<>();

        int y = startPoint.getY();
        for (int x = startPoint.getX(); x <= endPoint.getX(); x++) {
            for (int z = startPoint.getZ(); z <= endPoint.getZ(); z++) {
                Location location = new Location(startPoint.getWorld(), x, y, z);
                Block block = location.getBlock();

                if (block.getType() == Material.WATER) continue;

                blocks.add(location);
            }
        }

        if (blocks.isEmpty()) {
            civPlayer.sendMessage(Translator.ofString("editor.unknown-error"));
            return;
        }

        civPlayer.sendMessage(Translator.ofString("editor.blocks-found")
                .replace("%count%", String.valueOf(blocks.size())));
        civPlayer.sendMessage(Translator.ofString("editor.start-reset"));

        int all = blocks.size(); // количество блоков
        int canIterate = 3000; // сколько сможет итерировать сервер, мощность сервера

        int time = blocks.size() / canIterate; // сколько времени будем итерировать
        int count = blocks.size() / time; // сколько будет сервер итерировать в итоге

        Civilizations.getInstance().createRunnable(() -> {
            if (blocks.isEmpty()) {
                civPlayer.sendMessage(Translator.ofString("editor.reseted"));
                return true;
            }

            List<Location> deleteBlocks = new ArrayList<>();
            int iteratedCount = 0;
            for (int i = 0; i < blocks.size() && iteratedCount < count; i++) {
                Location location = blocks.get(i);
                Block block = location.getBlock();

                if (block.getType() != Material.BEDROCK)
                    block.setType(Material.BEDROCK);

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
