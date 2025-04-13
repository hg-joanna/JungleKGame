/**
 * Represents the wolf animal piece in the game with its corresponding strengths, extending the Piece class.
 */
public class Wolf extends Piece{
	
	/**
     * Instantiates a Wolf piece 
     */
    public Wolf(){
        super("Wolf", 4);
    }
	
	/**
	  * Instantiates a Wolf piece with specified attributes
	  * @param row row position of the piece
	  * @param col column position of the piece
	  * @param playerOwner player who owns the piece
	  * @param trappedState true if the piece is trapped, false otherwise
	  */
	public Wolf(int row, int col, Player playerOwner, boolean trappedState){
		super("Wolf", 4, row, col, playerOwner, trappedState);
	}

}