package moves;

import pokemon.Individual;

/*
 * This class is just for organizational purposes.
 * A common application for status moves is to have them change stats
 * This class contains everything necessary for that.
 *  all methods are static so they can be accessed without an object.
 *  There's a lot of stuff here, but the point is we want to just be able to say StatChange.(method) and have it do everything. 
 *  this does that.
 */
public class StatChange{

	// stats can only be raised or lowered to a max of 6 stages. make sure this limit is not surpassed
	public static boolean checkLimit(boolean raise, int stage) {
		if(raise) {
			if(stage == 12) { // 12 cause it starts at -6 and goes to +6
				return false; // cannot be raised
			}
			return true; // can be 
		}
		if(stage == 0) return false; // cannot be lowered

		return true; // can be

	}


	// a very common application for status moves is raising your own stats or decreasing the opponent's stats.
	// we can make general methods for this, that can be called at a later time.

	// if the attack goes up, we know it's for the user. so we don't need any other parameters
	// attack is the first statcode, so take that and increase by one.


	public static void OneAttackRaise(Individual pokemon) { // +1 attack
		doTheChanges(pokemon, true, 1, 0); // user is the one using the moves. true because the stat is raised. 1 because 1 stage, and 0 because that's where attack is.
		attackUp(); // the text we display needs to be seperate because the text is different based onthe number of stages.
	}

	// keep doing that for all these methods.
	public static void OneAttackDrop(Individual pokemon) { // -1 attack
		doTheChanges(pokemon, false, 1, 0);
		attackDown();


	}
	public static void TwoAttackRaise(Individual pokemon) { // +2 attack
		doTheChanges(pokemon, true, 2, 0);
		attackSharp();
	}
	public static void TwoAttackDrop(Individual pokemon) { // -2 attack
		doTheChanges(pokemon, false, 2, 0);
		attackHarsh();
	}



	public static void OneDefenseRaise(Individual pokemon) { // +1 Defense
		doTheChanges(pokemon, true, 1, 1);
		defenseUp();
	}
	public static void OneDefenseDrop(Individual pokemon) { // -1 Defense
		doTheChanges(pokemon, false, 1, 1);
		defenseDown();
	}
	public static void TwoDefenseRaise(Individual pokemon) { // +2 Defense
		doTheChanges(pokemon, true, 1, 1);
		defenseUp();
	}
	public static void TwoDefenseDrop(Individual pokemon) { // -2 Defense
		doTheChanges(pokemon, false, 1, 1);
		defenseDown();
	}


	public static void OneSpAttRaise(Individual pokemon) { // +1 SpAtt
		doTheChanges(pokemon, true, 1, 2);
		spAUp();
	}
	public static void OneSpAttDrop(Individual pokemon) { // -1 SpAtt
		doTheChanges(pokemon, false, 1, 2);
		spADown();
	}

	public static void TwoSpAttRaise(Individual pokemon) { // +2 SpAtt
		doTheChanges(pokemon, true, 2, 2);
		spASharp();
	}
	public static void TwoSpAttDrop(Individual pokemon) { // -2 SpAtt
		doTheChanges(pokemon, false, 2, 2);
		spADown();
	}


	public static void OneSpDefRaise(Individual pokemon) { // +1 SpDef
		doTheChanges(pokemon, true, 1, 3);
		spDUp();
	}
	public static void OneSpDefDrop(Individual pokemon) { // -1 SpDef
		doTheChanges(pokemon, false, 1, 3);
		spDDown();
	}
	public static void TwoSpDefRaise(Individual pokemon) { // +2 SpDef
		doTheChanges(pokemon, true, 2, 3);
		spDSharp();
	}
	public static void TwoSpDefDrop(Individual pokemon) { // -2 SpDef
		doTheChanges(pokemon, false, 2, 3);
		spDHarsh();
	}

	public static void OneSpeedRaise(Individual pokemon) { // +1 Speed
		doTheChanges(pokemon, true, 1, 4);
		SpeedUp();
	}
	public static void OneSpeedDrop(Individual pokemon) { // -1 Speed
		doTheChanges(pokemon, false, 1, 4);
		SpeedDown();

	}
	public static void TwoSpeedRaise(Individual pokemon) { // +2 Speed
		doTheChanges(pokemon, true, 2, 4);
		SpeedSharp();
	}
	public static void TwoSpeedDrop(Individual pokemon) { // -2 Speed
		doTheChanges(pokemon, false, 2, 4);
		SpeedHarsh();
	}


