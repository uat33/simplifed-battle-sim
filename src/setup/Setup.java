package setup;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import moves.Attack;
import moves.Move;
import moves.Status;
import pokemon.Individual;
import types.Type;

/*
 * This class is where all the setup is done.
 * We need four array lists which contain info taken from text files.
 * This is done here because those arraylists need to be easily accessible from other classes
 * Instead of doing it in the driver, we can make them static fields separate from the rest.
 * Much easier to keep track of.
 */


public class Setup {
	
	// these can be static cause there is only meant to be one per battle
	// last three must be public because we need to access from other classes
	// the moves can be private

	private static ArrayList<Move> moves; // contains all possible moves pokemon use
	public static ArrayList<Type> types; // contains the 17 types and their matchups with each other
	public static ArrayList<Individual> team1; // contains the first players team
	public static ArrayList<Individual> team2; // contains the second players team

	public Setup() throws FileNotFoundException {
		// first set up the types. In gen 5, there were 17 types, each of which interact seperately with each other. 
		// this is done by creating a type class. Each type is an object of that class. 
		// each of those objects has the matchups with other types as an attribute. 
		// using a text file which contains the details of these matchups, and a method
		// we create an arraylist that contains all the types and their matchups as we will need this later. 
		types = typeSetup(new Scanner(new File("textFiles/typeMatchups.txt"))); // setup types
		// next set up the moves that the pokemon will use. 
		// this is actually where the bulk of the work is for reasons that will become clear shortly
		// this is the same as the type pretty much.
		// an arraylist where each item is a move object.
		// name this one movesFile to avoid confusion. 
		moves =  moveSetup(new Scanner(new File("textFiles/moves.txt")), types); // create the array list

		// now that that's done, we can set up the teams.
		// we have a bunch of teams in a folder
		// we want different teams each time we need to choose two different teams randomly 
		Random rand = new Random();  // random object


		// create the random numbers. add one cause the text files start at 1 instead of 0.
		int team1File = rand.nextInt(5) + 1, team2File = rand.nextInt(5) + 1;


		while(team1File == team2File) { // we want different teams so while those two variables are different.
			team2File = rand.nextInt(5) + 1; // keep rerolling the second one.
		}

		// add the new text file numbers to the file path

		String teamFile = "textFiles/teamFiles/team";


		// create the team as an arraylist of individuals
		// teamSetup method does this for us. it needs the file, the moves arraylist and the type arraylist

		team1 =  teamSetup(new Scanner(new File(teamFile+team1File)), moves, types, 1);
		team2 = teamSetup(new Scanner(new File(teamFile+team2File)), moves, types, 2);
	}




	
	// when a pokemon dies, we just remove it from the team
	// when a team has no pokemon, we know the battle is over
	
	public static void removePokemon(Individual target, ArrayList<Individual> team) { // the pokemon to be removed, and the team it needs to be removed from.
		 
		for(int i = 0; i < team.size(); i++) { // for each pokemon in the team
			if (team.get(i).getName().equals(target.getName())) { // if the target name is found
				team.remove(i); // remove it
			}
		}

		// if the pokemon is part of team1, set team1 equal to the new team
		// otherwise its in team 2 and do the same thing there
		if (target.getTeamNum() == 1) team1 = team;
		else team2 = team;
		
	}
	
	

	// now to get the pokemon info.
	// this info is taken from a text file that contains the name, types, stats, etc. for every pokemon from gen 1-5
	// we use this to create our objects  
	public static String[] getPokemonInfo(Scanner inFile, String name) {
		String[] information = new String[8];
		// 8 pieces of information taken from the text file. not the teams text file, but the text file with every pokemon until generation 5
		// the purpose is to use that text file to get the type and other necessary info
		// name is the pokemon from the teams file that we're searching for

		while(inFile.hasNextLine()) { // go through the file
			String[] attributes = inFile.nextLine().split(","); // break the line into array
			if(attributes[1].equals(name)) { // if these two are equivalent, we've found the pokemon we're looking for

				for(int i = 0; i < 6; i++) { // the first six values match with what we need
					information[i] = attributes[i+5]; // so just add it in a loop 
				}
				information[6] = attributes[2]; // order's a bit weird, but this is where it goes
				information[7] = attributes[3];


				return information; // return this information
			}


		}
		return information;
	}

	public static Move[] getMoves(Scanner inFile) {

		Move[] fourMoves = new Move[4]; // create an array that will have the moves
		for(int i = 0; i < 4; i++) {
			String moveName = inFile.nextLine().substring(2); // put each movename from the text file into a string


			for(Move move : moves) { // go through the list of moves to find that move


				if(move.getName().equals(moveName)) { // if it is
					fourMoves[i] = move; // set the array value to the move
					break; // we found it so we can end this iteration
				}
			}

		}
		return fourMoves; // return the array
	}







