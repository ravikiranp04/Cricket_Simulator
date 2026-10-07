package org.example.ScoreCardUtils;

import org.example.Player;
import org.example.ScoreCardUtils.ScoreCardStats.BattingPlayerStat;
import org.example.ScoreCardUtils.ScoreCardStats.BowlingPlayerStat;
import org.example.Team;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class BowlingScoreCard {
    private Team battingTeam;
    private Team bowlingTeam;
    private Integer ballsDelivered;
    private Integer totalRunsConceeded;
    private Integer extras;
    private Integer wicketsTaken;



    Map<String,BowlingPlayerStat> playerIdToBowlingStatMap;

    public BowlingScoreCard(Team battingTeam, Team bowlingTeam){
        this.battingTeam=battingTeam;
        this.bowlingTeam=bowlingTeam;
        this.playerIdToBowlingStatMap = new HashMap<>();
        this.extras=0;
        this.totalRunsConceeded=0;
        this.ballsDelivered=0;
        this.wicketsTaken=0;
    }

    public Team getBowlingTeam() {
        return bowlingTeam;
    }

    public Team getBattingTeam() {
        return battingTeam;
    }

    public void increaseExtras(Integer extraRuns){
        extras+=extraRuns;
    }

    public String getOversFinished(){
        Integer overs = ballsDelivered/6;
        Integer balls = ballsDelivered%6;
        return overs + "." + balls;
    }

    public void increaseBallsDelivered(){
        ballsDelivered++;
    }


    public BowlingPlayerStat getPlayerBowlingStat(Player player) {
        String playerId = player.getPlayerId();
        if(!playerIdToBowlingStatMap.containsKey(playerId)){
            playerIdToBowlingStatMap.put(playerId, new BowlingPlayerStat(player.getPlayerName(),this));
        }
        return playerIdToBowlingStatMap.get(playerId);
    }

    public void increaseRunsConceededByTeam(Integer runs){
        totalRunsConceeded+=runs;
    }

    public Integer getBallsDelivered() {
        return ballsDelivered;
    }

    public Integer getTotalRunsConceeded() {
        return totalRunsConceeded;
    }

    public Integer getWicketsTaken() {
        return wicketsTaken;
    }

    public Integer getExtras() {
        return extras;
    }

    public void addExtras(Integer extraRuns){
        extras+=extraRuns;
        //add extra runs to total runs conceeded by team
        increaseRunsConceededByTeam(extraRuns);
    }

    public void addWicket(){
        wicketsTaken+=1;
    }

    public Map<String, BowlingPlayerStat> getPlayerIdToBowlingStatMap() {
        return playerIdToBowlingStatMap;
    }

}
