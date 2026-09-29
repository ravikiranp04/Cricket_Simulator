package org.example.MatchUtils;


import org.example.LoggerConfig;
import org.example.Player;
import org.example.ScoreCardUtils.BattingScoreCard;
import org.example.ScoreCardUtils.BowlingScoreCard;
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

    Player striker;
    Player nonStriker;
    BattingPlayerStat strikerBattingStat;
    BattingPlayerStat nonStrikerBattingStat;

    Player bowler;
    BowlingPlayerStat bowlerBowlingStat;

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
        striker = battingTeam.getNextBatter(currentBatterIdx);
        currentBatterIdx+=1;
        log.info("New Batsman: Striker -> "+striker.getPlayerName());

        nonStriker = battingTeam.getNextBatter(currentBatterIdx);
        currentBatterIdx+=1;
        log.info("New Batsman: Non Striker -> "+nonStriker.getPlayerName());

        strikerBattingStat = battingScoreCard.getPlayerBattingStat(striker);

        nonStrikerBattingStat = battingScoreCard.getPlayerBattingStat(nonStriker);

        
        while(currentOver<=noOfOvers){
            //Bowlers will be used in a round robin manner
            bowler = bowlingTeam.getNextBowler(currentBowlerIdx);
            currentBowlerIdx=(currentBowlerIdx+1)%bowlingTeam.getBowlersCount();

            bowlerBowlingStat = bowlingScoreCard.getPlayerBowlingStat(bowler);

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
        log.info("Striker: "+striker.getPlayerName()+"\t"+strikerBattingStat.getBattingScore()+"*");
        log.info("Non Striker: "+striker.getPlayerName()+"\t"+strikerBattingStat.getBattingScore()+"*");
        log.info("Bowler: "+bowler.getPlayerName()+"\t"+"Overs: "+ bowlerBowlingStat.getOversFinished()+"\t "+bowlerBowlingStat.getRunsConceeded()+" - "+bowlerBowlingStat.getWicketsTaken());
        currentBall = 1;
        while(currentBall<6){
            if(allOutStatus){
                return;
            }

            log.info("Overs: "+(currentOver-1)+"."+(currentBall-1));

            BallType ballResult = simulateBall();
            switch(ballResult){
                case BallType.DOT_BALL:
                    log.info("DOT BALL!!");
                    handleRuns(0);
                    currentBall++;
                    bowlerBowlingStat.increaseBallsDelivered();
                    break;
                case BallType.ONE_RUN:
                    log.info("1 Run!!");
                    handleRuns(1);
                    currentBall++;
                    bowlerBowlingStat.increaseBallsDelivered();
                    break;
                case BallType.TWO_RUN:
                    log.info("2 Runs!!");
                    handleRuns(2);
                    currentBall++;
                    bowlerBowlingStat.increaseBallsDelivered();
                    break;
                case BallType.THREE_RUN:
                    log.info("3 Runs!!");
                    handleRuns(3);
                    currentBall++;
                    bowlerBowlingStat.increaseBallsDelivered();
                    break;
                case BallType.FOUR:
                    log.info("FOUR!!");
                    handleRuns(4);
                    currentBall++;
                    bowlerBowlingStat.increaseBallsDelivered();
                    break;
                case BallType.SIX:
                    log.info("SIX!!");
                    handleRuns(6);
                    currentBall++;
                    bowlerBowlingStat.increaseBallsDelivered();
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
        Integer ballResult = random.nextInt(11);
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
            default:
                log.info("Invalid Ball");
                break;
        }
        return BallType.DOT_BALL;
    }

    void handleRuns(Integer runs){
        //Update batter stats
        strikerBattingStat.increaseBattingScore(runs);
        strikerBattingStat.increaseBallsFaced();

        //update batting team stats
        battingScoreCard.increaseTeamsScore(runs);

        //Update bowler stats
        bowlerBowlingStat.increaseRunsConceeded(runs);

        //check if striker rotated
        if(runs%2==1){
            swapStriker();
        }
        else if(runs==4){
            strikerBattingStat.increaseFoursHit(); //Updating fours hit in batter stats
        }
        else if(runs==6){
            strikerBattingStat.increaseFoursHit(); //Updating Sixes hit in batter stats
        }

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
            }

            //who caught? (choose one random player from bowling team for simulation)
            Integer caughtPlayerIdx = random.nextInt(11);
            String caughtPlayerName = bowlingTeam.getPlayerName(caughtPlayerIdx);

            //Update Batter and batting team stats
            strikerBattingStat.sendOut("c "+caughtPlayerName+" \t b "+bowler.getPlayerName());
            battingScoreCard.addWicket();

            //Update bowler and bowling team stats
            bowlerBowlingStat.addWicket();
            battingScoreCard.addWicket();

            //check if all out
            if(currentBatterIdx==10){
                setAllOutStatus(true);
                return;
            }

            //Get new striker batsman
            striker=battingTeam.getNextBatter(currentBatterIdx);
            currentBatterIdx=currentBatterIdx+1;

            strikerBattingStat = battingScoreCard.getPlayerBattingStat(striker);

        }
        else if (ballType.equals(BallType.BOWLED)){
            log.info("BOWLED!!");

            if(isFreeHit){
                log.info("NOT OUT DUE TO FREE HIT!");
                isFreeHit = false;
                return;
            }

            //Update Batter and batting team stats
            strikerBattingStat.sendOut("b "+bowler.getPlayerName());
            battingScoreCard.addWicket();

            //Update bowler and bowling team stats
            bowlerBowlingStat.addWicket();

            //check if all out
            if(currentBatterIdx==10){
                setAllOutStatus(true);
                return;
            }

            //Get new striker batsman
            striker=battingTeam.getNextBatter(currentBatterIdx);
            currentBatterIdx=currentBatterIdx+1;

            strikerBattingStat = battingScoreCard.getPlayerBattingStat(striker);
        }
        else{
            log.info("Run Out!!");

            if(isFreeHit){
                log.info("FREE HIT!");
                isFreeHit = false;
            }

            Integer extraRuns = random.nextInt(4);
            Integer totalRuns = extraRuns+1;

            //swap striker if odd number of extra runs.
            if(extraRuns%2==1){
                swapStriker();
            }

            // Run Out by?
            Integer runOutPlayerIdx = random.nextInt(11);

            String runOutPlayer = bowlingTeam.getPlayerName(runOutPlayerIdx);

            //which side hit? 0 -> striker out, 1 -> striker not out

            Integer side = random.nextInt(2);

            if(side==1){
                //Updating striker and team stats
                strikerBattingStat.sendOut("(Run Out) "+runOutPlayer);
                strikerBattingStat.increaseBattingScore(extraRuns);
                battingScoreCard.increaseExtras(1);
                battingScoreCard.increaseTeamsScore(totalRuns);
                battingScoreCard.addWicket();

                //updating bowler stats
                bowlerBowlingStat.increaseRunsConceeded(totalRuns);
                bowlingScoreCard.increaseExtras(1);

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

                //get new striker
                striker=battingTeam.getNextBatter(currentBatterIdx);
                currentBatterIdx=currentBatterIdx+1;

                strikerBattingStat = battingScoreCard.getPlayerBattingStat(striker);
            }
            else{
                //Updating non striker and team stats
                nonStrikerBattingStat.increaseBattingScore(extraRuns);
                battingScoreCard.increaseExtras(1);
                battingScoreCard.increaseTeamsScore(totalRuns);
                nonStrikerBattingStat.sendOut("(Run Out) "+runOutPlayer);
                battingScoreCard.addWicket();

                //updating bowler stats
                bowlerBowlingStat.increaseRunsConceeded(totalRuns);
                bowlingScoreCard.increaseExtras(1);

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

                //get new striker
                nonStriker=battingTeam.getNextBatter(currentBatterIdx);
                currentBatterIdx=currentBatterIdx+1;

                nonStrikerBattingStat = battingScoreCard.getPlayerBattingStat(nonStriker);

            }

        }
    }

    void handleNoBall(){
        log.info("NO BALL!!");

        // Check if any additional run conceeded
        //possible - dot, 1,2,3,4,6
        Integer extraRuns = random.nextInt(6);
        if(extraRuns==5){
            log.info("Scored Six Runs!!");
        }
        else{
            log.info("Scored "+extraRuns+" runs!!");

        }
        log.info("Free Hit!!");
        isFreeHit=true;
        Integer totalRuns = extraRuns+1;

        //Update striker stats - gets only extra scored runs
        strikerBattingStat.increaseBattingScore(extraRuns);
        //update bowler stats
        bowlerBowlingStat.increaseRunsConceeded(totalRuns);
        //update batting team stats
        battingScoreCard.increaseTeamsScore(totalRuns);
        battingScoreCard.increaseExtras(1);
        //update bowling team stats
        bowlingScoreCard.increaseExtras(1);

        // Swapping striker if additional odd no of runs scored
        if(extraRuns%2==1){
            swapStriker();
        }

        if(checkIfSecondInningsTeamChased()){
            return;
        }

    }

    void handleWide(){
        log.info("WIDE BALL!!");

        // Check if any additional run scored
        //possible - dot, 1,2,3,4, stump out (5)
        Integer extraRuns = random.nextInt(6);

        //Stump Out
        if(extraRuns==5){
            log.info("STUMP OUT!!");
            // Not out due to free hit and free hit continue due to wide ball
            if(isFreeHit){
                log.info("NOT OUT DUE TO FREE HIT!");
                return;
            }

            //Updating batter and team stats
            strikerBattingStat.sendOut("(Stump Out) "+bowlingTeam.getWicketKeeper().getPlayerName()+" \tb "+bowler.getPlayerName());
            battingScoreCard.addWicket();
            battingScoreCard.increaseExtras(1);
            //updating bowler stats
            bowlerBowlingStat.increaseRunsConceeded(1);
            bowlerBowlingStat.addWicket();
            bowlingScoreCard.increaseExtras(1);

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

            //Get new striker batsman
            striker=battingTeam.getNextBatter(currentBatterIdx);
            currentBatterIdx=currentBatterIdx+1;

            strikerBattingStat = battingScoreCard.getPlayerBattingStat(striker);

        }
        else{
            log.info("Scored "+extraRuns+" runs.");
            Integer totalRuns = extraRuns+1;
            //Updating batter and team stats
            strikerBattingStat.increaseBattingScore(extraRuns);
            battingScoreCard.increaseExtras(1);
            battingScoreCard.increaseTeamsScore(totalRuns);

            //updating bowler stats
            bowlerBowlingStat.increaseRunsConceeded(totalRuns);
            bowlingScoreCard.increaseExtras(1);

            //swap striker if odd number of extra runs.
            if(extraRuns%2==1){
                swapStriker();
            }

            if(checkIfSecondInningsTeamChased()){
                return;
            }
        }
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

    void swapStriker(){
        swapStatisticsMaps();
        Player temp = nonStriker;
        nonStriker=striker;
        striker=temp;
    }

    void swapStatisticsMaps(){
        BattingPlayerStat temp= nonStrikerBattingStat;
        nonStrikerBattingStat=strikerBattingStat;
        strikerBattingStat=temp;
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


}
