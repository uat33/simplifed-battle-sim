package pokemon;

import java.util.ArrayList;

import moves.Move;
import types.Type;


/*
 * subclass of pokemon. 
 * this is where the actual pokemon is created
 */

public class Individual extends Pokemon{

	private int evs[];
	private int accuracy = 100, evasion = 100;
	private int stats[];
	private int maxHP;
	// stats can be increased or decreased throughout a battle. the default multiplier is one, we can alter it here.
	// we need an array for this because we need to keep track of alterations, because alterations disappear if you switch out your pokemon

	// we need a way to know what is happening to the pokemon
	// we can use an int for that with my own system. 


	private int teamNum; // keep track of which team this pokemon belongs to. 1 for player 1, 2 for player 2






	private int[] statCodes = {6, 6, 6, 6, 6, 6, 6}; // the default multiplier is one, and we want this to be the index so 6's across the board for now
	// we need 7 actually.
	// the first 5 are attack - speed. 6 and 7 are accuracy and evasion, which for the purposes of alteration work the same as the other stats.
	private double natureCodes[] = {1, 1, 1, 1, 1};

	// take constructor from super class.
	public Individual(String name, ArrayList<Type> types, int[] baseStats, String nature, Move[] moves, int evs[], int teamNum) {

		super(name, types, baseStats, nature, moves);
		this.evs = evs;

		// except for stats because we can't calculate that until here

		// this part requires some explanation. 
		// natures do not affect hp, only the other five stats.
		// the default multiplier would be 1.
		// a nature that increases a stat results in a multiplier of 1.1, a decrease results in .9.
		// a fifth of natures increase and decrease the same stat and thus do nothing.
		// natures are always given as a word that corresponds to the changes.
		// adamant increases attack, lowers special attack etc.
		// an experienced human would just know which nature corresponds to which changes.
		// but the computer doesn't unless we tell it

		// use the method natures while throwing in the array we just made and the nature to make the necessary changes to the array
		// the appropriate multiplier will now be used.
		this.natureCodes = natures(natureCodes, nature);

		this.stats = statCalcs(baseStats, ivs, evs, level, natureCodes, statCodes, statChangesOne, statChangesTwo, accuracy, evasion);
		this.maxHP = stats[0]; // keep track of max hp so we can tell players what it is.
		this.teamNum = teamNum;
	}




	// here's a method to calculate the stats.
	// basically we take the basestats, ivs, evs, level and the nature code, throw it into a formula and call it a day.
	public static int[] statCalcs(int[] baseStats, int[] ivs, int[] evs, int level, double[] natureCodes, 
			int[] statCodes, double statChangesOne[], double statChangesTwo[], int accuracy, int evasion) {
		int stats[] = new int[6]; // array to hold the 6 stats
		// hp uses a different formula than the other 5

		stats[0] = (level * (2 * baseStats[0] + ivs[0] + evs[0] / 4)) / 100 + level + 10;

		for(int i = 1; i < 6; i++) { // can do the other five like this
			int stat = (int) (((int)(0.01 * (2 * baseStats[i] + ivs[i] + (int)(0.25 * evs[i])) * level) + 5) * natureCodes[i-1]);
			stats[i] = (int)(stat * statChangesOne[statCodes[i-1]] + .5);
		}


		accuracy *= statChangesTwo[statCodes[5]];
		evasion *= statChangesTwo[statCodes[6]];


		return stats; // return it
	}


	// getters and setters

	public int getTeamNum() {
		return teamNum;
	}

	public int getAccuracy() {
		return accuracy;
	}


	public void setAccuracy(int accuracy) {
		this.accuracy = accuracy;
	}

	public int getEvasion() {
		return evasion;
	}

	public void setEvasion(int evasion) {
		this.evasion = evasion;
	}




	public int[] getStats() {
		return stats;
	}


	public void setStats(int[] stats) {
		this.stats = stats;
	}





	public int[] getStatCodes() {
		return statCodes;
	}




	public void setStatCodes(int[] statCodes) { // just setting the statcodes does nothing

		this.statCodes = statCodes;
		// we also need to recalculate stats when this happens
		this.stats = statCalcs(getStats(), ivs, evs, level, natureCodes, statCodes, statChangesOne, statChangesTwo, accuracy, evasion);

	}





