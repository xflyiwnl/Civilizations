package me.xflyiwnl.civilizations.object;

import com.google.common.base.Objects;
import org.bukkit.Location;
import org.bukkit.World;

public class Point {

    private World world;
    private int x, y, z;

    public Point() {
    }

    public Point(Location location) {
        this.world = location.getWorld();
        this.x = (int) location.getX();
        this.y = (int) location.getY();
        this.z = (int) location.getZ();
    }

    public Point(World world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Location asLocation() {
        return new Location(world, x, y, z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y && z == point.z && java.util.Objects.equals(world, point.world);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(world, x, y, z);
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }
}
