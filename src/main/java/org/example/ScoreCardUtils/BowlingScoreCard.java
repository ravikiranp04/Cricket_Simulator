package org.example.ScoreCardUtils;

import org.example.Player;
import org.example.ScoreCardUtils.ScoreCardStats.BattingPlayerStat;
import org.example.ScoreCardUtils.ScoreCardStats.BowlingPlayerStat;
import org.example.Team;

import java.util.HashMap;
import java.util.Map;

public class BowlingScoreCard {
    private Team battingTeam;
    private Team bowlingTeam;

    Map<String,BowlingPlayerStat> playerIdToBowlingStatMap;

    private Integer extras;
    BowlingScoreCard(Team battingTeam, Team bowlingTeam){
        this.battingTeam=battingTeam;
        this.bowlingTeam=bowlingTeam;
        this.playerIdToBowlingStatMap = new HashMap<>();
        this.extras=0;
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


    public BowlingPlayerStat getPlayerBowlingStat(Player player) {
        String playerId = player.getPlayerId();
        if(!playerIdToBowlingStatMap.containsKey(playerId)){
            playerIdToBowlingStatMap.put(playerId, new BowlingPlayerStat(player.getPlayerName()));
        }
        return playerIdToBowlingStatMap.get(playerId);
    }

    public Integer getExtras() {
        return extras;
    }

}
