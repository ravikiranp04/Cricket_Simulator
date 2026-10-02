package org.example.ScoreCardUtils;

import org.example.Team;

public class InningsScoreCard {
    private String scoreCardId;


    private BattingScoreCard battingScoreCard;
    private BowlingScoreCard bowlingScoreCard;

    private Team battingTeam;
    private Team bowlingTeam;

    public void initializeScoreBoard(Team battingTeam, Team bowlingTeam){
        this.battingTeam=battingTeam;
        this.bowlingTeam=bowlingTeam;
    }

    public BattingScoreCard getBattingScoreCard() {
        return battingScoreCard;
    }

    public BowlingScoreCard getBowlingScoreCard() {
        return bowlingScoreCard;
    }

    public Team getBattingTeam() {
        return battingTeam;
    }

    public Team getBowlingTeam() {
        return bowlingTeam;
    }

    public void setBatAndBowlScoreCards(BattingScoreCard battingScoreCard, BowlingScoreCard bowlingScoreCard){
        this.battingScoreCard=battingScoreCard;
        this.bowlingScoreCard=bowlingScoreCard;
    }
}
