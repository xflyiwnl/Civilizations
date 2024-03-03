package me.xflyiwnl.civilizations.object;

public enum LandType {

    HILLS(-25, 0.75f, -10),
    MOUNTAIN(-50, 0.5f, -25),
    PLAINS,
    JUNGLE(-25, 0.75f, -10),
    DESERT(-10, 0.75f, -10);


    private float attackBonus = 0; // % к атаке
    private float unitSpeed = 1; // скорость солдат * умножение
    private float supplyLosses = 0; // % к истощению

    LandType() {
    }

    LandType(float attackBonus, float unitSpeed, float supplyLosses) {
        this.attackBonus = attackBonus;
        this.unitSpeed = unitSpeed;
        this.supplyLosses = supplyLosses;
    }

    public float getAttackBonus() {
        return attackBonus;
    }

    public float getUnitSpeed() {
        return unitSpeed;
    }

    public float getSupplyLosses() {
        return supplyLosses;
    }
}
