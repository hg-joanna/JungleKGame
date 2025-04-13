/**
 * Represents the leopard animal piece in the game with its corresponding strengths, extending the Piece class.
 */
public class Leopard extends Piece{
	
	/**
     * Instantiates a Leopard piece
     */
    public Leopard(){
        super("Leopard", 5);
    }
	
	/**
	  * Instantiates a Leopard piece with specified attributes
	  * @param row row position of the piece
	  * @param col column position of the piece
	  * @param playerOwner player who owns the piece
	  * @param trappedState true if the piece is trapped, false otherwise
	  */
	public Leopard(int row, int col, Player playerOwner, boolean trappedState){
		super("Leopard", 5, row, col, playerOwner, trappedState);
	}

}