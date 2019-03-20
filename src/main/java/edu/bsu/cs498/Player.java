package edu.bsu.cs498;

public class Player {
    public String playerName;
    public String playerNumber;
    public String playerPosition;

    public Player(String playerName, String playerNumber, String playerPosition) {
        this.playerName = playerName;
        this.playerNumber = playerNumber;
        this.playerPosition = playerPosition;
    }

    public String getPlayerName(){
        return playerName;
    }

    public String getPlayerNumber(){
        return playerNumber;
    }

    public String getPlayerPosition(){
        return playerPosition;
    }

    public void getPlayerStats(){

    }
}
