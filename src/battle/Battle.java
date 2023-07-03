package battle;

import moves.Move;
import pokemon.Individual;
import setup.Setup;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;


/*
 * This class is where the battle will happen.
 * So the issue here is to do with information.
 * For this battle to be fair and proper, you cannot be allowed to see the opponent's pokemon, apart from the pokemon they have sent out.
 * Nor should you be able to see the moves of their pokemon
 * And you definitely shouldn't be able to see what they did during their turn until after you've completed yours.
 * Unfortunately, this game requires two players which means we can't just output whatever we want to the console.
 * Luckily, there is a way around this.
 * It's not exactly a perfect system though.
 * The idea is to use joption pane which can create new windows in which the user can select options.
 * After a decision, the window is closed so information is hidden.
 * However, this essentially results in two players passing the computer back and forth, which is a little annoying.
 * but there's no way around that.
 */

public class Battle {

    // these are the pokemon currently in the battlefield
    // make them public fields, so that they can be altered from the attack class.
    // static cause there's only ever one needed per battle.
    public static Individual pokemon1;
    public static Individual pokemon2;


    public Battle() throws InterruptedException {
        Random rand = new Random(); // randomness is a big part of pokemon
        int turns = 1; // keep track of the turn number. in each turn, each player does 1 thing.


        // in random battles, a random pokemon is chosen from both teams and sent out.
        // so get a random number from 0 to 5 for each team, and set pokemon1 and pokemon2 to the pokemon in that slot in the team objects from the setup class
        pokemon1 = Setup.team1.get(rand.nextInt(6));
        pokemon2 = Setup.team2.get(rand.nextInt(6));


        // switch in those two pokemon
        pokemon1.switchPokemonIn();
        pokemon2.switchPokemonIn();

        System.out.println(); // spacing


        do {
            // print out the turn number in the console
            // this is done in battle sims, because players should be able to scroll up and see what's happened previously
            // as a result, we need to divide each turn
            System.out.println("TURN " + turns);
            System.out.println(); // spacing
            // this information should be seen by both players, so put it in the console.
            // do while loop because we want this to be printed out before any decisions have been made as well
            // outputs how much health the pokemon have at the end of the turn
            pokemon1.pokemonStatus();
            System.out.println(); // skip lines
            pokemon2.pokemonStatus();
            Thread.sleep(3000); // we need to give time before opening the next window, because the players cannot be allowed to see each other's info
            Object decision1 = playerTurn(pokemon1, turns, Setup.team1); // the decision of the first player, saved into type object
            // this is because it can be of type Individual or type Move.
            Thread.sleep(3000); // we need to give time before opening the next window, because the players cannot be allowed to see each other's info
            // ideally this time would be used to passover the computer
            Object decision2 = playerTurn(pokemon2, turns, Setup.team2); // the decision of the second player, same thought process as the first one.
            // we want to store both players' decisions so that they can be executed at the same time
            // this needs to be done because a decision can't be allowed to be made with knowledge of the other player's decision.

            // this part is important
            // switches take priority in pokemon
            // so if one player's pokemon is faster than the other, but the other switches, the pokemon still switches first
            // if both pokemon attack, speed stat determines which pokemon moves first.
            // if both switch, it doesn't matter which one happens first.

            // if the decision is of a type individual, it's a switch. if it's of type move, it's an attack.

            if(decision1 instanceof Individual && decision2 instanceof Individual) { // if both switch

                pokemon1 = switchPokemon(pokemon1, decision1); // this method will withdraw the pokemon that is currently out, and put the new pokemon in its variable
                pokemon1.switchPokemonIn(); // actually switch the new pokemon in
                // do the same here.
                pokemon2 = switchPokemon(pokemon2, decision2);
                pokemon2.switchPokemonIn();
            }
            else if (decision1 instanceof Individual) { // if only player 1 chooses to switch.
                // do the switching
                pokemon1 = switchPokemon(pokemon1, decision1); // set the field to the new pokemon
                pokemon1.switchPokemonIn(); // switch in

                ((Move) decision2).purpose(pokemon2, pokemon1); // decision 2 must be a move, so cast it and call its purpose method
            }
            else if (decision2 instanceof Individual) { // if only player 2 chooses to switch
                // do the switching
                pokemon2 = switchPokemon(pokemon2, decision2); // set the field to the new pokemon
                pokemon2.switchPokemonIn(); // switch it in
                // check if pokemon can attack
                ((Move) decision1).purpose(pokemon1, pokemon2); // decision 1 must be a move, so cast it and call its purpose method
            }
            else { // both attacked, so we have to check speed to see who moved first.
                if (pokemon1.getStats()[5] > pokemon2.getStats()[5]) { // pokemon 1 is faster
                    // make an attacks method to avoid repetition
                    // in the method, the first slot is the move that goes first
                    // so in this case decision1 goes first decision2 is second, then the pokemon that used them in the same order.
                    attacks(decision1, decision2, pokemon1, pokemon2);
                }
                else if (pokemon2.getStats()[5] > pokemon1.getStats()[5]) { // pokemon 2 is faster
                    // decision 2 is first this time
                    // then decision1
                    // then the pokemon
                    attacks(decision2, decision1, pokemon2, pokemon1); // so change who goes where
                }
                else { // if there's a speed tie, it's a 50-50 chance for who moves first
                    if(Math.random() < .5) {
                        attacks(decision1, decision2, pokemon1, pokemon2); // pokemon1 goes first
                    }
                    else {
                        attacks(decision2, decision1, pokemon2, pokemon1);  // pokemon 2 goes first
                    }
                }
            }

            turns++;

        } while (Setup.team1.size() > 0 && Setup.team2.size() > 0); // execute the loop while both players have pokemon




        if (Setup.team1.size() > 0) { // if team1 still has pokemon in it, player 1 is the winner
            System.out.println("Player 1 wins!");
        }
        else { // otherwise we know player 2 wins
            System.out.println("Player 2 wins!");

        }


    }

