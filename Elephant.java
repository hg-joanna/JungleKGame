/**
 * Represents the elephant animal piece in the game with its corresponding strengths, extending the Piece class.
 */


public class Elephant extends Piece{
	
	/**
     * Instantiates an Elephant piece
     */
    public Elephant(){
        super("Elephant", 8);
    }

	/**
	  * Instantiates an Elephant piece with specified attributes
	  * @param row row position of the piece
	  * @param col column position of the piece
	  * @param playerOwner player who owns the piece
	  * @param trappedState true if the piece is trapped, false otherwise
	  */
	public Elephant(int row, int col, Player playerOwner, boolean trappedState){
		super("Elephant", 8, row, col, playerOwner, trappedState);
	}
	

}