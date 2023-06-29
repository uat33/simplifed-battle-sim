package pokemon;

// everything common to all pokemon goes here

import java.util.HashMap;
import java.util.Map;

public interface PokemonInterface {

    int level = 100; // the level of the pokemon. in battles like this always 100
    int[] ivs = {31, 31, 31, 31, 31, 31}; // almost always like this in competitive battles, in this simplification, it is always like this



    /*
     * stat changes
     *
     * stats start off with a multiplier of 1
     *
     * they can be raised 6 stages and lowered 6 stages
     *
     * each of those stat changes has its own multiple
     * e.g. +1 is 1.5, -3 is 1/2.5
     *
     * this is where those are stored
     *
     * could store in an array, but that would make the purpose and implementation more difficult to understand

     * */


    // multipliers for attack, defense, special attack, special defense, speed
    // the key will be the stage, e.g. 1, for +1 stage, -3 for -3 stages
    // the double is the multiplier applied to the stat for that change
    Map<Integer, Double> statChanges = new HashMap<>();
    // accuracy and evasion use their own multipliers
    Map<Integer, Double> statChangesAccEvasion = new HashMap<>();



}
