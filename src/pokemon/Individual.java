package pokemon;

import moves.Move;
import type.Type;

import java.util.ArrayList;
import java.util.Arrays;


/*
 * subclass of pokemon.
 * this is where the actual pokemon is created
 */

public class Individual extends Species{

    private final int[] evs;
    private double accuracy = 100, evasion = 100;

    // stats can be increased or decreased throughout a battle
    // the default multiplier is one, we can alter it here.
    // we need an array for this because we need to keep track of alterations
    // because alterations disappear if you switch out your pokemon

    private int[] stats; // the actual stats of the pokemon
    private final int[] startingStats; // the stats when the pokemon first switches in
    // keeping this separate makes recalculation easier

    private final int maxHP;



    // natures. there are 25 of them. each one raises a stat and decreases one of the five stats (hp is excluded)
    // this change is 10 percent.
    private final String nature;


    private final int teamNum; // keep track of which team this pokemon belongs to. 1 for player 1, 2 for player 2

    public Move[] moves; // the actual moves the pokemon will have.


    // the stat changes
    // the default multiplier is one, and we want this to be the index of the array in PokemonInterface so 6's across the board for now
    private int[] statCodes = {6, 6, 6, 6, 6, 6, 6};
    // the first 5 are attack, def, spattack, spdef, speed. 6 and 7 are accuracy and evasion, which for the purposes of alteration work the same as the other stats.


    // the nature multipliers
    // default is 1
    private double[] natureCodes = {1, 1, 1, 1, 1};

    // take constructor from super class.
    public Individual(String name, ArrayList<Type> types, int[] baseStats, String nature, Move[] moves, int evs[], int teamNum) {

        super(name, types, baseStats);
        this.nature = nature;
        this.moves = moves;
        this.evs = evs;
        this.teamNum = teamNum;
        // except for stats because we can't calculate that until here

        // this part requires some explanation.
        // natures do not affect hp, only the other five stats.
        // the default multiplier would be 1.
        // a nature that increases a stat results in a multiplier of 1.1, a decrease results in .9.
        // a fifth of natures increase and decrease the same stat and thus do nothing.
        // natures are always given as a word that corresponds to the changes.
        // adamant increases attack, lowers special attack etc.

        // use the method natures while throwing in the array we just made and the nature to make the necessary changes to the array
        // the appropriate multiplier will now be used.
        natures(natureCodes, nature);

        this.stats = statCalcs();
        this.startingStats = this.stats.clone(); // keep it separate so we can change one without changing the other
        this.maxHP = stats[0]; // keep track of max hp so we can tell players what it is.


    }


    public int getPercentHealth(){
        return (int) Math.round((double) stats[0] / maxHP * 100);
    }


    // here's the method to calculate the stats.
    // basically we take the basestats, ivs, evs, level and the nature code, throw it into a formula and call it a day.
    public int[] statCalcs() {
        int[] stats = new int[6]; // array to hold the 6 stats
        int[] baseStats = getBaseStats();

        for(int i = 0; i < 6; i++) {

            // common among all stats
            int stat = ((2 * baseStats[i] + ivs[i] + evs[i] / 4) * level) / 100;

            // hp is a bit different
            if (i == 0) {
                stats[0] = stat + level + 10;
            }
            else{
                stats[i] = (int) ((stat + 5) * natureCodes[i - 1]);
            }

        }

//        accuracy *= statChangesTwo[statCodes[5]];
//        evasion *= statChangesTwo[statCodes[6]];


        return stats; // return it
    }


    // getters and setters

    public int getTeamNum() {
        return teamNum;
    }

    public double getAccuracy() {
        return accuracy;
    }




    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public double getEvasion() {
        return evasion;
    }

    public void setEvasion(int evasion) {
        this.evasion = evasion;
    }




    public int[] getStats() {
        return stats;
    }


    public void setStats(int[] stats) {
        this.stats = stats;
    }





    public int[] getStatCodes() {
        return statCodes;
    }




    public void recalculate() { // just setting the statcodes does nothing

        // we also need to recalculate stats when this happens
        // start at 1 because hp never needs recalculation
        for (int i = 1; i < startingStats.length; i++){
            System.out.println(startingStats[i] + " " + statCodes[i]);
            stats[i] = (int) (startingStats[i] * statChangesOne[statCodes[i]]);
        }

    }





