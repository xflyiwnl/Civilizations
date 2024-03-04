package me.xflyiwnl.civilizations.util;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ColorUtil {

    private static List<Material> selectedColors = new ArrayList<>();
    public static Material randomizeColor() {
        Material material;

        List<Material> colors = Arrays.stream(Material.values())
                .filter(color -> {
                    if (!color.toString().equalsIgnoreCase("white_wool")) {
                        if (color.toString().endsWith("WOOL")
                                || color.toString().endsWith("TERRACOTA")
                                || color.toString().endsWith("CONCRETE")) {
                            return true;
                        }
                        return false;
                    } else {
                        return false;
                    }
                })
                .collect(Collectors.toList());

        Random random = new Random();
        colors.removeAll(selectedColors);

        if (selectedColors.size() >= colors.size()) {
            selectedColors.clear();
        }

        material = colors.get(random.nextInt(colors.size()));
        selectedColors.add(material);

        return material;
    }

}
