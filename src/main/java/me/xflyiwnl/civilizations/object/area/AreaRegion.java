package me.xflyiwnl.civilizations.object.area;

import me.xflyiwnl.civilizations.Civilizations;
import me.xflyiwnl.civilizations.object.*;
import me.xflyiwnl.civilizations.util.ColorUtil;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class AreaRegion extends CivObject implements Saveable {

    private Area area;

    private Point city;
    private long residents = 10000;

    private LandType type = LandType.PLAINS;

    private List<UUID> neighbours = new ArrayList<>();

    private List<Point> blocks = new ArrayList<>();
    private HashMap<Resource, Integer> resources = new HashMap<>();

    private Material material = ColorUtil.randomizeColor();

    public AreaRegion() {
    }

    public AreaRegion(Area area, String name) {
        super(name);
        this.area = area;
    }

    public AreaRegion(Area area, String name, Point city, long residents) {
        super(name);
        this.area = area;
        this.city = city;
        this.residents = residents;
    }

    public AreaRegion(Area area, UUID uniqueId, String name, Point city, long residents, LandType type, List<Point> blocks, HashMap<Resource, Integer> resources, List<UUID> neighbours) {
        super(uniqueId, name);
        this.area = area;
        this.city = city;
        this.residents = residents;
        this.type = type;
        this.blocks = blocks;
        this.resources = resources;
        this.neighbours = neighbours;
    }

    @Override
    public void create(boolean save) {
        area.getRegions().add(this);
        if (save) save();
    }

    @Override
    public void save() {
        Civilizations.getInstance().getDataSource().area().saveRegion(area, this);
    }

    @Override
    public void remove() {
        Civilizations.getInstance().getDataSource().area().removeRegion(area, this);
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Point getCity() {
        return city;
    }

    public void setCity(Point city) {
        this.city = city;
    }

    public long getResidents() {
        return residents;
    }

    public void setResidents(long residents) {
        this.residents = residents;
    }

    public LandType getType() {
        return type;
    }

    public void setType(LandType type) {
        this.type = type;
    }

    public List<Point> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<Point> blocks) {
        this.blocks = blocks;
    }

    public HashMap<Resource, Integer> getResources() {
        return resources;
    }

    public void setResources(HashMap<Resource, Integer> resources) {
        this.resources = resources;
    }

    public List<UUID> getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(List<UUID> neighbours) {
        this.neighbours = neighbours;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
}
