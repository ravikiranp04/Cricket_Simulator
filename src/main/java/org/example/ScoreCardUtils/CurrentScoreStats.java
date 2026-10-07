package org.example.ScoreCardUtils;

import org.example.MatchUtils.BallType;
import org.example.Player;
import org.example.ScoreCardUtils.ScoreCardStats.BattingPlayerStat;
import org.example.ScoreCardUtils.ScoreCardStats.BowlingPlayerStat;

import java.math.BigDecimal;

public class CurrentScoreStats {

    Player striker;
    Player nonStriker;
    BattingPlayerStat strikerBattingStat;
    BattingPlayerStat nonStrikerBattingStat;

    Player bowler;

    BowlingPlayerStat bowlerBowlingStat;

    BattingScoreCard battingScoreCard;
    BowlingScoreCard bowlingScoreCard;

    public CurrentScoreStats(Player striker, Player nonStriker, BattingPlayerStat strikerBattingStat, BattingPlayerStat nonStrikerBattingStat, BattingScoreCard battingScoreCard, BowlingScoreCard bowlingScoreCard){
        this.striker=striker;
        this.strikerBattingStat=strikerBattingStat;
        this.nonStriker = nonStriker;
        this.nonStrikerBattingStat=nonStrikerBattingStat;
        this.battingScoreCard = battingScoreCard;
        this.bowlingScoreCard=bowlingScoreCard;
    }

    public String getOversFinishedByBowler(){
        return bowlerBowlingStat.getOversFinishedByBowler();
    }
    public String getOversFinished(){
        return bowlingScoreCard.getOversFinished();
    }

    public void swapStriker(){
        swapStatisticsMaps();
        Player temp = nonStriker;
        nonStriker=striker;
        striker=temp;
    }

    public void swapStatisticsMaps(){
        BattingPlayerStat temp= nonStrikerBattingStat;
        nonStrikerBattingStat=strikerBattingStat;
        strikerBattingStat=temp;
    }

    public void changeStriker(Player newStriker,BattingPlayerStat newStrikerBattingStat){
        this.strikerBattingStat=newStrikerBattingStat;
        this.striker=newStriker;
    }

    public void changeNonStriker(Player newNonStriker,BattingPlayerStat newNonStrikerBattingStat){
        this.nonStrikerBattingStat=newNonStrikerBattingStat;
        this.nonStriker=newNonStriker;
    }

    public void changeBowler(Player newBowler, BowlingPlayerStat bowlerBowlingStat){
        this.bowlerBowlingStat=bowlerBowlingStat;
        this.bowler=newBowler;
    }

    public void updateBallsDeliveredData(){
        // Update balls faced for batter
        strikerBattingStat.increaseBallsFaced();

        //update balls delivered by that bowler and team
        bowlerBowlingStat.increaseBallsDeliveredByBowler();;
    }


    public BowlingPlayerStat getBowlerBowlingStat() {
        return bowlerBowlingStat;
    }

    public BattingScoreCard getBattingScoreCard() {
        return battingScoreCard;
    }

    public BowlingScoreCard getBowlingScoreCard() {
        return bowlingScoreCard;
    }

    public BattingPlayerStat getNonStrikerBattingStat() {
        return nonStrikerBattingStat;
    }

    public BattingPlayerStat getStrikerBattingStat() {
        return strikerBattingStat;
    }
    public Player getBowler() {
        return bowler;
    }

    public Player getStriker() {
        return striker;
    }

    public Player getNonStriker() {
        return nonStriker;
    }

    public static boolean checkIfTeamAllOut(Integer currentBatterIdx){
        return currentBatterIdx==10;
    }

}
