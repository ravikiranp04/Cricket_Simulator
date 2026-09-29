package org.example;

import org.example.MatchUtils.LimitedOversMatch;
import org.example.MatchUtils.Match;
import org.example.MatchUtils.MatchConfig;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        String inputFile = "src/main/java/org/example/input.txt";
        MatchConfig matchConfig = new MatchConfig(inputFile);

        // Creating match config
        Match match = new LimitedOversMatch(matchConfig);

        match.play();
    }
}