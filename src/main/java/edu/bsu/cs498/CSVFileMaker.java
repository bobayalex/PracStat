package edu.bsu.cs498;

import java.io.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

class CSVFileMaker {
    private List<Player> players;
    private String teamName;
    private List<Double> teamStats;
    private List<String> practices;

    CSVFileMaker(List<Player> playerList, String team, List<Double> teamStatList, List<String> practiceList) {
        players = playerList;
        teamName = team;
        teamStats = teamStatList;
        practices = practiceList;
    }

    boolean generateCSVFile(String fileName, String folderPath) {
        int numPractices = practices.size();
        return writePracticeData(fileName, numPractices, folderPath);
    }

    boolean writePracticeData(String fileName, int numPractices, String folderPath) {
        File dir = new File(folderPath);
        if (!dir.exists()) dir.mkdirs();
        File outputFile = new File(folderPath + "/" + fileName);
        if (numPractices == 1) {
            // single practice data
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(outputFile), StandardCharsets.UTF_8))) {
                writer.write(teamName + "," + practices.get(0) + "\n");
                writer.write("#,PLAYER,K,E,TA,PCT,AST,SA,SE,RE,DIG,BS,BA,BE,BH,PTS\n");
                writePlayerStats(writer, false);
                writeTeamStats(writer);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            // average practice data
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(outputFile), StandardCharsets.UTF_8))) {
                String data = teamName + ",Average Stats,\n";
                writer.write(data);
                writer.write("#,PLAYER,K,E,TA,PCT,AST,SA,SE,RE,DIG,BS,BA,BE,BH,PTS\n");
                writePlayerStats(writer, true);
                writeTeamStats(writer);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    private void writePlayerStats(Writer writer, boolean isAvg) throws IOException {
        if (isAvg) {
            // find average of player stats
            players = updatePlayerStats(players);
        }
        StringBuilder data;
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            // calculate PCT and PTS for player
            String pct = calculatePCT(player.getStats());
            String pts = calculatePTS(player.getStats());
            // add line break if more than one player
            if (i > 0) {
                writer.write("\n");
            }
            writer.write(player.getNumber() + "," + player.getName() + ",");
            List<Double> stats = player.getStats();
            data = new StringBuilder();

            // calculate pct and pts before loop starts and then add based on index
            for (int j = 0; j < stats.size(); j++) {
                double currentTeamStat = teamStats.get(j);
                currentTeamStat += stats.get(j);
                teamStats.set(j, currentTeamStat);

                data.append(String.valueOf(stats.get(j))).append(",");
                if (j == 2) {// PCT
                    data.append(pct).append(",");
                }
            }
            data.append(pts); // pts is the last value
            writer.write(data.toString());
        }
    }

    private void writeTeamStats(Writer writer) throws IOException {
        StringBuilder data;
        writer.write("," + "\n" + ",Totals,");
        data = new StringBuilder();
        String pct = calculatePCT(teamStats);
        String pts = calculatePTS(teamStats);
        for (int i = 0; i < teamStats.size(); i++) {
            data.append(String.valueOf(teamStats.get(i))).append(",");
            if (i == 2) {// PCT
                data.append(pct).append(",");
            }
        }
        data.append(pts); // pts is the last value
        writer.write(data.toString());
    }

    private List<Player> updatePlayerStats(List<Player> players) {
        List<Player> updatedPlayers = new ArrayList<>();
        players = formatPlayersList(players);
        for (Player player : players) {
            List<Double> updatedStats = calculateAverageStats(player);
            player.setStats(updatedStats);
            updatedPlayers.add(player);
        }
        return updatedPlayers;
    }

    private List<Player> formatPlayersList(List<Player> players) {
        List<Player> uniquePlayers = new ArrayList<>();
        for (Player player : players) {
            boolean isFound = false;
            for (Player uniquePlayer : uniquePlayers) {
                if (uniquePlayer.getName().equals(player.getName()) && uniquePlayer.getNumber() == player.getNumber()) {
                    isFound = true;
                    combineStats(uniquePlayer, player);
                    break;
                }
            }
            if (!isFound) uniquePlayers.add(player);
        }
        return uniquePlayers;
    }

    private List<Double> calculateAverageStats(Player player) {
        int numPractices = practices.size();
        List<Double> currentStats = player.getStats();
        List<Double> averageStats = new ArrayList<>();
        for (Double currentStat : currentStats) {
            DecimalFormat format = new DecimalFormat("0.##");
            currentStat = currentStat / numPractices;
            currentStat = Double.parseDouble(format.format(currentStat));
            averageStats.add(currentStat);
        }
        return averageStats;
    }

    private void combineStats(Player uniquePlayer, Player player) {
        List<Double> uniquePlayerStats = uniquePlayer.getStats();
        List<Double> playerStats = player.getStats();
        for (int i = 0; i < playerStats.size(); i++) {
            double currentUniqueStat = uniquePlayerStats.get(i);
            double currentPlayerStat = playerStats.get(i);
            uniquePlayerStats.set(i, currentUniqueStat + currentPlayerStat);
        }
        uniquePlayer.setStats(uniquePlayerStats);
    }

    String calculatePTS(List<Double> stats) {
        BigDecimal kills = BigDecimal.valueOf(stats.get(0));
        BigDecimal soloBlocks = BigDecimal.valueOf(stats.get(8));
        BigDecimal serviceAces = BigDecimal.valueOf(stats.get(4));
        BigDecimal blockAssists = BigDecimal.valueOf(stats.get(9));
        BigDecimal pts = (blockAssists.multiply(new BigDecimal(.5)).add(kills).add(soloBlocks).add(serviceAces));
        pts = pts.round(new MathContext(3, RoundingMode.HALF_UP));
        return pts.toString();
    }

    String calculatePCT(List<Double> stats) {
        BigDecimal kills = BigDecimal.valueOf(stats.get(0));
        BigDecimal errors = BigDecimal.valueOf(stats.get(1));
        BigDecimal totalAttempts = BigDecimal.valueOf(stats.get(2));
        if (totalAttempts.toString().equals("0") || totalAttempts.toString().equals("0.0")) {
            return "0";
        }
        BigDecimal pct = (kills.subtract(errors)).divide(totalAttempts, 3, RoundingMode.HALF_UP);
        String pctString = pct.toString();
        return formatPCT(pctString);
    }

    String formatPCT(String pctString) {
        // remove first zero case
        if (pctString.charAt(0) == '0') {
            pctString = pctString.substring(1);
            return pctString;
        }
        // negative value case
        if (pctString.charAt(0) == '-' && pctString.charAt(1) == '0') {
            StringBuilder builder = new StringBuilder(pctString);
            builder.deleteCharAt(1);
            pctString = builder.toString();
            return pctString;
        }
        return pctString;
    }
}