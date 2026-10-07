package org.example.Handlers;

public class RunsValidator {
    public static boolean isSix(Integer runs){
        return runs==6;
    }

    public static boolean isFour(Integer runs){
        return runs==4;
    }

    public  static boolean isStrikerToBeSwapped(Integer runs){
        return runs%2==1;
    }

}
