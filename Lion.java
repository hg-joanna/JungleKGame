/**
 * Represents a  Lion piece in the game, as it can cross the lakes in the board
 * This class extends the Piece class
 */
public class  Lion extends Piece {

	private final boolean canCrossLake = true;
	
	/**
     * Instantiates a Lion piece
     */
	public Lion(){
        super(" Lion", 7);
    }
	
	/**
	  * Instantiates a Lion piece with specified attributes
	  * @param row row position of the piece
	  * @param col column position of the piece
	  * @param playerOwner player who owns the piece
	  * @param trappedState true if the piece is trapped, false otherwise
	  */
	public Lion(int row, int col, Player playerOwner, boolean trappedState){
		super("Lion", 7, row, col, playerOwner, trappedState);
	}
	
	
	/**
	 * Instantiates a Lion piece with specified attributes
	 * @param row row position of the piece
	 * @param col column position of the piece
	 * @param playerOwner player who owns the piece
	 * @param trappedState true if the piece is trapped, false otherwise
	 * @param canCrossLake true if the piece can cross the lake, false otherwise
	 */
	public Lion(int row, int col, Player playerOwner, boolean trappedState, boolean canCrossLake){
		super("Lion", 7, row, col, playerOwner, trappedState);
		
	}

	/**
	 * Determines if the piece can cross the lake
	 */
	@Override
    public boolean canCrossLake() {
		return true;
	}

}