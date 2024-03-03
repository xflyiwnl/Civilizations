package me.xflyiwnl.civilizations.object.editor;

public enum EditorType {

    AREA("Территория"),
    SCENARIO("Сценарий");

    private String name;

    EditorType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static EditorType getEditor(String formatted) {
        for (EditorType editor : EditorType.values()) {
            if (editor.toString().equalsIgnoreCase(formatted)) {
                return editor;
            }
        }
        return null;
    }

}
