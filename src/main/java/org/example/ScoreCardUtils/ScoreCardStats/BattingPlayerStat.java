package org.example.ScoreCardUtils.ScoreCardStats;

import org.example.Player;

import java.security.PublicKey;

public class BattingPlayerStat {


    private String playerName;
    private String outReason;
    private Integer battingScore;
    private Integer ballsFaced;
    private Integer sixesHit;
    private Integer foursHit;
    private boolean isOut;

    public  BattingPlayerStat(){

    }

    public Integer getFoursHit() {
        return foursHit;
    }

    public void increaseBattingScore(Integer runs){
        battingScore+=runs;
    }

    public void increaseSixesHit(){
        sixesHit+=1;
    }
    public void increaseFoursHit(){
        foursHit+=1;
    }

    public void increaseBallsFaced(){
        ballsFaced+=1;
    }

    public String getPlayerName() {
        return playerName;
    }




    public String getOutReason() {
        return outReason;
    }

    public void setOutReason(String outReason) {
        this.outReason = outReason;
    }

    public Integer getBattingScore() {
        return battingScore;
    }


    public Integer getBallsFaced() {
        return ballsFaced;
    }

    public Integer getSixesHit() {
        return sixesHit;
    }


    public boolean isOut() {
        return isOut;
    }

    public void sendOut(String outReason) {
        isOut = true;
        setOutReason(outReason);
    }

    public BattingPlayerStat(String playerName){
        this.playerName=playerName;
        this.ballsFaced=0;
        this.battingScore=0;
        this.sixesHit=0;
        this.foursHit=0;
    }

//    private BattingPlayerStat(BattingStatBuilder battingStatBuilder ){
//        this.playerName = battingStatBuilder.playerName;;
//        this.outReason=battingStatBuilder.outReason;
//        this.battingScore=battingStatBuilder.battingScore;
//        this.ballsFaced=battingStatBuilder.ballsFaced;
//        this.sixesHit=battingStatBuilder.sixesHit;
//        this.foursHit=battingStatBuilder.foursHit;
//        this.isOut=battingStatBuilder.isOut;
//    }
//    public static class BattingStatBuilder{
//        private String playerName;
//        private String outReason;
//        private Integer battingScore;
//        private Integer ballsFaced;
//        private Integer sixesHit;
//        private Integer foursHit;
//        private boolean isOut;
//
//
//        public BattingStatBuilder player(String playerName){
//            this.playerName=playerName;
//            return this;
//        }
//
//        public BattingStatBuilder outReason(String reason){
//            this.outReason=reason;
//            return this;
//        }
//
//        public BattingStatBuilder battingScore(Integer battingScore){
//            this.battingScore=battingScore;
//            return this;
//        }
//
//        public BattingStatBuilder ballsFaced(Integer ballsFaced){
//            this.ballsFaced=ballsFaced;
//            return this;
//        }
//
//        public BattingStatBuilder sixesHit(Integer sixesHit){
//            this.sixesHit=sixesHit;
//            return this;
//        }
//        public BattingStatBuilder foursHit(Integer foursHit){
//            this.foursHit=foursHit;
//            return this;
//        }
//
//        public BattingStatBuilder isOut(boolean isOut){
//            this.isOut=isOut;
//            return this;
//        }
//
//        public  BattingPlayerStat build(){
//            return new BattingPlayerStat(this);
//        }
//
//    }

}