	// this method gets the evs from the text file and puts it into an array
	// evs are to calculate stats.
	// if unspecified, the value is always 0.
	public static int[] getEvs(String evLine) { // all the ev values will be in one line
		String[] stats = {"HP", "Atk", "Def", "SpA", "SpD", "Spe"}; // these are the Strings used in the teamFiles to specify which stat has which ev 
		int[] evs = new int[6]; // create the array
		for(int i = 0; i < 6; i++) { // loop through 6 times, once for each stat
			int index = evLine.indexOf(stats[i]); // get the index of the stat string we're on
			if(index > 0) { // if it's there we know there's an ev value to add
				// the rest is just understanding the format of the text files, which is universal to pokemon on the internet.
				int end = evLine.lastIndexOf(' ', index);
				int start = evLine.lastIndexOf(' ', end-1);
				int evVal = Integer.parseInt(evLine.substring(start+1, end));
				// get the value, parse it into an int and set as the ev value
				evs[i] = evVal;
			}
			else { // if it's not, we know the ev value for this stat is 0
				evs[i] = 0;
			}


		}
		return evs; // return the evs

	}
	public static ArrayList<Individual> teamSetup(Scanner teamFile, ArrayList<Move> moves, ArrayList<Type> typeSetup, int teamNum) throws FileNotFoundException{
		ArrayList<Individual> team = new ArrayList<Individual>(); // create arraylist we will return

		/*
		 * Note: Pokemon text files use a universal format, so this will work with any text file. 
		 * However certain things need to be taken into account
		 * 1. this version of the game doesn't have items or abilities. so those need to be removed from the text file.
		 * 2. ivs are almost always all 31. in rare instances this is not the case.
		 * for simplicity, this program makes all ivs 31. if a text file specifies that an iv is not 31, that line must be removed
		 * 3. there have to be two blank lines at the end of the text file.
		 * 4. there also needs to be one line of space between each pokemon. this is always done by default, but just in case.
		 * 
		 * If everything was done correctly, there will always be exactly 49 lines in the text file
		 */

		while(teamFile.hasNextLine()) {
			Scanner pokeFile = new Scanner(new File("textFiles/pokemon.txt")); // we need to remake the text file so we can start at the top to find each pokemon
			String name = teamFile.nextLine(); // the pokemon's name will be the next line

			int[] evs = getEvs(teamFile.nextLine()); // get the evs from the method

			String nature = (teamFile.nextLine().split(" ")[0]); // get the nature


			Move[] fourMoves = getMoves(teamFile); // get the moves from the method



			String[] info = getPokemonInfo(pokeFile, name); // get the info from the method

			int [] baseStats = new int[6]; // create empty array for base stats

			for(int i = 0; i < 6; i++) {
				baseStats[i] = Integer.parseInt(info[i]); // use info values to parse into int and store them
			}


			ArrayList<Type> types = new ArrayList<Type>(); // create an arraylist of type type.
			// this will hold the types for this pokemon. 

			types.add(Type.getTypeFromName(typeSetup, info[6])); // use the method to find the type object from its name
			if(info[7].length() > 0) { // if there is a second type
				types.add(Type.getTypeFromName(typeSetup, info[7])); // add that as well.
			}
			// now we have everything
			// can create pokemon object


			Individual pokemon = new Individual(name, types, baseStats, nature, fourMoves, evs, teamNum); // create the object

			team.add(pokemon); // add it to th team

			teamFile.nextLine(); // skip a line b/w pokemon

		}


		return team; // return the team
	}


	public static ArrayList<Move> moveSetup(Scanner inFile, ArrayList<Type> typeSetup){ // recieves textfile with all moves, and the arraylist of types
		// create the arraylist of all the moves. 


		ArrayList<Move> moves = new ArrayList<Move>(); 

		while(inFile.hasNextLine()) { // for each line in text file
			String[] components = inFile.nextLine().split("\t"); // split line into array
			if(components[3].equals("Status")){ // move is a non-attacking move. 
				// take appopriate action
				// create status move object
				Status status = new Status(components[1], Type.getTypeFromName(typeSetup, components[2]), components[3], Integer.parseInt(components[4]), components[5], components[6]);
				moves.add(status); // add to array


			}
			else{ // attacking move. 
				// appropriate action
				// create attack move object
				Attack attack = new Attack(components[1], Type.getTypeFromName(typeSetup, components[2]), components[3], Integer.parseInt(components[4]), components[5], components[6]);
				moves.add(attack); // add to array
			}
		}


		return moves; // return

	}


	public static ArrayList<Type> typeSetup(Scanner inFile) { // recieves a textfile with all the type matchups

		ArrayList<Type> typeSetup = new ArrayList<Type>(); // create the array we will return


		while(inFile.hasNextLine()) { // go through each of the lines.

			String[] components = inFile.nextLine().split("\t"); // break the line into an array of strings. 
			// this array has 18 values. the first is the type name. the next 17 are the matchups

			double[] multipliers = new double[17]; // create the array called multipliers because that is essentially what they are.

			for(int i = 1; i < components.length; i++) { // start at the first element and go till the end.
				multipliers[i-1] = Double.parseDouble(components[i]); // parse the element into a double. put that into multipliers.
			}

			Type type = new Type(components[0], multipliers); // create the object with the string name which is the first component. and then the multipliers.

			typeSetup.add(type); // add the object to the arraylist

		}


		return typeSetup; // return the arraylist
	}
}
