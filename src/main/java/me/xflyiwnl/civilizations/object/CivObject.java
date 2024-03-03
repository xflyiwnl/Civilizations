package me.xflyiwnl.civilizations.object;

import java.util.UUID;

public abstract class CivObject implements Identifyable, Nameable {

    private UUID uniqueId = UUID.randomUUID();
    private String name = "";

    public CivObject() {
    }

    public CivObject(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public CivObject(String name) {
        this.name = name;
    }

    public CivObject(UUID uniqueId, String name) {
        this.uniqueId = uniqueId;
        this.name = name;
    }

    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
