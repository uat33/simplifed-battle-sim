package driverPackage;
// package everything so its easier to organize. 

/*
 * This program is a pokemon battle simulator, for the 5th generation of pokemon. 
 * There is quite a bit of simplification done, otherwise this would take way longer to make. 
 * Here is all the simplification. 
 * 
 * 1. No items or abilites. If you add a new text file to the teamFiles, these must be removed.
 * 2. IVs are automatically assumed to be 31. this must also be removed if you add a new file.
 * 3. The most difficult part are status moves. there are literally hundreds, and many have no connection with each other.
 * 	  As a result, it would take a lot of time to implement every status move.
 * 	  So the only status moves present in this game, are ones that raise your stats and lower the opponent's stats. 
 * 
 * Teams are taken randomly from text files. There are only 5 currently, but more could be added easily. 
 * 
 * 
 * Note: there are a lot of text files and classes and stuff and i lose track of things really easily.
 * so i seperated everything into different packages and folders even if it wasn't always necessary.
 * i use file paths to access most of the stuff
 * This game requires two people to play against each other. 
 * 
 */

import java.io.FileNotFoundException;
import battle.Battle;
import setup.Setup;

public class Driver {

	
	
	public static void main(String[] args) throws FileNotFoundException, InterruptedException{

		
		System.out.println("Welcome to the Pokemon Battle Simulator!\nThis program's purpose is"
				+ " to simulate a simplifed version of pokemon battles (up to date until the 5th generation)."
				+ "\nIf you don't play pokemon, you won't notice the simplification.\nSee the documentation for the simplification."
				+ "\nThe program gives each player a random team."
				+ "\nThey battle each other."
				+ "\nOnce all of a player's pokemon have fainted, the battle is over.");
		
		System.out.println(); // skip lines
		System.out.println(); // skip lines

		Setup set = new Setup(); // just create the setup object so all the setup is done in that class

		Battle battle = new Battle(); // create a battle class, where the battle happens.
 
	}
	
}