    public String toString() { // when we call this, we want all the info the user could want on the pokemon displayed in a readable way
        return super.toString() + "\nNature: " + nature + "\nHealth: " + stats[0] + " / " + maxHP + "\nAttack: " + stats[1] + "\nDefense: " + stats[2]
                + "\nSpecial Attack: " + stats[3] + "\nSpecial Defense: " + stats[4]
                + "\nSpeed: " + stats[5];
    }




    public void pokemonStatus() { // inform players the status of this pokemon
        System.out.println(); // skip
        System.out.printf("%s, Health remaining: %d%%\n", getName(), getPercentHealth());
    }



    // we're going to make a custom get moves method
    // the default one can't be understood due to moves being of type Type. thats kind of confusing.
    // we can't just use moves.toString() cause there are four of them. so we have to add those together.
    // we want this method to give extra information on the moves.

    // some setters are not there, because they will never be used
    public StringBuilder getMoves() {

        StringBuilder moveString = new StringBuilder();
        for (Move move : moves) {


            moveString.append(move.getMove()).append("\n");
        }
        return moveString; // return the string

    }


    // when a pokemon is switched out, statCodes reset to the default value, 6
    public void switchPokemonOut() {

        Arrays.fill(statCodes, 6);

        System.out.println(getName() + " is switched out."); // let the players know what's happening
    }

    public void switchPokemonIn() { // this needs to run when the pokemon is sent in.
        System.out.println("Player " + teamNum + " sends out " + getName() + '.'); // let the players know what's happening
    }

    public boolean isAlive() { // if the hp is more than 0 pokemon is alive, return true, otherwise return false
        return stats[0] > 0;

    }

    public static void natures(double[] natureCodes, String nature) {
        // here's the method to change the multiplier based on the nature
        // we're comparing one string with 20 so switch statements are easier.

        // if it matches one of these cases, change the increased multiplier to 1.1 and the decreased to .9
        // we don't even need a default case because the default case is that there are no matches to these 20 strings
        // which would mean it is one of the 5 natures that don't change anything
        // natures that do nothing are never seen competitively, but they exist so have to take them into account
        // the default case is to do nothing
        switch (nature) {
            case "Adamant" -> {
                natureCodes[0] = 1.1;
                natureCodes[2] = .9;
            }
            case "Modest" -> {
                natureCodes[2] = 1.1;
                natureCodes[0] = .9;
            }
            case "Jolly" -> {
                natureCodes[4] = 1.1;
                natureCodes[2] = .9;
            }
            case "Naive" -> {
                natureCodes[4] = 1.1;
                natureCodes[3] = .9;
            }
            case "Lonely" -> {
                natureCodes[0] = 1.1;
                natureCodes[1] = .9;
            }
            case "Timid" -> {
                natureCodes[4] = 1.1;
                natureCodes[0] = .9;
            }
            case "Hasty" -> {
                natureCodes[4] = 1.1;
                natureCodes[1] = .9;
            }
            case "Naughty" -> {
                natureCodes[0] = 1.1;
                natureCodes[3] = .9;
            }
            case "Brave" -> {
                natureCodes[0] = 1.1;
                natureCodes[4] = .9;
            }
            case "Bold" -> {
                natureCodes[1] = 1.1;
                natureCodes[0] = .9;
            }
            case "Impish" -> {
                natureCodes[1] = 1.1;
                natureCodes[2] = .9;
            }
            case "Lax" -> {
                natureCodes[1] = 1.1;
                natureCodes[3] = .9;
            }
            case "Relaxed" -> {
                natureCodes[1] = 1.1;
                natureCodes[4] = .9;
            }
            case "Rash" -> {
                natureCodes[2] = 1.1;
                natureCodes[3] = .9;
            }
            case "Calm" -> {
                natureCodes[3] = 1.1;
                natureCodes[0] = .9;
            }
            case "Gentle" -> {
                natureCodes[3] = 1.1;
                natureCodes[1] = .9;
            }
            case "Sassy" -> {
                natureCodes[3] = 1.1;
                natureCodes[4] = .9;
            }
            case "Careful" -> {
                natureCodes[3] = 1.1;
                natureCodes[2] = .9;
            }
            case "Mild" -> {
                natureCodes[2] = 1.1;
                natureCodes[1] = .9;
            }
            case "Quiet" -> {
                natureCodes[2] = 1.1;
                natureCodes[4] = .9;
            }
        }
    }


}
