package org.example.LogUtils;

import org.example.ScoreCardUtils.CurrentScoreStats;

public class LogUtils {
    public  LogUtils(){}
    public static String printCurrentScoreStats(CurrentScoreStats currentScoreStats){
        return String.format("""
            
            ============================================================
                             CURRENT SCORE
            ============================================================
            Striker      : %-20s %3d*
            Non-Striker  : %-20s %3d
            Bowler       : %-20s
            Overs        : %-20s
            Runs-Wickets : %d - %d
            ============================================================
            """,
                currentScoreStats.getStriker().getPlayerName(),
                currentScoreStats.getStrikerBattingStat().getBattingScore(),

                currentScoreStats.getNonStriker().getPlayerName(),
                currentScoreStats.getNonStrikerBattingStat().getBattingScore(),

                currentScoreStats.getBowler().getPlayerName(),

                currentScoreStats.getBowlerBowlingStat().getOversFinishedByBowler(),

                currentScoreStats.getBowlerBowlingStat().getRunsConceeded(),
                currentScoreStats.getBowlerBowlingStat().getWicketsTaken()
        );
    }
}
