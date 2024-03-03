package me.xflyiwnl.civilizations;

import me.xflyiwnl.civilizations.config.YAML;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class FileManager {

    private YAML language, settings;

    public void generate() {

        language = new YAML("language.yml");
        settings = new YAML("settings.yml");

        createFolders();
    }

    public YAML get(String path) {
        return new YAML(path);
    }

    public void createFolders() {
        List<String> folders = Arrays.asList(
                "database",
                "database/map",
                "database/scenario",
                "database/player",
                "database/area"
        );
        folders.forEach(this::createFolder);
    }

    public void createFolder(String folder) {
        File file = new File(Civilizations.getInstance().getDataFolder(), folder);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public YAML getLanguage() {
        return language;
    }

    public YAML getSettings() {
        return settings;
    }
}
