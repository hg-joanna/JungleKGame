/**
 * Represents the cat animal piece in the game with its corresponding strengths, extending the Piece class.
 */
public class Cat extends Piece{
	
	 /**
     * Instantiates a Cat piece
     */
    public Cat(){
        super("Cat", 2);
    }
	
	/**
	  * Instantiates a Cat piece with specified attributes
	  * @param row row position of the piece
	  * @param col column position of the piece
	  * @param playerOwner player who owns the piece
	  * @param trappedState true if the piece is trapped, false otherwise
	  */
	public Cat(int row, int col, Player playerOwner, boolean trappedState){
		super("Cat", 2, row, col, playerOwner, trappedState);
	}

}