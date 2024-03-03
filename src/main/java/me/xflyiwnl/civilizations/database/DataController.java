package me.xflyiwnl.civilizations.database;

import java.util.List;

public abstract class DataController<T, K> {

    public abstract void load();
    public abstract void unload();

    public abstract List<T> all();
    public abstract T get(K id);

    public abstract void create(T t);
    public abstract void save(T t);

    public abstract void remove(T t);

}
