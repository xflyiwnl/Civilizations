package me.xflyiwnl.civilizations.object;

public enum Resource {

    OIL("Нефть"),
    RUBBER("Резина"),
    ALUMINIUM("Алюминий"),
    STEEL("Сталь"),
    TUNGSTEN("Вольфрам"),
    CHROME("Хром");

    private String name;

    Resource(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
