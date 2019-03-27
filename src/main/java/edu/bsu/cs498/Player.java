package edu.bsu.cs498;

import java.util.List;

public class Player {
    private String name;
    private int number;
    private List<String> stats;

    public Player(String pName, int pNumber, List<String> pStats){
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

    public List<String> getStats(){
        return stats;
    }
}
