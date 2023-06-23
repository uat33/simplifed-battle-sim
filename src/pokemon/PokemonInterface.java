package pokemon;

// everything common to all pokemon goes here

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
     * each of those stat changes has its own multipler
     * e.g. +1 is 1.5, -3 is 1/2.5
     *
     * this is where those are stores
     * */


    // multipliers for attack, defense, special attack, special defense, speed
    double[] statChangesOne = {1.0/4, 1/3.5, 1.0/3, 1/2.5, 1.0/2, 1/1.5, 1, 1.5, 2, 2.5, 3, 3.5, 4};

    // accuracy and evasion use their own multipliers
    double[] statChangesTwo = {1.0/3, 1.0/(8.0/3), 1.0/(7.0/3), 1.0/2, 3.0/5, 3.0/4, 1, 4.0/3, 5.0/3, 2, 7.0/3, 8.0/3, 3};






}
