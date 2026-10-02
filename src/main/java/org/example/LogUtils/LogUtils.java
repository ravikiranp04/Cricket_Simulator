package org.example.LogUtils;

import org.example.ScoreCardUtils.CurrentScoreStats;

public class LogUtils {
    public  LogUtils(){}
    public static String printCurrentScoreStats(CurrentScoreStats currentScoreStats){
        return String.format("""
            
            ============================================================
                             CURRENT SCORE
            ============================================================
            Striker      : %-20s %3d (%d)*
            Non-Striker  : %-20s %3d (%d)
            Bowler       : %-20s ( %d - %d)
            Overs        : %-20s
            Runs-Wickets : %d - %d
            ============================================================
            """,
                currentScoreStats.getStriker().getPlayerName(),
                currentScoreStats.getStrikerBattingStat().getBattingScore(),
                currentScoreStats.getStrikerBattingStat().getBallsFaced(),

                currentScoreStats.getNonStriker().getPlayerName(),
                currentScoreStats.getNonStrikerBattingStat().getBattingScore(),
                currentScoreStats.getNonStrikerBattingStat().getBallsFaced(),
                currentScoreStats.getBowler().getPlayerName(),
                currentScoreStats.getBowlerBowlingStat().getWicketsTaken(),
                currentScoreStats.getBowlerBowlingStat().getRunsConceeded(),

                currentScoreStats.getBattingScoreCard().getOversFinished(),

                currentScoreStats.getBattingScoreCard().getTotalRunsScored(),
                currentScoreStats.getBattingScoreCard().getWickets()
        );
    }
}
