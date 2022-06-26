package me.maanraj514.contrapvp.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum GameMode {

    DUOS(2, 16, 4),
    TEAMS(4, 16, 8),
    DUELS(1, 2, 2);

    @Getter
    private final int playersPerTeam;
    @Getter
    private final int maxPlayers;
    @Getter
    private final int minPlayers;
}
