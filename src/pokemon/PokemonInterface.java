package pokemon;

/*
 * this interface contains everything that is common to all pokemon in this simulation. Not a lot, but its something
 * 
 */
public interface PokemonInterface {

	// all variables common to all pokemon that will not be changed go here

	public int level = 100; // the level of the pokemon. in battles like this always 100
	public int[] ivs = {31, 31, 31, 31, 31, 31}; // almost always like this, in this simplification, it is always like this

	public double statChangesOne[] = {1/4, 1/3.5, 1/3, 1/2.5, 1/2, 1/1.5, 1, 1.5, 2, 2.5, 3, 3.5, 4}; // these are the multipliers for attack - speed 
	public double statChangesTwo[] = {1/3, 1/(8/3), 1/(7/3), 1/2, 3/5, 3/4, 1, 4/3, 5/3, 2, 7/3, 8/3, 3}; // accuracy and evasion use different multipliers
	
	

}
