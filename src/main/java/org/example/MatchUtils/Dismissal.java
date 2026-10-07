package org.example.MatchUtils;

import org.example.Player;

public class Dismissal {
    private DismissalType dismissalType;
    private String fielder;
    private String bowler;

    public  Dismissal(DismissalType dismissalType, String fielder, String bowler){
        this.dismissalType=dismissalType;
        this.fielder=fielder;
        this.bowler=bowler;
    }

    public String getBowler() {
        return bowler;
    }

    public DismissalType getDismissalType() {
        return dismissalType;
    }

    public String getFielder() {
        return fielder;
    }
}
