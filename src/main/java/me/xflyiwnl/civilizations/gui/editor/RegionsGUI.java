package me.xflyiwnl.civilizations.gui.editor;

import me.xflyiwnl.civilizations.Civilizations;
import me.xflyiwnl.civilizations.gui.BaseGUI;
import me.xflyiwnl.civilizations.gui.area.AreaGUI;
import me.xflyiwnl.civilizations.object.CivMap;
import me.xflyiwnl.civilizations.object.CivPlayer;
import me.xflyiwnl.civilizations.object.area.Area;
import me.xflyiwnl.civilizations.object.area.AreaRegion;
import me.xflyiwnl.civilizations.object.editor.AreaEditor;
import me.xflyiwnl.civilizations.object.editor.Editor;
import me.xflyiwnl.civilizations.task.editor.AreaRegionRemoveTask;
import me.xflyiwnl.civilizations.util.Translator;
import me.xflyiwnl.colorfulgui.ColorfulGUI;
import me.xflyiwnl.colorfulgui.object.StaticItem;
import me.xflyiwnl.colorfulgui.object.event.click.ClickStaticItemEvent;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class RegionsGUI extends BaseGUI {

    private Area area;

    public RegionsGUI(Player player, Area area) {
        super(player, "gui/editor/region-list.yml");
        this.area = area;
    }

    @Override
    public void init() {
        super.init();
        regions();
    }

    public void regions() {

        CivPlayer civPlayer = Civilizations.getInstance().getCivPlayer(getPlayer());
        CivMap map = Civilizations.getInstance().getMapEditor(civPlayer);

        if (!map.hasEditor()) return;

        AreaEditor editor = (AreaEditor) map.getEditor();

        if (area.getRegions().isEmpty()) return;

        for (AreaRegion region : area.getRegions()) {
            String path = "region-item";

            String name = getYaml().getString(path + ".name");
            List<String> lore = new ArrayList<>();

            getYaml().getStringList(path + ".lore").forEach(s -> {
                if (s.equalsIgnoreCase("%resources%")) {
                    if (region.getResources().isEmpty()) {
                        lore.add(getYaml().getString(path + ".list-empty"));
                        return;
                    }
                    region.getResources().forEach((resource, integer) -> {
                        lore.add(getYaml().getString(path + ".list-format")
                                .replace("%text%", resource.getName() + ", " + integer));
                    });
                } else if (s.equalsIgnoreCase("%neighbours%")) {
                    if (region.getNeighbours().isEmpty()) {
                        lore.add(getYaml().getString(path + ".list-empty"));
                        return;
                    }
                    for (UUID regionId : region.getNeighbours()) {
                        AreaRegion neighbourRegion = area.getRegion(regionId);
                        if (neighbourRegion == null) continue;
                        lore.add(getYaml().getString(path + ".list-format")
                                .replace("%text%", neighbourRegion.getFormattedName()));
                    }
                } else {
                    lore.add(applyPlaceholders(s, region));
                }
            });

            StaticItem mapItem = getApi().staticItem()
                    .material(Material.valueOf(getYaml().getString(path + ".material").toUpperCase()))
                    .name(applyPlaceholders(name, region))
                    .lore(lore)
                    .action(event -> {
                        if (event.getClick() == ClickType.LEFT) {

                            if (editor.getRegion() != null && editor.getRegion().getUniqueId() == region.getUniqueId()) {
                                civPlayer.sendMessage(Translator.ofString("editor.area.already-selected-region"));
                                return;
                            }

                            civPlayer.sendMessage(Translator.ofString("editor.area.select-region")
                                    .replace("%region%", region.getFormattedName()));
                            editor.setRegion(region);

                        } else if (event.getClick() == ClickType.RIGHT) {

                            civPlayer.sendMessage(Translator.ofString("editor.area.remove-region")
                                    .replace("%region%", region.getFormattedName()));

                            Civilizations.getInstance().createReply(civPlayer,
                                    Translator.ofString("editor.area.remove-question")
                                            .replace("%region%", region.getFormattedName()),
                                    chatEvent -> {

                                        String result = chatEvent.getMessage();

                                        if (result.equalsIgnoreCase("да")) {

                                            if (editor.getRegion() != null && editor.getRegion().getUniqueId().equals(region.getUniqueId())) {
                                                editor.setRegion(null);
                                            }

                                            AreaRegionRemoveTask task = new AreaRegionRemoveTask(region);
                                            task.execute();

                                            civPlayer.sendMessage(Translator.ofString("editor.area.remove-region")
                                                    .replace("%region%", region.getFormattedName()));

                                            area.removeRegion(region);

                                            RegionsGUI.open(getPlayer(), area);

                                            return true;
                                        } else if (result.equalsIgnoreCase("нет")) {
                                            civPlayer.sendMessage(Translator.ofString("editor.area.remove-cancel"));
                                            return true;
                                        }

                                        return false;
                                    });
                        }
                    })
                    .build();

            getGui().addItem(mapItem);

        }

    }

    public String applyPlaceholders(String text, AreaRegion region) {
        return text
                .replace("%region%", region.getFormattedName())
                .replace("%area%", String.valueOf(region.getArea().getFormattedName()))
                .replace("%residents%", String.valueOf(region.getResidents()))
                .replace("%city%", region.getCity() == null ? Translator.ofString("other.no-word") : Translator.ofString("other.yes-word"))
                .replace("%type%", region.getType().toString())
                .replace("%blocks%", String.valueOf(region.getBlocks().size()));
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

    public static void open(Player player, Area area) {
        ColorfulGUI api = Civilizations.getGuiApi();
        FileConfiguration yaml = Civilizations.getFileManager().get("gui/editor/region-list.yml").yaml();

        api.paginated()
                .mask(yaml.getStringList("gui.mask"))
                .rows(yaml.getInt("gui.rows"))
                .title(
                        yaml.getString("gui.title")
                )
                .holder(new RegionsGUI(player, area))
                .build();

    }

}
