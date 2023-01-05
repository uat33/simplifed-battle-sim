package moves;

import java.util.Random;
import pokemon.Individual;
import types.Type;


/*
 * A subclass of move. this is where all the status moves are stored as they need to be seperated from the attacking moves.
 * This class is a nightmare to make because status moves are a very broad term.
 * All status moves don't deal direct damage, but that's all they have in common.
 * In terms of function, they are very varied and have little in common with each other
 * Unfortunately, in many cases there is no way to do this then to literally make methods for each status move.
 * This is a gargantuan task, so I chose to simplify by only including status moves that change stats. 
 * The teams have been made so that this does not cause any issues.
 * 
 */

public class Status extends Move{

	// constructor
	public Status(String name, Type type, String category, int pp, String power, String accuracy) {
		super(name, type, category, pp, power, accuracy);
	}

	@Override
	public void purpose(Individual user, Individual target, Move move) {
		// this method is universal to all status moves. we want to be able to call it and have it always do the right thing.
		// short of creating a subclass for each move, i think the only way to do this is to check the name of the move
		// and then based on that call a seperate method.

		// which means we're about to write a lot of if statements. actually probably switch statement, cause we're comparing one value to many
		
		// not everything is commented, cause there's quite a bit and its all the same idea
		
		System.out.println(user.getName() + " used " + getName()); // let the players know which move was used.

		switch(getName()) { // take the move's name into the switch statement so we can match to appropriate case
		case "Acupressure":	// this move is annoying. it picks a random stat, and raises it two stages.
			Random rand = new Random();
			Acupressure(user, rand.nextInt(7)); // make a method, cause it requires a lot of code.
			break;

			// moves that raise speed two stages
		case "Rock Polish":
		case "Agility":	// raise speed two stages.
			StatChange.TwoSpeedRaise(user);
			break;
			// moves that raise attack two stages 
		case "Swords Dance": // raise attack two stages.
			StatChange.TwoAttackRaise(user);
			break;


			// moves that raise defense two stages 
		case "Iron Defense":
		case "Acid Armor":
		case "Barrier": // this move raises defense stat two stages
		case "Cotton Guard":
			StatChange.TwoDefenseRaise(user);
			break;
			// moves that raise special attack two stages 
		case "Tail Glow":
		case "Nasty Plot": // raise special attack two stages.
			StatChange.TwoSpAttRaise(user);
			break;
			// moves that raise special defense two stages 
		case "Amnesia":	// raise special defense two stages.
			StatChange.TwoSpDefRaise(user);
			break;
			// moves that raise accuracy two stages 

			// moves that raise evasion two stages 
		case "Minimize": // raise evasion two stages.
			StatChange.TwoEvasionRaise(user);
			break;



			// moves that raise speed one stages: none

			// moves that raise attack one stages 

		case "Meditate":
		case "Sharpen": 
		case "Howl":
			StatChange.OneAttackRaise(user);;
			break;	
			// moves that raise defense one stages 

		case "Harden":
		case "Defense Curl":
		case "Withdraw":
			StatChange.OneDefenseRaise(user);
			break;
			// moves that raise special attack one stages: none


			// moves that raise special defense one stages : none

			// moves that raise accuracy one stages: none

			// moves that raise evasion one stages 

		case "Double Team":	
			StatChange.OneEvasionRaise(user);
			break;


			// moves that lower speed two stages

		case "Cotton Spore":
		case "Scary Face": 
			StatChange.TwoSpeedDrop(target);
			break;

			// moves that lower attack two stages 

		case "Feather Dance":
		case "Charm": 
			StatChange.TwoAttackDrop(target);
			break;

			// moves that lower defense two stages 

		case "Screech": 
			StatChange.TwoDefenseDrop(target);
			break;


			// moves that lower special attack two stages: there are none

			// moves that lower special defense two stages
		case "Metal Sound": 
			StatChange.TwoSpDefDrop(target);
			break;

			// moves that lower accuracy two stages: none

			// moves that lower evasion two stages: none


			// moves that lower speed one stages
		case "String Shot": 
			StatChange.OneSpeedDrop(target);
			break;		
			// moves that lower attack one stages 
		case "Growl": 
			StatChange.OneAttackDrop(target);
			break;
			// moves that lower defense one stages 
		case "Tail Whip":
		case "Leer":	
			StatChange.OneDefenseDrop(target);
			break;

			// moves that lower special attack one stages: there are none


			// moves that lower special defense one stages: there are none

			// moves that lower accuracy one stages 

		case "Sand Attack":
		case "Smokescreen":
		case "Kinesis":	
		case "Flash":
			StatChange.OneAccuracyDrop(target);
			break;
			// moves that lower evasion one stages
		case "Defog":
		case "Sweet Scent": 
			StatChange.OneEvasionDrop(target);
			break;





			// there are status moves that affect different stats as well

			// +1 attack and special attack
		case "Work Up":
		case "Growth": 
			StatChange.OneAttackRaise(user);
			StatChange.OneSpAttRaise(user);
			break;
			// moves that raise defense and sp defense one stage
		case "Cosmic Power":
		case "Defend Order":
		case "Stockpile": 
			StatChange.OneDefenseRaise(user);
			StatChange.OneSpDefRaise(user);
			break;

			// +1 att and def
		case "Bulk Up": 
			StatChange.OneAttackRaise(user);
			StatChange.OneDefenseRaise(user);
			break;

			// +1 spa and special defense
		case "Calm Mind": 
			StatChange.OneSpAttRaise(user);
			StatChange.OneSpDefRaise(user);
			break;

			// +1 attack and speed
		case "Dragon Dance": 
			StatChange.OneAttackRaise(user);
			StatChange.OneSpeedRaise(user);
			break;

			// -1 attack and defense
		case "Tickle": 
			StatChange.OneAttackDrop(target);
			StatChange.OneDefenseDrop(target);
			break;
			// +1 att, def and acc
		case "Coil": 
			StatChange.OneAttackRaise(user);
			StatChange.OneDefenseRaise(user);
			StatChange.OneAccuracyRaise(user);
			break;
		case "Hone Claws": 
			StatChange.OneAttackRaise(user);
			StatChange.OneAccuracyRaise(user);
			break;
			// +1 spa, special def, and dpeed
		case "Quiver Dance": 
			StatChange.OneSpAttRaise(user);
			StatChange.OneSpDefRaise(user);
			StatChange.OneSpeedRaise(user);
			break;
			// +1 att, +2 speed
		case "Shift Gear": 
			StatChange.OneAttackRaise(user);
			StatChange.TwoSpeedRaise(user);
			break;
			// +2 attack, special attack and speed
			// -2 defense and special defense, except this one is weird.
		case "Shell Smash": // weird because it lowers some of your own stats
			StatChange.TwoAttackRaise(user);
			StatChange.TwoSpAttRaise(user);
			StatChange.TwoSpeedRaise(user);
			StatChange.TwoDefenseDrop(user);
			StatChange.TwoSpDefDrop(user);
			break;
		case "Curse": // weird because it lowers some of your own stats
			StatChange.OneAttackRaise(user);
			StatChange.OneDefenseRaise(user);
			StatChange.OneSpeedDrop(user);
			// and those are all the stat changing moves
		}
	}



	// make methods for moves with too many lines or else it gets very cluttered

	public void Acupressure(Individual user, int stat) {
		// chooses stat randomly
		// raise that stat two stages, and print out the text
		if (stat == 0) { 
			StatChange.TwoAttackRaise(user);
		}
		else if (stat == 1) {
			StatChange.TwoDefenseRaise(user);

		}
		else if (stat == 2) {
			StatChange.TwoSpAttRaise(user);

		}
		else if (stat == 3) {
			StatChange.TwoSpDefRaise(user);

		}
		else if (stat == 4) {
			StatChange.TwoSpeedRaise(user);

		}
		else if (stat == 5) {
			StatChange.TwoAccuracyRaise(user);

		}
		else {
			StatChange.TwoEvasionRaise(user);

		}
	}

}
