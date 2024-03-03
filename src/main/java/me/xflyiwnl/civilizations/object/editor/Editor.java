package me.xflyiwnl.civilizations.object.editor;

import me.xflyiwnl.civilizations.object.CivPlayer;

import java.util.List;

public interface Editor {

    void enable();
    void disable();

    void addPlayer(CivPlayer player);
    void removePlayer(CivPlayer player);

    EditorType getType();
    List<CivPlayer> getPlayers();

}
