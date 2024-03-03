package me.xflyiwnl.civilizations.object.helper.tool.tools;

import me.xflyiwnl.civilizations.object.CivPlayer;
import me.xflyiwnl.civilizations.object.helper.CivTool;
import me.xflyiwnl.civilizations.object.helper.tool.ToolItem;
import me.xflyiwnl.civilizations.object.helper.tool.ToolType;
import org.bukkit.Material;

import java.util.*;

public class AreaEditorTool implements CivTool {

    private ToolType type;
    private CivPlayer player;
    private Map<Integer, ToolItem> items = new HashMap<>();

    @Override
    public void init() {
        ToolItem createItem = ToolItem.builder()
                .material(Material.CLOCK)
                .name("Создать регион")
                .lore(Arrays.asList(
                        "test"
                ))
                .action(event -> {
                    // создание региона
                }).build();
        items.put(0, createItem);
    }

    @Override
    public CivPlayer getPlayer() {
        return player;
    }

    @Override
    public ToolType getType() {
        return type;
    }

    @Override
    public Map<Integer, ToolItem> getItems() {
        return items;
    }

}
