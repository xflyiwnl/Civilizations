package me.xflyiwnl.civilizations.gui;

import me.xflyiwnl.civilizations.Civilizations;
import me.xflyiwnl.colorfulgui.ColorfulGUI;
import me.xflyiwnl.colorfulgui.object.PaginatedGui;
import me.xflyiwnl.colorfulgui.object.StaticItem;
import me.xflyiwnl.colorfulgui.object.event.click.ClickStaticItemEvent;
import me.xflyiwnl.colorfulgui.provider.ColorfulProvider;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.List;

public class BaseGUI extends ColorfulProvider<PaginatedGui> {

    private FileConfiguration yaml;
    private ColorfulGUI api = Civilizations.getGuiApi();

    public BaseGUI(Player player, String path) {
        super(player);
        this.yaml = Civilizations.getFileManager().get(path).yaml();;
    }

    public BaseGUI(Player player, int updateTime, String path) {
        super(player, updateTime);
        this.yaml = Civilizations.getFileManager().get(path).yaml();;
    }

    @Override
    public void init() {
        if (!yaml.isConfigurationSection("items")) {
            return;
        }

        for (String section : yaml.getConfigurationSection("items").getKeys(false)) {
            String path = "items." + section;

            Material material = Material.valueOf(yaml.getString(path + ".material").toUpperCase());
            int amount = yaml.getInt(path + ".amount");

            String name = yaml.getString(path + ".name");
            List<String> lore = yaml.getStringList(path + ".lore");

            List<String> action = yaml.get(path + ".action") != null ? yaml.getStringList(path + ".action") : new ArrayList<>();

            StaticItem staticItem = api.staticItem()
                    .material(material)
                    .amount(amount)
                    .name(name)
                    .lore(lore)
                    .flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS)
                    .action(event -> {
                        handleAction(event, action);
                    })
                    .build();

            if (yaml.get(path + ".mask") != null) {
                getGui().addMask(yaml.getString(path + ".mask"), staticItem);
            }

            if (yaml.get(path + ".slot") != null) {
                getGui().setItem(yaml.getInt(path + ".slot"), staticItem);
            }

            if (yaml.get(path + ".slots") != null) {
                yaml.getIntegerList(path + ".slots").forEach(integer -> {
                    getGui().setItem(integer, staticItem);
                });
            }

        }

    }

    public void handleAction(ClickStaticItemEvent event, List<String> actions) {

    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setResult(Event.Result.DENY);
    }

    public ColorfulGUI getApi() {
        return api;
    }

    public FileConfiguration getYaml() {
        return yaml;
    }
}
