package me.xflyiwnl.civilizations.command;

import me.xflyiwnl.civilizations.Civilizations;
import me.xflyiwnl.civilizations.gui.map.MapGUI;
import me.xflyiwnl.civilizations.object.CivMap;
import me.xflyiwnl.civilizations.object.Point;
import me.xflyiwnl.civilizations.util.FormatUtil;
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

public class MapCommand implements CommandExecutor, TabCompleter {

    private List<String> arguments = Arrays.asList(
            "list",
            "create",
            "remove",
            "setspawn",
            "setstart",
            "setend"
    );

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        switch (args.length) {
            case 1 -> {
                return arguments;
            }
            case 2 -> {
                switch (args[0].toLowerCase()) {
                    case "remove", "setend", "setstart", "setspawn" -> {
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
            Translator.ofStringList("map.command-args").forEach(sender::sendMessage);
            return;
        }

        switch (args[0].toLowerCase()) {
            case "list" -> {

                if (!(sender instanceof Player)) {
                    sender.sendMessage(Translator.ofString("console-error"));
                    return;
                }

                Player player = (Player) sender;
                MapGUI.open(player, "gui/map/map-list.yml");
            }

            case "create" -> {

                if (args.length < 2) {
                    sender.sendMessage(Translator.ofString("args-error"));
                    return;
                }

                String name = args[1];
                CivMap map = Civilizations.getInstance().getMap(name);

                if (map != null) {
                    sender.sendMessage(Translator.ofString("map.exists"));
                    return;
                }

                map = new CivMap(name);
                map.create(true);

                sender.sendMessage(Translator.ofString("map.created")
                        .replace("%name%", name));

            }

            case "remove" -> {

                if (args.length < 2) {
                    sender.sendMessage(Translator.ofString("args-error"));
                    return;
                }

                String name = args[1];
                CivMap map = Civilizations.getInstance().getMap(name);

                if (map == null) {
                    sender.sendMessage(Translator.ofString("map.not-exists"));
                    return;
                }

                map.remove();

                sender.sendMessage(Translator.ofString("map.removed")
                        .replace("%name%", name));

            }

            case "setspawn" -> {

                if (!(sender instanceof Player)) {
                    sender.sendMessage(Translator.ofString("console-error"));
                    return;
                }

                Player player = (Player) sender;

                if (args.length < 2) {
                    sender.sendMessage(Translator.ofString("args-error"));
                    return;
                }

                String name = args[1];
                CivMap map = Civilizations.getInstance().getMap(name);

                if (map == null) {
                    sender.sendMessage(Translator.ofString("map.not-exists"));
                    return;
                }

                map.setSpawn(player.getLocation());
                map.save();

                sender.sendMessage(Translator.ofString("map.set-spawn")
                        .replace("%map%", map.getFormattedName())
                        .replace("%location%", FormatUtil.formatLocation(player.getLocation())));

            }

            case "setstart" -> {

                if (!(sender instanceof Player)) {
                    sender.sendMessage(Translator.ofString("console-error"));
                    return;
                }

                Player player = (Player) sender;

                if (args.length < 2) {
                    sender.sendMessage(Translator.ofString("args-error"));
                    return;
                }

                String name = args[1];
                CivMap map = Civilizations.getInstance().getMap(name);

                if (map == null) {
                    sender.sendMessage(Translator.ofString("map.not-exists"));
                    return;
                }

                map.setStartPoint(new Point(player.getLocation()));
                map.save();

                sender.sendMessage(Translator.ofString("map.set-start")
                        .replace("%map%", map.getFormattedName())
                        .replace("%location%", FormatUtil.formatLocation(map.getStartPoint().asLocation())));

            }

            case "setend" -> {

                if (!(sender instanceof Player)) {
                    sender.sendMessage(Translator.ofString("console-error"));
                    return;
                }

                Player player = (Player) sender;

                if (args.length < 2) {
                    sender.sendMessage(Translator.ofString("args-error"));
                    return;
                }

                String name = args[1];
                CivMap map = Civilizations.getInstance().getMap(name);

                if (map == null) {
                    sender.sendMessage(Translator.ofString("map.not-exists"));
                    return;
                }

                map.setEndPoint(new Point(player.getLocation()));
                map.save();

                sender.sendMessage(Translator.ofString("map.set-end")
                        .replace("%map%", map.getFormattedName())
                        .replace("%location%", FormatUtil.formatLocation(map.getEndPoint().asLocation())));

            }

        }

    }

}
