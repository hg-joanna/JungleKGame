/**
 * Represents the dog animal piece in the game with its corresponding strengths, extending the Piece class.
 */

public class Dog extends Piece{
	
	/**
     * Instantiates a Dog piece 
     */
	public Dog(){
        super("Dog", 3);
    }
	
	/**
	  * Instantiates a Dog piece with specified attributes
	  * @param row row position of the piece
	  * @param col column position of the piece
	  * @param playerOwner player who owns the piece
	  * @param trappedState true if the piece is trapped, false otherwise
	  */
	public Dog(int row, int col, Player playerOwner, boolean trappedState){
		super("Dog", 3, row, col, playerOwner, trappedState);
	}
	
}