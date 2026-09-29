package org.example.ScoreCardUtils;

import org.example.Player;
import org.example.ScoreCardUtils.ScoreCardStats.BattingPlayerStat;
import org.example.Team;

import java.util.HashMap;
import java.util.Map;

public class BattingScoreCard {

    private Team battingTeam;
    private Team bowlingTeam;
    Map<String, BattingPlayerStat> playerIdToBattingStatMap;

    private Integer totalRunsScored=0;



    private Integer wickets=0;

    private Integer extras=0;

    BattingScoreCard(Team battingTeam, Team bowlingTeam){
        this.battingTeam=battingTeam;
        this.bowlingTeam=bowlingTeam;
        this.playerIdToBattingStatMap = new HashMap<>();
        this.extras=0;
        this.totalRunsScored=0;
        this.wickets=0;
    }
    public Integer getWickets() {
        return wickets;
    }
    public Team getBattingTeam() {
        return battingTeam;
    }

    public void increaseTeamsScore(Integer runs){
        totalRunsScored+=runs;
    }

    public void increaseExtras(Integer extraRuns){
        extras+=extraRuns;
    }

    public Team getBowlingTeam() {
        return bowlingTeam;
    }
    public BattingPlayerStat getPlayerBattingStat(Player player) {
        String playerId= player.getPlayerId();
        if(!playerIdToBattingStatMap.containsKey(playerId)){
            playerIdToBattingStatMap.put(playerId, new BattingPlayerStat(player.getPlayerName()));
        }
        return playerIdToBattingStatMap.get(playerId);
    }

    public void addWicket(){
        wickets+=1;
    }

    public Integer getTotalRunsScored() {
        return totalRunsScored;
    }

    public Integer getExtras() {
        return extras;
    }

    public void setTotalRunsScored(Integer totalRunsScored) {
        this.totalRunsScored = totalRunsScored;
    }

    public void setExtras(Integer extras) {
        this.extras = extras;
    }

}
