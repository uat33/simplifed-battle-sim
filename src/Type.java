import java.util.ArrayList;
import java.util.HashMap;


/*
*
* The type class.
*
* Each of the 17 types will have objects of this class.
*
*
*
* */

public class Type {




	// the name of the type e.g. Fire, Water
	private final String typeName;

	// a hashmap representing how this type matches up against all the other type.
	// the key will be the name of that type
	// the double is the multiplier this type has against that type.
	// possible values are 0, 0.5, 1, and 2.
	private final HashMap<String, Double> matchup; //


	// this is so we know what order the array of match-ups come in
	private final String[] typeNames = {"Normal", "Fire", "Water",
	"Electric", "Grass", "Ice", "Fighting", "Poison", "Ground", "Flying",
	"Psychic", "Bug", "Rock", "Ghost", "Dragon", "Dark", "Steel"};

	// constructor recieves the name of the type
	// and the line in the textfile. using that line, we will make the matchups
	// using those we can make the types.
	public Type(String line) {
		// turn the line into an array of its components
		// the array will have 18 values, the first of which is the type name
		// the second will be how that type matches up against all the other types.
		String[] components = line.split("\t");

		this.typeName = components[0];
		this.matchup = matchups(components);
	}

	public HashMap<String, Double> matchups(String[] line){
		HashMap<String, Double> m = new HashMap<>();

		for (int i = 1; i < line.length; i++){
			m.put(typeNames[i - 1], Double.parseDouble(line[i]));
		}

		return m;
	}


	// do the getters, we don't need setters

	public String getTypeName() {
		return typeName;
	}

	public HashMap<String, Double> getMatchup() {
		return matchup;
	}



	// the match-ups are 17 numbers and have no context. so it's not really useful to include it in the to string.
	@Override
	public String toString() {

		return typeName + matchup.toString();

	}












	// this is a method that recieves the arraylist we make in the driver that contains all the types and a string
	// the string contains a type name such as fire, water etc.
	// the method matches the type name to the corresponding object in the list of types
	// this is so that we can find how the types interact.
	// static so we don't need to use the types object to access it.
	public static Type getTypeFromName(ArrayList<Type> typeSetup, String givenType) {

		for(Type type : typeSetup) {

			if(type.typeName.equals(givenType)) { // if the type name attribute equals the given type
				return type; // return the type object
			}

		}


		return null;


	}

	// this method recieves the arraylist and the type
	// the purpose is to get the number at which the type is stored in the list
	// this is done because the list is constructed from the text file, which is actually a table
	// which means the vertical order is the same as the horizontal order
	// so if we know the number going down, we know the number the type appears going to the right.
	// this gives us the multiplier
	public static int getNumfromType(ArrayList<Type> typeSetup, String givenType) {
		for(int i = 0; i < typeSetup.size();i++) {
			if(typeSetup.get(i).typeName.equals(givenType)) {
				return i;
			}
		}

		return 0;

	}








}