	public String toString() { // when we call this, we want all the info the user could want on the pokemon displayed in a readable way
		// we do this part here instead of in the abstract class, because this info doesn't exist in the abstract class
		return super.toString() + "\nAttack: " + stats[1] + "\nDefense: " + stats[2] 
				+ "\nSpecial Attack: " + stats[3] + "\nSpecial Defense: " + stats[4] 
				+ "\nSpeed: " + stats[5];
	}




	public void pokemonStatus() { // inform players the status of this pokemon
		System.out.println(); // skip lines
		System.out.println(getName() + ", Health Remaining: " + stats[0] + "/" + maxHP);


	}




	// when a pokemon is switched out, statCodes reset to the default value, 6
	public void switchPokemonOut() {

		for(int i = 0; i < statCodes.length; i++) {
			statCodes[i] = 6;
		}

		System.out.println(getName() + " is switched out."); // let the players know what's happening
	}

	public void switchPokemonIn() { // this needs to run when the pokemon is sent in.
		System.out.println("Player " + teamNum + " sends out " + getName() + '.'); // let the players know what's happening
	}

	public boolean isAlive() { // pretty self-explanatory. if the hp is more than 0 pokemon is alive, return it. if not return that.
		if(stats[0] > 0) return true;

		return false;

	}

	public static double[] natures(double[] natureCodes, String nature) {
		// here's the method to change the multiplier based on the nature
		// we're comparing one string with 20 so switch statements are easier.

		// if it matches one of these cases, change the increased multiplier to 1.1 and the decreased to .9
		// we don't even need a default case because the default case is that there are no matches to these 20 strings
		// which would mean it is one of the 5 natures that don't change anything
		// natures that do nothing are never seen competitively, but they exist so have to take them into account
		// the default case is to do nothing, so just return the array after the last case.
		switch (nature) {
		case "Adamant": 
			natureCodes[0] = 1.1;
			natureCodes[2] = .9;
			break;
		case "Modest":
			natureCodes[2] = 1.1;
			natureCodes[0] = .9;
			break;
		case "Jolly":
			natureCodes[4] = 1.1;
			natureCodes[2] = .9;
			break;
		case "Naive":
			natureCodes[4] = 1.1;
			natureCodes[3] = .9;
			break;
		case "Lonely":
			natureCodes[0] = 1.1;
			natureCodes[1] = .9;
			break;
		case "Timid":
			natureCodes[4] = 1.1;
			natureCodes[0] = .9;
			break;
		case "Hasty":
			natureCodes[4] = 1.1;
			natureCodes[1] = .9;
			break;
		case "Naughty":
			natureCodes[0] = 1.1;
			natureCodes[3] = .9;
			break;
		case "Brave":
			natureCodes[0] = 1.1;
			natureCodes[4] = .9;
			break;
		case "Bold":
			natureCodes[1] = 1.1;
			natureCodes[0] = .9;
			break;
		case "Impish":
			natureCodes[1] = 1.1;
			natureCodes[2] = .9;
			break;
		case "Lax":
			natureCodes[1] = 1.1;
			natureCodes[3] = .9;
			break;
		case "Relaxed":
			natureCodes[1] = 1.1;
			natureCodes[4] = .9;
			break;
		case "Rash":
			natureCodes[2] = 1.1;
			natureCodes[3] = .9;
			break;
		case "Calm":
			natureCodes[3] = 1.1;
			natureCodes[0] = .9;
			break;
		case "Gentle":
			natureCodes[3] = 1.1;
			natureCodes[1] = .9;
			break;
		case "Sassy":
			natureCodes[3] = 1.1;
			natureCodes[4] = .9;
			break;
		case "Careful":
			natureCodes[0] = 1.1;
			natureCodes[2] = .9;
			break;
		case "Mild":
			natureCodes[2] = 1.1;
			natureCodes[1] = .9;
			break;
		case "Quiet":
			natureCodes[2] = 1.1;
			natureCodes[4] = .9;
			break;
		}
		return natureCodes;
	}


}
