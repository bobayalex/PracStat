package edu.bsu.cs498;

import java.io.*;
import java.math.BigDecimal;
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
                // calculate PCT and PTS for player
                String pct = calculatePCT(player);
                String pts = calculatePTS(player);
                writer.write(player.getNumber() + "," + player.getName() + ",");
                List<Integer> stats = player.getStats();
                StringBuilder data = new StringBuilder();

                // calculate pct and pts before loop starts and then add based on index
                for(int i = 0; i < stats.size(); i++){
                    data.append(String.valueOf(stats.get(i)) + ",");
                    if(i == 2){// PCT
                        data.append(pct + ",");
                    }
                }
                data.append(pts); // pts is the last value
                writer.write(data.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String calculatePTS(Player player) {
        List<Integer> stats = player.getStats();
        BigDecimal kills = BigDecimal.valueOf(stats.get(0));
        BigDecimal soloBlocks = BigDecimal.valueOf(stats.get(8));
        BigDecimal serviceAces = BigDecimal.valueOf(stats.get(4));
        BigDecimal blockAssists = BigDecimal.valueOf(stats.get(9));
        BigDecimal pts = (blockAssists.multiply(new BigDecimal(.5)).add(kills).add(soloBlocks).add(serviceAces));
        return pts.toString();
    }

    String calculatePCT(Player player) {
        List<Integer> stats = player.getStats();
        BigDecimal kills = BigDecimal.valueOf(stats.get(0));
        BigDecimal errors = BigDecimal.valueOf(stats.get(1));
        BigDecimal totalAttempts = BigDecimal.valueOf(stats.get(2));
        BigDecimal pct = (kills.subtract(errors)).divide(totalAttempts, 3, RoundingMode.CEILING);
        String pctString = pct.toString();
        return formatPCT(pctString);
    }

    private String formatPCT(String pctString) {
        // remove first zero case
        if(pctString.charAt(0) == '0'){
            pctString = pctString.substring(1);
            return pctString;
        }
        // negative value case
        if(pctString.charAt(0) == '-' && pctString.charAt(1) == '0'){
            StringBuilder builder = new StringBuilder(pctString);
            builder.deleteCharAt(1);
            pctString = builder.toString();
            return pctString;
        }
        return pctString;
    }



}