    // this method will run when a pokemon faints
    public static Individual newPokemon (Individual target, ArrayList<Individual> team) {  // takes the pokemon that died and its team as parameters
        System.out.println(target.getName() + " fainted."); // let the players know whats happening.


        String[] names = new String[team.size()]; // different sizes possible based on how many pokemon are left

        for(int i = 0; i < names.length; i++) { // for each name we need
            names[i] = team.get(i).getName(); // add the name of the pokemon in team to our array
        }

        // get the users decision
        int returnValue = JOptionPane.showOptionDialog(null, "Player " + target.getTeamNum(), "Send in new pokemon",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, names, null);

        return team.get(returnValue); // return the chosen pokemon

    }



    // this method simply switchout the passed in pokemon, and returns decision after casting it to type Individual
    public Individual switchPokemon(Individual pokemonOut, Object decision) {
        pokemonOut.switchPokemonOut();
        return (Individual) decision;
    }


    //  we can write less lines by making this method.
    // put the appropriate pokemon and move in the right slots based on who's moving first
    public void attacks(Object firstMove, Object secondMove, Individual firstPokemon, Individual secondPokemon) throws InterruptedException {
        ((Move) firstMove).purpose(firstPokemon, secondPokemon);

        if (secondPokemon.isAlive()){ // if the secondPokemon is dead, it cannot attack
            ((Move) secondMove).purpose(secondPokemon, firstPokemon); // decision 2 must be a move
        }
    }


