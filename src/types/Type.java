package types;

import java.util.ArrayList;

public class Type {



	private String typeName = "";

	private double[] matchup = new double[17]; // an attribute. this is an array of 17 doubles. 
	// each double represents how this type fares against another type, including itself. 
	// possible values are 0, 0.5, 1, and 2.



	// constructor recieves the name of the type
	// and the matchups
	// using those we can make the types. 
	public Type(String typeName, double[] matchup) {
		super();
		this.typeName = typeName;
		this.matchup = matchup;
	}





	// the matchups are 17 numbers and have no context. so it's not really useful to include it in the to string. 
	@Override
	public String toString() {
		return typeName;
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


	// do the getters, setters are never gonna be used
	
	public String getTypeName() {
		return typeName;
	}
	

	public double[] getMatchup() {
		return matchup;
	}





}