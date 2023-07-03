package moves;

import pokemon.Individual;

/*
 * This class is just for organizational purposes.
 * A common application for status moves is to have them change stats
 * This class contains everything necessary for that.
 *  all methods are static, so they can be accessed without an object.
 *  There's a lot of stuff here, but the point is we want to just be able to say StatChange.(method) and have it do everything.
 *  this does that.
 */
public class StatChange{

    // stats can only be raised or lowered to a max of 6 stages. make sure this limit is not surpassed
    public static boolean checkLimit(boolean raise, int stage) {
        return raise ? stage != 6 : stage != -6;
       // if trying to raise, stage cannot be 6
       // otherwise we are trying to lower and stage cannot be -6

    }


    // pokemon is the pokemon whose stats we are changing
    // if the stat needs to go up, raise is true
    // otherwise its false
    // stages is always 1 or 2. it's how many times the stat needs to go up or down
    // finally, stat is the index in statcodes that needs to be changed
    // 0 is attack, 1 is defense etc.

    public static void doTheChanges(Individual pokemon, boolean raise, int stages, int stat) {

        // even though it is slower we have to change the stats one stage at a time
        // otherwise if we are 1 below the limit, the stat could be raised two stages and that is not allowed
        for(int i = 0; i < stages; i++) {
            int[] statCodes = pokemon.getStatCodes();
            if(checkLimit(raise, statCodes[stat])) { // if this evaluates to true, then the stat can be changed


                if (raise) statCodes[stat]++;
                else statCodes[stat]--;
                pokemon.recalculate(); // need to recalculate stats

            }
            else{
                String statName = switch(stat){
                    case 1 -> "Attack";
                    case 2 -> "Defense";
                    case 3 -> "Special Attack";
                    case 4 -> "Special Defense";
                    case 5 -> "Speed";
                    default -> throw new IllegalStateException("Unexpected value: " + stat);
                };

                System.out.printf("%s's %s cannot be %s anymore.\n", pokemon.getName(), statName,
                        raise ? "raised" : "lowered");
            }
        }
    }

    // a very common application for status moves is raising your own stats or decreasing the opponent's stats.
    // we can make general methods for this, that can be called at a later time.

    // note: for some of these, there are no moves that make that change, however the method is included anyway as it makes future changes easier
    // attack is the first statcode, so take that and increase by one.


    public static void OneAttackRaise(Individual pokemon) { // +1 attack
        // user is the one using the moves. true because the stat is raised. 1 because 1 stage, and 0 because that's where attack is.
        doTheChanges(pokemon, true, 1, 0);
        // the text we display needs to be separate because the text is different based on the number of stages.
        change(pokemon.getName(), "attack", "rose");
    }

    // keep doing that for all these methods.
    public static void OneAttackDrop(Individual pokemon) { // -1 attack
        doTheChanges(pokemon, false, 1, 0);
        change(pokemon.getName(), "attack", "fell");


    }
    public static void TwoAttackRaise(Individual pokemon) { // +2 attack
        doTheChanges(pokemon, true, 2, 0);
        change(pokemon.getName(), "attack", "rose", "sharply");
    }
    public static void TwoAttackDrop(Individual pokemon) { // -2 attack
        doTheChanges(pokemon, false, 2, 0);
        change(pokemon.getName(), "attack", "fell", "harshly");
    }



    public static void OneDefenseRaise(Individual pokemon) { // +1 Defense
        doTheChanges(pokemon, true, 1, 1);
        change(pokemon.getName(), "defense", "rose");
    }
    public static void OneDefenseDrop(Individual pokemon) { // -1 Defense
        doTheChanges(pokemon, false, 1, 1);
        change(pokemon.getName(), "defense", "fell");
    }
    public static void TwoDefenseRaise(Individual pokemon) { // +2 Defense
        doTheChanges(pokemon, true, 1, 1);
        change(pokemon.getName(), "defense", "rose", "sharply");
    }
    public static void TwoDefenseDrop(Individual pokemon) { // -2 Defense
        doTheChanges(pokemon, false, 1, 1);
        change(pokemon.getName(), "defense", "fell", "harshly");
    }


    public static void OneSpAttRaise(Individual pokemon) { // +1 SpAtt
        doTheChanges(pokemon, true, 1, 2);
        change(pokemon.getName(), "special attack", "rose");
    }

    public static void OneSpAttDrop(Individual pokemon) { // -1 SpAtt
        doTheChanges(pokemon, false, 1, 2);
        change(pokemon.getName(), "special attack", "fell");
    }

