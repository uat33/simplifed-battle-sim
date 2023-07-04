package moves;


import pokemon.Individual;
import pokemon.PokemonInterface;
import type.Type;

import java.util.Random;

/*
 * this is where the information for the move is stored. implements MoveInterface but the class is abstract
 *  so some of the implemented methods can be defined later
 *  Attacking and status moves have many attributes and methods in common.
 *  Do that all here.
 */
public abstract class Move{

    // attributes of moves
    private final String name; // name of the move
    private final Type type; // move type
    private int pp; // current pp: (how many times the move can be used)
    private final int maxPP; // the max pp that you start with
    private final String category; // the type of move: physical, special or status
    private final String power; // move power
    private final String accuracy; // move accuracy

    // if you think it would make more sense for the power and accuracy to be ints, you would be correct
    // but this doesn't work due to some details, the explanation is below if you care to read it.

    // status moves always deal no damage upfront, and they are saved as - in the text file, so cannot be saved as ints
    // you could change it to 0 when adding it to the array list
    // however the damage is literally nothing, so that value is never used, so there isn't actually any point in doing the conversion

    // accuracy can't be saved as an int either because certain moves cannot miss no matter what.
    // these moves are different from moves with a 100 accuracy because those can miss due to stat changes or special effects
    // those unmissable moves are also saved as -
    // and once again we can't convert it
    // but this time, this is actually useful for us.
    // because we need a way to check that moves cannot miss.

    // so power and accuracy are strings


    // constructor. a lot is the same for attacking and status moves, so just do all that here.
    public Move(String name, Type type, String category, int pp, String power, String accuracy) {
        this.name = name;
        this.type = type;
        this.category = category;
        this.pp = pp;
        this.maxPP = pp; // when the move is created, maxpp and pp are the same
        this.power = power;
        this.accuracy = accuracy;
    }

    // the functionality of the move
    public abstract void purpose(Individual user, Individual target) throws InterruptedException;

    public boolean accuracyCheck(Individual user, Individual target) {
        ppUpdate(); // even if the move misses, the pp must be decremented
        if (accuracy.equals("—")) return true; // a move that always hits

        // accuracy changes
        // this is more complex than it appears
        // first subtract user accuracy stages and target evasion stages
        int stageMultiplier =  user.getStatCodes()[5] -  target.getStatCodes()[6];
        // probably to prevent excessive accuracy manipulation, this number is capped at -6 and +6
        // anything below -6 is set to -6, anything above is set to 6
        if (stageMultiplier < -6) stageMultiplier = -6;
        else if (stageMultiplier > 6) stageMultiplier = 6;
        // now what you do is take the accuracy of the move and multiply it with the accuracy multiplier of the stage we just calculated
        double acc = Integer.parseInt(accuracy) * PokemonInterface.statChangesAccEvasion.get(stageMultiplier);
        Random rand = new Random();

        // get a random number between 1 and 100

        // if that number is less than or equal to our accuracy, it hits
        // otherwise the move misses.

        return rand.nextInt(100) + 1 <= acc;

    }

    public void ppUpdate(){
        this.pp--;
    }



    // two string methods for two different things.

    // the toString is for when the user decides the move. there is a lot of information and not a lot of space
    // the name and the pp are the standard in pokemon games

    // the getMove is for if the user asks for more information, in which case we tell them everything they could want to know

    // the toString
    @Override
    public String toString() {
        return name + " (" + pp + "/" +  maxPP + ")";

    }


    public String getMove() { // this is used to get information on the move.
        return name + ": type=" + type + ", category=" + category + ", pp=" + pp + ", power=" + power
                + ", accuracy=" + accuracy;
    }


    // getters

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public int getPp() {
        return pp;
    }


    public String getPower() {
        return power;
    }

}
