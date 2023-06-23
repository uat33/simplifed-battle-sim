// package everything so it is easier to organize.

/*
 * This program is a pokemon battle simulator, for the 5th generation of pokemon.
 * There is quite a bit of simplification done.
 * See the README for more info regarding this.
 *

 */

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, InterruptedException{

        System.out.println("""
                "Welcome to the Pokemon Battle Simulator!
                "This game is intended to be played by two people."
                "Each player will be given a random team, and will battle each other until one player is out of Pokemon.
                
                
                """);

        Setup set = new Setup(); // just create the setup object so all the setup is done in that class

//        Battle battle = new Battle(); // create a battle class, where the battle happens.

    }

}

