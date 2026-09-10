/**
 * Represents the board of Jungle King consisting of multiple tiles, where each represents a different animal piece or special tile.
 */
public class Board {
	/**Length of board*/
    private final int ROW = 7;
	/**Width of board*/
    private final int COL = 9;
	/**Tiles of the board*/
    private Tile tiles[][];
	
	/**Player 1 of the game*/
    private Player player1;
	/**Player 2 of the game*/
    private Player player2;
	/**Information of the current player*/
    private Player currentPlayer;
	/**Holds capture message*/
    private String message;
	/**New Tile position of piece */
    private Tile newTilePosition;

    /**
     * Creates an instantiation of the game board used. It sets the tiles of the game to the starting setup.
     * @param player1 the first player
     * @param player2 the second player
     * @param currentPlayer the current player selecting an animal piece and movement
     */
    public Board(Player player1, Player player2, Player currentPlayer)
    {
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = currentPlayer;

        tiles = new Tile[ROW][COL];

        for (int i = 0; i < ROW; i++)
        {
            for (int j = 0; j < COL; j++)
            {
                boolean isTrap = (i == 2 && (j == 0 || j == 8)) || 
                                 (i == 3 && (j == 1 || j == 7)) || 
                                 (i == 4 && (j == 0 || j == 8));
                boolean isHomeBase = (i == 3 && (j == 0 || j == 8));
				
                boolean isLake = (i >= 1 && i <= 2 || i >= 4 && i <= 5) && (j >= 3 && j <= 5);
                
                tiles[i][j] = new Tile(i, j, isTrap, isLake, isHomeBase);
				
            }
        }
    }
    
    /**
     * Returns the current player whose turn it is.
     * @return current player as Player object
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Sets the current player whose turn it is.
     * @param currentPlayer Player object to set as active turn
     */
    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
	
	/**
	* Initializes pieces to each player
	*/
    // sets pieces on the tile then adds the piece to the piecesOwned arrayList of players
    public void initializePieces()
    {
        // Elephant
        Elephant e1 = new Elephant(0, 2, player1, false);
        tiles[0][2].setPiece(e1);
        player1.addPiece(e1);

        Elephant  e2 = new Elephant(6, 6, player2, false);
        tiles[6][6].setPiece(e2);
        player2.addPiece(e2);

        // Lion
        Lion l1 = new Lion(6, 0, player1, false);
        tiles[6][0].setPiece(l1);
        player1.addPiece(l1);

        Lion l2 = new Lion(0, 8, player2, false);
        tiles[0][8].setPiece(l2);
        player2.addPiece(l2);

        // Tiger
        Tiger t1 = new Tiger(0, 0, player1, false);
        tiles[0][0].setPiece(t1);
        player1.addPiece(t1);

        Tiger t2 = new Tiger(6, 8, player2, false);
        tiles[6][8].setPiece(t2);
        player2.addPiece(t2);

        // Leopard
        Leopard p1 = new Leopard(4, 2, player1, false);
        tiles[4][2].setPiece(p1);
        player1.addPiece(p1); 

        Leopard p2 = new Leopard(2, 6, player2, false);
        tiles[2][6].setPiece(p2);
        player2.addPiece(p2); 

        // Wolf
        Wolf w1 = new Wolf(2, 2, player1, false);
        tiles[2][2].setPiece(w1);
        player1.addPiece(w1); 

        Wolf w2 = new Wolf(4, 6, player2, false);
        tiles[4][6].setPiece(w2);
        player2.addPiece(w2); 

        // Dog
        Dog d1 = new Dog(5, 1, player1, false);
        tiles[5][1].setPiece(d1);
        player1.addPiece(d1); 

        Dog d2 = new Dog(1, 7, player2, false);
        tiles[1][7].setPiece(d2);
        player2.addPiece(d2);

        // Cat
        Cat c1 = new Cat(1, 1, player1, false);
        tiles[1][1].setPiece(c1);
        player1.addPiece(c1); 

        Cat c2 = new Cat(5, 7, player2, false);
        tiles[5][7].setPiece(c2);
        player2.addPiece(c2);

        // Rat
        Rat r1 = new Rat(6, 2, player1, false);
        tiles[6][2].setPiece(r1);
        player1.addPiece(r1); 

        Rat r2 = new Rat(0, 6, player2, false);
        tiles[0][6].setPiece(r2);
        player2.addPiece(r2);
		
		player1.setHomeBase(tiles[3][0]);
		player2.setHomeBase(tiles[3][8]);
		
    }

