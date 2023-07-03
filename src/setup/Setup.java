package setup;/*
 * This class is where all the setup is done.
 * This setup includes the pokemon, moves and type match-ups.
 */


import moves.Attack;
import moves.Move;
import moves.Status;
import pokemon.Individual;
import type.Type;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


/*
* This class is where all the setup to make the battle possible happens.
*
* */
public class Setup {



    // these fields are all static as there should only be one per battle

    // create some constants so it's clear what these numbers mean
    final static int NUMBER_OF_TYPES = 17, NUMBER_OF_MOVES = 559, NUMBER_OF_POKEMON = 676, TEAM_SIZE = 6;

    // in gen 5, there are 17 types, each of which interact differently with each other
    // we can use an array as the size of this will never change
    private static final Type[] types = new Type[NUMBER_OF_TYPES];
    // make a similar array for moves and pokemon
    private static String[] moves = new String[NUMBER_OF_MOVES];
    private static String[] pokemon = new String[NUMBER_OF_POKEMON];
    public static ArrayList<Individual> team1 = new ArrayList<>(TEAM_SIZE); // contains the first players team
    public static ArrayList<Individual> team2 = new ArrayList<>(TEAM_SIZE); // contains the second players team



    public Setup() throws IOException {

        // create a constant for the number of team files we have so this can be easily changed if we add more files
        final int NUMBER_OF_TEAM_FILES = 5;


        // first set up the types
        typeSetup();

        // now setup the moves
        // instead of making an object for all 559 moves, can just make objects for the 48 moves we use
        // even if there are duplicates among the 48, still need to make separate objects
        // if separate objects are not made, then one pokemon using that move will reduce pp for all the pokemon using that move
        String allMoves = Files.readString(Path.of("textFiles/moves.txt")); // read in the moves
        moves = allMoves.split("\n"); // split into an array where each entry is a move
        // next sort that array in alphabetical order by the name of the move

        // make the comparator
        // there are some extra steps here, as the name of the move is the second entry in the file
        Comparator<String> moveComp = (String a, String b) -> {
            int start1 = a.indexOf("\t") + 1; // start of the move name
            int end1 = a.indexOf("\t", start1); // end of the move name
            int start2 = b.indexOf("\t") + 1; // start of the move name
            int end2 = b.indexOf("\t", start2); // end of the move name
            return a.substring(start1, end1).compareTo(b.substring(start2, end2));
        };
        // sorting it will make it faster to find our move -- we won't have to loop through up to 559 entries several times
        Arrays.sort(moves, moveComp);

        // we can do something similar for the pokemon
        // instead of looping through up to 676 entries several times, we can sort it
        String allPokemon = Files.readString(Path.of("textFiles/pokemon.txt"));
        pokemon = allPokemon.split("\n");

        Comparator<String> pokemonComp = (String a, String b) -> {
            int start1 = a.indexOf(",") + 1; // start of the pokemon name -- this file is separated by commas instead of tabs
            int end1 = a.indexOf(",", start1); // end of the pokemon name
            int start2 = b.indexOf(",") + 1; // start of the pokemon name
            int end2 = b.indexOf(",", start2); // end of the pokemon name
            return a.substring(start1, end1).compareTo(b.substring(start2, end2));
        };
        Arrays.sort(pokemon, pokemonComp); // the pokemon array is now sorted by the name of the pokemon


        // now that that's done, we can set up the teams.
        // we have a bunch of teams in a folder
        // we want different teams each time we need to choose two different teams randomly
        Random rand = new Random();  // random object
        // create the random numbers. add one cause the text files start at 1 instead of 0.
        int team1File = rand.nextInt(NUMBER_OF_TEAM_FILES) + 1, team2File = rand.nextInt(NUMBER_OF_TEAM_FILES) + 1;

        while (team1File == team2File) { // we want different teams so while those two variables are equal.
            team2File = rand.nextInt(NUMBER_OF_TEAM_FILES) + 1; // keep re-rolling the second one.
        }


        // add the new text file numbers to the file path
        String teamFile = "textFiles/teamFiles/team";
        // the second parameter is the team number
        // player 1's team number is 1, player 2's team number is 2
        team1 = teamSetup(new Scanner(new File(teamFile + team1File)), 1);
        team2 = teamSetup(new Scanner(new File(teamFile + team2File)), 2);

    }


    /*
    * This method sets up a pokemon team.
    *
    * Parameters:
    *   teamFile - the file containing out team
    *   teamNum - the player this team belongs to
    *
    * */

