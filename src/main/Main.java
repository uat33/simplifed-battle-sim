package main;// package everything so it is easier to organize.

/*
 * This program is a pokemon battle simulator, for the 5th generation of pokemon.
 * There is some simplification done.
 * See the README for more info regarding this.
 *

 */

import battle.Battle;
import setup.Setup;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException{

        System.out.println("""
                Welcome to the Pokemon Battle Simulator!
                This game is intended to be played by two people.
                Each player will be given a random team, and will battle each other until one player is out of Pokemon.
                
                Players will take turns choosing what to do, there will be 3 seconds in between turns for players to pass the computer
                to the other player.
                
                Players should be reading the console and the JavaFX window for all necessary information.
                
                The JavaFX window is there to keep all information about the player's team that the other player should not be able to see.
                """);

        Setup set = new Setup(); // create the setup object, all the setup is done in that class

        Battle battle = new Battle(); // create a battle class, where the battle happens.

    }

}

