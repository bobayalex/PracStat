package edu.bsu.cs498;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.List;

class CSVFileMaker {
    private List<Player> players;
    private String teamName;
    private List<Integer> teamStats;
    private String practiceName;
    CSVFileMaker(List<Player> playerList, String team, List<Integer> teamStatList, String practice){
        players = playerList;
        teamName = team;
        teamStats = teamStatList;
        practiceName = practice;
    }

    void generateCSVFile() {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("testCSVFile.csv"), StandardCharsets.UTF_8))) {
            writer.write(teamName + "," + practiceName + "\n");
            writer.write("#,PLAYER,K,E,TA,PCT,AST,SA,SE,RE,DIG,BS,BA,BE,BH,PTS\n");
            StringBuilder data;
            for(int i = 0; i < players.size(); i++){
                Player player = players.get(i);
                // calculate PCT and PTS for player
                String pct = calculatePCT(player);
                String pts = calculatePTS(player);
                // add line break if more than one player
                if(i > 0){
                    writer.write("\n");
                }
                writer.write(player.getNumber() + "," + player.getName() + ",");
                List<Integer> stats = player.getStats();
                data = new StringBuilder();

                // calculate pct and pts before loop starts and then add based on index
                for(int j = 0; j < stats.size(); j++){
                    int currentTeamStat = teamStats.get(j);
                    currentTeamStat += stats.get(j);
                    teamStats.set(j, currentTeamStat);

                    data.append(String.valueOf(stats.get(j))).append(",");
                    if(j == 2){// PCT
                        data.append(pct).append(",");
                    }
                }
                data.append(pts); // pts is the last value
                writer.write(data.toString());
                //updateTeamStats(teamStats, stats);
            }
            // write total team stats here
            writer.write("," + "\n" + ",Totals,");
            data = new StringBuilder();
            String pct = calculateTeamPCT(teamStats);
            String pts = calculateTeamPTS(teamStats);
            for(int k = 0; k < teamStats.size(); k++){
                data.append(String.valueOf(teamStats.get(k))).append(",");
                if(k == 2){// PCT
                    data.append(pct).append(",");
                }
            }
            data.append(pts); // pts is the last value
            writer.write(data.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String calculatePTS(Player player) {
        List<Integer> stats = player.getStats();
        BigDecimal kills = BigDecimal.valueOf(stats.get(0));
        BigDecimal soloBlocks = BigDecimal.valueOf(stats.get(8));
        BigDecimal serviceAces = BigDecimal.valueOf(stats.get(4));
        BigDecimal blockAssists = BigDecimal.valueOf(stats.get(9));
        BigDecimal pts = (blockAssists.multiply(new BigDecimal(.5)).add(kills).add(soloBlocks).add(serviceAces));
        return pts.toString();
    }

    private String calculateTeamPTS(List<Integer> stats) {
        BigDecimal kills = BigDecimal.valueOf(stats.get(0));
        BigDecimal soloBlocks = BigDecimal.valueOf(stats.get(8));
        BigDecimal serviceAces = BigDecimal.valueOf(stats.get(4));
        BigDecimal blockAssists = BigDecimal.valueOf(stats.get(9));
        BigDecimal pts = (blockAssists.multiply(new BigDecimal(.5)).add(kills).add(soloBlocks).add(serviceAces));
        return pts.toString();
    }

    private String calculatePCT(Player player) {
        List<Integer> stats = player.getStats();
        BigDecimal kills = BigDecimal.valueOf(stats.get(0));
        BigDecimal errors = BigDecimal.valueOf(stats.get(1));
        BigDecimal totalAttempts = BigDecimal.valueOf(stats.get(2));
        BigDecimal pct = (kills.subtract(errors)).divide(totalAttempts, 3, RoundingMode.CEILING);
        String pctString = pct.toString();
        return formatPCT(pctString);
    }

    private String calculateTeamPCT(List<Integer> stats) {
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