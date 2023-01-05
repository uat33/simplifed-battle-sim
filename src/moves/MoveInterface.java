package moves;
import pokemon.Individual;



/*
 * Interface with some methods that can be defined later. Superclass of all moves as they all need these methods.
 */

public interface MoveInterface {

	// this is the interface that will be the ancestor of every move.

	// so naturally we put all the methods that are universal to moves here

	public void ppUpdate(); // remove one power point after a move is used

	public boolean accuracyCheck(Individual user, Individual target); // check if the move lands. 
	
	public void purpose(Individual user, Individual target, Move move) throws InterruptedException; // actually does what the move is supposed to do.
	// receives the user, target and move as parameters so the appropriate thing can be done
	// needs the exception here so it can be used in the subclasses
	
	
	



}
