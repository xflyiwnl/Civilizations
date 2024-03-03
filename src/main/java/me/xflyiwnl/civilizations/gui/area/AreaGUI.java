package me.xflyiwnl.civilizations.gui.area;

import me.xflyiwnl.civilizations.Civilizations;
import me.xflyiwnl.civilizations.gui.BaseGUI;
import me.xflyiwnl.civilizations.object.area.Area;
import me.xflyiwnl.colorfulgui.ColorfulGUI;
import me.xflyiwnl.colorfulgui.object.StaticItem;
import me.xflyiwnl.colorfulgui.object.event.click.ClickStaticItemEvent;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class AreaGUI extends BaseGUI {

    public AreaGUI(Player player) {
        super(player, "gui/area/area-list.yml");
    }

    @Override
    public void init() {
        super.init();
        areas();
    }

    public void areas() {

        List<Area> areas = Civilizations.getInstance().getAreas();

        if (areas.isEmpty()) return;

        for (Area area : areas) {
            String path = "area-item";

            String name = getYaml().getString(path + ".name");
            List<String> lore = getYaml().getStringList(path + ".lore");

            StaticItem mapItem = getApi().staticItem()
                    .material(Material.valueOf(getYaml().getString(path + ".material").toUpperCase()))
                    .name(applyPlaceholders(name, area))
                    .lore(lore.stream()
                            .map(s -> applyPlaceholders(s, area))
                            .collect(Collectors.toList()))
                    .build();

            getGui().addItem(mapItem);

        }

    }

    public String applyPlaceholders(String text, Area area) {
        return text
                .replace("%area%", area.getFormattedName())
                .replace("%count%", String.valueOf(area.getRegions().size()))
                .replace("%map%", area.getMap().getFormattedName());
    }

    @Override
    public void handleAction(ClickStaticItemEvent event, List<String> actions) {
        for (String action : actions) {

            if (action.equalsIgnoreCase("[next]")) {
                getGui().next();
            } else if (action.equalsIgnoreCase("[prev]")) {
                getGui().previous();
            }

        }
    }

    public static void open(Player player, String path) {
        ColorfulGUI api = Civilizations.getGuiApi();
        FileConfiguration yaml = Civilizations.getFileManager().get(path).yaml();

        api.paginated()
                .mask(yaml.getStringList("gui.mask"))
                .rows(yaml.getInt("gui.rows"))
                .title(
                        yaml.getString("gui.title")
                )
                .holder(new AreaGUI(player))
                .build();

    }

}
