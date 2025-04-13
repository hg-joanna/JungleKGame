/**
 * Represents a tiger piece in the game, as it can cross the lakes in the board
 * This class extends the Piece class
 */
public class Tiger extends Piece {

	private final boolean canCrossLake = true;
	
	/**
     * Instantiates a Tiger piece
     */
	public Tiger(){
        super("Tiger", 6);
    }
	
	/**
	  * Instantiates a Tiger piece with specified attributes
	  * @param row row position of the piece
	  * @param col column position of the piece
	  * @param playerOwner player who owns the piece
	  * @param trappedState true if the piece is trapped, false otherwise
	  */
	public Tiger(int row, int col, Player playerOwner, boolean trappedState){
		super("Tiger", 6, row, col, playerOwner, trappedState);
	}
	
	/**
	 * Instantiates a Tiger piece with specified attributes
	 * @param strength strength of the piece
	 * @param row row position of the piece
	 * @param col column position of the piece
	 * @param playerOwner player who owns the piece
	 * @param trappedState true if the piece is trapped, false otherwise
	 * @param canCrossLake true if the piece can cross the lake, false otherwise
	 */
	public Tiger(int strength, int row, int col, Player playerOwner, boolean trappedState, boolean canCrossLake){
		super("Tiger", 6, row, col, playerOwner, trappedState);
		
	}

	/**
	 * Determines if the piece can cross the lake
	 */
	@Override
    public boolean canCrossLake() {
		return true;
	}
	

}