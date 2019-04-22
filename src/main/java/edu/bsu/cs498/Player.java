package edu.bsu.cs498;

import java.util.List;

public class Player {
    private String name;
    private int number;
    private List<Integer> stats;
    private String practice;

    public Player(String playerName, int playerNumber, List<Integer> playerStats, String playerPractice){
        name = playerName;
        number = playerNumber;
        stats = playerStats;
        practice = playerPractice;
    }

    String getName() {
        return name;
    }

    int getNumber(){
        return number;
    }

    List<Integer> getStats(){
        return stats;
    }

    void setStats(List<Integer> playerStats){
        stats = playerStats;
    }

    String getPractice(){
        return practice;
    }
}
