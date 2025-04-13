/**
 * Represents a lion or tiger piece in the game, as it can cross the lakes in the board
 * This class extends the Piece class
 */
public class Panthera extends Piece {

	/**Panthera (Lions and Tigers) can cross lakes */
	private final boolean canCrossLake = true;
	
	/**
	 * Instantiates a panthera piece with a name and strength
	 * @param name name of the piece
	 * @param strength strength of the piece
	 */
	public Panthera(String name, int strength){
		super(name, strength);
	}

	 /**
	  * Instantiates a panthera piece with specified attributes
	  * @param name name of the piece
	  * @param strength strength of the piece
	  * @param row row position of the piece
	  * @param col column position of the piece
	  * @param playerOwner player who owns the piece
	  * @param trappedState true if the piece is trapped, false otherwise
	  */
	public Panthera(String name, int strength, int row, int col, Player playerOwner, boolean trappedState){
		super(name, strength, row, col, playerOwner, trappedState);
	}
	
	/**
	 * Instantiates a panthera piece with specified attributes
	* @param name name of the piece
	* @param strength strength of the piece
	* @param row row position of the piece
	* @param col column position of the piece
	* @param playerOwner player who owns the piece
	* @param trappedState true if the piece is trapped, false otherwise
	 * @param canCrossLake true if the piece can cross the lake, false otherwise
	 */
	public Panthera(String name, int strength, int row, int col, Player playerOwner, boolean trappedState, boolean canCrossLake){
		super(name, strength, row, col, playerOwner, trappedState);
		
	}

	/**
	 * Determines if the piece can cross the lake
	 */
	@Override
    public boolean canCrossLake() {
		return canCrossLake;
	}

}
