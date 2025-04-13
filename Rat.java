/**
 * Represents a rat piece in the game, as it can swim in the lakes in the board
 * This class extends the Piece class
 */
public class Rat extends Piece{
    
	/**Rat can be in water */
	private final boolean beInWater = true;
	
	/**
     * Instantiates a Rat piece
     */
	public Rat(){
		super("Rat", 1);
	}
	
	/**
	  * Instantiates a Rat piece with specified attributes
	  * @param row row position of the piece
	  * @param col column position of the piece
	  * @param playerOwner player who owns the piece
	  * @param trappedState true if the piece is trapped, false otherwise
	  */
	public Rat(int row, int col, Player playerOwner, boolean trappedState){
		super("Rat", 1, row, col, playerOwner, trappedState);
	}
	
	/**
	 * Instantiates a rat piece with specified attributes
	 * 
	 * @param row row position of the piece on the board
	 * @param col column position of the piece on the board
	 * @param playerOwner player who owns the piece
	 * @param trappedState true if the piece is trapped, false otherwise
	 * @param beInWater true if the piece can be in water, false otherwise
	 */
	public Rat(int row, int col, Player playerOwner, boolean trappedState,boolean beInWater){
		super("Rat", 1, row, col, playerOwner, trappedState, beInWater);
		
	}

	/**
	 * Determines if the piece can be in water
	 * @return true since rats can swim in water
	 */
	@Override
	public boolean beInWater(){
		return beInWater;
	}

}