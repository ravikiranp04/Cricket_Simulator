package org.example.MatchUtils;


import org.example.Handlers.WicketAndRunsHandler;
import org.example.LogUtils.LogUtils;
import org.example.LogUtils.LoggerConfig;
import org.example.Player;
import org.example.ScoreCardUtils.BattingScoreCard;
import org.example.ScoreCardUtils.BowlingScoreCard;
import org.example.ScoreCardUtils.CurrentScoreStats;
import org.example.ScoreCardUtils.InningsScoreCard;
import org.example.ScoreCardUtils.ScoreCardStats.BattingPlayerStat;
import org.example.ScoreCardUtils.ScoreCardStats.BowlingPlayerStat;
import org.example.Team;

import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;

public class LimitedOversMatch implements  Match{
    Random random = new Random();
    String matchId;
    Integer noOfOvers;
    Team teamA;
    Team teamB;
    Team battingTeam , bowlingTeam;
    InningsScoreCard firstInningsScoreCard;
    InningsScoreCard secondInningsScoreCard;

//    Player striker;
//    Player nonStriker;
//    BattingPlayerStat strikerBattingStat;
//    BattingPlayerStat nonStrikerBattingStat;

    CurrentScoreStats currentScoreStats;

//    Player bowler;
//    BowlingPlayerStat bowlerBowlingStat;

    BattingScoreCard battingScoreCard;
    BowlingScoreCard bowlingScoreCard;

    Integer secondInningsTarget = -1;
    Integer currentOver;
    Integer currentBall;
    boolean isTossWinnerTeamA = false;
    boolean isTossWinnerTeamB = false;

    boolean isFreeHit=false;
    private final Logger log;
    private Integer currentBatterIdx;

    boolean allOutStatus = false;
    boolean isGameFinished = false;

    public LimitedOversMatch(MatchConfig config){
        this.matchId = UUID.randomUUID().toString();
        this.teamA = config.getTeamA();
        this.teamB = config.getTeamB();
        this.noOfOvers = config.getNoOfOvers();
        this.log = LoggerConfig.configure(matchId);
        this.firstInningsScoreCard=new InningsScoreCard();
        this.secondInningsScoreCard = new InningsScoreCard();
    }

    public Logger getLogger() {
        return log;
    }

    @Override
    public void play(){
        simulateToss();
        log.info("*******************************************************");
        log.info("First Innings Started!!");
        log.info("*******************************************************");

        firstInningsScoreCard.initializeScoreBoard(battingTeam,bowlingTeam);

        playInnings(firstInningsScoreCard);
        log.info("*******************************************************");
        log.info("First Innings Finished!!");
        log.info("*******************************************************");
        log.info("First Innings Score: "+firstInningsScoreCard.getBattingScoreCard().getTotalRunsScored()+" - "+firstInningsScoreCard.getBattingScoreCard().getWickets());
        log.info("Setting Target..............");
        secondInningsTarget = calculateTarget(firstInningsScoreCard);

        //Roles reversing for both teams
        swapTeamRolesForSecondInnings();

        log.info("Team "+ battingTeam.getTeamName()+" needs "+secondInningsTarget+" to win!!");


        log.info("*******************************************************");
        log.info("Second Innings Started!!");
        log.info("*******************************************************");


        secondInningsScoreCard.initializeScoreBoard(battingTeam,bowlingTeam);

        playInnings(secondInningsScoreCard);

        log.info("*******************************************************");
        log.info("Second Innings Finished!!");
        log.info("*******************************************************");
        log.info("Second Innings Score: "+secondInningsScoreCard.getBattingScoreCard().getTotalRunsScored()+" - "+secondInningsScoreCard.getBattingScoreCard().getWickets());

        // If chasing team got all out or overs finished before chasing
        if((!isGameFinished && allOutStatus) || (secondInningsTarget>battingScoreCard.getTotalRunsScored())){
            log.info("Team "+bowlingTeam.getTeamName()+" won by "+(secondInningsTarget-battingScoreCard.getTotalRunsScored())+" runs.");
        }

        log.info("Game Finished!!");

    }

    public void swapTeamRolesForSecondInnings(){
        Team temp = battingTeam;
        battingTeam = bowlingTeam;
        bowlingTeam=temp;
    }

    public  Integer calculateTarget(InningsScoreCard firstInningsScoreCard){
        return firstInningsScoreCard.getBattingScoreCard().getTotalRunsScored()+1;
    }

