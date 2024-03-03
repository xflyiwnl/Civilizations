package me.xflyiwnl.civilizations.object.helper;

import me.xflyiwnl.civilizations.object.CivPlayer;
import me.xflyiwnl.civilizations.object.helper.tool.ToolItem;
import me.xflyiwnl.civilizations.object.helper.tool.ToolType;

import java.util.List;
import java.util.Map;

public interface CivTool {

    void init();

    CivPlayer getPlayer();
    ToolType getType();
    Map<Integer, ToolItem> getItems();

}
