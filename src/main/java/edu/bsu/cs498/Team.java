package edu.bsu.cs498;

import javafx.collections.ObservableList;

public class Team {
    private String teamName;
    private ObservableList<PlayerData> playerDataList;
    private XMLFileHandler reader = new XMLFileHandler();

    public String getTeamName(){
        return (teamName);
    }

    public ObservableList<PlayerData> getPlayersData(){
        return playerDataList;
    }

    public Team(String teamName, ObservableList<PlayerData> playerDataList) {
        if (reader.doesTeamExist(teamName)){
            System.out.println("Team already exists");
            throw new IllegalArgumentException();
        }else{
            this.teamName = teamName;
            this.playerDataList = playerDataList;
            reader.addTeam(teamName, playerDataList);
        }
    }
}