    public void playInnings(InningsScoreCard inningsScoreCard){
        //Reset states
        setAllOutStatus(false);
        isGameFinished = false;
        isFreeHit = false;

        //Getting respective batting and bowling score cards
        battingScoreCard= inningsScoreCard.getBattingScoreCard();
        bowlingScoreCard = inningsScoreCard.getBowlingScoreCard();
        currentOver=1;
        //Retrieving first two batsman and bowler
        Integer  currentBowlerIdx=-1;
        currentBatterIdx=-1;
        Player striker = battingTeam.getNextBatter(currentBatterIdx);
        currentBatterIdx+=1;
        log.info("New Batsman: Striker -> "+striker.getPlayerName());

        Player nonStriker = battingTeam.getNextBatter(currentBatterIdx);
        currentBatterIdx+=1;
        log.info("New Batsman: Non Striker -> "+nonStriker.getPlayerName());

        BattingPlayerStat strikerBattingStat = battingScoreCard.getPlayerBattingStat(striker);

        BattingPlayerStat nonStrikerBattingStat = battingScoreCard.getPlayerBattingStat(nonStriker);

        currentScoreStats = new CurrentScoreStats(striker, nonStriker, strikerBattingStat,nonStrikerBattingStat,battingScoreCard,bowlingScoreCard);

        while(currentOver<=noOfOvers){
            //Bowlers will be used in a round robin manner
            Player bowler = bowlingTeam.getNextBowler(currentBowlerIdx);
            currentBowlerIdx=(currentBowlerIdx+1)%bowlingTeam.getBowlersCount();

            BowlingPlayerStat bowlerBowlingStat = bowlingScoreCard.getPlayerBowlingStat(bowler);

            // Change the bowler in the current score board;
            currentScoreStats.changeBowler(bowler, bowlerBowlingStat);

            log.info("Begin Over "+(currentOver-1));

            playCurrentOver();

            log.info("-----------------------------------------------");

            if(isGameFinished || allOutStatus){
                return;
            }

            currentOver+=1;
        }
    }

