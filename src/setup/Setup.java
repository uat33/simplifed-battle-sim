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

public class Setup {

    // in gen 5, there are 17 types, each of which interact differently with each other
    // we can use an array as the size of this will never change

    // these fields are all static as there should only be one per battle
    private static final Type[] types = new Type[17];
    private static String[] moves = new String[559];
    public static ArrayList<Individual> team1 = new ArrayList<>(); // contains the first players team
    public static ArrayList<Individual> team2 = new ArrayList<>(); // contains the second players team


    // a special move that we want to  be able to access from the battle class

    public Setup() throws IOException {

        // first set up the types
        typeSetup();

        // now setup the moves
        // instead of making an object for all 559 moves
        // we can just make objects for the 48 moves we use
        // even if there are duplicates among the 48, we still need to make separate objects
        // if we don't, the pp will be the same for all pokemon with that move
        String l = Files.readString(Path.of("textFiles/moves.txt")); // read in the moves
        moves = l.split("\n"); // split into an array where each entry is a move
        // sort that array in alphabetical order by the name of the move
        Comparator<String> comp = (String a, String b) -> {
            int start1 = a.indexOf("\t") + 1; // start of the move name
            int end1 = a.indexOf("\t", start1); // end of the move name
            int start2 = b.indexOf("\t") + 1; // start of the move name
            int end2 = b.indexOf("\t", start2); // end of the move name
            return a.substring(start1, end1).compareTo(b.substring(start2, end2));
        };

        Arrays.sort(moves, comp);
        // sorting it will make it faster to find our move -- we won't have to loop through up to 559 entries several times



        // now that that's done, we can set up the teams.
        // we have a bunch of teams in a folder
        // we want different teams each time we need to choose two different teams randomly
        Random rand = new Random();  // random object
        // create the random numbers. add one cause the text files start at 1 instead of 0.
        int team1File = rand.nextInt(5) + 1, team2File = rand.nextInt(5) + 1;

        while (team1File == team2File) { // we want different teams so while those two variables are different.
            team2File = rand.nextInt(5) + 1; // keep rerolling the second one.
        }


        // add the new text file numbers to the file path
        String teamFile = "textFiles/teamFiles/team";

        team1 = teamSetup(new Scanner(new File(teamFile + team1File)), 1);
        team2 = teamSetup(new Scanner(new File(teamFile + team2File)), 2);

    }


    // now to get the pokemon info.
    // this info is taken from a text file that contains the name, types, stats, etc. for every pokemon from gen 1-5
    // we use this to create our objects
    public static String[] getPokemonInfo(Scanner inFile, String name) {
        String[] information = new String[8];
        // 8 pieces of information taken from the text file. not the teams text file, but the text file with every pokemon until generation 5
        // the purpose is to use that text file to get the type and other necessary info
        // name is the pokemon from the teams file that we're searching for

        while (inFile.hasNextLine()) { // go through the file
            String[] attributes = inFile.nextLine().split(","); // break the line into array
            if (attributes[1].equals(name)) { // if these two are equivalent, we've found the pokemon we're looking for

                // the first six values match with what we need
                System.arraycopy(attributes, 5, information, 0, 6);
                information[6] = attributes[2]; // order's a bit weird, but this is where it goes
                information[7] = attributes[3];


                return information; // return this information
            }


        }
        return information;
    }



    public static Move[] getMoves(Scanner inFile) {


        Move[] fourMoves = new Move[4]; // create an array that will have the moves
        for (int i = 0; i < 4; i++) {
            String moveName = inFile.nextLine().substring(2); // put each movename from the text file into a string

            // find the movename in the array using Arrays.binarySearch
            // use a lambda expression for the comparator, needed because all the move info is in the moves array, not just the name
            int index = Arrays.binarySearch(moves, moveName, (String a, String b) -> {
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


    public static ArrayList<Individual> teamSetup(Scanner teamFile, int teamNum) throws IOException {
        ArrayList<Individual> team = new ArrayList<>(); // create arraylist we will return

        while (teamFile.hasNextLine()) {
            Scanner pokeFile = new Scanner(new File("textFiles/pokemon.txt")); // we need to remake the text file so we can start at the top to find each pokemon
            String name = teamFile.nextLine(); // the pokemon's name will be the next line

            int[] evs = getEvs(teamFile.nextLine()); // get the evs from the method

            String nature = (teamFile.nextLine().split(" ")[0]); // get the nature


            Move[] fourMoves = getMoves(teamFile); // get the moves from the method


            String[] info = getPokemonInfo(pokeFile, name); // get the info from the method

            int[] baseStats = new int[6]; // create empty array for base stats

            for (int i = 0; i < 6; i++) {
                baseStats[i] = Integer.parseInt(info[i]); // use info values to parse into int and store them
            }


            ArrayList<Type> types = new ArrayList<>(); // create an arraylist of type Type.
            // this will hold the types for this pokemon.
            // each pokemon can have one or two types.
            // since we don't necessarily know how many types, use an arraylist

            types.add(getTypeFromName(info[6])); // use the method to find the type object from its name
            if (info[7].length() > 0) { // if there is a second type
                types.add(getTypeFromName(info[7])); // add that as well.
            }
            // now we have everything
            // can create pokemon object


            Individual pokemon = new Individual(name, types, baseStats, nature, fourMoves, evs, teamNum); // create the object

            team.add(pokemon); // add it to the team

            teamFile.nextLine(); // skip a line b/w pokemon

        }


        return team; // return the team
    }


    // this method gets the evs from the text file and puts it into an array
    // evs are to calculate stats.
    // if unspecified, the value is always 0.
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


    public static Type getTypeFromName(String name) {
        // get the type object in the types array from a name

        for (Type t : types) {
            if (t.getTypeName().equals(name)) return t;
        }
        return null;
    }


    public static void removePokemon(Individual target) { // the pokemon to be removed, and the team it needs to be removed from.

        // if the pokemon is part of team1, remove from team1
        // otherwise its in team 2 and do the same thing there
        if (target.getTeamNum() == 1) team1.remove(target);
        else team2.remove(target);

    }


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
