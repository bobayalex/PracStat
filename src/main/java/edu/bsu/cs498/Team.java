package edu.bsu.cs498;

import javafx.collections.ObservableList;

public class Team {
    private String teamName;

    String getTeamName(){
        return (teamName);
    }

    public Team(String teamName, ObservableList<PlayerData> playerDataList) {
        XMLFileHandler reader = new XMLFileHandler();
        if (reader.doesTeamExist(teamName)){
            System.out.println("Team already exists");
            throw new IllegalArgumentException();
        }else{
            this.teamName = teamName;
            reader.addTeam(teamName, playerDataList);
        }
    }
}