    public static ArrayList<Individual> teamSetup(Scanner teamFile, int teamNum) {
        ArrayList<Individual> team = new ArrayList<>(TEAM_SIZE); // create arraylist we will return

        while (teamFile.hasNextLine()) {
            String pokemonName = teamFile.nextLine(); // the pokemon's name will be the next line

            int[] evs = getEvs(teamFile.nextLine()); // get the evs from the method

            String nature = (teamFile.nextLine().split(" ")[0]); // get the nature


            Move[] fourMoves = getMoves(teamFile); // get the moves from the method


            // use binarysearch to get the index of the pokemon
            int index = Arrays.binarySearch(pokemon, pokemonName, (String a, String b) -> {
                // since all the pokemon info is there, not just the name, we have to separate out the name
                int start1 = a.indexOf(",") + 1;
                int end1 = a.indexOf(",", start1);
                return a.substring(start1, end1).compareTo(b);
            });

            String[] attributes = pokemon[index].split(","); // the attributes of the pokemon we are looking for


            int[] baseStats = new int[6]; // create empty array for base stats

            for (int i = 0; i < 6; i++) {
                // the base start at index 5 of attributes
                baseStats[i] = Integer.parseInt(attributes[i + 5]); // parse the base stats from the information array into ints and store them
            }


            ArrayList<Type> types = new ArrayList<>(2); // create an arraylist of type Type.
            // this will hold the types for this pokemon.
            // each pokemon can have one or two types.
            // since we don't necessarily know how many types, use an arraylist

            types.add(getTypeFromName(attributes[2])); // use the method to find the type object from its name
            if (attributes[3].length() > 0) { // if there is a second type
                types.add(getTypeFromName(attributes[3])); // add that as well.
            }
            // now we have everything
            // can create pokemon object

            Individual pokemon = new Individual(pokemonName, types, baseStats, nature, fourMoves, evs, teamNum); // create the object

            team.add(pokemon); // add it to the team

            teamFile.nextLine(); // skip a line b/w pokemon

        }


        return team; // return the team
    }


    /*
    * This method makes the four move objects that every pokemon will have.
    *
    * Parameter: Scanner inFile, the teamfile for which we are creating the pokemon
    *
    *
    * */
    public static Move[] getMoves(Scanner inFile) {


        Move[] fourMoves = new Move[4]; // create an array that will have the moves
        for (int i = 0; i < 4; i++) {
            String moveName = inFile.nextLine().substring(2); // put each movename from the text file into a string

            // find the movename in the array using Arrays.binarySearch
            // use a lambda expression for the comparator
            int index = Arrays.binarySearch(moves, moveName, (String a, String b) -> {
                // needed because all the move info is in the moves array, not just the name
                int start1 = a.indexOf("\t") + 1;
                int end1 = a.indexOf("\t", start1);
                return a.substring(start1, end1).compareTo(b);
            });

            String[] components = moves[index].split("\t"); // split line into array of components
            if (components[3].equals("Status")) { // move is a non-attacking move.
                // take appropriate action
                // create status move object
                Status status = new Status(components[1], getTypeFromName(components[2]), components[3], Integer.parseInt(components[4]), components[5], components[6]);
                fourMoves[i] = status; // add to array


            } else { // attacking move.
                // appropriate action
                // create attack move object
                Attack attack = new Attack(components[1], getTypeFromName(components[2]), components[3], Integer.parseInt(components[4]), components[5], components[6]);
                fourMoves[i] = attack; // add to array
            }

        }
        return fourMoves; // return the array
    }



    /*
    *
    *
    * this method gets the evs from the text file and puts it into an array
    * evs are to calculate stats.
    * if unspecified, the value is always 0.
    *
    *
    * Parameter:
    *   - evLine, which is the line every pokemon has in the textfile specifying what its evs are
    *
    * */
    public static int[] getEvs(String evLine) { // all the ev values will be in one line
        String[] stats = {"HP", "Atk", "Def", "SpA", "SpD", "Spe"}; // these are the Strings used in the teamFiles to specify which stat has which ev
        int[] evs = new int[6]; // create the array
        for (int i = 0; i < 6; i++) { // loop through 6 times, once for each stat
            int index = evLine.indexOf(stats[i]); // get the index of the stat string we're on
            if (index > 0) { // if it's there we know there's an ev value to add
                // the rest is just understanding the format of the text files, which is universal to pokemon on the internet.
                int end = evLine.lastIndexOf(' ', index);
                int start = evLine.lastIndexOf(' ', end - 1);
                int evVal = Integer.parseInt(evLine.substring(start + 1, end));
                // get the value, parse it into an int and set as the ev value
                evs[i] = evVal;
            } else { // if it's not, we know the ev value for this stat is 0
                evs[i] = 0;
            }


        }
        return evs; // return the evs

    }


    /*
    * get the type object in the types array from a name
    * Parameter:
    *    name - the name of the type whose object we are looking for.
    *
    * */
    public static Type getTypeFromName(String name) {
        for (Type t : types) {
            if (t.getTypeName().equals(name)) return t;
        }
        return null;
    }

    /*
     * This method removes a pokemon from the game after it has been knocked out
     * Parameters:
     *   target - the pokemon that has been knocked out
     *
     * */
    public static void removePokemon(Individual target) {

        // if the pokemon is part of team1, remove from team1
        // otherwise its in team 2 and do the same thing there
        if (target.getTeamNum() == 1) team1.remove(target);
        else team2.remove(target);
    }


    /*
    *
    * Sets up the types array
    *
    * does this by reading in the typematchups file, and calling the type constructor for each line
    *
    * */

    private void typeSetup() throws FileNotFoundException {
        // read in the file with the type matchups
        Scanner in = new Scanner(new File("textFiles/typeMatchups.txt"));
        // start at index 0 of the types array
        int index = 0;

        while (in.hasNextLine()) {
            // for each line, call the type constructor
            // post-increment
            types[index++] = new Type(in.nextLine());
        }
    }

}
