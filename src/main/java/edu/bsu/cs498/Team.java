package edu.bsu.cs498;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Team {
    private String teamName;
    private ObservableList<Player> playerList;
    private XMLFileReader reader = new XMLFileReader();

    private void addPlayer(String playerName, String playerNumber, String position){
        Player newPlayer = new Player(playerName, playerNumber, position);
        playerList.add(newPlayer);
    }

    private void editTeam(){

    }

    private void teamSettings(){

    }

    public String getTeamInfo(){
        return ("Team Name: " + teamName + "\n");
    }

    public void getTeamStats(){

    }

    public ObservableList<Player> getPlayers(){
        return playerList;
    }

    public Team(String teamName, ObservableList<Player> playerList) {
        this.teamName = teamName;
        this.playerList = playerList;
        reader.addTeam(teamName, playerList);
        System.out.println(reader.isConfigured());
    }
}
