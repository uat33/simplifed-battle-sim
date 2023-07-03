package moves;

import pokemon.Individual;
import type.Type;

import java.util.Random;


/*
 * A subclass of move. this is where all the status moves are stored as they need to be separated from the attacking moves.
 * All status moves don't deal direct damage, but that's all they have in common.
 * In terms of function, they are very varied and have little in common with each other
 * Unfortunately, in many cases there is no way to do this than to literally make methods for each status move.
 * This is a gargantuan task, so I chose to simplify by only including status moves that change stats.
 * The teams have been made so that this does not cause any issues.
 *
 */

public class Status extends Move{

    // constructor
    public Status(String name, Type type, String category, int pp, String power, String accuracy) {
        super(name, type, category, pp, power, accuracy);
    }

    @Override
    public void purpose(Individual user, Individual target) {
        // this method is universal to all moves. we want to be able to call it and have it always do the right thing.

        String moveName = getName();

        System.out.println(user.getName() + " used " + getName());// let the players know which move was used.
        if(!accuracyCheck(user, target)) { // check if the move misses. update pp at the same time within that method
            System.out.println("The attack missed.");
            return;
        }

        switch (moveName) { // take the move's name into the switch statement, so we can match to appropriate case
            case "Acupressure" -> {    // this picks a random stat, and raises it two stages.
                Random rand = new Random();
                Acupressure(user, rand.nextInt(7)); // make a method, cause it requires a lot of code.
            }

            // moves that raise speed two stages
            case "Rock Polish", "Agility" ->    // raise speed two stages.
                    StatChange.TwoSpeedRaise(user);

            // moves that raise attack two stages
            case "Swords Dance" -> // raise attack two stages.
                    StatChange.TwoAttackRaise(user);


            // moves that raise defense two stages
            case "Iron Defense", "Acid Armor", "Barrier" -> StatChange.TwoDefenseRaise(user);

            // tail glow and cotton guard are their own category
            case "Tail Glow" -> StatChange.TailGlow(user);
            case "Cotton Guard" -> StatChange.CottonGuard(user);

            // moves that raise special attack two stages
            case "Nasty Plot" -> // raise special attack two stages.
                    StatChange.TwoSpAttRaise(user);

            // moves that raise special defense two stages
            case "Amnesia" ->
                    StatChange.TwoSpDefRaise(user);

            // moves that raise accuracy two stages: none

            // moves that raise evasion two stages
            case "Minimize" -> // raise evasion two stages.
                    StatChange.TwoEvasionRaise(user);


            // moves that raise speed one stages: none

            // moves that raise attack one stages

            case "Meditate", "Sharpen", "Howl" -> StatChange.OneAttackRaise(user);
            // moves that raise defense one stages

            case "Harden", "Defense Curl", "Withdraw" -> StatChange.OneDefenseRaise(user);

            // moves that raise special attack one stage: none


            // moves that raise special defense one stage : none

            // moves that raise accuracy one stage: none

            // moves that raise evasion one stage

            case "Double Team" -> StatChange.OneEvasionRaise(user);


            // moves that lower speed two stages

            case "Cotton Spore", "Scary Face" -> StatChange.TwoSpeedDrop(target);


            // moves that lower attack two stages

            case "Feather Dance", "Charm" -> StatChange.TwoAttackDrop(target);


            // moves that lower defense two stages

            case "Screech" -> StatChange.TwoDefenseDrop(target);


            // moves that lower special attack two stages: there are none

            // moves that lower special defense two stages
            case "Metal Sound" -> StatChange.TwoSpDefDrop(target);


            // moves that lower accuracy two stages: none

            // moves that lower evasion two stages: none


            // moves that lower speed one stages
            case "String Shot" -> StatChange.OneSpeedDrop(target);

            // moves that lower attack one stages
            case "Growl" -> StatChange.OneAttackDrop(target);

            // moves that lower defense one stages
            case "Tail Whip", "Leer" -> StatChange.OneDefenseDrop(target);


            // moves that lower special attack one stages: there are none


            // moves that lower special defense one stages: there are none

            // moves that lower accuracy one stages

            case "Sand Attack", "Smokescreen", "Kinesis", "Flash" -> StatChange.OneAccuracyDrop(target);

            // moves that lower evasion one stages
            case "Defog", "Sweet Scent" -> StatChange.OneEvasionDrop(target);


            // there are status moves that affect different stats as well

            // +1 attack and special attack
            case "Work Up", "Growth" -> {
                StatChange.OneAttackRaise(user);
                StatChange.OneSpAttRaise(user);
            }
            // moves that raise defense and sp defense one stage
            case "Cosmic Power", "Defend Order", "Stockpile" -> {
                StatChange.OneDefenseRaise(user);
                StatChange.OneSpDefRaise(user);
            }

            // +1 att and def
            case "Bulk Up" -> {
                StatChange.OneAttackRaise(user);
                StatChange.OneDefenseRaise(user);
            }

            // +1 spa and special defense
            case "Calm Mind" -> {
                StatChange.OneSpAttRaise(user);
                StatChange.OneSpDefRaise(user);
            }

            // +1 attack and speed
            case "Dragon Dance" -> {
                StatChange.OneAttackRaise(user);
                StatChange.OneSpeedRaise(user);
            }

            // -1 attack and defense
            case "Tickle" -> {
                StatChange.OneAttackDrop(target);
                StatChange.OneDefenseDrop(target);
            }
            // +1 att, def and acc
            case "Coil" -> {
                StatChange.OneAttackRaise(user);
                StatChange.OneDefenseRaise(user);
                StatChange.OneAccuracyRaise(user);
            }

            // + 1 att, acc
            case "Hone Claws" -> {
                StatChange.OneAttackRaise(user);
                StatChange.OneAccuracyRaise(user);
            }
            // +1 spa, special def, and speed
            case "Quiver Dance" -> {
                StatChange.OneSpAttRaise(user);
                StatChange.OneSpDefRaise(user);
                StatChange.OneSpeedRaise(user);
            }
            // +1 att, +2 speed
            case "Shift Gear" -> {
                StatChange.OneAttackRaise(user);
                StatChange.TwoSpeedRaise(user);
            }
            // +2 attack, special attack and speed
            // -2 defense and special defense
            case "Shell Smash" -> { // weird because it lowers some of your own stats
                StatChange.TwoAttackRaise(user);
                StatChange.TwoSpAttRaise(user);
                StatChange.TwoSpeedRaise(user);
                StatChange.TwoDefenseDrop(user);
                StatChange.TwoSpDefDrop(user);
            }
            case "Curse" -> { // weird because it lowers some of your own stats
                StatChange.OneAttackRaise(user);
                StatChange.OneDefenseRaise(user);
                StatChange.OneSpeedDrop(user);
            }
            // and those are all the stat changing moves
        }
    }



    // make methods for moves with too many lines or else it gets very cluttered

    public void Acupressure(Individual user, int stat) {
        // chooses stat randomly
        // raise that stat two stage
        switch(stat){
            case 1 -> StatChange.TwoAttackRaise(user);
            case 2 -> StatChange.TwoDefenseRaise(user);
            case 3 -> StatChange.TwoSpAttRaise(user);
            case 4 -> StatChange.TwoSpDefRaise(user);
            case 5 -> StatChange.TwoSpeedRaise(user);
            case 6 -> StatChange.TwoAccuracyRaise(user);
            default -> StatChange.TwoEvasionRaise(user);
        }

    }

}
