package me.xflyiwnl.civilizations.object.helper.tool.tools;

import me.xflyiwnl.civilizations.Civilizations;
import me.xflyiwnl.civilizations.gui.editor.RegionsGUI;
import me.xflyiwnl.civilizations.object.CivMap;
import me.xflyiwnl.civilizations.object.CivPlayer;
import me.xflyiwnl.civilizations.object.Point;
import me.xflyiwnl.civilizations.object.Resource;
import me.xflyiwnl.civilizations.object.area.AreaRegion;
import me.xflyiwnl.civilizations.object.editor.AreaEditor;
import me.xflyiwnl.civilizations.object.editor.EditorType;
import me.xflyiwnl.civilizations.object.helper.CivTool;
import me.xflyiwnl.civilizations.object.helper.tool.ToolItem;
import me.xflyiwnl.civilizations.object.helper.tool.ToolType;
import me.xflyiwnl.civilizations.task.editor.AreaRegionRemoveTask;
import me.xflyiwnl.civilizations.util.Translator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

public class AreaEditorTool implements CivTool {

    private final ToolType type = ToolType.EDITOR_AREA;
    private CivPlayer player;
    private Map<Integer, ToolItem> items = new HashMap<>();
    private AreaEditor editor;

    public AreaEditorTool(CivPlayer player) {
        this.player = player;

        CivMap map = Civilizations.getInstance().getMapEditor(player);

        if (map == null) return;
        if (map.getEditor().getType() != EditorType.AREA) return;

        this.editor = (AreaEditor) map.getEditor();

        init();
    }

    private int radius = 1;

