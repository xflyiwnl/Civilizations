package me.xflyiwnl.civilizations.object;

import me.xflyiwnl.civilizations.Civilizations;
import me.xflyiwnl.civilizations.object.editor.Editor;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.UUID;

public class CivMap extends CivObject implements Saveable {

    private Point startPoint;
    private Point endPoint;

    private Location spawn;
    private Editor editor;

    public CivMap() {
    }

    public CivMap(String name) {
        super(name);
    }

    public CivMap(String name, Point startPoint, Point endPoint, Location spawn) {
        super(name);
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.spawn = spawn;

        checkPoints();
    }

    public CivMap(UUID uniqueId, String name, Point startPoint, Point endPoint, Location spawn) {
        super(uniqueId, name);
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.spawn = spawn;

        checkPoints();
    }

    @Override
    public void create(boolean save) {
        Civilizations.getInstance().getDataSource().map().create(this);
        if (save) save();
    }

    @Override
    public void save() {
        Civilizations.getInstance().getDataSource().map().save(this);
    }

    @Override
    public void remove() {
        Civilizations.getInstance().getDataSource().map().remove(this);
    }

    public void checkPoints() {

        if (startPoint == null ||
                endPoint == null) return;

        World world = startPoint.getWorld();

        int minX = (int) Math.min(startPoint.getX(), endPoint.getX());
        int maxX = (int) Math.max(startPoint.getX(), endPoint.getX());

        int minY = (int) Math.min(startPoint.getY(), endPoint.getY());
        int maxY = (int) Math.max(startPoint.getY(), endPoint.getY());

        int minZ = (int) Math.min(startPoint.getZ(), endPoint.getZ());
        int maxZ = (int) Math.max(startPoint.getZ(), endPoint.getZ());

        this.startPoint = new Point(world, minX, minY, minZ);
        this.endPoint = new Point(world, maxX, maxY, maxZ);
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
        checkPoints();
    }

    public Point getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Point endPoint) {
        this.endPoint = endPoint;
        checkPoints();
    }

    public Location getSpawn() {
        return spawn;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }

    public boolean hasEditor() {
        return editor != null;
    }

    public Editor getEditor() {
        return editor;
    }

    public void setEditor(Editor editor) {
        this.editor = editor;
    }
}
