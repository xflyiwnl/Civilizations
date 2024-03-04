package me.xflyiwnl.civilizations.task.editor;

import me.xflyiwnl.civilizations.Civilizations;
import me.xflyiwnl.civilizations.object.CivMap;
import me.xflyiwnl.civilizations.object.CivPlayer;
import me.xflyiwnl.civilizations.object.Point;
import me.xflyiwnl.civilizations.object.area.Area;
import me.xflyiwnl.civilizations.task.CivTask;
import me.xflyiwnl.civilizations.util.TextUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AreaUnloadTask implements CivTask {

    private Player player;
    private CivPlayer civPlayer;
    private Area area;

    public AreaUnloadTask(Player player, CivPlayer civPlayer, Area area) {
        this.player = player;
        this.civPlayer = civPlayer;
        this.area = area;
    }

    @Override
    public void execute() {

        CivMap map = area.getMap();

        if (map.hasEditor())
            map.getEditor().disable();

        area.save();

        AreaResetTask resetTask = new AreaResetTask(player, civPlayer, area.getMap());
        resetTask.execute();

    }

}
