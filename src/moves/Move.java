package moves;

import types.Type;
import java.util.Random;

import pokemon.Individual;

/*
 * this is where the information for the move is stored. implements moveinterface but the class is abstract
 *  so some of the implemented methods can be defined later
 *  Attacking and status moves have many attributes and methods in commmon. 
 *  Do that all here.
 */

public abstract class Move implements MoveInterface{


	

	private String name; // its name
	private Type type; // its type

	private String category; // the type of move: physical, special or status


	private int pp; // number of power points (number of times it can be used). 
	// in a normal game you could heal to get pp back, but in competitive which this is supposed to simulate, that is not possible
	private int maxPP; // max number of pp for a move
	
	
	private String power; // amount of damage
	private String accuracy; // chance of it landing

	// if you think it would make more sense for the power and accuracy to be ints, you would be correct
	// but this doesn't work due to some details, the explanation is below if you care to read it.
	
	// status moves always deal no damage upfront and they are saved as - in the text file, so cannot be saved as ints
	// you could change it to 0 when adding it to the array list
	// however the damage is literally nothing, so that value is never used, so there isn't actually any point in doing the conversion
	
	// accuracy can't be saved as an int either because certain moves cannot miss no matter what. 
	// these moves are different from moves with a 100 accuracy because those can miss due to stat changes or special effects
	// those unmissable moves are also saved as - 
	// and once again we can't convert it
	// but this time, this is actually useful for us.
	// because we need a way to check that moves cannot miss.

	// so power and accuracy are strings





	// we can define some of those abstract methods here

	// all moves check for accuracy
	public boolean accuracyCheck(Individual user, Individual target) {
		ppUpdate(); // update pp. pp is removed regardless of whether a move hits or not.
		if(accuracy.equals("-")) {
			return true; // if this is the value for accuracy, it is a move that cannot miss
		}
		// so return true.

		Random rand = new Random(); // create a random object to get a new random number from 1 to 100
		if(rand.nextInt(100) + 1 <= Integer.parseInt(accuracy) * user.getAccuracy() * target.getEvasion()) { 
			return true; // if that number is less than or equal to accuracy, it hits
		}
		// we multiply by the user's accuracy and evasion, using the formula to take those stat changes into account.
		
		return false; // if we get here, it must miss.

	}


	// removes one power point
	public void ppUpdate() {
		this.pp -= 1;
	}
	

	// constructor. a lot is the same for attacking and status moves, so just do all that here.
	public Move(String name, Type type, String category, int pp, String power, String accuracy) {
		this.name = name;
		this.type = type;
		this.category = category;
		this.pp = pp;
		this.maxPP = pp; // when the move is created, maxpp and pp are the same
		this.power = power;
		this.accuracy = accuracy;
	}


	// getters and setters and the toString
	// some setters are removed because they will never be used

	public String getName() {
		return name;
	}

	public Type getType() {
		return type;
	}

	public String getCategory() {
		return category;
	}

	public int getPp() {
		return pp;
	}


	public String getPower() {
		return power;
	}


	public String getAccuracy() {
		return accuracy;
	}

	
	// i have two seperate methods here. for two different things. 
	
	// the toString is for when the user decides the move. there is a lot of information so we can't output everything here. 
	// the name and the pp are the standard in pokemon games, because pokemon players just know everything else based off of the name
	
	// the getMove is for if the user asks for more information, in which case we tell them everything they could want to know
	
	// the toString
	@Override
	public String toString() {
		return name + " (" + pp + "/" +  maxPP + ")";
		
	}


	public String getMove() { // this is used to get information on the move.
		return name + ": type=" + type + ", category=" + category + ", pp=" + pp + ", power=" + power
				+ ", accuracy=" + accuracy;
	}

}
