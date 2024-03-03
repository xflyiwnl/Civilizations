package me.xflyiwnl.civilizations.database.flat;

import me.xflyiwnl.civilizations.Civilizations;
import me.xflyiwnl.civilizations.database.DataController;
import me.xflyiwnl.civilizations.database.FlatDataSource;
import me.xflyiwnl.civilizations.object.CivMap;
import me.xflyiwnl.civilizations.object.Point;
import me.xflyiwnl.civilizations.util.FormatUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FlatMapController extends DataController<CivMap, String> {

    private final FlatDataSource dataSource;

    public FlatMapController(FlatDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void load() {
        allFromFiles().forEach(map -> {
            dataSource.getMaps().put(map.getName(), map);
        });
    }

    @Override
    public void unload() {
        dataSource.getMaps().forEach((name, map) -> {
            save(map);
        });
    }

    @Override
    public List<CivMap> all() {
        return new ArrayList<CivMap>(dataSource.getMaps().values());
    }

    public List<CivMap> allFromFiles() {
        List<CivMap> result = new ArrayList<CivMap>();

        File folder = new File(Civilizations.getInstance().getDataFolder(), "database/map");
        if (!folder.exists()) return result;
        for (File file : folder.listFiles()) {
            if (file.isDirectory() && !file.isFile()) continue;
            CivMap civMap = getFromFile(file);
            result.add(civMap);
        }

        return result;
    }

    @Override
    public CivMap get(String id) {
        return dataSource.getMaps().get(id);
    }

    public CivMap getFromFile(File file) {

        FileConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        CivMap map = new CivMap();

        if (yaml.contains("uniqueId"))
            map.setUniqueId(UUID.fromString(yaml.getString("uniqueId")));

        if (yaml.contains("name"))
            map.setName(yaml.getString("name"));

        if (yaml.contains("spawn"))
            map.setSpawn(FormatUtil.unformatLocation(yaml.getString("spawn")));

        if (yaml.contains("startPoint"))
            map.setStartPoint(new Point(FormatUtil.unformatLocation(yaml.getString("startPoint"))));

        if (yaml.contains("endPoint"))
            map.setEndPoint(new Point(FormatUtil.unformatLocation(yaml.getString("endPoint"))));

        return map;
    }

    @Override
    public void create(CivMap map) {
        dataSource.getMaps().put(map.getName(), map);
    }

    @Override
    public void save(CivMap map) {
        File file = new File(Civilizations.getInstance().getDataFolder(),
                "database/map/" + map.getUniqueId().toString() + ".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        FileConfiguration yaml = YamlConfiguration.loadConfiguration(file);

        yaml.set("uniqueId", map.getUniqueId().toString());
        yaml.set("name", map.getName());

        if (map.getSpawn() != null)
            yaml.set("spawn", FormatUtil.formatLocation(map.getSpawn()));

        if (map.getStartPoint() != null)
            yaml.set("startPoint", FormatUtil.formatLocation(map.getStartPoint().asLocation()));

        if (map.getEndPoint() != null)
            yaml.set("endPoint", FormatUtil.formatLocation(map.getEndPoint().asLocation()));

        try {
            yaml.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void remove(CivMap map) {
        dataSource.getMaps().remove(map.getName());
        File file = new File(Civilizations.getInstance().getDataFolder(),
                "database/map/" + map.getUniqueId().toString() + ".yml");
        if (!file.exists()) return;
        file.delete();
    }

}
