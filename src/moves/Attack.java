package moves;

import battle.Battle;
import pokemon.Individual;
import pokemon.PokemonInterface;
import setup.Setup;
import type.Type;

import java.util.Random;

public class Attack extends Move {


    public Attack(String name, Type type, String category, int pp, String power, String accuracy) {
        super(name, type, category, pp, power, accuracy);
    }


    public void purpose(Individual user, Individual target) throws InterruptedException {


        System.out.println(user.getName() + " used " + getName());
        if (!accuracyCheck(user, target)) { // check if the move misses. update pp at the same time within that method
            System.out.println("The attack missed.");
            return;
        }
        // if the move lands

        // first things first, there are two types of attacking moves. physical or special.
        // physical moves use attack and defense, sometimes called physical attack and physical defense to avoid confusion
        // special moves use special attack and special defense.
        // there is one exception: psyshock uses special attack and physical defense

        int att = 0, def = 0;


        if (getCategory().equals("Physical")) {

            att = user.getStats()[1]; // attack
            def = target.getStats()[2]; // defense

        } else if (getName().equals("Psyshock")) { // psyshock is a special move still, so we can minimize the amount of times we check this by doing it here
            att = user.getStats()[3]; // special attack
            def = target.getStats()[2]; // defense
        } else { // regular special move

            att = user.getStats()[3]; // special attack
            def = target.getStats()[4]; // defense

        }
        // seeing as we're here, this is an attacking move and power should be an int. so we can cast it
        int damage = 2 * PokemonInterface.level / 5 + 2;
        if (getName().equals("Gyro Ball")) { // a special move, with a special way of calculating damage
            damage = ((( damage * att * 25 * (target.getStats()[5] / user.getStats()[5]) / def) / 50) + 2);
        }
        else damage = (((damage * att * Integer.parseInt(getPower()) / def) / 50) + 2);
        // not done yet. the next part is tricky.



        // there are two multipliers that have to do with type.
        // the first is same type attack bonus called STAB.
        // if the user is the same time as the move being used, there is a 50 percent increase in the damage.
        // the second is how the type of the move interacts with the type of the target


        // if the moves type is equal to either of the user's types
        Type moveType = getType();
        boolean stab = user.getTypes().contains(moveType);

        if (stab) {
            damage *= 1.5; // multiply damage by 1.5 if stab is true
        }

        // find the number that the first type appears in the typeSetup arrayList.
        // that number is the same as where it will appear in the matchup array.
        // so take this attacks type and find the value in that array
        // multiply that with damage

        double typeMult = 1;

        for (Type type : target.getTypes()) {
            // get the matchups and use the type.getTypeName() string value as they key
            typeMult *= moveType.getMatchup().get(type.getTypeName());
        }

        damage *= typeMult; // multiply damage by typeMultiplier


        // pokemon has a mechanic known as rolls or a range.
        // once the damage is calculated, there are different amounts of damage it can do
        // it is 85 percent to 100 percent of the damage, inclusive
        // this means there are 16 possibilites for the damage, and it is completely random.

        Random rand = new Random();

        damage *= (rand.nextInt(16) + 85) / 100.0; // random number from 0 to 15 has 85 added to it.
        // multiply that with damage to get the final amount.


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

        if (getName().equals("Overheat") || getName().equals("Leaf Storm") || getName().equals("Draco Meteor")) {
            StatChange.TwoSpAttDrop(user); // these moves lower your spA two stages

        } else if (getName().equals("Close Combat")) { // - 1 def, - 1 spDef
            StatChange.OneDefenseDrop(user);
            StatChange.OneSpDefDrop(user);

        } else if (getName().equals("Superpower")) { // - 1 att, -1 def
            StatChange.OneAttackDrop(user);
            StatChange.OneDefenseDrop(user);
        } else if (getName().equals("Hammer Arm")) { // -1 speed
            StatChange.OneSpeedDrop(user);

        }

        // if the pokemon dies, you're not supposed to reveal how much damage the move does.
        // so take that into account;
        Thread.sleep(500); // we want a gap, so it flows better

        if (target.getStats()[0] - damage <= 0) { // the pokemon has fainted
            System.out.println(target.getName() + " lost " + target.getStats()[0] + " HP.");
            // telling the player how much damage it did when it killed is more information than is given in a pokemon game
            // so we do it like this. if it is going to kill, we just output the current health as the damage done.

            // prompt the correct user to send out another pokemon

            // but only if they have pokemon left


            if (target.getTeamNum() == 1) {
                if (Setup.team1.size() > 0) {
                    Battle.pokemon1 = Battle.newPokemon(target, Setup.team1);
                    Battle.pokemon1.switchPokemonIn();
                }

            } else {
                if (Setup.team2.size() > 0) {
                    Battle.pokemon2 = Battle.newPokemon(target, Setup.team2);
                    Battle.pokemon2.switchPokemonIn();
                }

            }

        } else {
            System.out.println(target.getName() + " lost " + damage + " HP.");
        }
        // now to deal the damage.
        target.getStats()[0] -= damage;


    }
}
