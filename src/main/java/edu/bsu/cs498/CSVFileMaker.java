package edu.bsu.cs498;

import java.io.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

class CSVFileMaker {
    private List<Integer> spinnerVals;
    private List<Player> players;
    private List<String> statNames;
    private String teamName;
    private String practiceName;

    CSVFileMaker(List<Integer> sVals, List<Player> playerList, List<String> statList, String team, String practice){
        spinnerVals = sVals;
        players = playerList;
        statNames = statList;
        teamName = team;
        practiceName = practice;
    }

    public void generateCSVFile() {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("testCSVFile.csv"), "utf-8"))) {
            writer.write(teamName + "\n" + practiceName + "\n");
            writer.write("#,PLAYER,K,E,TA,PCT,AST,SA,SE,RE,DIG,BS,BA,BE,BH,PTS\n");
            for(Player player: players){
                writer.write(player.getNumber() + "," + player.getName() + ",");
                List<Integer> stats = player.getStats();
                StringBuilder data = new StringBuilder();
                for(int i = 0; i < stats.size(); i++){
                    data.append(String.valueOf(stats.get(i)) + ",");
                    if(i == 2){// PCT
                        calculatePCT(player);
                    }
                }
                data.deleteCharAt(data.lastIndexOf(","));
                writer.write(data.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void calculatePCT(Player player) {
        List<Integer> stats = player.getStats();
        BigDecimal kills = BigDecimal.valueOf(stats.get(0));
        BigDecimal errors = BigDecimal.valueOf(stats.get(1));
        BigDecimal totalAttempts = BigDecimal.valueOf(stats.get(2));
        BigDecimal pct = (kills.subtract(errors)).divide(totalAttempts, 3, RoundingMode.CEILING);
        String pctString = pct.toString();
        pctString = cleanStr(pctString);
        System.out.println(pctString);

    }

    private String cleanStr(String pctString) {
        if(pctString.charAt(0) == '0'){
            pctString = pctString.substring(1);
        }
        if(pctString.charAt(1) == '0'){
            StringBuilder builder = new StringBuilder(pctString);
            builder.deleteCharAt(1);
            pctString = builder.toString();
        }
        return pctString;
    }

}
