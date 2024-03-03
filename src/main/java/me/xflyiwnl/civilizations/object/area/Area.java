package me.xflyiwnl.civilizations.object.area;

import me.xflyiwnl.civilizations.Civilizations;
import me.xflyiwnl.civilizations.object.CivMap;
import me.xflyiwnl.civilizations.object.CivObject;
import me.xflyiwnl.civilizations.object.Saveable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Area extends CivObject implements Saveable {

    private CivMap map;
    private List<AreaRegion> areaRegions = new ArrayList<AreaRegion>();

    public Area() {
    }

    public Area(String name, CivMap map) {
        super(name);
        this.map = map;
    }

    public Area(UUID uniqueId, String name, CivMap map, List<AreaRegion> areaRegions) {
        super(uniqueId, name);
        this.map = map;
        this.areaRegions = areaRegions;
    }

    @Override
    public void create(boolean save) {
        Civilizations.getInstance().getDataSource().area().create(this);
        if (save) save();
    }

    @Override
    public void save() {
        Civilizations.getInstance().getDataSource().area().save(this);
    }

    @Override
    public void remove() {
        Civilizations.getInstance().getDataSource().area().remove(this);
    }

    public CivMap getMap() {
        return map;
    }

    public void setMap(CivMap map) {
        this.map = map;
    }

    public List<AreaRegion> getRegions() {
        return areaRegions;
    }

    public void setRegions(List<AreaRegion> areaRegions) {
        this.areaRegions = areaRegions;
    }
}
