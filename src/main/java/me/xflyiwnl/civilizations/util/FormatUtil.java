package me.xflyiwnl.civilizations.util;

import me.xflyiwnl.civilizations.object.Point;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class FormatUtil {

    public static String formatLocation(Point point) {
        return formatLocation(point.asLocation());
    }

    public static String formatLocation(Location location) {
        return location.getWorld().getName() + ", " +
                location.getX() + ", " +
                location.getY() + ", " +
                location.getZ() + ", " +
                location.getYaw() + ", " +
                location.getPitch();
    }

    public static Location unformatLocation(String formatted) {
        String[] split = formatted.split(", ");
        return new Location(
                Bukkit.getWorld(split[0]),
                Double.parseDouble(split[1]),
                Double.parseDouble(split[2]),
                Double.parseDouble(split[3]),
                Float.parseFloat(split[4]),
                Float.parseFloat(split[5]));
    }

    public static Point unformatPoint(String formatted) {
        return new Point(unformatLocation(formatted));
    }

}
