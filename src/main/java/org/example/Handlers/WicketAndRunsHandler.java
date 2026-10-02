package org.example.Handlers;

import org.example.MatchUtils.BallType;
import org.example.Player;
import org.example.ScoreCardUtils.BattingScoreCard;
import org.example.ScoreCardUtils.BowlingScoreCard;
import org.example.ScoreCardUtils.CurrentScoreStats;
import org.example.ScoreCardUtils.ScoreCardStats.BattingPlayerStat;
import org.example.ScoreCardUtils.ScoreCardStats.BowlingPlayerStat;

public class WicketAndRunsHandler {

    public static  void handleRuns(Integer runs, CurrentScoreStats currentScoreStats){

        BattingPlayerStat strikerBattingStat  = currentScoreStats.getStrikerBattingStat();
        BowlingPlayerStat bowlerBowlingStat = currentScoreStats.getBowlerBowlingStat();

        //Update batter stats
        strikerBattingStat.increaseBattingScore(runs);


        //Update bowler stats
        bowlerBowlingStat.increaseRunsConceeded(runs);

        //batter and bowler balls faced
        currentScoreStats.updateBallsDeliveredData();

        if(runs==4){
            strikerBattingStat.increaseFoursHit(); //Updating fours hit in batter stats
        }
        else if(runs==6){
            strikerBattingStat.increaseSixesHit(); //Updating Sixes hit in batter stats
        }

        //check if striker rotated
        if(runs%2==1){
            currentScoreStats.swapStriker();
        }
    }

    public static void handleCaughtWicket(String caughtFielderName, CurrentScoreStats currentScoreStats){
        BattingPlayerStat strikerBattingStat  = currentScoreStats.getStrikerBattingStat();
        BowlingPlayerStat bowlerBowlingStat = currentScoreStats.getBowlerBowlingStat();

        //Update batter stats
        strikerBattingStat.sendOut("c "+caughtFielderName+" \t b "+bowlerBowlingStat.getPlayerName());

        //Update bowler and bowling team stats
        bowlerBowlingStat.addWicket();

        //batter and bowler balls faced
        currentScoreStats.updateBallsDeliveredData();;
    }

    public static void handleBowledWicket(CurrentScoreStats currentScoreStats){
        BattingPlayerStat strikerBattingStat  = currentScoreStats.getStrikerBattingStat();
        BowlingPlayerStat bowlerBowlingStat = currentScoreStats.getBowlerBowlingStat();


        strikerBattingStat.sendOut("b "+bowlerBowlingStat.getPlayerName());


        //Update bowler and bowling team stats
        bowlerBowlingStat.addWicket();

        //batter and bowler balls faced
        currentScoreStats.updateBallsDeliveredData();;
    }

    public static void handleRunOut(BattingPlayerStat playerBattingStat,String runOutPlayer,Integer additionalRunsScored, CurrentScoreStats currentScoreStats){
        // Send out batter
        playerBattingStat.sendOut("(Run Out) "+runOutPlayer);

        BowlingPlayerStat bowlerBowlingStat = currentScoreStats.getBowlerBowlingStat();
        BowlingScoreCard bowlingScoreCard = currentScoreStats.getBowlingScoreCard();

        //updating bowler stats
        bowlerBowlingStat.increaseRunsConceeded(additionalRunsScored);

        // Run outs will be updated only to team stats
        bowlingScoreCard.addWicket();

        //update balls delivered by that bowler and team
        bowlerBowlingStat.increaseBallsDeliveredByBowler();;
    }

    public static void handleNoBall(Integer additionalRuns, CurrentScoreStats currentScoreStats){
        BattingPlayerStat strikerBattingStat = currentScoreStats.getStrikerBattingStat();
        BowlingPlayerStat bowlingPlayerStat = currentScoreStats.getBowlerBowlingStat();
        BattingScoreCard battingScoreCard = currentScoreStats.getBattingScoreCard();

        // updates additional runs for batter
        strikerBattingStat.increaseBattingScore(additionalRuns);

        //adds extra no ball run to the team, but not to player
        battingScoreCard.addExtras(1);

        // Updates the bowler and bowling team stats
        bowlingPlayerStat.increaseRunsConceeded(additionalRuns);
        bowlingPlayerStat.addExtras(1);

    }

    public static void handleWide(Integer additionalRuns, CurrentScoreStats currentScoreStats ){

        BowlingPlayerStat bowlingPlayerStat = currentScoreStats.getBowlerBowlingStat();
        BattingScoreCard battingScoreCard = currentScoreStats.getBattingScoreCard();

        // wide runs are added as extras to batting team and bowler
        bowlingPlayerStat.addExtras(additionalRuns+1); // additional runs + 1 wide
        battingScoreCard.addExtras(additionalRuns+1);

    }

    public static void handleStumpOut(String wicketKeeperName, CurrentScoreStats currentScoreStats, boolean isWide){
        BattingPlayerStat strikerBattingStat = currentScoreStats.getStrikerBattingStat();
        BowlingPlayerStat bowlerBowlingStat = currentScoreStats.getBowlerBowlingStat();
        BattingScoreCard battingScoreCard = currentScoreStats.getBattingScoreCard();

        // Update batter and team stats
        strikerBattingStat.sendOut("(Stump Out) "+wicketKeeperName+" \tb "+bowlerBowlingStat.getPlayerName());
        // Update bowler and team stats
        bowlerBowlingStat.addWicket();

        if(isWide){ // wide+stump out is not a legal delivery
            // Update batting team stats
            battingScoreCard.addExtras(1);

            // update bowler and team stats
            bowlerBowlingStat.addExtras(1);
        }
        else{
            //update balls delivered by that bowler and team
            currentScoreStats.updateBallsDeliveredData(); // if not wide, then it s a legal delivery
        }
    }


}
