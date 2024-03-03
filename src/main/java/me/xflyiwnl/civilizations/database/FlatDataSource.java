package me.xflyiwnl.civilizations.database;

import me.xflyiwnl.civilizations.database.flat.FlatAreaController;
import me.xflyiwnl.civilizations.database.flat.FlatMapController;
import me.xflyiwnl.civilizations.object.CivMap;
import me.xflyiwnl.civilizations.object.CivPlayer;
import me.xflyiwnl.civilizations.object.area.Area;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FlatDataSource implements DataSource {

    private final FlatMapController mapController =
            new FlatMapController(this);
    private final FlatAreaController areaController =
            new FlatAreaController(this);

    private final Map<String, CivMap> maps = new HashMap<>();
    private final Map<String, Area> areas = new HashMap<>();
    private final Map<UUID, CivPlayer> civPlayers = new HashMap<>();

    @Override
    public void load() {

        mapController.load();
        areaController.load();

    }

    @Override
    public void unload() {
        mapController.unload();
        areaController.unload();
    }

    @Override
    public FlatMapController map() {
        return mapController;
    }

    @Override
    public FlatAreaController area() {
        return areaController;
    }

    public Map<String, CivMap> getMaps() {
        return maps;
    }

    public Map<String, Area> getAreas() {
        return areas;
    }

    public Map<UUID, CivPlayer> getCivPlayers() {
        return civPlayers;
    }

}
