package org.example.MatchUtils;

import org.example.Player;
import org.example.PlayerType;
import org.example.Team;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class MatchConfig {

    private Team teamA;
    private Team teamB;

    private Integer noOfOvers;

    public MatchConfig(String inputFileName){

        File inputFile = new File(inputFileName);

        try(Scanner fileScanner = new Scanner(inputFile)){
            //No of Overs
            Integer overs ;
            if(fileScanner.hasNextInt()){
                overs=fileScanner.nextInt();
                if(overs!=20 && overs!=50){
                    throw new InputMismatchException("Number of Overs should be 20 or 50\n");
                }
                fileScanner.nextLine();

            }
            else{
                throw new InputMismatchException("Invalid Input at Number of Overs\n");
            }
            // Input for team A name
            String teamAName;
            if(fileScanner.hasNextLine()){
                teamAName=fileScanner.nextLine();
            }
            else{
                throw new InputMismatchException("Invalid Input at Team Name\n");
            }

            // Scanning the team A players
            Team teamA = scanForTeam(fileScanner, teamAName);

            fileScanner.nextLine();

            // Input for team B name
            String teamBName;
            if(fileScanner.hasNextLine()){
                teamBName=fileScanner.nextLine();
            }
            else{
                throw new InputMismatchException("Invalid Input at Team Name\n");
            }

            // Scanning the team B players
            Team teamB = scanForTeam(fileScanner, teamBName);


            this.noOfOvers=overs;
            this.teamA=teamA;
            this.teamB=teamB;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InputMismatchException e){
            throw new RuntimeException(e);
        }

    }

    public Team scanForTeam(Scanner fileScanner, String teamName){
        try{

            //Building players list by scanning all player names and players list
            Integer currentPlayersCount=0, bowlersCount=0;
            Player wicketKeeper=null;
            List<Player> playerList = new ArrayList<>();
            List<Player> bowlersList = new ArrayList<>();
            while(fileScanner.hasNext() && currentPlayersCount<11){
                String playerName = fileScanner.next();
                PlayerType playerType = PlayerType.valueOf(fileScanner.next());
                Player player = new Player(playerName,playerType);
                if(playerType==PlayerType.BOWLER){
                    bowlersCount++;
                    bowlersList.add(player);
                }
                if(playerType==PlayerType.WICKET_KEEPER){
                    wicketKeeper = player;
                }
                playerList.add(player);
                currentPlayersCount++;
            }

            // Requires minimum 5 bowlers and exactly 11 players
            if(currentPlayersCount!=11 ){
                throw new InputMismatchException("Insufficient Players at a Team");
            }

            if(bowlersCount<5){
                throw new InputMismatchException("Insufficient Bowlers at a Team");
            }

            if(wicketKeeper==null){
                throw new InputMismatchException("Team must have atleast one wicket keeper");

            }
            //Building team object
            teamA = new Team.TeamBuilder().teamName(teamName).wicketKeeper(wicketKeeper).bowlersCount(bowlersCount).playersList(playerList).bowlersList(bowlersList).battersCount(10-bowlersCount).build();
        }catch (InputMismatchException e){
            throw new InputMismatchException();
        }
        return teamA;
    }

    public Integer getNoOfOvers() {
        return noOfOvers;
    }

    public Team getTeamB() {
        return teamB;
    }

    public Team getTeamA() {
        return teamA;
    }
}
