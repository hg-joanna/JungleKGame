/**
 * Represents a tile on the board
 * 
 * It can either contain a piece, a trap, a homebase, a lake, or be empty.
 */
public class Tile {
    /**The piece on the tile (if any) */
    private Piece piece; 
    /**Determines if the tile is a trap */
    private boolean isTrap;
    /**Determines if the tile is a lake */
    private boolean isLake;
    /**Determines if the tile is a player's homebase */
    private boolean isHomeBase;
    /**Row index position of the tile */
    private int row;
    /**Column index position of the tile */
    private int col;

    /**
     * Instantiates a tile given the specified attributes
     * @param row row index position of tile in the board
     * @param col column index position of tile in the board
     * @param isTrap determines if the tile is a trap
     * @param isLake determines if the tile is a lake
     * @param isHomeBase determines if the tile is a player's homebase
     */
    public Tile(int row, int col, boolean isTrap, boolean isLake, boolean isHomeBase)
    {
        this.row = row;
        this.col = col;
        this.isTrap = isTrap;
        this.isLake = isLake;
        this.isHomeBase = isHomeBase;
        this.piece = null;
    }

    /**
     * Returns the piece on the tile
     * @return Piece on the tile
     */
    public Piece getPiece()
    {
        return piece;
    }
	
    /**
     * Returns the row position of the tile
     * @return row index
     */
	public int getRow(){
		return row;
	}

    /**
     * Returns the column position of the tile
     * @return column index
     */
	public int getCol(){
		return col;
	}
	
    /**
     * Sets the piece on the tile
     * @param piece piece to be set on the tile
     */
    public void setPiece(Piece piece)
    {
        this.piece = piece;
        
        if (piece != null) 
            piece.setPosition(row, col); 
    }

    /**
     * Removes the piece from the tile
     * @return Piece removed
     */
    public Piece removePiece()
    {
        Piece temp = piece;
        
        if (piece != null)
            piece.setPosition(-1, -1);
            
        this.piece = null;
        return temp;
    }

    /**
     * Determines if the tile is occupied by a piece
     * @return true if occupied by a piece, otherwise false
     */
    public boolean isOccupied() // occupied by a piece
    {
        return piece != null; 
    }

    /**
     * Determines if the tile contains a trap
     * @return true if the tile is a trap, otherwise false
     */
    public boolean isTrap()
    {
        return isTrap;
    }

    /**
     * Determines if the tile contains a lake
     * @return true if the tile is a lake, otherwise false
     */
    public boolean isLake()
    {
        return isLake;
    }

    /**
     * Determines if the tile contains a homebase
     * @return true if the tile is a homebase, otherwise false
     */
    public boolean isHomeBase()
    {
        return isHomeBase;
    }

    /**
     * Determines if the tile is empty (no homebase, no lake, no trap, and no piece)
     * @return true if empty, otherwise false
     */
    public boolean isEmpty()
    {
        return !isHomeBase && !isLake && !isTrap() && !isOccupied();
    }


}
