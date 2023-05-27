/*
 * MegaMek - Copyright (C) 2007-2008 Ben Mazur (bmazur@sev.org)
 *
 *  This program is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU General Public License as published by the Free
 *  Software Foundation; either version 2 of the License, or (at your option)
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *  for more details.
 */
package megamek.server.victory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

import megamek.common.IPlayer;
import megamek.common.Report;

/**
 * quick implementation of a Victory.Result stores player scores and a flag if
 * game-ending victory is achieved or not
 */
public class VictoryResult implements IResult {
    protected boolean victory;
    protected Throwable tr;
    protected ArrayList<Report> reports = new ArrayList<Report>();
    protected HashMap<Integer, Double> playerScore = new HashMap<Integer, Double>();
    protected HashMap<Integer, Double> teamScore = new HashMap<Integer, Double>();
    protected double hiScore = 0;

    protected VictoryResult(boolean win) {
        this.victory = win;
        tr = new Throwable();
    }
    
    protected VictoryResult(boolean win, int player, int team) {
    	this.victory = win;
    	tr = new Throwable();
        if (player != IPlayer.PLAYER_NONE) {
            addPlayerScore(player, 1.0);
        }
        if (team != IPlayer.TEAM_NONE) {
            addTeamScore(team, 1.0);
        }
    }
    
    protected static VictoryResult noResult() {
    	return new VictoryResult(false, IPlayer.PLAYER_NONE, IPlayer.TEAM_NONE);
    }
    
    protected static VictoryResult drawResult() {
        return new VictoryResult(true, IPlayer.PLAYER_NONE, IPlayer.TEAM_NONE);
    }

    public int getWinningPlayer() {
        return getWinningUnit(true);
    }

    public int getWinningTeam() {
        return getWinningUnit(false);
    }

    public int getWinningUnit(boolean isPlayer){
        HashMap<Integer, Double> scores = isPlayer ? playerScore : teamScore;
        double max = Double.MIN_VALUE;
        int maxUnit = isPlayer ? IPlayer.PLAYER_NONE : IPlayer.TEAM_NONE;
        boolean draw = false;
        for (Map.Entry<Integer, Double> entry : scores.entrySet()) {
            double score = entry.getValue();
            if (score == max) {
                draw = true;
            }
            if (score > max) {
                draw = false;
                max = score;
                maxUnit = entry.getKey();
            }
        }
        if (draw){
            return isPlayer ? IPlayer.PLAYER_NONE : IPlayer.TEAM_NONE;
        }
        return maxUnit;
    }

    protected void updateHiScore() {
        // used to calculate winner
        hiScore = Double.MIN_VALUE;
        for (Double d : playerScore.values()) {
            if (d > hiScore)
                hiScore = d;
        }
        for (Double d : teamScore.values()) {
            if (d > hiScore)
                hiScore = d;
        }
    }

    public void addPlayerScore(int id, double score) {
        playerScore.put(id, score);
        updateHiScore();
    }

    public void addTeamScore(int id, double score) {
        teamScore.put(id, score);
        updateHiScore();
    }

    public boolean isWinningPlayer(int id) {
        return isWinningUnit(getPlayerScore(id));
    }

    public boolean isWinningTeam(int id) {
        return isWinningUnit(getTeamScore(id));
    }

    public boolean isWinningUnit(double score){
        // two decimal compare..
        return ((score * 100) % 100) == ((hiScore * 100) % 100);
    }

    public boolean victory() {
        return victory;
    }

    public void setVictory(boolean b) {
        this.victory = b;
    }

    public double getPlayerScore(int id) {
        if (playerScore.get(id) == null)
            return 0.0;
        return playerScore.get(id);
    }

    public int[] getPlayers() {
        return intify(playerScore.keySet().toArray(new Integer[0]));
    }

    public double getTeamScore(int id) {
        if (teamScore.get(id) == null)
            return 0.0;
        return teamScore.get(id);
    }

    public int[] getTeams() {
        return intify(teamScore.keySet().toArray(new Integer[0]));
    }

    public void addReport(Report r) {
        reports.add(r);
    }

    /**
     * Adds all reports from a provided list of reports to the internal list of reports
     * @param reports The list of reports to be added.
     */
    public void addReports(List<Report> reports) {
        for (Report report : reports) {
            addReport(report);
        }
    }

    public ArrayList<Report> getReports() {
        return reports;
    }

    protected String getTrace() {
        StringWriter sw = new StringWriter();
        PrintWriter pr = new PrintWriter(sw);
        tr.printStackTrace(pr);
        pr.flush();
        return sw.toString();
    }

    private int[] intify(Integer[] ar) {
        int[] ret = new int[ar.length];
        for (int i = 0; i < ar.length; i++)
            ret[i] = ar[i];
        return ret;
    }

    @Override
    public String toString() {
        return "victory provided to you by:" + getTrace();
    }

    public boolean isDraw() {
        return (getWinningPlayer() == IPlayer.PLAYER_NONE && getWinningTeam() == IPlayer.TEAM_NONE);
    }
}