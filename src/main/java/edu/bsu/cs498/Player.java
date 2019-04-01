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

    public String getName() {
        return name;
    }

    public int getNumber(){
        return number;
    }

    public List<Integer> getStats(){
        return stats;
    }
}
