package edu.bsu.cs498;

import java.util.List;

public class Player {
    private String name;
    private int number;
    private List<Integer> stats;

    public Player(String pName, int pNumber, List<Integer> pStats){
        name = pName;
        number = pNumber;
        stats = pStats;
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
}
