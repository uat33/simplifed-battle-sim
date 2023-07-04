package moves;

import battle.Battle;
import pokemon.Individual;
import pokemon.PokemonInterface;
import setup.Setup;
import type.Type;

import java.util.Random;


/*
* This class extends the move class, and consists of Attacking moves.
*
* Everything to do with dealing damage is done here.
*
*
* */

public class Attack extends Move {


    public Attack(String name, Type type, String category, int pp, String power, String accuracy) {
        super(name, type, category, pp, power, accuracy);
    }

    /*
     * This method calculates the part of the damage formula universal to all attacking moves.
     *
     * Parameters
     *  - power: an integer representing the base power of the move
     *  - att: the attacking stat of the pokemon using the move
     *  - def: the defensive stat of the target
     *  - crit: whether this attack is a critical hit or not
     * */
    private int calculateDamage(int power, int att, int def, boolean crit){
        int base = ((2 * PokemonInterface.level / 5 + 2) * power * att / def) / 50 + 2; // the base formula
        // pokemon has a mechanic known as rolls or a range.
        // once the damage is calculated, there are different amounts of damage it can do
        // it is 85 percent to 100 percent of the damage, inclusive
        // this means there are 16 possibilites for the damage, and it is completely random.

        base *= (new Random().nextInt(16) + 85) / 100.0; // random number from 0 to 15 has 85 added to it.
        // multiply that with damage to get the final amount.

        // if it is a crit, the damage doubles
        return crit ? base * 2 : base;

    }

    @Override
    public void purpose(Individual user, Individual target) throws InterruptedException {


        System.out.println(user.getName() + " used " + getName());
        // struggle bypasses accuracy checks, pp decrements and everything related to types
        if (!getName().equals("Struggle") && !accuracyCheck(user, target)) { // check if the move misses. update pp at the same time within that method
            System.out.println("The attack missed.");
            return;
        }
        // if the move lands

        // first things first, there are two types of attacking moves. physical or special.
        // physical moves use attack and defense, sometimes called physical attack and physical defense to avoid confusion
        // special moves use special attack and special defense.
        // there is one exception: psyshock uses special attack and physical defense

        // critical hits. a 1/16 chance to deal twice the damage (as of gen 5, it's 1.5 in later games)
        // critical hits also override any detrimental stat changes, but not beneficial ones

        boolean crit =  new Random().nextInt(16) == 15; // 1 in 16 chance that this will be 15


        int att, def;

        if (getCategory().equals("Physical")) {
            // the indices are strange here because statcodes doesn't have hp because that can't be lowered, whereas the other two arrays we have do have hp
            // if it is a crit and the user's attack has been lowered
            // use the startingstats attack
            // otherwise use the normal attack
            att = crit && user.getStatCodes()[0] < 0 ? user.getStartingStats()[1] : user.getStats()[1]; // attack
            // if it is a crit and target's defense has been raised
            // use startingstats defense
            // otherwise use normal defense
            def = crit && target.getStatCodes()[1] > 0 ? target.getStartingStats()[2] : target.getStats()[2]; // defense

        }
        // similar logic here, but with physical defense and special attack
        else if (getName().equals("Psyshock")) { // psyshock is a special move still, so we can minimize the amount of times we check this by doing it here
            att = crit && user.getStatCodes()[2] < 0 ? user.getStartingStats()[3] : user.getStats()[3]; // special attack

            def = crit && target.getStatCodes()[1] > 0 ? target.getStartingStats()[2] : target.getStats()[2]; // defense
        }
        else { // regular special move

            att = crit && user.getStatCodes()[2] < 0 ? user.getStartingStats()[3] : user.getStats()[3]; // special attack
            def = crit && target.getStatCodes()[3] > 0 ? target.getStartingStats()[2] : target.getStats()[4]; // special defense

        }
        int damage;
        if (getName().equals("Gyro Ball")) { // a special move, with a special way of calculating damage
            damage = ((( (2 * PokemonInterface.level / 5 + 2) * att * 25
                    * (target.getStats()[5] / user.getStats()[5]) / def) / 50) + 2);
        }
        else damage = calculateDamage(Integer.parseInt(getPower()), att, def, crit);

        // if the move is struggle
        if (getName().equals("Struggle")){
            dealDamage(target, damage);
            // struggle deals 1/4 of the user's health as recoil damage
            int recoil = user.getMaxHP() / 4;
            if (user.getStats()[0] <= recoil){
                System.out.println(user.getName() + " lost " + user.getStats()[0] + " HP from recoil damage.");
                newPokemon(user);
            }
            else{
                System.out.println(user.getName() + " lost " + recoil + " HP from recoil damage.");
                user.getStats()[0] -= recoil;
            }

            return;
        }

        // not done yet

        // there are two multipliers that have to do with type.
        // the first is same type attack bonus called STAB.
        // if the user is the same time as the move being used, there is a 50 percent increase in the damage.



        // if the moves type is equal to either of the user's types
        Type moveType = getType();

        if (user.getTypes().contains(moveType)) {
            damage *= 1.5; // multiply damage by 1.5 if stab is true
        }


        // the second is how the type of the move interacts with the type of the target
        double typeMult = 1; // base multiplier

        for (Type type : target.getTypes()) {
            // get the matchups and use the type.getTypeName() string value as the key for that hashmap
            // this gets us the typemultiplier which we multiply with what we have
            typeMult *= moveType.getMatchup().get(type.getTypeName());
        }

        damage *= typeMult; // multiply damage by typeMultiplier

        // tell users what happened
        // depending on how the types stack up, sometimes text is printed here
        Thread.sleep(500); // we want a gap, so it flows better

        // have to output text based on the typemultiplier
        // 1 means no text
        // more than one is super effective
        // 0 is no effect
        // < 1 but not 0 is not very effective
        if (typeMult > 1) System.out.println("It's super effective!");
        else if (typeMult == 0) System.out.println("It had no effect.");
        else if (typeMult < 1) System.out.println("It's not very effective.");


        // certain moves lower your stats after using them

        switch (getName()) {
            case "Overheat", "Leaf Storm", "Draco Meteor" ->
                    StatChange.TwoSpAttDrop(user); // these moves lower your spA two stages
            case "Close Combat" -> {  // - 1 def, - 1 spDef
                StatChange.OneDefenseDrop(user);
                StatChange.OneSpDefDrop(user);
            }
            case "Superpower" -> {  // - 1 att, -1 def
                StatChange.OneAttackDrop(user);
                StatChange.OneDefenseDrop(user);
            }
            case "Hammer Arm" ->  // -1 speed
                    StatChange.OneSpeedDrop(user);
        }

        Thread.sleep(500); // we want a gap, so it flows better



        dealDamage(target, damage); // delegate to method so struggle can also use it
    }

