package me.xflyiwnl.civilizations.command;

import me.xflyiwnl.civilizations.Civilizations;
import me.xflyiwnl.civilizations.gui.area.AreaGUI;
import me.xflyiwnl.civilizations.object.CivMap;
import me.xflyiwnl.civilizations.object.area.Area;
import me.xflyiwnl.civilizations.util.Translator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AreaCommand implements TabCompleter, CommandExecutor {

    private List<String> arguments = Arrays.asList(
            "create",
            "remove",
            "list"
    );

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        switch (args.length) {
            case 1 -> {
                return arguments;
            }
            case 2 -> {
                switch (args[0].toLowerCase()) {
                    case "remove" -> {
                        return Civilizations.getInstance().getAreas().stream()
                                .map(Area::getName)
                                .collect(Collectors.toList());
                    }
                    case "create" -> {
                        return Civilizations.getInstance().getMaps().stream()
                                .map(CivMap::getName)
                                .collect(Collectors.toList());
                    }
                }
            }
        }

        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        parseCommand(sender, args);
        return true;
    }

    public void parseCommand(CommandSender sender, String[] args) {

        if (args.length == 0) {
            Translator.ofStringList("area.command-args").forEach(sender::sendMessage);
            return;
        }

        switch (args[0].toLowerCase()) {
            case "list" -> {

                if (!(sender instanceof Player)) {
                    sender.sendMessage(Translator.ofString("console-error"));
                    return;
                }

                Player player = (Player) sender;
                AreaGUI.open(player, "gui/area/area-list.yml");
            }

            case "create" -> {

                if (args.length < 3) {
                    sender.sendMessage(Translator.ofString("args-error"));
                    return;
                }

                String mapName = args[1];
                CivMap map = Civilizations.getInstance().getMap(mapName);

                if (map == null) {
                    sender.sendMessage(Translator.ofString("map.not-exists"));
                    return;
                }

                String name = args[2];
                Area area = Civilizations.getInstance().getArea(name);

                if (area != null) {
                    sender.sendMessage(Translator.ofString("area.exists"));
                    return;
                }

                area = new Area(name, map);
                area.create(true);

                sender.sendMessage(Translator.ofString("area.created")
                        .replace("%name%", name)
                        .replace("%map%", area.getMap().getFormattedName()));

            }

            case "remove" -> {

                if (args.length < 2) {
                    sender.sendMessage(Translator.ofString("args-error"));
                    return;
                }

                String name = args[1];
                Area area = Civilizations.getInstance().getArea(name);

                if (area == null) {
                    sender.sendMessage(Translator.ofString("area.not-exists"));
                    return;
                }

                area.remove();

                sender.sendMessage(Translator.ofString("area.removed")
                        .replace("%name%", name)
                        .replace("%map%", area.getMap().getFormattedName()));

            }

        }

    }


}