    void playCurrentOver(){
        // Playing the over
        //Printing the current score stats before startign the over
        log.info(LogUtils.printCurrentScoreStats(currentScoreStats));

        currentBall = 1;
        while(currentBall<=6){
            if(allOutStatus){
                return;
            }

            log.info(String.valueOf(currentScoreStats.getOversFinished()));

            BallType ballResult = simulateBall();
            switch(ballResult){
                case BallType.DOT_BALL:
                    log.info("DOT BALL!!");
                    handleRuns(0);
                    currentBall++;
                    break;
                case BallType.ONE_RUN:
                    log.info("1 Run!!");
                    handleRuns(1);
                    currentBall++;
                    break;
                case BallType.TWO_RUN:
                    log.info("2 Runs!!");
                    handleRuns(2);
                    currentBall++;
                    break;
                case BallType.THREE_RUN:
                    log.info("3 Runs!!");
                    handleRuns(3);
                    currentBall++;
                    break;
                case BallType.FOUR:
                    log.info("FOUR!!");
                    handleRuns(4);
                    currentBall++;
                    break;
                case BallType.SIX:
                    log.info("SIX!!");
                    handleRuns(6);
                    currentBall++;
                    break;
                case BallType.BOWLED:
                    handleWicket(BallType.BOWLED);
                    currentBall++;
                    break;
                case BallType.CAUGHT:
                    handleWicket(BallType.CAUGHT);
                    currentBall++;
                    break;
                case BallType.RUN_OUT:
                    handleWicket(BallType.RUN_OUT);
                    currentBall++;
                    break;
                case BallType.NO_BALL:
                    handleNoBall();
                    break;
                case BallType.WIDE_BALL:
                    handleWide();
                    break;
                case BallType.STUMP_OUT:
                    handleStumpOut();
                    break;

            }

            if(allOutStatus || isGameFinished){
                return;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
        log.info("OVER FINISHED!!");
    }

    BallType simulateBall(){
        Integer ballResult = random.nextInt(12);
        switch (ballResult){
            case 0:
                return BallType.DOT_BALL;
            case 1:
                return BallType.ONE_RUN;
            case 2:
                return BallType.TWO_RUN;
            case 3:
                return BallType.THREE_RUN;
            case 4:
                return BallType.FOUR;
            case 5:
                return BallType.SIX;
            case 6:
                return BallType.BOWLED;
            case 7:
                return BallType.CAUGHT;
            case 8:
                return BallType.RUN_OUT;
            case 9:
                return BallType.NO_BALL;
            case 10:
                return BallType.WIDE_BALL;
            case 11:
                return BallType.STUMP_OUT;
            default:
                log.info("Invalid Ball");
                break;
        }
        return BallType.DOT_BALL;
    }

    void handleRuns(Integer runs){

        WicketAndRunsHandler.handleRuns(runs,currentScoreStats);

        //Set free hit status to normal if the delivery was a free hit
        if(isFreeHit){
            log.info("Free Hit Accomplished!!");
            isFreeHit=false;
        }

        //Check if its second innings and score chased successfully
        if(checkIfSecondInningsTeamChased()){
            return;
        }
    }

    void handleWicket(BallType ballType){
        if(ballType.equals(BallType.CAUGHT)){
            log.info("CAUGHT!!");

            if(isFreeHit){
                log.info("NOT OUT DUE TO FREE HIT!");
                isFreeHit = false;
                return;
            }

            //who caught? (choose one random player from bowling team for simulation)
            Integer caughtFielderIdx = random.nextInt(11);
            String caughtFielderName = bowlingTeam.getPlayerName(caughtFielderIdx);

            WicketAndRunsHandler.handleCaughtWicket(caughtFielderName,currentScoreStats);

            //check if all out
            if(currentBatterIdx==10) {
                setAllOutStatus(true);
                return;
            }

            //Get new striker batsman
            UpdateNewStriker();
        }
        else if (ballType.equals(BallType.BOWLED)){
            log.info("BOWLED!!");

            if(isFreeHit){
                log.info("NOT OUT DUE TO FREE HIT!");
                isFreeHit = false;
                return;
            }

            WicketAndRunsHandler.handleBowledWicket(currentScoreStats);

            //check if all out
            if(currentBatterIdx==10){
                setAllOutStatus(true);
                return;
            }

            //Get new striker batsman
            UpdateNewStriker();
        }
        else{
            log.info("Run Out!!");

            if(isFreeHit){
                log.info("FREE HIT!");
                isFreeHit = false;
            }
            //Runs taken before run out by striker
            Integer extraRuns = random.nextInt(4);

            BattingPlayerStat strikerBattingStat = currentScoreStats.getStrikerBattingStat();
            BattingPlayerStat nonStrikerBattingStat = currentScoreStats.getNonStrikerBattingStat();
            strikerBattingStat.increaseBattingScore(extraRuns);
            strikerBattingStat.increaseBallsFaced();

            //swap striker if odd number of extra runs.
            if(extraRuns%2==1){
                currentScoreStats.swapStriker();
            }

            // Run Out by?
            Integer runOutPlayerIdx = random.nextInt(11);

            String runOutPlayer = bowlingTeam.getPlayerName(runOutPlayerIdx);

            //which side hit? 0 -> striker out, 1 -> striker not out

            Integer side = random.nextInt(2);

            if(side==1){
                //Updating striker and team stats
                WicketAndRunsHandler.handleRunOut(strikerBattingStat,runOutPlayer,extraRuns,currentScoreStats);

                //Check if its second innings and score chased successfully
                if(checkIfSecondInningsTeamChased()){
                    if(currentBatterIdx==10){
                        setAllOutStatus(true);
                    }
                    return;
                }

                //check if all out
                if(currentBatterIdx==10){
                    setAllOutStatus(true);
                    return;
                }
                UpdateNewStriker();
            }
            else{
                //Updating non striker and team stats
                WicketAndRunsHandler.handleRunOut(nonStrikerBattingStat,runOutPlayer,extraRuns,currentScoreStats);

                //Check if its second innings and score chased successfully
                if(checkIfSecondInningsTeamChased()){
                    if(currentBatterIdx==10){
                        setAllOutStatus(true);
                    }
                    return;
                }

                if(currentBatterIdx==10){
                    setAllOutStatus(true);
                    return;
                }

                //get new non striker
                UpdateNewNonStriker();
            }
        }
    }

    void handleNoBall(){
        log.info("NO BALL!!");

        // Check if any additional run conceeded
        //possible - dot, 1,2,3,4,6
        Integer additionalRuns = random.nextInt(6);
        if(additionalRuns==5){
            log.info("Scored Six Runs!!");
            additionalRuns=6;
        }
        else{
            log.info("Scored "+additionalRuns+" runs!!");

        }
        WicketAndRunsHandler.handleNoBall(additionalRuns, currentScoreStats);
        isFreeHit=true;

        // Swapping striker if additional odd no of runs scored
        if(additionalRuns%2==1){
            currentScoreStats.swapStriker();
        }
        if(checkIfSecondInningsTeamChased()){
            return;
        }

    }

    void handleWide(){
        log.info("WIDE BALL!!");

        // Check if any additional run scored
        //possible - dot, 1,2,3,4, stump out (5)
        Integer additionalRuns = random.nextInt(5);

        log.info("Scored "+additionalRuns+" runs.");
        WicketAndRunsHandler.handleWide(additionalRuns,currentScoreStats);

        //swap striker if odd number of extra runs.
        if(additionalRuns%2==1){
            currentScoreStats.swapStriker();
        }

        if(checkIfSecondInningsTeamChased()){
            return;
        }

    }

    void handleStumpOut(){
        // to check direct stump out or wide+ stump out (if mode==0, direct stump out, if mode==1, wide+stump out)
        boolean isWide = random.nextBoolean();

        log.info("STUMP OUT!!");
        // Not out due to free hit and free hit continue due to wide ball
        if(isFreeHit){
            log.info("NOT OUT DUE TO FREE HIT!");

            // Free hit dont continue for a legal ball
            if(!isWide){
                isFreeHit=false;
                return;
            }
        }

        String wicketKeeperName = bowlingTeam.getWicketKeeper().getPlayerName();

        if(isWide){
            WicketAndRunsHandler.handleStumpOut(wicketKeeperName, currentScoreStats,true);
        }
        else{
            WicketAndRunsHandler.handleStumpOut(wicketKeeperName, currentScoreStats,false);
        }

        //Check if its second innings and score chased successfully
        if(checkIfSecondInningsTeamChased()){
            if(currentBatterIdx==10){
                setAllOutStatus(true);
            }
            return;
        }

        if(currentBatterIdx==10){
            setAllOutStatus(true);
            return;
        }
        //get a new striker
        UpdateNewStriker();
    }

    void simulateToss(){
        Integer tossValue = random.nextInt(2)+1;

        //Always Team A toss.
        //If toss Value = 1, team A wins--------If toss value =2 , team B wins;

        if(tossValue==1){
            isTossWinnerTeamA=true;
        }
        else{
            isTossWinnerTeamB=true;
            bowlingTeam = teamB;
        }

        Integer chooseBatOrBowl = random.nextInt(2)+1;
        //If 1 -> bat, If 2-> bowl

        if(isTossWinnerTeamA){
            log.info("Team "+teamA.getTeamName()+" wins the toss");
            if(chooseBatOrBowl==1){
                battingTeam=teamA;
                bowlingTeam=teamB;
                log.info("Team "+teamA.getTeamName()+" chooses to bat");
            }
            else{
                battingTeam=teamB;
                bowlingTeam=teamA;
                log.info("Team "+teamA.getTeamName()+" chooses to bowl");
            }
        }
        else{
            log.info("Team "+teamB.getTeamName()+" wins the toss");
            if(chooseBatOrBowl==1){
                battingTeam=teamB;
                bowlingTeam=teamA;
                log.info("Team "+teamB.getTeamName()+" chooses to bat");
            }
            else{
                battingTeam=teamA;
                bowlingTeam=teamB;
                log.info("Team "+teamB.getTeamName()+" chooses to bowl");
            }
        }
    }



    void setAllOutStatus(boolean allOutStatus){
        this.allOutStatus=allOutStatus;
    }

    public boolean checkIfSecondInningsTeamChased(){
        if(secondInningsTarget==-1){
            return false;
        }
        Integer currentRuns = battingScoreCard.getTotalRunsScored();
        Integer ballsLeft = (noOfOvers*6)-((currentOver-1)*6+currentBall-1);
        log.info((battingScoreCard.getTotalRunsScored()-currentRuns)+"runs needed of"+ballsLeft+" balls.");
        if(secondInningsTarget<=battingScoreCard.getTotalRunsScored()){
            log.info("Team "+battingTeam.getTeamName()+" won by "+ (10 - battingScoreCard.getWickets())+" wickets!!");
            isGameFinished=true;
            return true;
        }
        return false;

    }

    public void UpdateNewStriker(){
        //Get new striker batsman
        Player newStriker=battingTeam.getNextBatter(currentBatterIdx);
        currentBatterIdx=currentBatterIdx+1;

        BattingPlayerStat newStrikerBattingStat = battingScoreCard.getPlayerBattingStat(newStriker);

        currentScoreStats.changeStriker(newStriker, newStrikerBattingStat);
    }

    public void UpdateNewNonStriker(){
        //Get new striker batsman
        Player newNonStriker=battingTeam.getNextBatter(currentBatterIdx);
        currentBatterIdx=currentBatterIdx+1;

        BattingPlayerStat newNonStrikerBattingStat = battingScoreCard.getPlayerBattingStat(newNonStriker);

        currentScoreStats.changeNonStriker(newNonStriker, newNonStrikerBattingStat);
    }

}