	/**
	* getsTile returns tile and determines if it is out of bounds
	* @param row row of the tile
	* @param col col of the tile
    * @return the tile given the row and column
	*/
    public Tile getTile(int row, int col) {

        if (row >= 0 && row < 7 && col >= 0 && col < 9) 
            return tiles[row][col];

        return null; // out of bounds
    }

    /**
     * Moves the selected piece according to the player's specified direction, given that it is a valid movement
     * @param selectedPiece - the animal piece the player wants to move
     * @param direction - direction of movement (up, down, left, or right)
     * @param currentPlayer - player making the movement
     * @return true if the piece is successfully moved, false if otherwise
     */
    public boolean movePiece(Piece selectedPiece, String direction, Player currentPlayer) // UPDATE TO STATUS AND DISPLAYMESSAGE()
    {
		
        if (selectedPiece == null || direction == null) 
        {
            return false; // Invalid input
        }

        int rowLocation, colLocation;
        Tile currentTile, newTile;

        rowLocation = selectedPiece.getRow(); 
        colLocation = selectedPiece.getCol();
           
        currentTile = getTile(rowLocation, colLocation);
        newTile = getNewTile(rowLocation, colLocation, direction);

        if(!isValidMove(currentTile, newTile, currentPlayer, direction))
        {
            return false;
        }
            
		//crossing lake logic
        if(newTile.isLake())
        {
			
            if (selectedPiece.canCrossLake()) {
                Tile landingTile = getLandTile(currentTile, newTile, direction);
                
                if (landingTile != null) { // if it is a valid tile (no rat)
                    newTile = landingTile; // space after lake is new tile
                } else{
					return false;
				}
            }
			
        }
		
		if (newTile.isOccupied()) // if the new tile is occupied
        {
            if(canCapture(selectedPiece, newTile, currentTile)) // verifies if animal piece is capturable
            {
                setCaptureMessage(currentPlayer.getName() + " has captured the opponent's " + newTile.getPiece().getName() + "!");
			    Piece capturedPiece = newTile.getPiece(); 
				capturedPiece.getOwner().removePiece(capturedPiece);
				newTile.removePiece();
            }
            else
            {
                return false; // piece is stronger or your own
            }
                
		}
        
		currentTile.removePiece(); //remove old and set new location of piece
        newTile.setPiece(selectedPiece);
        selectedPiece.setPosition(newTile.getRow(), newTile.getCol());

		if (newTile.isTrap() && !currentPlayer.isPlayerTrap(newTile)) 
        {
			selectedPiece.setTrapped(true);
		} 
        else 
        {
			selectedPiece.setTrapped(false);
		}
		
        newTilePosition = newTile;

        return true; 
    }

    /**
     * Returns the new tile position selected by player
     * @return new tile position that the animal piece will move to
     */
    public Tile getNewTilePosition()
    {
        return newTilePosition;
    }

	/**
	* Checks if the target tile is valid and returns the direction based on current piece position and target position
	* @param selectedPiece chosen piece to move
	* @param clickedTile chosen tile piece to move to
	* @return direction direction or invalid message
	*/
    public String findDirection(Piece selectedPiece, Tile clickedTile){
		
		int sRow = selectedPiece.getRow();//row and col of selected piece
		int sCol = selectedPiece.getCol();
		
		if(clickedTile != null){ //there is a proper tile
		
			int tRow = clickedTile.getRow(); // row and col of target tile
			int tCol = clickedTile.getCol();
			
			int rowDiff = Math.abs(tRow - sRow); //checker for crossing
			int colDiff = Math.abs(tCol - sCol);
			
			int totalDiff = rowDiff + colDiff;
			
			if(totalDiff == 0){
				return "NC"; //no change in position
			}
			else if (totalDiff==3 || totalDiff == 4) { // crossing lake, vertically or horizontally
			
				if(!selectedPiece.canCrossLake() ){
					return "ADJ"; //can't cross lake
				
				} else{
					
					if (sRow == tRow) { //HORIZONTAL MOVEMENT
					
						if((sCol + 1 < 9 && tiles[sRow][sCol + 1].isLake()) || 
							(sCol - 1 >= 0 && tiles[sRow][sCol - 1].isLake())) { // if next tiles are lake -> allows jump
								
							if (tCol > sCol) { //RIGHT
								return "D";
							} else if (tCol < sCol) { //LEFT
								return "A";
							}
								
						} else{
							return "NCL";
						}
					}
					 else if (sCol == tCol) { //VERTICAL MOVEMENT
						
						if((sRow - 1 >= 0 && tiles[sRow - 1][sCol].isLake()) || 
							(sRow + 1 < 7 && tiles[sRow + 1][sCol].isLake())) { // if next tiles are lake -> allows jump
								
								if (tRow > sRow) {//DOWN
									return "S";
								} else if (tRow < sRow) { //UP
									return "W";
								}
						} else{
							return "NCL";
						}
					}	
				}
				
			} else if(totalDiff ==1){ //normal moving movement
			
				if (sRow == tRow) { //HORIZONTAL MOVEMENT
					if (tCol > sCol) { //RIGHT
						return "D";
					} else if (tCol < sCol) { //LEFT
						return "A";
					}
					
				} else if (sCol == tCol) { //VERTICAL MOVEMENT
					if (tRow > sRow) {//DOWN
						return "S";
					} else if (tRow < sRow) { //UP
						return "W";
					}
				}
				
			}
		} 
		
		return "INVALID";  // If the movement is not W,A,S,D or they didnt click a proper tile
    }


