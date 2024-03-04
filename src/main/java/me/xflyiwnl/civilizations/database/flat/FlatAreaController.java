package me.xflyiwnl.civilizations.database.flat;

import me.xflyiwnl.civilizations.Civilizations;
import me.xflyiwnl.civilizations.database.DataController;
import me.xflyiwnl.civilizations.database.FlatDataSource;
import me.xflyiwnl.civilizations.object.LandType;
import me.xflyiwnl.civilizations.object.Resource;
import me.xflyiwnl.civilizations.object.area.Area;
import me.xflyiwnl.civilizations.object.area.AreaRegion;
import me.xflyiwnl.civilizations.util.FormatUtil;
import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class FlatAreaController extends DataController<Area, String> {

    private final FlatDataSource dataSource;

    public FlatAreaController(FlatDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void load() {
        allFromFiles().forEach(map -> {
            dataSource.getAreas().put(map.getName(), map);
        });
    }

    @Override
    public void unload() {
        dataSource.getAreas().forEach((name, map) -> {
            save(map);
        });
    }

    @Override
    public List<Area> all() {
        return new ArrayList<Area>(dataSource.getAreas().values());
    }

    public List<Area> allFromFiles() {
        List<Area> result = new ArrayList<Area>();

        File folder = new File(Civilizations.getInstance().getDataFolder(), "database/area");
        if (!folder.exists()) return result;
        for (File file : folder.listFiles()) {
            if (!file.isDirectory()) continue;
            Area area = getFromFile(file);
            if (area == null) continue;
            result.add(area);
        }

        return result;
    }

    @Override
    public Area get(String id) {
        return dataSource.getAreas().get(id);
    }

    public Area getFromFile(File file) {

        File infoFile = new File(file.getPath(), "info.yml");

        if (!infoFile.exists()) {
            System.out.println("area null");
            return null;
        }

        FileConfiguration yaml = YamlConfiguration.loadConfiguration(infoFile);
        Area area = new Area();

        if (yaml.contains("uniqueId"))
            area.setUniqueId(UUID.fromString(yaml.getString("uniqueId")));

        if (yaml.contains("name"))
            area.setName(yaml.getString("name"));

        if (yaml.contains("map"))
            area.setMap(Civilizations.getInstance().getMap(yaml.getString("map")));

        File regionFolder = new File(file.getPath(), "regions");

        if (regionFolder.exists()) {
            for (File regionFile : regionFolder.listFiles()) {

                if (!regionFile.isFile()) continue;

                AreaRegion region = this.getRegionFromFile(regionFile);

                if (region == null) continue;

                region.setArea(area);

                area.getRegions().add(region);

                System.out.println("loaded=" + region.getName());
            }
        }

        return area;
    }

    public AreaRegion getRegionFromFile(File file) {
        FileConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        AreaRegion region = new AreaRegion();

        if (yaml.contains("uniqueId"))
            region.setUniqueId(UUID.fromString(yaml.getString("uniqueId")));

        if (yaml.contains("name"))
            region.setName(yaml.getString("name"));

        if (yaml.contains("city"))
            region.setCity(FormatUtil.unformatPoint(yaml.getString("city")));

        if (yaml.contains("residents"))
            region.setResidents(yaml.getInt("residents"));

        if (yaml.contains("type"))
            region.setType(LandType.valueOf(yaml.getString("type").toUpperCase()));

        if (yaml.contains("blocks"))
            region.setBlocks(yaml.getStringList("blocks").stream()
                    .map(FormatUtil::unformatPoint)
                    .collect(Collectors.toList()));

        if (yaml.contains("neighbours"))
            region.setNeighbours(yaml.getStringList("neighbours").stream()
                    .map(UUID::fromString)
                    .collect(Collectors.toList()));

        if (yaml.isConfigurationSection("resources"))
            yaml.getConfigurationSection("resources").getKeys(false).forEach(s -> {
                region.getResources().put(Resource.valueOf(s.toUpperCase()),
                        yaml.getInt("resources." + s));
            });

        return region;
    }

    public void removeRegion(Area area, AreaRegion region) {
        File regionFile = new File(Civilizations.getInstance().getDataFolder(),
                "database/area/" + area.getUniqueId().toString() + "/regions/" + region.getUniqueId().toString() + ".yml");

        if (!regionFile.exists()) {
            return;
        }

        regionFile.delete();
    }

    @Override
    public void create(Area area) {
        dataSource.getAreas().put(area.getName(), area);
    }

    @Override
    public void save(Area area) {
        File folder = new File(Civilizations.getInstance().getDataFolder(),
                "database/area/" + area.getUniqueId().toString());
        if (!folder.exists()) {
            folder.mkdir();
        }

        File file = new File(folder.getPath(), "info.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        FileConfiguration yaml = YamlConfiguration.loadConfiguration(file);

        yaml.set("uniqueId", area.getUniqueId().toString());
        yaml.set("name", area.getName());
        yaml.set("map", area.getMap().getName());

        try {
            yaml.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        file = new File(Civilizations.getInstance().getDataFolder(),
                "database/area/" + area.getUniqueId().toString() + "/regions");

        if (!file.exists()) {
            file.mkdir();
        }

        for (AreaRegion region : area.getRegions()) {
            saveRegion(area, region);
        }

    }

    public void saveRegion(Area area, AreaRegion region) {
        File regionFile = new File(Civilizations.getInstance().getDataFolder(),
                "database/area/" + area.getUniqueId().toString() + "/regions/" + region.getUniqueId().toString() + ".yml");

        if (!regionFile.exists()) {
            try {
                regionFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        FileConfiguration regionYaml = YamlConfiguration.loadConfiguration(regionFile);

        regionYaml.set("uniqueId", region.getUniqueId().toString());
        regionYaml.set("name", region.getName());

        if (region.getCity() != null)
            regionYaml.set("city", FormatUtil.formatLocation(region.getCity().asLocation()));

        regionYaml.set("residents", region.getResidents());

        regionYaml.set("type", region.getType().toString());
        regionYaml.set("blocks", region.getBlocks().stream().map(FormatUtil::formatLocation).collect(Collectors.toList()));

        for (Resource resource : region.getResources().keySet()) {
            int count = region.getResources().getOrDefault(resource, 0);

            regionYaml.set("resources." + resource.toString(), count);

        }

        regionYaml.set("neighbours", region.getNeighbours().stream()
                .map(String::valueOf)
                .collect(Collectors.toList()));

        try {
            regionYaml.save(regionFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(Area area) {
        dataSource.getAreas().remove(area.getName());
        File file = new File(Civilizations.getInstance().getDataFolder(),
                "database/area/" + area.getUniqueId().toString());
        if (!file.exists()) return;
        try {
            FileUtils.deleteDirectory(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
