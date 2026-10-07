package org.example;

import java.util.UUID;

public class Player {


    private String playerId;

    private String playerName;
    private PlayerType playerType;
    public Player(String playerName, PlayerType playerType){
        this.playerName=playerName;
        this.playerId = UUID.randomUUID().toString();
        this.playerType = playerType;
    }

    public String getPlayerName() {
        return playerName;
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    public String getPlayerId() {
        return playerId;
    }
}
