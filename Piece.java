/**
 * Represents an animal piece and its corresponding name and strength
 * 
 * These pieces are moved around the board and can capture another player's pieces
 */
public class Piece {
    /**Name of the piece */
    private String name;
    /**Strength of the piece */
    private int strength;
    /**Player who owns the piece */
    private Player playerOwner;
    /**Determines if the piece is trapped in opponent's homebase */
	private boolean trappedState;
	/**Row index position of the piece */
    private int row;
    /**Column index position of the piece */
    private int col;

    /**
     * Instantiates a piece given the specified attributes
     * @param name name of the piece
     * @param strength strength of the piece
     * @param row row index position of the piece on the tile
     * @param col column index position of the piece on the tile
     * @param playerOwner player who owns the piece
     * @param trappedState determines whether the piece is trapped
     */
    public Piece(String name, int strength, int row, int col, Player playerOwner, boolean trappedState)
    {
        this.name = name;
        this.strength = strength;
        this.row = row;
        this.col = col;
        this.playerOwner = playerOwner;
		this.trappedState = trappedState;
    }

    /**
     * Instantiates a piece given the name and strength for randomizing to determine the first player
     * @param name name of the piece
     * @param strength strength of the piece
     */
    public Piece(String name, int strength)
    {
        this.name = name;
        this.strength = strength;
        this.playerOwner = null;
    }

    /**
     * Returns the name of the piece.
     * @return name of the piece as a String
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns the row index where the piece is located at.
     * @return row index
     */
    public int getRow()
    {
        return row;
    }

    /**
     * Returns the column index where the piece is located at.
     * @return column index
     */
    public int getCol()
    {
        return col;
    }

    /**
     * Returns the strength of the piece.
     * @return strength of the piece (integer value)
     */
    public int getStrength()
    {
        return strength;
    }

    /**
     * Returns the player who wons the piece.
     * @return Player who owns the piece
     */
    public Player getOwner()
    {
        return playerOwner;
    }

    /**
     * Sets the position of the piece on the board
     * @param row - the new row index of the piece
     * @param col - the new col index of the piece
     */
    public void setPosition(int row, int col)
    {
        this.row = row;
        this.col = col;
    }

    /**
     * Returns the symbol representing the animal piece. This method returns the first letter of the piece's name, except for "Leopard" where "P" is returned.
     * @return a single character representing the symbol of the animal piece
     */
    public String getSymbol()
    {
        if (name == "Leopard")
            return "P";

        return name.substring(0, 1); 
    }
	
    /**
     * Returns the current state of the piece, whether it is trapped or not.
     * @return true if trapped, otherwise false
     */
	public boolean getState()
    {
        return trappedState;
    }

	/**
     * Sets the trapped state of the piece
     * @param trappedState - true if the piece is trapped, false if not
     */
	public void setTrapped(boolean trappedState){
		this.trappedState = trappedState;
	}
	
    /**
     * Determines whether the piece can be in water or not
     * @return false by default as only the rat piece can be in water. The rat class overrides this method.
     */
	public boolean beInWater() { 
		return false; // animals cant be in water except rat (override)
	}

    /**
     * Determines whether the piece can cross the lake or not
     * @return false by default as only the panthera (tiger and lion) pieces can cross the lake. The panthera class overrides this method.
     */
	public boolean canCrossLake() { 
		return false; // only liona and tiger can cross (override)
	}

    /**
     * Compares the piece to another piece to check if it's equal
     * @return true if the pieces are the same, otherwise false
     */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof String) {
			return this.name.equalsIgnoreCase((String) obj);
		}
		if (obj instanceof Piece) {
			return this.name.equalsIgnoreCase(((Piece) obj).name); 
		}
		return false;
	}


}

