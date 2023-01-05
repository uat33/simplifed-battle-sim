package moves;

import pokemon.Individual;
import pokemon.PokemonInterface;
import setup.Setup;
import types.Type;
import java.util.Random;

import battle.Battle;


/*
 * A subclass of move. this is where all the attacking moves are stored as they need to be seperated from the status moves.
 * Attacking moves are very easy in comparison to status as the only purpose is to to deal damage which is just a formula.
 * 
 * However, there is a bit more to it. Certain moves have effects that need to be taken into account -- not every single on of these is implemented.
 * 
 */


public class Attack extends Move{



	// the purpose method is meant to contain what the move actually does.
	// in this case, that's just a damage formula.
	// this is done so that each move is an object, with the purpose method. that method can be called regardless of the move, and the correct thing will happen.

	//  Damage = ((((2 * Level / 5 + 2) * AttackStat * AttackPower / DefenseStat) / 50) + 2) * STAB * Weakness/Resistance * RandomNumber(85-100) / 100

	// thats the formula. there's actually more to it, but the other aspects are not implemented in this simplified version. 
	@Override
	public void purpose(Individual user, Individual target, Move move) throws InterruptedException {

		if (move.getPp() == 0) System.out.println("No more PP for this move.");
		else if(!move.accuracyCheck(user, target)) { // check if the move misses
			System.out.println("The attack missed.");
		}
		else { // a very long else block, if the move lands

			// first things first, there are two types of attacking moves. physical or special.
			// physical moves use attack and defense, sometimes called physical attack and physical defense to avoid confusion
			// special moves use special attack and special defense.
			// there is one exception: psyshock uses special attack and physical defense

			int att = 0, def = 0;


			if(getCategory().equals("Physical")) {

				att = user.getStats()[1]; // attack
				def = target.getStats()[2]; // defense

			}

			else if(getName().equals("Psyshock")) { // psychock is a special move still, so we can minimize the amount of times we check this by doing it here
				att = user.getStats()[3]; // special attack
				def = target.getStats()[2]; // defense
			}
			else { // regular special move

				att = user.getStats()[3]; // special attack
				def = target.getStats()[4]; // defense

			}
			// seeing as we're here, this is an attacking move and power should be an int. so we can cast it

			int damage = ((((2 * PokemonInterface.level / 5 + 2) * att * Integer.parseInt(getPower()) / def) / 50) + 2);
			// not done yet. the next part is tricky.

			if (getName().equals("Gyro Ball")) { // a special move, with a special way of calculating damage
				damage = ((((2 * PokemonInterface.level / 5 + 2) * att *  25 * (target.getStats()[5] / user.getStats()[5]) / def) / 50) + 2);
			}

			// there are two multipliers that have to do with type. 
			// the first is same type attack bonus, lovingly called STAB.
			// if the user is the same time as the move being used, there is a 50 percent increase in the damage.
			// the second is how the type of the move interacts with the type of the target


			// if the moves type is equal to either of the user's types
			boolean stab = false;
			// go through each of the types, if there's only, one iteration
			for(Type t : user.getTypes()) {
				if (t.getTypeName().equals(getType().getTypeName())) stab = true; // if the types type name is equal to this types type name, stab is true
			}
			if (stab){
				damage *= 1.5; // multiply damage by 1.5 if stab is true
			}

			// find the number that the first type appears in the typeSetup arrayList.
			// that number is the same as where it will appear in the matchup array.
			// so take this attacks type and find the value in that array
			// multiply that with damage

			double typeMult = 1;

			for(Type type : target.getTypes()) {
				// call the static method getNumfromType from the class types, while passing in the field types from the class setup and the name of the target's type
				// use the method to find the value
				typeMult *= move.getType().getMatchup()[Type.getNumfromType(Setup.types, type.getTypeName())]; 
			}

			damage *= typeMult; // multiply damage by typeMultiplier


			// pokemon has a mechanic known as rolls or a range.
			// once the damage is calculated, there are different amounts of damage it can do
			// it is 85 percent to 100 percent of the damage, inclusive
			// this means there are 16 possibilites for the damage, and it is completely random.

			Random rand = new Random();

			damage *= (rand.nextInt(16) + 85) / 100.0; // random number from 0 to 15 has 85 added to it.
			// multiply that with damage to get the final amount.



			// tell users what happened
			Thread.sleep(500); // we want a gap, so it flows better
			System.out.println(user.getName() + " used " + move.getName());

			// depending on how the types stack up, sometimes text is printed here
			Thread.sleep(500); // we want a gap, so it flows better

			// have to output text based on the typemultiplier
			// 1 means no text
			// more than one is super effective
			// 0 is no effect
			// < 1 but not 0 is not very effective
			if(typeMult > 1) System.out.println("It's super effective!");
			else if (typeMult == 0) System.out.println("It had no effect.");
			else if(typeMult < 1) System.out.println("It's not very effective.");


			// certain moves lower your stats after using them

			if(getName().equals("Overheat") || getName().equals("Leaf Storm") || getName().equals("Draco Meteor")) {
				StatChange.TwoSpAttDrop(user); // these moves lower your spA two stages

			}
			else if (getName().equals("Close Combat")) { // - 1 def, - 1 spDef
				StatChange.OneDefenseDrop(user);
				StatChange.OneSpDefDrop(user);

			}
			else if (getName().equals("Superpower")) { // - 1 att, -1 def
				StatChange.OneAttackDrop(user);
				StatChange.OneDefenseDrop(user);
			}
			else if (getName().equals("Hammer Arm")) { // -1 speed
				StatChange.OneSpeedDrop(user);

			}

			// if the pokemon dies, you're not supposed to reveal how much damage the move does. 
			// so take that into account;
			Thread.sleep(500); // we want a gap, so it flows better

			if(target.getStats()[0] - damage <= 0) { // the pokemon has fainted
				System.out.println(target.getName() + " lost " + target.getStats()[0] + " HP." );
				// telling the player how much damage it did when it killed is more information than is given in a pokemon game
				// so we do it like this. if it is going to kill, we just output the current health as the damage done.
				
				// prompt the correct user to send out another pokemon
				
				// but only if they have pokemon left
				
				
				if(target.getTeamNum() == 1) {
					if (Setup.team1.size() > 0) {
						Battle.pokemon1 = Battle.newPokemon(target, Setup.team1);
						Battle.pokemon1.switchPokemonIn();
					}
					
				}
				else {
					if (Setup.team2.size() > 0) {
						Battle.pokemon2 = Battle.newPokemon(target, Setup.team2);
						Battle.pokemon2.switchPokemonIn();
					}
					
				}

			}	
			else {
				System.out.println(target.getName() + " lost " + damage + " HP." );
			}
			// now to deal the damage.
			target.getStats()[0] -= damage;


		}

	}

	@Override
	public String toString() {
		return super.toString();
	}

	// constructor. need the type, category, pp, power, and accuracy
	public Attack(String name, Type type, String category, int pp, String power, String accuracy) {
		super(name, type, category, pp, power, accuracy);

	}

}
