package org.example;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Team {
    private String teamId;
    private String teamName;
    private List<Player> playersList;
    private Integer bowlersCount;
    private Integer battersCount;
    private List<Player> bowlersList;



    private Player wicketKeeper;

    Team(TeamBuilder teamBuilder){
        this.teamId = teamBuilder.teamId;
        this.teamName=teamBuilder.teamName;
        this.playersList=teamBuilder.playersList;
        this.bowlersCount = teamBuilder.bowlersCount;
        this.battersCount = teamBuilder.battersCount;
        this.wicketKeeper=teamBuilder.wicketKeeper;
        this.bowlersList=teamBuilder.bowlersList;
    }

    public static class TeamBuilder{
        private String teamId;
        private String teamName;
        private List<Player> playersList;
        private Integer bowlersCount;
        private Integer battersCount;
        private Player wicketKeeper;
        private List<Player> bowlersList;
        public TeamBuilder(){
            this.teamId = UUID.randomUUID().toString();
        }

        public TeamBuilder teamName(String teamName){
            this.teamName = teamName;
            return this;
        }

        public TeamBuilder playersList(List<Player> playerList){
            this.playersList=playerList;
            return this;
        }

        public TeamBuilder bowlersCount(Integer bowlersCount){
            this.bowlersCount=bowlersCount;
            return this;
        }

        public TeamBuilder battersCount(Integer battersCount){
            this.battersCount=battersCount;
            return this;
        }

        public TeamBuilder bowlersList(List<Player> bowlersList){
            this.bowlersList=bowlersList;
            return this;
        }

        public  TeamBuilder wicketKeeper(Player wicketKeeper){
            this.wicketKeeper=wicketKeeper;
            return this;
        }
        public Team build(){
            return new Team(this);
        }

    }
    public Player getWicketKeeper() {
        return wicketKeeper;
    }

    public List<Player> getPlayersList() {
        return playersList;
    }

    public Integer getBowlersCount() {
        return bowlersCount;
    }

    public Integer getBattersCount() {
        return battersCount;
    }

    public String getTeamName() {
        return teamName;
    }

    public Player getNextBatter(Integer lastBatterIdx){
        Integer nextBatterIdx = lastBatterIdx+1;
       return playersList.get(nextBatterIdx);
    }

    public Player getNextBowler(Integer lastBowlerIdx){
        Integer nextBowlerIdx = (lastBowlerIdx+1)%bowlersCount;
        return bowlersList.get(nextBowlerIdx);
    }

    public String getPlayerName(Integer playerIdx){
        return playersList.get(playerIdx).getPlayerName();
    }
}
