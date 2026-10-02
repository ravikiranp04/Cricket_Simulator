package org.example.ScoreCardUtils.ScoreCardStats;

import org.example.Player;
import org.example.ScoreCardUtils.BattingScoreCard;

import java.security.PublicKey;

public class BattingPlayerStat {


    private String playerName;
    private String outReason;
    private Integer battingScore;
    private Integer ballsFaced;
    private Integer sixesHit;
    private Integer foursHit;
    private boolean isOut;
    private  BattingScoreCard battingScoreCard;
    public  BattingPlayerStat(String playerName, BattingScoreCard battingScoreCard){
        this.playerName=playerName;
        this.battingScoreCard=battingScoreCard;
        this.battingScore=0;
        this.ballsFaced=0;
        this.foursHit=0;
        this.sixesHit=0;
        this.isOut=false;
    }

    public Integer getFoursHit() {
        return foursHit;
    }

    public void increaseBattingScore(Integer runs){
        battingScore+=runs;
        //Update teams score
        battingScoreCard.increaseTeamsScore(runs);
    }

    public void increaseSixesHit(){
        sixesHit+=1;
    }
    public void increaseFoursHit(){
        foursHit+=1;
    }

    public void increaseBallsFaced(){
        ballsFaced+=1;
        //Update teams stats
        battingScoreCard.increaseBallsFacedByTeam();
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getOutReason() {
        return outReason;
    }

    public void setOutReason(String outReason) {
        this.outReason = outReason;
    }

    public Integer getBattingScore() {
        return battingScore;
    }


    public Integer getBallsFaced() {
        return ballsFaced;
    }

    public Integer getSixesHit() {
        return sixesHit;
    }


    public boolean isOut() {
        return isOut;
    }

    public void sendOut(String outReason) {
        isOut = true;
        setOutReason(outReason);

        //Add wicket to teams stats
        battingScoreCard.addWicket();
    }

}
