package org.example.Handlers;


import org.example.MatchUtils.Dismissal;
import org.example.MatchUtils.DismissalType;
import org.example.ScoreCardUtils.BattingScoreCard;
import org.example.ScoreCardUtils.BowlingScoreCard;
import org.example.ScoreCardUtils.ScoreCardStats.BattingPlayerStat;
import org.example.ScoreCardUtils.ScoreCardStats.BowlingPlayerStat;

public class WicketAndRunsHandler {

    public static  void handleRuns(Integer runs, BattingPlayerStat strikerBattingStat, BowlingPlayerStat bowlerBowlingStat){

        //Update batter stats
        strikerBattingStat.increaseBattingScore(runs);


        //Update bowler stats
        bowlerBowlingStat.increaseRunsConceeded(runs);

        //batter and bowler balls faced
        strikerBattingStat.increaseBallsFaced();
        bowlerBowlingStat.increaseBallsDeliveredByBowler();

        if(RunsValidator.isFour(runs)){
            strikerBattingStat.increaseFoursHit(); //Updating fours hit in batter stats
        }
        else if(RunsValidator.isSix(runs)){
            strikerBattingStat.increaseSixesHit(); //Updating Sixes hit in batter stats
        }
    }

    public static void handleCaughtWicket(String caughtFielderName, BattingPlayerStat strikerBattingStat, BowlingPlayerStat bowlerBowlingStat){


        String bowlerName = bowlerBowlingStat.getPlayerName();

        //Update batter stats
        strikerBattingStat.sendOut(new Dismissal(DismissalType.CAUGHT,caughtFielderName,bowlerName));

        //Update bowler and bowling team stats
        bowlerBowlingStat.addWicket();

        //batter and bowler balls faced
        strikerBattingStat.increaseBallsFaced();
        bowlerBowlingStat.increaseBallsDeliveredByBowler();
    }

    public static void handleBowledWicket(BattingPlayerStat strikerBattingStat, BowlingPlayerStat bowlerBowlingStat){


        String bowlerName = bowlerBowlingStat.getPlayerName();

        strikerBattingStat.sendOut(new Dismissal(DismissalType.BOWLED,null,bowlerName));


        //Update bowler and bowling team stats
        bowlerBowlingStat.addWicket();

        //batter and bowler balls faced
        strikerBattingStat.increaseBallsFaced();
        bowlerBowlingStat.increaseBallsDeliveredByBowler();
    }

    public static void handleRunOut(BattingPlayerStat playerBattingStat,String runOutPlayer,Integer additionalRunsScored, BowlingPlayerStat bowlerBowlingStat){
        // Send out batter
        playerBattingStat.sendOut(new Dismissal(DismissalType.RUN_OUT, runOutPlayer, null));


        BowlingScoreCard bowlingScoreCard = bowlerBowlingStat.getBowlingScoreCard();

        //updating bowler stats
        bowlerBowlingStat.increaseRunsConceeded(additionalRunsScored);

        // Run outs will be updated only to team stats
        bowlingScoreCard.addWicket();

        //update balls delivered by that bowler and team
        bowlerBowlingStat.increaseBallsDeliveredByBowler();
    }

    public static void handleNoBall(Integer additionalRuns, BattingPlayerStat strikerBattingStat,BowlingPlayerStat bowlingPlayerStat ){

        BattingScoreCard battingScoreCard = strikerBattingStat.getBattingScoreCard();

        // updates additional runs for batter
        strikerBattingStat.increaseBattingScore(additionalRuns);

        //if its a four or six, increase count
        if(RunsValidator.isFour(additionalRuns)){
            strikerBattingStat.increaseFoursHit();
        }
        if(RunsValidator.isSix(additionalRuns)){
            strikerBattingStat.increaseSixesHit();
        }

        //adds extra no ball run to the team, but not to player
        battingScoreCard.addExtras(1);

        // Updates the bowler and bowling team stats
        bowlingPlayerStat.increaseRunsConceeded(additionalRuns);
        bowlingPlayerStat.addExtras(1);


    }

    public static void handleWide(Integer additionalRuns, BattingScoreCard battingScoreCard,BowlingPlayerStat bowlingPlayerStat ){

        // wide runs are added as extras to batting team score and bowler stats
        bowlingPlayerStat.addExtras(additionalRuns+1); // additional runs + 1 wide
        battingScoreCard.addExtras(additionalRuns+1);

    }

    public static void handleStumpOut(String wicketKeeperName,BattingPlayerStat strikerBattingStat,BowlingPlayerStat bowlerBowlingStat, boolean isWide){

        BattingScoreCard battingScoreCard = strikerBattingStat.getBattingScoreCard();

        String bowlerName = bowlerBowlingStat.getPlayerName();

        // Update batter and team stats
        strikerBattingStat.sendOut(new Dismissal(DismissalType.STUMP_OUT, wicketKeeperName, bowlerName));

        // Update bowler and team stats
        bowlerBowlingStat.addWicket();

        if(isWide){ // wide+stump out is not a legal delivery
            // Update batting team stats
            battingScoreCard.addExtras(1);

            // update bowler and team stats
            bowlerBowlingStat.addExtras(1);
        }
        else{
            //update balls delivered by that bowler and batter
            strikerBattingStat.increaseBallsFaced();
            bowlerBowlingStat.increaseBallsDeliveredByBowler();// if not wide, then it s a legal delivery
        }
    }


}
