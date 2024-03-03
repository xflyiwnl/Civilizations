package me.xflyiwnl.civilizations.database;

import me.xflyiwnl.civilizations.object.CivMap;
import me.xflyiwnl.civilizations.object.area.Area;

public interface DataSource {

    void load();
    void unload();

    DataController<CivMap, String> map();
    DataController<Area, String> area();

}