	public static void OneAccuracyRaise(Individual pokemon) { // +1 Accuracy
		doTheChanges(pokemon, true, 1, 5);
		accuracyUp();
	}
	public static void OneAccuracyDrop(Individual pokemon) { // -1 Accuracy
		doTheChanges(pokemon, false, 1, 5);
		accuracyDown();
	}
	public static void TwoAccuracyRaise(Individual pokemon) { // +2 Accuracy
		doTheChanges(pokemon, true, 2, 5);
		accuracySharp();
	}
	public static void TwoAccuracyDrop(Individual pokemon) { // -2 Accuracy
		doTheChanges(pokemon, false, 2, 5);
		accuracyHarsh();
	}


	public static void OneEvasionRaise(Individual pokemon) { // +1 Evasion
		doTheChanges(pokemon, true, 1, 6);
		evasionUp();
	}
	public static void OneEvasionDrop(Individual pokemon) { // -1 Evasion
		doTheChanges(pokemon, false, 1, 6);
		evasionDown();
	}
	public static void TwoEvasionRaise(Individual pokemon) { // +2 Evasion
		doTheChanges(pokemon, true, 2, 6);
		evasionSharp();
	}
	// there are no moves that drop evasion two stages, so we don't need that method


	// pokemon is the pokemon. 
	// if the stat needs to go up, raise is true
	// otherwise its false
	// stages is always 1 or 2. it's how many times the stat needs to go up or down
	// finally, stat is the index in statcodes that needs to be changed 
	// 0 is attack, 1 is defense etc.

	public static void doTheChanges(Individual pokemon, boolean raise, int stages, int stat) {

		for(int i = 0; i < stages; i++) {
			if(checkLimit(raise, pokemon.getStatCodes()[stat])) { // if this evaluates to true, then the stat can be changed.otherwise we do nothing
				if(raise) pokemon.getStatCodes()[stat] ++; // if raise is true, we increase the stat
				else pokemon.getStatCodes()[stat] --; // otherwise we decrease
			}
		}
	}



	// we need to tell the user these changes happened.
	// sharp and harsh are the terminology for two stages

	public static void attackSharp() {
		System.out.println("Attack rose sharply.");
	}
	public static void defenseSharp() {
		System.out.println("Defense rose sharply.");
	}
	public static void spASharp() {
		System.out.println("Special Attack rose sharply.");
	}
	public static void spDSharp() {
		System.out.println("Special Defense rose sharply.");
	}
	public static void SpeedSharp() {
		System.out.println("Speed rose sharply.");
	}
	public static void accuracySharp() {
		System.out.println("Accuracy rose sharply.");
	}
	public static void evasionSharp() {
		System.out.println("Evasion rose sharply.");
	}
	public static void attackHarsh() {
		System.out.println("Attack fell harshly.");
	}
	public static void defenseHarsh() {
		System.out.println("Defense fell harshly.");
	}
	public static void spAHarsh() {
		System.out.println("Special Attack fell harshly.");
	}
	public static void spDHarsh() {
		System.out.println("Special Defense fell harshly.");
	}
	public static void SpeedHarsh() {
		System.out.println("Speed fell harshly.");
	}
	public static void accuracyHarsh() {
		System.out.println("Accuracy fell harshly.");
	}
	public static void evasionHarsh() {
		System.out.println("Evasion fell harshly.");
	}

	// now again, but for one stage

	public static void attackUp() {
		System.out.println("Attack rose.");
	}
	public static void defenseUp() {
		System.out.println("Defense rose.");
	}
	public static void spAUp() {
		System.out.println("Special Attack rose.");
	}
	public static void spDUp() {
		System.out.println("Special Defense rose.");
	}
	public static void SpeedUp() {
		System.out.println("Speed rose.");
	}
	public static void accuracyUp() {
		System.out.println("Accuracy rose.");
	}
	public static void evasionUp() {
		System.out.println("Evasion rose.");
	}
	public static void attackDown() {
		System.out.println("Attack fell.");
	}
	public static void defenseDown() {
		System.out.println("Defense fell.");
	}
	public static void spADown() {
		System.out.println("Special Attack fell.");
	}
	public static void spDDown() {
		System.out.println("Special Defense fell.");
	}
	public static void SpeedDown() {
		System.out.println("Speed fell.");
	}
	public static void accuracyDown() {
		System.out.println("Accuracy fell.");
	}
	public static void evasionDown() {
		System.out.println("Evasion fell.");
	}


}