    // this is the method that runs for each player to get their decision
    // get their pokemon, turn number and team
    public Object playerTurn(Individual pokemon, int turns, ArrayList<Individual> team) {


        // create our array of choices for the user

        String[] options;
        if (pokemon.hasPP()) {
            options = new String[]{"More Info", "Switch out", pokemon.moves[0].toString(), pokemon.moves[1].toString()
                    , pokemon.moves[2].toString(), pokemon.moves[3].toString()};
        }
        else{
            options = new String[]{"More Info", "Switch out", "Struggle"};
        }


        // I have no idea why, but it shows options in reverse order in the pane
        // so i looked it up, and apparently this is a thing in macOS
        // it shows it from right to left, but on windows and linux its left to right



        // this second parameter is text that will be outputted above the buttons
        // we want to show the player number and the name of their pokemon
        // the third parameter is the window title, where we're just putting the turn number
        int returnValue = JOptionPane.showOptionDialog(null, "Player " + pokemon.getTeamNum() + ": " + pokemon.getName(), "Turn " + turns,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, options, null);
        // returnValue is the index of what the user chose, hidden from the other player.

        if (returnValue == 2 && options.length == 3){ // if struggle is an option and they chose it
            return Individual.struggle;
        }

        if (returnValue > 1) {
            if (pokemon.moves[returnValue - 2].getPp() == 0){
                // tell the user they are out of pp for that move
                JOptionPane.showMessageDialog(null, "Out of PP. Select a different option.", "No more PP.", JOptionPane.WARNING_MESSAGE);
                // return the method so they go again.
                return playerTurn(pokemon, turns, team);
            }

            return pokemon.moves[returnValue-2];  // -2 because of the two initial values
        }

        if(returnValue == 0) { // wants more info
            // show them another pane with more information received from the methods we made.

            // we don't actually know what specifically they're looking for, and its possible they don't either

            // so show them everything they could want to know (as long as it is permitted)
            // for example, we are not going to show anything about the opposing pokemon except for its type and health

            // add the pokemons info this adds the pokemons moves, name, nature, and stats.
            String info = pokemon.getMoves().toString() + '\n' + pokemon;


            // we also want to show the types of the pokemon the opponent has out and the percent health it has left
            // type is not seen in games, and you don't get a percentage of health
            // but it is seen in battle simulators.
            // if their pokemons num is 1, the opponents pokemon is in the field pokemon2
            // if their pokemons num is 2, the opponents pokemon is in the field pokemon1
            // add the appropriate pokemons types
            // skip a line for readability
            Individual opponent = pokemon.getTeamNum() == 1 ? pokemon2 : pokemon1;
            info += "\n\nOpposing pokemon %s is a %s with %d percent health remaining".formatted(opponent.getName(),
                    opponent.displayTypes(), opponent.getPercentHealth());


            String[] back = {"Back"}; // back to make a decision
            // show the window
            int choice = JOptionPane.showOptionDialog(null, info, "More Information",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, back, null);


            if (choice == 0) return playerTurn(pokemon, turns, team); // if the choose back, just call the method again -- recursion.
        }


        // wants to switch
        ArrayList<String> teamPokemon = new ArrayList<>(); // create an arraylist of strings
        // the player wants to switch
        teamPokemon.add("Back"); // add a back option so they are not locked into switching
        // make back the first option because we want it to appear as the last
        for(Individual poke : team) { // so go through the names of each pokemon in the team
            if(!pokemon.getName().equals(poke.getName())) teamPokemon.add(poke.getName()); // if the name is not the name of the pokemon battling, add to list.
        }


        Object[] optionsFinal = teamPokemon.toArray(); // convert to this so they can be displayed in a new pane.

        // create the pane and get the response.
        // show the name of the pokemon they're switching out

        int switchPokemon = JOptionPane.showOptionDialog(null, "Pokemon Out: " + pokemon.getName(), "Switch",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, optionsFinal, null);

        // if they choose back, just call the method again -- recursion.
        // we need to check it like this, because the index of back changes based on how many pokemon are alive
        if (optionsFinal[switchPokemon].equals("Back")) return playerTurn(pokemon, turns, team);
        String name = (String) optionsFinal[switchPokemon];
        for(Individual poke : team) { // go through the names of each pokemon in the team
            if(name.equals(poke.getName())) return poke; // if there is a match, return the pokemon.
        }
        return null;
    }



}
