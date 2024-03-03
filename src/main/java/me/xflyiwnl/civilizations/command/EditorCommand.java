package me.xflyiwnl.civilizations.command;

import me.xflyiwnl.civilizations.Civilizations;
import me.xflyiwnl.civilizations.object.CivMap;
import me.xflyiwnl.civilizations.object.CivPlayer;
import me.xflyiwnl.civilizations.object.area.Area;
import me.xflyiwnl.civilizations.object.editor.AreaEditor;
import me.xflyiwnl.civilizations.object.editor.EditorType;
import me.xflyiwnl.civilizations.task.editor.AreaLoadTask;
import me.xflyiwnl.civilizations.task.editor.AreaResetTask;
import me.xflyiwnl.civilizations.task.editor.AreaUnloadTask;
import me.xflyiwnl.civilizations.util.Translator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class EditorCommand implements TabCompleter, CommandExecutor {

    private List<String> types = Arrays.stream(EditorType.values()).map(editorType -> editorType.toString().toLowerCase()).collect(Collectors.toList());

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        switch (args.length) {
            case 1 -> {
                List<String> tabs = new ArrayList<>(types);
                tabs.add("off");
                tabs.add("reset");
                tabs.add("exit");
                return tabs;
            }
            case 2 -> {
                switch (args[0].toLowerCase()) {
                    case "area" -> {
                        return Civilizations.getInstance().getAreas().stream()
                                .map(Area::getName)
                                .collect(Collectors.toList());
                    }
                    case "reset" -> {
                        return Civilizations.getInstance().getMaps().stream()
                                .map(CivMap::getName)
                                .collect(Collectors.toList());
                    }
                    case "scenario" -> {
                        return Arrays.asList("scenarios", "#todo");
                    }
                }
            }
        }

        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        parseCommand(commandSender, strings);
        return true;
    }

    public void parseCommand(CommandSender sender, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(Translator.ofString("console-error"));
            return;
        }

        if (args.length == 0) {
            Translator.ofStringList("editor.command-args").forEach(sender::sendMessage);
            return;
        }

        Player player = (Player) sender;
        CivPlayer civPlayer = Civilizations.getInstance().getCivPlayer(player);

        switch (args[0].toLowerCase()) {

            case "reset" -> {

                if (args.length < 2) {
                    sender.sendMessage(Translator.ofString("args-error"));
                    return;
                }

                CivMap map = Civilizations.getInstance().getMap(args[1]);

                if (map == null) {
                    sender.sendMessage(Translator.ofString("map.not-exists"));
                    return;
                }

                AreaResetTask resetTask = new AreaResetTask(player, civPlayer, map);
                resetTask.execute();

            }

            case "off" -> {

                CivMap map = Civilizations.getInstance().getMapEditor(civPlayer);

                if (map == null) {
                    sender.sendMessage(Translator.ofString("editor.no-editor"));
                    return;
                }

                EditorType type = map.getEditor().getType();

                if (type == EditorType.AREA) {

                    AreaEditor editor = (AreaEditor) map.getEditor();

                    AreaUnloadTask unloadTask = new AreaUnloadTask(player, civPlayer, editor.getArea());
                    unloadTask.execute();

                }

            }

            case "exit" -> {

                CivMap map = Civilizations.getInstance().getMapEditor(civPlayer);

                if (map == null) {
                    sender.sendMessage(Translator.ofString("editor.no-editor"));
                    return;
                }

                EditorType type = map.getEditor().getType();

                if (type == EditorType.AREA) {

                    AreaEditor editor = (AreaEditor) map.getEditor();

                    editor.removePlayer(civPlayer);

                }

            }

            default -> parseTypes(sender, args);
        }


    }

    public void parseTypes(CommandSender sender, String[] args) {

        if (!(sender instanceof Player)) {
            return;
        }

        Player player = (Player) sender;
        CivPlayer civPlayer = Civilizations.getInstance().getCivPlayer(player);

        if (args.length < 2) {
            sender.sendMessage(Translator.ofString("args-error"));
            return;
        }

        EditorType type = EditorType.getEditor(args[0]);

        if (type == null) {
            return;
        }

        if (type == EditorType.AREA) {

            Area area = Civilizations.getInstance().getArea(args[1]);

            if (area == null) return;

            CivMap map = area.getMap();

            if (map == null) {
                return;
            }

            if (map.hasEditor()) {
                map.getEditor().addPlayer(civPlayer);
                return;
            }

            AreaLoadTask task = new AreaLoadTask(civPlayer, area);
            task.execute();

        } else if (type == EditorType.SCENARIO) {
            player.sendMessage("loading scenario");
        }

    }

}
