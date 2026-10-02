package org.example.ScoreCardUtils.ScoreCardStats;

import org.example.Player;
import org.example.ScoreCardUtils.BowlingScoreCard;

import java.math.BigDecimal;
import java.util.concurrent.locks.AbstractOwnableSynchronizer;

public class BowlingPlayerStat {


    private String playerName;
    private Integer ballsDelivered;
    private Integer runsConceeded;
    private Integer extras;
    private Integer wicketsTaken;
    private BowlingScoreCard bowlingScoreCard;
    public  BowlingPlayerStat(String playerName, BowlingScoreCard bowlingScoreCard){
        this.playerName=playerName;
        this.runsConceeded=0;
        this.extras=0;
        this.wicketsTaken=0;
        this.bowlingScoreCard=bowlingScoreCard;
        this.ballsDelivered=0;
    }


    public void increaseBallsDeliveredByBowler(){
        ballsDelivered++;

        //update teams stats
        bowlingScoreCard.increaseBallsDelivered();
    }

    public String getOversFinishedByBowler() {
        Integer overs = ballsDelivered/6;
        Integer balls = ballsDelivered%6;
        return overs + "." + balls;
    }

    public Integer getRunsConceeded() {
        return runsConceeded;
    }

    public void increaseRunsConceeded(Integer runs){
        runsConceeded+=runs;

        //updating team stats
        bowlingScoreCard.increaseRunsConceededByTeam(runs);
    }

    public void addWicket(){
        wicketsTaken+=1;
        bowlingScoreCard.addWicket();
    }

    public void addExtras(Integer extraRuns){
        extras+=extraRuns;

        //Updating team stats
        bowlingScoreCard.addExtras(extraRuns);
    }

    public void setRunsConceeded(Integer runsConceeded) {
        this.runsConceeded = runsConceeded;
    }

    public Integer getExtras() {
        return extras;
    }

    public void setExtras(Integer extras) {
        this.extras = extras;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Integer getWicketsTaken() {
        return wicketsTaken;
    }

}
