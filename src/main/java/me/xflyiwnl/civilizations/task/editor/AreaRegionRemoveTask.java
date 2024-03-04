package me.xflyiwnl.civilizations.task.editor;

import me.xflyiwnl.civilizations.object.Point;
import me.xflyiwnl.civilizations.object.area.AreaRegion;
import me.xflyiwnl.civilizations.task.CivTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class AreaRegionRemoveTask implements CivTask {

    private AreaRegion region;

    public AreaRegionRemoveTask(AreaRegion region) {
        this.region = region;
    }

    @Override
    public void execute() {

        for (Point point : region.getBlocks()) {
            Location location = point.asLocation();
            Block block = point.getWorld().getBlockAt(location);

            if (block.getType() == Material.WATER) continue;

            block.setType(Material.WHITE_WOOL);
        }

    }

}
