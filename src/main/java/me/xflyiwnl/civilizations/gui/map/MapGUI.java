package me.xflyiwnl.civilizations.gui.map;

import me.xflyiwnl.civilizations.Civilizations;
import me.xflyiwnl.civilizations.gui.BaseGUI;
import me.xflyiwnl.civilizations.object.CivMap;
import me.xflyiwnl.civilizations.util.Translator;
import me.xflyiwnl.colorfulgui.ColorfulGUI;
import me.xflyiwnl.colorfulgui.object.StaticItem;
import me.xflyiwnl.colorfulgui.object.event.click.ClickStaticItemEvent;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class MapGUI extends BaseGUI {
    public MapGUI(Player player) {
        super(player, "gui/map/map-list.yml");
    }

    @Override
    public void init() {
        super.init();
        maps();
    }

    public void maps() {

        List<CivMap> maps = Civilizations.getInstance().getMaps();

        if (maps.isEmpty()) return;

        for (CivMap map : maps) {
            String path = "map-item";

            String name = getYaml().getString(path + ".name");
            List<String> lore = getYaml().getStringList(path + ".lore");

            StaticItem mapItem = getApi().staticItem()
                    .material(Material.valueOf(getYaml().getString(path + ".material").toUpperCase()))
                    .name(applyPlaceholders(name, map))
                    .lore(lore.stream()
                            .map(s -> applyPlaceholders(s, map))
                            .collect(Collectors.toList()))
                    .build();

            getGui().addItem(mapItem);

        }

    }

    public String applyPlaceholders(String text, CivMap map) {
        return text
                .replace("%map%", map.getFormattedName())
                .replace("%spawn%", map.getSpawn() == null ? Translator.ofString("other.no-word") : Translator.ofString("other.yes-word"))
                .replace("%start%", map.getStartPoint() == null ? Translator.ofString("other.no-word") : Translator.ofString("other.yes-word"))
                .replace("%end%", map.getEndPoint() == null ? Translator.ofString("other.no-word") : Translator.ofString("other.yes-word"));
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
                .holder(new MapGUI(player))
                .build();

    }

}
