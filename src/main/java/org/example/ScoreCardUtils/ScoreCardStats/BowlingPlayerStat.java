package org.example.ScoreCardUtils.ScoreCardStats;

import org.example.Player;

import java.math.BigDecimal;
import java.util.concurrent.locks.AbstractOwnableSynchronizer;

public class BowlingPlayerStat {


    private String playerName;
    private BigDecimal oversFinished;
    private Integer runsConceeded=0;
    private Integer extras=0;
    private Integer wicketsTaken=0;



    //    private BowlingPlayerStat(BowlingStatBuilder bowlingStatBuilder){
//        this.playerName= bowlingStatBuilder.playerName;
//        this.oversFinished= bowlingStatBuilder.oversFinished;
//        this.runsConceeded=bowlingStatBuilder.runsConceeded;
//        this.extras = bowlingStatBuilder.extras;
//    }
    public  BowlingPlayerStat(String playerName){
        this.playerName=playerName;
        this.runsConceeded=0;
        this.extras=0;
        this.wicketsTaken=0;
        this.oversFinished=BigDecimal.ZERO;
    }

    public BigDecimal getOversFinished() {
        return oversFinished;
    }

    public void setOversFinished(BigDecimal oversFinished) {
        this.oversFinished = oversFinished;
    }

    public Integer getRunsConceeded() {
        return runsConceeded;
    }

    public void increaseRunsConceeded(Integer runs){
        runsConceeded+=runs;
    }

    public void addWicket(){
        wicketsTaken+=1;
    }

    public void addExtras(Integer extraRuns){
        extras+=extraRuns;
    }

    public void increaseBallsDelivered(){
        oversFinished=oversFinished.add(new BigDecimal("0.1")).setScale(1);
    }
    public void setRunsConceeded(Integer runsConceeded) {
        this.runsConceeded = runsConceeded;
    }

    public Integer getExtras() {
        return extras;
    }

    public void setExtras(Integer extras) {
        this.extras = extras;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Integer getWicketsTaken() {
        return wicketsTaken;
    }

//    private static class BowlingStatBuilder{
//        private String playerName;
//        private Double oversFinished;
//        private Integer runsConceeded;
//        private Integer extras;
//
//        public BowlingStatBuilder player(String playerName){
//            this.playerName=playerName;
//            return this;
//        }
//        public BowlingStatBuilder oversFinished(Double oversFinished){
//            this.oversFinished=oversFinished;
//            return this;
//        }
//
//        public  BowlingStatBuilder runsConceeded(Integer runs){
//            this.runsConceeded=runs;
//            return this;
//        }
//
//        public BowlingStatBuilder extras(Integer extras){
//            this.extras=extras;
//            return this;
//        }
//
//        public BowlingPlayerStat build(){
//            return new BowlingPlayerStat(this);
//        }
//    }
}