    /**
     * Determines the new tile given the row and column position of the current tile
     * @param row - current row index of the player's piece
     * @param col - current column index of the player's piece
     * @param direction - direction of the movement (up, down, left, or right)
     * @return the new tile 
     */
	public Tile getNewTile(int row, int col, String direction){
		{
			switch(direction)
			{
				case "W": 
					row--;
					break;
				case "A":
					col--;
					break;
				case "S":
					row++;
					break;
				case "D":
					col++;
					break;
				default: 
					return null;
			}

			if (row < 0 || row >= ROW || col < 0 || col >= COL) 
				return null;

			return tiles[row][col];
			
		}

	}

     /**
     * Checks if the movement the player wants to make is valid
     * @param currentTile - current tile where the selected piece is
     * @param newTile - the tile where the player wants the selected piece to be in
     * @param currentPlayer - the player making the move
     * @param direction - direction of movement (up, down, left, or right)
     * @return true if the movement is valid, otherwise false
     */
	public boolean isValidMove(Tile currentTile, Tile newTile, Player currentPlayer, String direction)
    {
        // NOTE: pieces of player cannot land on his own homebase

        // out of the bounds = NOT VAlID MOVE
        if (newTile == null)
        {
            return false; //move is out of bounds
        }
        
		//cannot land on own homebase
		if (newTile.isHomeBase())
        {
			if(newTile.equals(currentPlayer.getHomeBase())){
				return false;//cant go to your own homebase.
			}else{
				currentPlayer.setIsInOpponentHomeBase(true);
				return true;
			}
        }
	
		// for comparator of lake tiles (specifically: Lion, Tiger, Rat)
		Piece movingPiece = currentTile.getPiece();

        if (newTile.isLake()) {
            if (movingPiece.beInWater()) 
            {
                return true; //allow rat to swim
            }
			
            if (movingPiece.canCrossLake()) // if its tiger or lion
            {
				Tile landingTile = getLandTile(currentTile, newTile, direction);
                if (landingTile == null) { //if invalid tile (may rat)
                    return false; //A rat is in the lake. You may not cross.
                }
                
                if (landingTile.isOccupied() && !canCapture(movingPiece, landingTile, currentTile)) {
                        return false; // cant capture piece in landing / stronger animal
                }
            } 
            else 
            {
                return false;//animal cannot swim or cross the lake
            }
        }
		
		// check if there is a piece occupying, check if capturable (not currentPlayer's)
		if (newTile.isOccupied()) {
			if (newTile.getPiece().getOwner() == currentPlayer) //cannot capture own piece
            {
                return false; // cannot capture your own piece
            } 
				
		}
		
        return true; // if empty or trap (assuming moving to non occupied trap / tile)
    }

