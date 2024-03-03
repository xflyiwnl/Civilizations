package me.xflyiwnl.civilizations.object.helper;

import me.xflyiwnl.civilizations.Civilizations;
import org.bukkit.scheduler.BukkitRunnable;

public class CivRunnable extends BukkitRunnable {

    private CivAction action;

    public CivRunnable(CivAction action, int time) {
        this.action = action;
        this.runTaskTimer(Civilizations.getInstance(), 0, time * 20L);
    }

    @Override
    public void run() {
        boolean result = action.execute();
        if (result)
            cancel();
    }

}