    /*
     * This method is run when a pokemon is knocked out.
     *
     * Parameters
     *  - target: the pokemon that was knocked out
     * */
    private void newPokemon(Individual target){

        Setup.removePokemon(target); // first call this method to remove the pokemon


        // prompt the correct user to send out another pokemon

        // but only if they have pokemon left
        if (target.getTeamNum() == 1) {
            if (Setup.team1.size() > 0) {
                Battle.pokemon1 = Battle.newPokemon(target, Setup.team1);
                Battle.pokemon1.switchPokemonIn();
            }

        }
        else {
            if (Setup.team2.size() > 0) {
                Battle.pokemon2 = Battle.newPokemon(target, Setup.team2);
                Battle.pokemon2.switchPokemonIn();
            }

        }
    }


    /*
     * This method deals the damage.
     *
     * Parameters
     *  - target: the pokemon the move is used on.
     *  - damage: the amount of damage that will be dealt.
     * */
    public void dealDamage(Individual target, int damage){

        if (target.getStats()[0] - damage <= 0) { // the pokemon has fainted
            System.out.println(target.getName() + " lost " + target.getStats()[0] + " HP.");
            // telling the player how much damage it did when it killed is more information than is given in a pokemon game
            // so we do it like this. if it is going to kill, we just output the current health as the damage done.

            // call the new Pokemon method
            newPokemon(target);

        } else {
            System.out.println(target.getName() + " lost " + damage + " HP.");
        }
        // now to deal the damage.
        target.getStats()[0] -= damage;

    }

}
