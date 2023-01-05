package pokemon;

import java.util.ArrayList;

import moves.Move;
import types.Type;


/*
 * Pokemon abstract class. implements pokemonInterface because we want those values. 
 * Here is where a lot of the setup is done as we can have a lot of info to be defined at a later instance. 
 * The reason this exists, is that if you expand the number of teams, eventually you are going to see tons and tons of pokemon of the same species.
 * In fact, there are already a couple. 
 * This is important, because pokemon of the same species share a lot of attributes, such as name, types, baseStats and etc.
 * But they can also have a lot of differences, like actualStats, nature and moves. 
 */
public abstract class Pokemon implements PokemonInterface{
	// this is the parent class for all the pokemon in the battle

	private String name; // the pokemons name 

	private ArrayList<Type> types; // the types of the pokemon. 

	// base stats are universal to all pokemon of the same species.
	private int[] baseStats;
	// natures. there are 25 of them. each one raises a stat and decreases one of the five stats (hp is excluded)
	// this change is 10 percent.
	private String nature;
	public Move[] moves; // the actual moves the pokemon will have. this needs to be public because the getter is used for something else and the field is a custom type.

	// just do the constructor here, so we don't have to do it later. 
	public Pokemon(String name, ArrayList<Type> types, int[] baseStats, String nature, Move[] moves) {
		this.name = name;
		this.types = types;
		this.baseStats = baseStats;
		this.nature = nature;
		this.moves = moves;
	}


	// we're going to make a custom get moves method
	// the default one can't be understood due to moves being of type Type. thats kind of confusing. 
	// we can't just use moves.toString() cause there are four of them. so we have to add those together.
	// we want this method to give extra information on the moves.
	
	// some setters are not there, because they will never be used
	public String getMoves() { 

		String moveString = "";
		for(int i = 0; i < moves.length; i++) { 
			
		
			moveString += (moves[i]).getMove() + "\n";
		}
		return moveString; // return the string 

	}



	public String getName() {
		return name;
	}


	public int[] getBaseStats() {
		return baseStats;
	}
	
	public String getNature() {
		return nature;
	}


	public void setMoves(Move[] moves) {
		this.moves = moves;
	}

	public ArrayList<Type> getTypes() {
		return types;
	}
	
	public String displayTypes() {  // we want to be able to call this method, and return the type's of the pokemon in a readable manner.
		String typeText = "";
		
		if(types.size() == 1) {
			typeText += types.get(0).getTypeName() + " Type";
			return typeText;
		}
		
		typeText += types.get(0).getTypeName() + " and " + types.get(1).getTypeName() + " Type";
		return typeText;
		
	}

	public String toString() { // when we call this, we want all the info the user could want on the pokemon displayed in a readable way
		// we don't need to do the moves, that's taken care of elsewhere
		return "Name: " + name + "\nNature: " + nature + "\n" + displayTypes(); 
		
	}


}
