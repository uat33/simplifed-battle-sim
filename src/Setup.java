/*
 * This class is where all the setup is done.
 * This setup includes the pokemon, moves and type match-ups.
 */


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Setup {

	// in gen 5, there are 17 types, each of which interact differently with each other
	// we can use an array as the size of this will never change
	// static is also good as there should only be one per battle
	private static final Type[] types = new Type[17];

	public Setup() throws FileNotFoundException {

		// first set up the types
		typeSetup();



	}

	private void typeSetup() throws FileNotFoundException {
		Scanner in = new Scanner(new File("../textFiles/typeMatchups.txt"));
		int index = 0;
		while (in.hasNextLine()){
			types[index++] = new Type(in.nextLine());
		}

		System.out.println(Arrays.toString(types));

	}




}
