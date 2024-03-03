package me.xflyiwnl.civilizations.config;

import me.xflyiwnl.civilizations.Civilizations;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class YAML {

    private File file;
    private FileConfiguration yaml;

    private String path;

    public YAML(String path) {
        this.path = path;
        generate();
    }

    public void generate() {
        file = new File(Civilizations.getInstance().getDataFolder(), path);
        if (!file.exists()) {
            Civilizations.getInstance().saveResource(path, false);
        }
        yaml = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            yaml.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public File file() {
        return file;
    }

    public FileConfiguration yaml() {
        return yaml;
    }
}
