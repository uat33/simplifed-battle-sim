package type;

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

		return typeName;

	}
}