    @Override
    public void init() {
        ToolItem createItem = ToolItem.builder()
                .material(Material.CLOCK)
                .name("Создать регион")
                .lore(Arrays.asList(
                        "test"
                ))
                .action(event -> {

                    if (player.hasReply()) {
                        player.sendMessage(Translator.ofString("player.reply-error"));
                        return;
                    }

                    Civilizations.getInstance().createReply(player,
                            Translator.ofString("editor.area.create-region-chat"),
                            chatEvent -> {

                                String name = chatEvent.getMessage();

                                if (name.split(" ").length != 1) {
                                    player.sendMessage(Translator.ofString("editor.area.region-format"));
                                    return false;
                                }

                                if (editor.getArea().getRegion(name) != null) {
                                    player.sendMessage(editor.getArea().getRegion(name).getName());
                                    player.sendMessage(Translator.ofString("editor.area.region-exists"));
                                    return false;
                                }

                                player.sendMessage(Translator.ofString("editor.area.create-region")
                                        .replace("%region%", name));

                                AreaRegion region = new AreaRegion(
                                        editor.getArea(),
                                        name
                                );

                                editor.getArea().getRegions().add(region);
                                editor.setRegion(region);

                                region.save();

                                player.sendMessage(Translator.ofString("editor.area.select-region")
                                        .replace("%region%", region.getFormattedName()));

                                return true;

                            });

                }).build();
        items.put(0, createItem);

        ToolItem removeItem = ToolItem.builder()
                .material(Material.BARRIER)
                .name("Удалить регион")
                .lore(Arrays.asList(
                        "test"
                ))
                .action(event -> {

                    AreaRegion region = editor.getRegion();

                    if (region == null) {
                        player.sendMessage(Translator.ofString("editor.area.no-selected-region"));
                        return;
                    }

                    Civilizations.getInstance().createReply(player,
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

                                    player.sendMessage(Translator.ofString("editor.area.remove-region")
                                            .replace("%region%", region.getFormattedName()));

                                    editor.getArea().removeRegion(region);

                                    player.sendMessage(Translator.ofString("editor.area.remove-region")
                                            .replace("%region%", region.getFormattedName()));
                                    return true;
                                } else if (result.equalsIgnoreCase("нет")) {
                                    player.sendMessage(Translator.ofString("editor.area.remove-cancel"));
                                    return true;
                                }

                                return false;
                            });

                })
                .build();
        items.put(1, removeItem);

        ToolItem selectItem = ToolItem.builder()
                .material(Material.FEATHER)
                .name("Выбрать регион")
                .lore(Arrays.asList(
                        "test"
                ))
                .action(event -> {

                    Block block = player.getPlayer().getTargetBlock(25);

                    if (block.getType() == Material.WATER
                        || block.getType() == Material.SAND
                        || block.getType() == Material.AIR
                        || block.getType() == Material.BEDROCK
                        || block.getType() == Material.BARRIER) {
                        return;
                    }

                    Point point = new Point(block.getLocation());
                    AreaRegion region = editor.getArea().getRegion(point);

                    if (region == null) return;

                    editor.setRegion(region);

                    player.sendMessage(Translator.ofString("editor.area.select-region")
                            .replace("%region%", region.getFormattedName()));

                })
                .build();
        items.put(2, selectItem);

        ToolItem listItem = ToolItem.builder()
                .material(Material.BOOK)
                .name("Список регионов")
                .lore(Arrays.asList(
                        "test"
                ))
                .action(event -> {
                    RegionsGUI.open(player.getPlayer(), editor.getArea());
                })
                .build();
        items.put(3, listItem);

        ToolItem firstBrush = ToolItem.builder()
                .material(Material.STICK)
                .name("Кисть")
                .lore(Arrays.asList(
                        "test"
                ))
                .action(event -> {

                    if (event.getHand() == EquipmentSlot.HAND) {

                        if (event.getAction() == Action.RIGHT_CLICK_BLOCK ||
                            event.getAction() == Action.RIGHT_CLICK_AIR) {

                            if (editor.getRegion() == null) {
                                player.sendMessage(Translator.ofString("editor.area.no-selected-region"));
                                return;
                            }

                            Block targetBlock = player.getPlayer().getTargetBlock(25);

                            if (targetBlock == null) return;

                            if (radius == 1) {
                                if (targetBlock.getType() != Material.WHITE_WOOL) return;
                                addBlockToRegion(targetBlock);
                            } else if (radius == 2) {
                                int y = targetBlock.getY();
                                for (int x = targetBlock.getX() - 1; x <= targetBlock.getX(); x++) {
                                    for (int z = targetBlock.getZ() - 1; z <= targetBlock.getZ(); z++) {
                                        Block block = targetBlock.getWorld().getBlockAt(x, y, z);

                                        if (block.getType() != Material.WHITE_WOOL) continue;

                                        addBlockToRegion(block);
                                    }
                                }
                            } else if (radius == 3) {
                                int y = targetBlock.getY();
                                for (int x = targetBlock.getX() - 1; x <= targetBlock.getX() + 1; x++) {
                                    for (int z = targetBlock.getZ() - 1; z <= targetBlock.getZ() + 1; z++) {
                                        Block block = targetBlock.getWorld().getBlockAt(x, y, z);

                                        if (block.getType() != Material.WHITE_WOOL) continue;

                                        addBlockToRegion(block);
                                    }
                                }
                            }

                        } else {
                            if (radius == 3) {
                                player.sendMessage(Translator.ofString("editor.area.radius-change")
                                        .replace("%radius%", "1"));
                                radius = 1;
                                return;
                            }

                            player.sendMessage(Translator.ofString("editor.area.radius-change")
                                    .replace("%radius%", String.valueOf(radius + 1)));
                            radius++;
                        }

                    }

                })
                .build();
        items.put(5, firstBrush);

        ToolItem secondBrush = ToolItem.builder()
                .material(Material.BLAZE_ROD)
                .name("Ластик")
                .lore(Arrays.asList(
                        "test"
                ))
                .action(event -> {

                    if (event.getHand() == EquipmentSlot.HAND) {

                        if (event.getAction() == Action.RIGHT_CLICK_BLOCK ||
                                event.getAction() == Action.RIGHT_CLICK_AIR) {

                            if (editor.getRegion() == null) {
                                player.sendMessage(Translator.ofString("editor.area.no-selected-region"));
                                return;
                            }

                            Material regionMaterial = editor.getRegion().getMaterial();
                            Block targetBlock = player.getPlayer().getTargetBlock(25);

                            if (targetBlock == null) return;

                            if (radius == 1) {
                                if (targetBlock.getType() == Material.WHITE_WOOL
                                        || regionMaterial != targetBlock.getType()) return;

                                if (!editor.getRegion().getBlocks().contains(new Point(targetBlock.getLocation()))) return;

                                removeBlockFromRegion(targetBlock);
                            } else if (radius == 2) {
                                int y = targetBlock.getY();
                                for (int x = targetBlock.getX() - 1; x <= targetBlock.getX(); x++) {
                                    for (int z = targetBlock.getZ() - 1; z <= targetBlock.getZ(); z++) {
                                        Block block = targetBlock.getWorld().getBlockAt(x, y, z);

                                        if (block.getType() == Material.WHITE_WOOL
                                        || regionMaterial != block.getType()) continue;
                                        if (!editor.getRegion().getBlocks().contains(new Point(block.getLocation()))) return;

                                        removeBlockFromRegion(block);
                                    }
                                }
                            } else if (radius == 3) {
                                int y = targetBlock.getY();
                                for (int x = targetBlock.getX() - 1; x <= targetBlock.getX() + 1; x++) {
                                    for (int z = targetBlock.getZ() - 1; z <= targetBlock.getZ() + 1; z++) {
                                        Block block = targetBlock.getWorld().getBlockAt(x, y, z);

                                        if (block.getType() == Material.WHITE_WOOL
                                                || regionMaterial != block.getType()) continue;
                                        if (!editor.getRegion().getBlocks().contains(new Point(block.getLocation()))) return;

                                        removeBlockFromRegion(block);
                                    }
                                }
                            }

                        } else {
                            if (radius == 3) {
                                player.sendMessage(Translator.ofString("editor.area.radius-change")
                                        .replace("%radius%", "1"));
                                radius = 1;
                                return;
                            }

                            player.sendMessage(Translator.ofString("editor.area.radius-change")
                                    .replace("%radius%", String.valueOf(radius + 1)));
                            radius++;
                        }

                    }

                })
                .build();
        items.put(6, secondBrush);

        ToolItem neighborItem = ToolItem.builder()
                .material(Material.OAK_FENCE)
                .name("Редактировать соседа")
                .lore(Arrays.asList(
                        "test"
                ))
                .action(event -> {
                    if (event.getHand() == EquipmentSlot.HAND) {

                        if (editor.getRegion() == null) {
                            player.sendMessage(Translator.ofString("editor.area.no-selected-region"));
                            return;
                        }

                        if (event.getAction() == Action.RIGHT_CLICK_BLOCK ||
                                event.getAction() == Action.RIGHT_CLICK_AIR) {

                            if (editor.getRegion().getNeighbours().isEmpty()) return;

                            AreaRegion selectedRegion = editor.getRegion();

                            for (Point point : selectedRegion.getBlocks()) {
                                player.getPlayer().sendBlockChange(point.asLocation(),
                                        Material.RED_GLAZED_TERRACOTTA.createBlockData());
                            }

                            List<Material> selectedColors = new ArrayList<>();

                            Random random = new Random();

                            for (UUID uniqueId : selectedRegion.getNeighbours()) {

                                List<Material> colors = Arrays.asList(
                                        Material.BLUE_GLAZED_TERRACOTTA,
                                        Material.LIGHT_BLUE_GLAZED_TERRACOTTA,
                                        Material.CYAN_GLAZED_TERRACOTTA
                                ).stream().filter(material -> !selectedColors.contains(material))
                                        .collect(Collectors.toList());

                                if (selectedColors.size() >= colors.size()) {
                                    selectedColors.clear();
                                }

                                Material material = colors.get(random.nextInt(colors.size()));
                                selectedColors.add(material);

                                AreaRegion neighbourRegion = editor.getArea().getRegion(uniqueId);
                                if (neighbourRegion == null) {
                                    selectedRegion.getNeighbours().remove(uniqueId);
                                    return;
                                }
                                for (Point point : neighbourRegion.getBlocks()) {
                                    player.getPlayer().sendBlockChange(point.asLocation(),
                                            material.createBlockData());
                                }
                            }

                            Bukkit.getScheduler().runTaskLater(Civilizations.getInstance(), () -> {

                                if (selectedRegion == null) return;

                                for (Point point : selectedRegion.getBlocks()) {
                                    Block block = point.getWorld().getBlockAt(point.asLocation());
                                    block.getState().update();
                                }

                                for (UUID uniqueId : selectedRegion.getNeighbours()) {

                                    AreaRegion neighbourRegion = editor.getArea().getRegion(uniqueId);

                                    if (neighbourRegion == null) {
                                        selectedRegion.getNeighbours().remove(uniqueId);
                                        return;
                                    }

                                    for (Point point : neighbourRegion.getBlocks()) {
                                        Block block = point.getWorld().getBlockAt(point.asLocation());
                                        block.getState().update();
                                    }
                                }

                            }, 20*3);


                        } else {

                            Block block = player.getPlayer().getTargetBlock(25);

                            if (block.getType() == Material.WATER
                                    || block.getType() == Material.SAND
                                    || block.getType() == Material.AIR
                                    || block.getType() == Material.BEDROCK
                                    || block.getType() == Material.BARRIER) {
                                return;
                            }

                            Point point = new Point(block.getLocation());
                            AreaRegion region = editor.getArea().getRegion(point);

                            if (region == null) return;

                            if (region.getUniqueId() == editor.getRegion().getUniqueId()) {
                                return;
                            }

                            if (editor.getRegion().getNeighbours().contains(region.getUniqueId())) {
                                editor.getRegion().getNeighbours().remove(region.getUniqueId());
                                player.sendMessage(Translator.ofString("editor.area.remove-neighbour")
                                        .replace("%region%", region.getFormattedName()));
                                return;
                            }

                            editor.getRegion().getNeighbours().add(region.getUniqueId());

                            player.sendMessage(Translator.ofString("editor.area.add-neighbour")
                                    .replace("%region%", region.getFormattedName()));

                        }

                    }
                })
                .build();
        items.put(7, neighborItem);

        ToolItem resourceItem = ToolItem.builder()
                .material(Material.GOLD_ORE)
                .name("Редактировать ресурсы")
                .lore(Arrays.asList(
                        "test"
                ))
                .action(event -> {

                    AreaRegion region = editor.getRegion();

                    if (region == null) return;

                    Civilizations.getInstance().createReply(
                            player,
                            Translator.ofString("editor.area.resource-question"),
                            chatEvent -> {

                                String formatted = chatEvent.getMessage();
                                String[] split = formatted.split(", ");

                                if (split.length < 1) {
                                    player.sendMessage(Translator.ofString("editor.area.resource-not-found"));
                                    return false;
                                }

                                Resource resource = Resource.getResource(split[0]);

                                if (resource == null) {
                                    player.sendMessage(Translator.ofString("editor.area.resource-not-found"));
                                    return false;
                                }

                                int count = 0;
                                try {
                                    count = Integer.parseInt(split[1]);
                                } catch (NumberFormatException e) {
                                    player.sendMessage(Translator.ofString("number-exception"));
                                    return false;
                                }

                                if (count <= 0) return true;

                                region.getResources().put(resource, count);

                                player.sendMessage(Translator.ofString("editor.area.added-resource")
                                        .replace("%resource%", resource.getName())
                                        .replace("%count%", String.valueOf(count)));

                                return true;
                            });

                }).build();
        items.put(8, resourceItem);

    }

    public void addBlockToRegion(Block block) {
        block.setType(editor.getRegion().getMaterial());
        editor.getRegion().getBlocks().add(new Point(block.getLocation()));
    }

    public void removeBlockFromRegion(Block block) {
        block.setType(Material.WHITE_WOOL);
        editor.getRegion().getBlocks().remove(new Point(block.getLocation()));
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