    /**
     * Retrieves the tile an animal piece will go to if it can cross the lake
     * @param currentTile - current tile where the selected piece is
     * @param newTile - the tile where the player wants the selected piece to be in
     * @param direction - direction of movement (up, down, left, or right)
     * @return the Tile at the new position
     */
	public Tile getLandTile(Tile currentTile, Tile newTile, String direction) {
		int rowStart = currentTile.getRow();
		int colStart = currentTile.getCol();
		int rowEnd = newTile.getRow();
		int colEnd = newTile.getCol();

		int step = 0;
		if (direction.equals("W") || direction.equals("S")) {
			step = 2; // up/down: 2 lake tiles
		} else if (direction.equals("A") || direction.equals("D")) {
			step = 3; // left/right: 3 lake tiles
		}

		int rowStep = 0;
		int colStep = 0;

		if (rowStart < rowEnd) { //S
			rowStep = 1;
		} else if (rowStart > rowEnd) { //W
			rowStep = -1;
		}

		if (colStart < colEnd) { //D
			colStep = 1;
		} else if (colStart > colEnd) { // S
			colStep = -1;
		}

		//check ALL tiles in the lake path before moving
		for (int i = 1; i <= step; i++) {
			int checkRow = rowStart + i * rowStep;
			int checkCol = colStart + i * colStep;

			// ensure valid tile before checking
			if (checkRow < 0 || checkRow >= tiles.length || checkCol < 0 || checkCol >= tiles[0].length) {
				return null; 
			}

			Tile tile = tiles[checkRow][checkCol];

			if (tile.isLake()) 
            { 
				if (tile.isOccupied() && tile.getPiece().beInWater()) 
                { 
					return null;//cannot cross the lake because of the rat
				}
			} 
            else 
            { 
				// If we hit land early, something is wrong, and we should not move
				return null; 
			}
		}

		// move to landing tile
		rowStart += (step + 1) * rowStep;
		colStart += (step + 1) * colStep;

		// double check valid tile
		if (rowStart < 0 || rowStart >= tiles.length || colStart < 0 || colStart >= tiles[0].length) {
			return null;
		}

		return tiles[rowStart][colStart]; 
	}

    /**
     * Determines if the selected piece can capture the opponent's piece on the new tile
     * @param selectedPiece - the current player's piece 
     * @param newTile - the location of the opponent's piece that the current player wants to capture
     * @param currentTile - the location of the current player's piece
     * @return true if the player's piece can capture the opponent's piece, otherwise returns false
     */
	public boolean canCapture(Piece selectedPiece, Tile newTile, Tile currentTile) {
        Piece otherPlayer = newTile.getPiece();

        if (otherPlayer.getState()) { // trapped piece = capture
            return true;
        }
		
		if (selectedPiece.getOwner() == otherPlayer.getOwner()) {
            return false; //cannot capture your own piece
        }

		if (currentTile.isLake() != newTile.isLake()) {// for land and land/ lake and lake 
			if (!selectedPiece.canCrossLake()) { //instance of tiger or lion trying to cross since newTile is current+1
				return false; // cannot capture a piece on a different terrain
			}
		}
		
		if (newTile.isLake()){

			if(selectedPiece.beInWater() && otherPlayer.beInWater()) { // rat and rat
				return selectedPiece.equals("Rat") && otherPlayer.equals("Rat");
			}
		
			if (!selectedPiece.beInWater()) {
				return false; //No other animal can capture a rat in the lake
			}
			if (!otherPlayer.beInWater()) {
				return false; // rat in water cannot capture another animal
			}
		}
		
		if (selectedPiece.equals("Rat") && otherPlayer.equals("Elephant")) {
			if (currentTile.isLake() || newTile.isLake()) {
				return false; // rat cannot capture an Elephant while in water
			}
			return true; // rat can capture elephant
		}

        if (selectedPiece.getStrength() < otherPlayer.getStrength())
        {
			return false; // cannot capture a stronger animal
        }
		
        return selectedPiece.getStrength() >= otherPlayer.getStrength(); //checks strength manually
    }

	/**
     * Determines if the board is already in the winning condition
     * @return true if one player is on the opponent's homebase or one player has no more pieces, otherwise false
     */
    public boolean checkWinningCondition() {
        return player1.isInOpponentHomeBase() || 
               player2.isInOpponentHomeBase() || 
               player1.getPieces().isEmpty() || 
               player2.getPieces().isEmpty();
    }

    
	/**
	* Sets the capture message
	* @param message string message
	*/
    public void setCaptureMessage(String message) {
        this.message = message;
    }

    /**
     * Returns the capture message
     * @return capture message
     */
    public String getCaptureMessage()
    {
        return message;
    }

    /**
     * Returns the tiles of the board
     * @return tiles of the board
     */
    public Tile[][] getTiles() {
        return tiles;
    }

    /**
     * Returns player 1
     * @return player 1 as Player object
     */
    public Player getPlayer1() {
        return player1;
    }
    
    /**
     * Returns player 2
     * @return player 2 as Player object
     */
    public Player getPlayer2() {
        return player2;
    }

}