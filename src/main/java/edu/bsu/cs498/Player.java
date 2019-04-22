package edu.bsu.cs498;

import java.util.List;

public class Player {
    private String name;
    private int number;
    private List<Double> stats;
    private String practice;

    public Player(String playerName, int playerNumber, List<Double> playerStats, String playerPractice){
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

    List<Double> getStats(){
        return stats;
    }

    void setStats(List<Double> playerStats){
        stats = playerStats;
    }

    String getPractice(){
        return practice;
    }
}
