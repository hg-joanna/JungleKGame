import java.util.ArrayList;

/**
 * Represents a player in the game
 * 
 * A player has a name and its own pieces to move around the board
 */
public class Player {

    /**Name of the player */
    private String name;
    /**The list of pieces the player owns */
    private ArrayList<Piece> piecesOwned; 
    /**Indicates whether the player is current player */
    private boolean bCurrentPlayer;
    /**Indicates whether the player is in their opponent's homebase */
    private boolean isInOpponentHomeBase;
    /**Tile location of the player's homebase */
    private Tile homeBaseLoc;
    /**List of traps the player owns */
    private ArrayList<Tile> traps;
	private Piece selectedPiece;

    /**
     * Instantiates a new player with a name
     * Initializes an empty list of pieces owned and traps
     * @param name name of the player
     */
    public Player(String name)
    {
        this.name = name;
        this.piecesOwned = new ArrayList<>();
        this.bCurrentPlayer = false;
        this.isInOpponentHomeBase = false;
        this.homeBaseLoc = null;
        this.traps = new ArrayList<>();
    }

    /**
     * Returns the player's name.
     * @return the player's name as a String
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns the tile where the player's homebase is located
     * @return tile where homebase is located at
     */
    public Tile getHomeBase()
    {
        return homeBaseLoc;
    }

    /**
     * Sets the name of the player
     * @param name name assigned to the player
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Sets the homebase tile of the player
     * @param homebase specified tile where the player's homebase is located at
     */
    public void setHomeBase(Tile homebase)
    {
        this.homeBaseLoc = homebase;
    }

    /**
     * Sets whether any of the player's pieces is located at their opponent's homebase
     * @param isInOpponentHomeBase true if the piece is there, false otherwise
     */
    public void setIsInOpponentHomeBase(boolean isInOpponentHomeBase)
    {
        this.isInOpponentHomeBase = isInOpponentHomeBase;
    }

    /**
     * Adds a piece to a player's owned pieces
     * @param piece piece to be added to the player's owned pieces
     */
    public void addPiece(Piece piece)
    {
        piecesOwned.add(piece);
    }

    /**
     * Removes a piece from the player's owned pieces
     * @param piece piece to be removed from the player's owned pieces
     */
    public void removePiece(Piece piece)
    {
        piecesOwned.remove(piece);
    }

    /**
     * Returns the list of pieces a player owns
     * @return ArrayList containing the player's owned pieces
     */
    public ArrayList<Piece> getPieces()
    {
        return piecesOwned;
    }

    /**
     * Sets true if the player is the current player
     * @param bCurrentPlayer true if the player will be the current player, otherwise false
     */
    public void setCurrentPlayer(boolean bCurrentPlayer)
    {
        this.bCurrentPlayer = bCurrentPlayer;
    }

    /**
     * Returns if the player is the current player
     * @return true if player is current player, otherwise false
     */
    public boolean isCurrentPlayer()
    {
        return bCurrentPlayer;
    }

    /**
     * Adds the traps that belong to a player in the game
     * @param trap1 tile of the first trap
     * @param trap2 tile of the second trap
     * @param trap3 tile of the third trap
     */
    public void addTrap(Tile trap1, Tile trap2, Tile trap3)
    {
        traps.add(trap1);
        traps.add(trap2);
        traps.add(trap3);
    }

    /**
     * Returns if the given tile location is a trap belonging to the player
     * @param tile specified tile
     * @return true if the tile contains the player's trap, otherwise false
     */
    public boolean isPlayerTrap(Tile tile)
    {
        return traps.contains(tile);
    }
	
	/**Returns the selected piece of the player
	* @return piece*/
	public Piece getSelectedPiece() { //was used in mouse click move tile
        return selectedPiece;
    }

	/**
     * Sets the selected piece of the player
     * @param selectedPiece selected piece of the player
     */
    public void setSelectedPiece(Piece selectedPiece) {
        this.selectedPiece = selectedPiece;
    }

    /**
     * Returns if the player is in their opponent's homebase
     * @return true if the player is in their opponent's homebase, otherwise false
     */
    public boolean isInOpponentHomeBase()
    {
        return isInOpponentHomeBase;
    }

}