    public static void TwoSpAttRaise(Individual pokemon) { // +2 SpAtt
        doTheChanges(pokemon, true, 2, 2);
        change(pokemon.getName(), "special attack", "rose", "sharply");
    }
    public static void TwoSpAttDrop(Individual pokemon) { // -2 SpAtt
        doTheChanges(pokemon, false, 2, 2);
        change(pokemon.getName(), "special attack", "fell", "harshly");
    }


    public static void OneSpDefRaise(Individual pokemon) { // +1 SpDef
        doTheChanges(pokemon, true, 1, 3);
        change(pokemon.getName(), "special defense", "rose");
    }
    public static void OneSpDefDrop(Individual pokemon) { // -1 SpDef
        doTheChanges(pokemon, false, 1, 3);
        change(pokemon.getName(), "special defense", "fell");
    }
    public static void TwoSpDefRaise(Individual pokemon) { // +2 SpDef
        doTheChanges(pokemon, true, 2, 3);
        change(pokemon.getName(), "special defense", "rose", "sharply");
    }
    public static void TwoSpDefDrop(Individual pokemon) { // -2 SpDef
        doTheChanges(pokemon, false, 2, 3);
        change(pokemon.getName(), "special defense", "fell", "harshly");
    }

    public static void OneSpeedRaise(Individual pokemon) { // +1 Speed
        doTheChanges(pokemon, true, 1, 4);
        change(pokemon.getName(), "speed", "rose");
    }
    public static void OneSpeedDrop(Individual pokemon) { // -1 Speed
        doTheChanges(pokemon, false, 1, 4);
        change(pokemon.getName(), "speed", "fell");

    }
    public static void TwoSpeedRaise(Individual pokemon) { // +2 Speed
        doTheChanges(pokemon, true, 2, 4);
        change(pokemon.getName(), "speed", "rose", "sharply");
    }
    public static void TwoSpeedDrop(Individual pokemon) { // -2 Speed
        doTheChanges(pokemon, false, 2, 4);
        change(pokemon.getName(), "speed", "fell", "harshly");
    }


    public static void OneAccuracyRaise(Individual pokemon) { // +1 Accuracy
        doTheChanges(pokemon, true, 1, 5);
        change(pokemon.getName(), "accuracy", "rose");
    }
    public static void OneAccuracyDrop(Individual pokemon) { // -1 Accuracy
        doTheChanges(pokemon, false, 1, 5);
        change(pokemon.getName(), "accuracy", "fell");
    }
    public static void TwoAccuracyRaise(Individual pokemon) { // +2 Accuracy
        doTheChanges(pokemon, true, 2, 5);
        change(pokemon.getName(), "accuracy", "rose", "sharply");
    }
    public static void TwoAccuracyDrop(Individual pokemon) { // -2 Accuracy
        doTheChanges(pokemon, false, 2, 5);
        change(pokemon.getName(), "accuracy", "fell", "harshly");
    }


    public static void OneEvasionRaise(Individual pokemon) { // +1 Evasion
        doTheChanges(pokemon, true, 1, 6);
        change(pokemon.getName(), "evasion", "rose");
    }
    public static void OneEvasionDrop(Individual pokemon) { // -1 Evasion
        doTheChanges(pokemon, false, 1, 6);
        change(pokemon.getName(), "evasion", "fell");
    }
    public static void TwoEvasionRaise(Individual pokemon) { // +2 Evasion
        doTheChanges(pokemon, true, 2, 6);
        change(pokemon.getName(), "evasion", "rose", "sharply");
    }
    // there are no moves that drop evasion two stages as of gen 5, so we don't need that method

    // very rare
    // raises special attack three stages
    public static void TailGlow(Individual pokemon){
        doTheChanges(pokemon, true, 3, 2);
        change(pokemon.getName(), "special attack", "rose", "drastically");

    }

    // very rare
    // raises defense three stages
    public static void CottonGuard(Individual pokemon){
        doTheChanges(pokemon, true, 3, 1);
        change(pokemon.getName(), "defense", "rose", "drastically");

    }




    // we need to tell the user these changes happened.
    // sharp and harsh are the quantifiers for two stages
    // overload the methods
    public static void change(String name, String stat, String verb, String quantifier) {
        System.out.printf("%s's %s %s %s.\n", name, stat, verb, quantifier);
    }


    public static void change(String name, String stat, String verb) {
        System.out.printf("%s's %s %s.\n", name, stat, verb);
    }

}
