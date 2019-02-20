package edu.bsu.cs498;

import java.util.ArrayList;

public class Team {
    private String teamName;
    private int numberOfPlayers;
    private ArrayList<Player> playerList = new ArrayList<Player>();

    private void addTeam(){

    }

    private void addPlayer(String playerName, int playerNumber, String position){
        Player newPlayer = new Player(playerName, playerNumber, position);
        playerList.add(newPlayer);
    }

    private void editTeam(){

    }

    private void teamSettings(){

    }

    public String getTeamInfo(){
        return ("Team Name: " + teamName + "\n" + "Number of Players: " + numberOfPlayers);
    }

    public void getTeamStats(){

    }

    private ArrayList<Player> getPlayers(){
        return playerList;
    }

    public Team(String teamName, int numberOfPlayers) {
        this.teamName = teamName;
        this.numberOfPlayers = numberOfPlayers;
    }
}
