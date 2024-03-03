package me.xflyiwnl.civilizations.util;


import me.xflyiwnl.civilizations.Civilizations;

import java.util.List;

public class Settinger {

    public static String ofString(String path) {
        return Civilizations.getFileManager().getSettings().yaml()
                .getString("settings." + path);
    }

    public static List<String> ofStringList(String path) {
        return Civilizations.getFileManager().getSettings().yaml()
                .getStringList("settings." + path);
    }

    public static int ofInt(String path) {
        return Civilizations.getFileManager().getSettings().yaml()
                .getInt("settings." + path);
    }

    public static Double ofDouble(String path) {
        return Civilizations.getFileManager().getSettings().yaml()
                .getDouble("settings." + path);
    }

    public static boolean ofBoolean(String path) {
        return Civilizations.getFileManager().getSettings().yaml()
                .getBoolean("settings." + path);
    }

}
