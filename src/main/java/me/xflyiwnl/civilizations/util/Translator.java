package me.xflyiwnl.civilizations.util;

import me.xflyiwnl.civilizations.Civilizations;

import java.util.List;

public class Translator {

    public static String ofString(String path) {
        return TextUtil.colorize(Civilizations.getFileManager().getLanguage().yaml()
                .getString("language." + path));
    }

    public static List<String> ofStringList(String path) {
        return TextUtil.colorize(Civilizations.getFileManager().getLanguage().yaml()
                .getStringList("language." + path));
    }

}
