package org.example.LogUtils;

import org.example.MatchUtils.Dismissal;
import org.example.MatchUtils.DismissalType;
import org.example.ScoreCardUtils.BattingScoreCard;
import org.example.ScoreCardUtils.BowlingScoreCard;
import org.example.ScoreCardUtils.CurrentScoreStats;
import org.example.ScoreCardUtils.InningsScoreCard;
import org.example.ScoreCardUtils.ScoreCardStats.BattingPlayerStat;
import org.example.ScoreCardUtils.ScoreCardStats.BowlingPlayerStat;

import java.math.BigDecimal;
import java.util.Map;

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

    public static String printBattingScoreCard(BattingScoreCard battingScoreCard) {
        Map<String, BattingPlayerStat> playerIdToBattingStatMap = battingScoreCard.getPlayerIdToBattingStatMap();
        StringBuilder log = new StringBuilder();

        log.append("\n");
        log.append("============================================================\n");
        log.append("                    BATTING SCORECARD\n");
        log.append("============================================================\n");

        log.append(String.format(
                "%-22s %5s %5s %5s %5s %8s   %s%n",
                "Player",
                "R",
                "B",
                "4s",
                "6s",
                "SR",
                "Dismissal"
        ));

        log.append("------------------------------------------------------------\n");

        for (BattingPlayerStat playerStat : playerIdToBattingStatMap.values()) {

            Integer runs = playerStat.getBattingScore();
            Integer balls = playerStat.getBallsFaced();
            Integer fours = playerStat.getFoursHit();
            Integer sixes = playerStat.getSixesHit();

            BigDecimal strikeRate = BigDecimal.ZERO;

            if (balls != 0) {
                strikeRate = BigDecimal.valueOf(runs * 100.0 / balls)
                        .setScale(2, BigDecimal.ROUND_HALF_UP);
            }

            String dismissal = printDismissal(playerStat.getDismissal());

            if (!playerStat.isOut()) {
                dismissal = "not out";
            }

            log.append(String.format(
                    "%-22s %5d %5d %5d %5d %8s   %s%n",
                    playerStat.getPlayerName(),
                    runs,
                    balls,
                    fours,
                    sixes,
                    strikeRate,
                    dismissal
            ));
        }

        log.append("------------------------------------------------------------\n");

        log.append(String.format(
                "Extras: %d%n",
                battingScoreCard.getExtras()
        ));

        log.append(String.format(
                "TOTAL: %d/%d (%s overs)%n",
                battingScoreCard.getTotalRunsScored(),
                battingScoreCard.getWickets(),
                battingScoreCard.getOversFinished()
        ));

        log.append("============================================================\n");

        return log.toString();
    }


    public static String printBowlingScoreCard(BowlingScoreCard bowlingScoreCard) {

        Map<String , BowlingPlayerStat> playerIdToBowlingStatMap = bowlingScoreCard.getPlayerIdToBowlingStatMap();
        StringBuilder log = new StringBuilder();

        log.append("\n");
        log.append("============================================================\n");
        log.append("                    BOWLING SCORECARD\n");
        log.append("============================================================\n");

        log.append(String.format(
                "%-22s %5s %5s %5s %8s%n",
                "Bowler",
                "O",
                "R",
                "W",
                "Econ"
        ));

        log.append("------------------------------------------------------------\n");

        for (BowlingPlayerStat playerStat : playerIdToBowlingStatMap.values()) {

            Integer balls = playerStat.getBallsDelivered();
            Integer runs = playerStat.getRunsConceeded();
            Integer wickets = playerStat.getWicketsTaken();

            Integer overs = balls / 6;
            Integer remainingBalls = balls % 6;

            String oversString = overs + "." + remainingBalls;

            BigDecimal economy = BigDecimal.ZERO;

            if (balls != 0) {
                economy = BigDecimal.valueOf(runs * 6.0 / balls)
                        .setScale(2, BigDecimal.ROUND_HALF_UP);
            }

            log.append(String.format(
                    "%-22s %5s %5d %5d %8s%n",
                    playerStat.getPlayerName(),
                    oversString,
                    runs,
                    wickets,
                    economy
            ));
        }

        log.append("------------------------------------------------------------\n");

        log.append(String.format(
                "Extras: %d%n",
                bowlingScoreCard.getExtras()
        ));

        log.append(String.format(
                "TOTAL: %d runs, %d wickets%n",
                bowlingScoreCard.getTotalRunsConceeded(),
                bowlingScoreCard.getWicketsTaken()
        ));

        log.append("============================================================\n");

        return log.toString();
    }


    public static String printInningsScoreCard(InningsScoreCard inningsScoreCard){

        BattingScoreCard battingScoreCard = inningsScoreCard.getBattingScoreCard();
        BowlingScoreCard bowlingScoreCard = inningsScoreCard.getBowlingScoreCard();
        return printBattingScoreCard(battingScoreCard)
                + "\n"
                + printBowlingScoreCard(bowlingScoreCard);
    }

    public static String printDismissal(Dismissal dismissal) {
        if (dismissal == null) {
            return "not out";
        }

        switch (dismissal.getDismissalType()) {
            case BOWLED:
                return "b " + dismissal.getBowler();
            case CAUGHT:
                return "c " + dismissal.getFielder()
                        + " b " + dismissal.getBowler();
            case RUN_OUT:
                return "run out (" + dismissal.getFielder() + ")";
            case STUMP_OUT:
                return "st " + dismissal.getFielder()
                        + " b " + dismissal.getBowler();
            default:
                throw new IllegalArgumentException(
                        "Unknown dismissal type: " + dismissal.getDismissalType());
        }
    }
}
