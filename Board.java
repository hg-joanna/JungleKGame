/**
 * Represents the game board of Jungle King.
 * It consists of tiles where pieces, traps, lakes, and homebases are places. 
 * This is where piece movement occurs based on the rules of the game.
 */
public class Board {
    /**Number of rows the board has */
    private final int ROW = 7;
    /**Number of columns the board has */
    private final int COL = 9;
    /**A 2D array representing the board tiles */
    private Tile tiles[][];

    /**A player in the game*/
    private Player player1;
    /**A player in the game */
    private Player player2;

    /**
     * Instantiates a board by setting all the pieces on the board's tiles.
     * @param player1 player in the game
     * @param player2 player in the game
     */
    public Board(Player player1, Player player2)
    {
        this.player1 = player1;
        this.player2 = player2;

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

        player1.setHomeBase(tiles[3][0]);
        player2.setHomeBase(tiles[3][8]);

        player1.addTrap(tiles[2][0], tiles[3][1], tiles[4][0]);
        player2.addTrap(tiles[2][8], tiles[3][7], tiles[4][8]);

        initializePieces();
    }
    
    /**
     * Sets pieces on the tiles then adds the piece to the piecesOwned arrayList of players. 
     */
    
    public void initializePieces()
    {
        // Elephant
        Piece e1 = new Piece("Elephant", 8, 0, 2, player1, false);
        tiles[0][2].setPiece(e1);
        player1.addPiece(e1);

        Piece  e2 = new Piece("Elephant", 8, 6, 6, player2, false);
        tiles[6][6].setPiece(e2);
        player2.addPiece(e2);

        // Lion
        Panthera l1 = new Panthera("Lion", 7, 6, 0, player1, false);
        tiles[6][0].setPiece(l1);
        player1.addPiece(l1);

        Panthera l2 = new Panthera("Lion", 7, 0, 8, player2, false);
        tiles[0][8].setPiece(l2);
        player2.addPiece(l2);

        // Tiger
        Panthera t1 = new Panthera("Tiger", 6, 0, 0, player1, false);
        tiles[0][0].setPiece(t1);
        player1.addPiece(t1);

        Panthera t2 = new Panthera("Tiger", 6, 6, 8, player2, false);
        tiles[6][8].setPiece(t2);
        player2.addPiece(t2);

        // Leopard
        Piece p1 = new Piece("Leopard", 5, 4, 2, player1, false);
        tiles[4][2].setPiece(p1);
        player1.addPiece(p1); 

        Piece p2 = new Piece("Leopard", 5, 2, 6, player2, false);
        tiles[2][6].setPiece(p2);
        player2.addPiece(p2); 

        // Wolf
        Piece w1 = new Piece("Wolf", 4, 2, 2, player1, false);
        tiles[2][2].setPiece(w1);
        player1.addPiece(w1); 

        Piece w2 = new Piece("Wolf", 4, 4, 6, player2, false);
        tiles[4][6].setPiece(w2);
        player2.addPiece(w2); 

        // Dog
        Piece d1 = new Piece("Dog", 3, 5, 1, player1, false);
        tiles[5][1].setPiece(d1);
        player1.addPiece(d1); 

        Piece d2 = new Piece("Dog", 3, 1, 7, player2, false);
        tiles[1][7].setPiece(d2);
        player2.addPiece(d2);

        // Cat
        Piece c1 = new Piece("Cat", 2, 1, 1, player1, false);
        tiles[1][1].setPiece(c1);
        player1.addPiece(c1); 

        Piece c2 = new Piece("Cat", 2, 5, 7, player2, false);
        tiles[5][7].setPiece(c2);
        player2.addPiece(c2);

        // Rat
        Rat r1 = new Rat("Rat", 1, 6, 2, player1, false);
        tiles[6][2].setPiece(r1);
        player1.addPiece(r1); 

        Rat r2 = new Rat("Rat", 1, 0, 6, player2, false);
        tiles[0][6].setPiece(r2);
        player2.addPiece(r2);
    }

    /**
     *  Displays the animal legend and the current situation of the board
     */
    public void displayBoard()
    {
        String color;
        System.out.print(JungleKing.CYAN);
        System.out.println("\n┌──────────────────────────────────────────────────────────────────────────────┐");
        System.out.println("|                                " + JungleKing.GREEN + "Animal Legend" + JungleKing.CYAN + "                                 |");
        System.out.println("└──────────────────────────────────────────────────────────────────────────────┘");
        System.out.println("|  1 - E   |  2 - L  |   3 - T  |  4 - P  |  5 - W  |  6 - D |  7 - C |  8 - R |");
        System.out.println("| Elephant |  Lion   |   Tiger  | Leopard |  Wolf   |  Dog   |  Cat   |   Rat  |");
        System.out.println("|                                                                              |");
        System.out.println("| Note: The Rat(R) can capture the Elephant(E).                                |");
        System.out.println("└──────────────────────────────────────────────────────────────────────────────┘");
        System.out.println(JungleKing.RESET);
		
        System.out.println(" ┌───┬───┬───┬───┬───┬───┬───┬───┬───┐");

        for (int i = 0; i < ROW; i++)
        {
            System.out.print(" |");
            
            for (int j = 0; j < COL; j++)
            {
                if (tiles[i][j].isHomeBase()) {
                    if (tiles[i][j].isOccupied()) {
                        if (tiles[i][j].getPiece().getOwner() == player1)
                            color = JungleKing.GREEN;
                        else 
                            color = JungleKing.YELLOW;
                
                        System.out.print(color + " " + tiles[i][j].getPiece().getSymbol() + " " + JungleKing.RESET);
                    } else {
                        System.out.print(" # ");
                    }
                }
                else if(tiles[i][j].isLake())
                {
                    if(tiles[i][j].isOccupied()) 
                    {
                        if(tiles[i][j].getPiece().getOwner() == player1)
                            color = JungleKing.GREEN;
                        else 
                            color = JungleKing.YELLOW;
                        
                        System.out.print(JungleKing.BLUE_BG + color + " " + tiles[i][j].getPiece().getSymbol() + " " + JungleKing.RESET);
                    } 
                    else 
                    {
                        System.out.print(JungleKing.BLUE_BG + " ~ " + JungleKing.RESET);
                    }
                }
                else if(tiles[i][j].isTrap()) {
                    if(tiles[i][j].isOccupied()) {
                        if(tiles[i][j].getPiece().getOwner() == player1) {
                            color = JungleKing.GREEN;
                        } else {
                            color = JungleKing.YELLOW;
                        }
                        System.out.print(JungleKing.RED_BG + " " + color + tiles[i][j].getPiece().getSymbol() + JungleKing.RED_BG + " " + JungleKing.RESET);
                    } else {
                        System.out.print(JungleKing.RED_BG + "   " + JungleKing.RESET);
                    }
                }
                else if(tiles[i][j].isOccupied())
                {
                    if(tiles[i][j].getPiece().getOwner() == player1)
                        color = JungleKing.GREEN;
                    else 
                        color = JungleKing.YELLOW;

                    System.out.print(color + " " + tiles[i][j].getPiece().getSymbol() + " " + JungleKing.RESET);
                }
                else 
                    System.out.print("   ");
                
                System.out.print("|");
            }

            System.out.println();

            if (i < ROW - 1) {
                System.out.println(" ├───┼───┼───┼───┼───┼───┼───┼───┼───┤");
            }
            
        } 
        System.out.println(" └───┴───┴───┴───┴───┴───┴───┴───┴───┘");
    }

    /**
     * Retrieves the tile given the specified row and column
     * @param row - row index of the tile (must be 0 to 7)
     * @param col - column index of the tile (must be 0 to 9)
     * @return the Tile at the position 
     */
    public Tile getTile(int row, int col) {

        if (row >= 0 && row < 7 && col >= 0 && col < 9) 
            return tiles[row][col];

        // out of bounds
        return null; 
    }

    /**
     * Moves the selected piece according to the player's specified direction, given that it is a valid movement
     * @param selectedPiece - the animal piece the player wants to move
     * @param direction - direction of movement (up, down, left, or right)
     * @param currentPlayer - player making the movement
     * @return true if the piece is successfully moved, false if otherwise
     */
    public boolean movePiece(Piece selectedPiece, String direction, Player currentPlayer)
    {
        // for color formatting ----------
        String color;

        if (currentPlayer == player1)
            color = JungleKing.GREEN;
        else 
            color = JungleKing.YELLOW;
        // -------------------------------

        int rowLocation, colLocation;
        Tile currentTile, newTile;

        rowLocation = selectedPiece.getRow(); 
        colLocation = selectedPiece.getCol();
           
        currentTile = getTile(rowLocation, colLocation);
        newTile = getNewTile(rowLocation, colLocation, direction);

        if(!isValidMove(currentTile, newTile, currentPlayer, direction))
            return false;
		
		//crossing lake logic
        if(newTile.isLake())
        {
            if (selectedPiece.equals("Tiger") || selectedPiece.equals("Lion")) {
                Tile landingTile = getLandTile(currentTile, newTile, direction);
                
                if (landingTile != null) { // if it is a valid tile (no rat)
                    newTile = landingTile; // space after lake is new tile
                } else{
					return false;
				}
            }
        }
		
		if (newTile.isOccupied()) 
        {
            if(canCapture(selectedPiece, newTile, currentTile))
            {
                System.out.println("\n" + color + currentPlayer.getName() + " has captured the opponent's " + newTile.getPiece().getName() + "!" + JungleKing.RESET);
			    Piece capturedPiece = newTile.getPiece(); 
				capturedPiece.getOwner().removePiece(capturedPiece);
				newTile.removePiece();
            }
            else
            {
                return false;
            }
                
		}
        
		currentTile.removePiece();
        newTile.setPiece(selectedPiece);
        selectedPiece.setPosition(newTile.getRow(), newTile.getCol());

        // check if in opponent's homebase
        if(newTile.isHomeBase() && !newTile.equals(currentPlayer.getHomeBase()))
        {
            currentPlayer.setIsInOpponentHomeBase(true);
        }

		if (newTile.isTrap() && !currentPlayer.isPlayerTrap(newTile)) 
        {
			selectedPiece.setTrapped(true);
		} 
        else 
        {
			selectedPiece.setTrapped(false);
		}
		
        return true;
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
            System.out.println(JungleKing.RED + "Your move is out of bounds!" + JungleKing.RESET);
            return false;
        }
        
		//cannot land on own homebase
		if (newTile.isHomeBase() && newTile.equals(currentPlayer.getHomeBase()))
        {
            System.out.println(JungleKing.RED + "You cannot go to your own homebase." + JungleKing.RESET);
            return false;
        }
	
		// for comparator of lake tiles (specifically: Lion, Tiger, Rat)
		Piece movingPiece = currentTile.getPiece();

        if (newTile.isLake()) {
            if (movingPiece.beInWater()) // rat can move
            {
                return true;
            }
            if (movingPiece.canCrossLake()) // if its tiger or lion
            {
				
                Tile landingTile = getLandTile(currentTile, newTile, direction);
                
                if (landingTile == null) { //if invalid tile (may rat)
                    return false;
                }
                
                if (landingTile.isOccupied() && !canCapture(movingPiece, landingTile, currentTile)) {
						System.out.println(JungleKing.RED + "You cannot capture a stronger animal." + JungleKing.RESET);
                        return false; // cant capture piece in landing
                }
            } 
            else 
            {
				System.out.println(JungleKing.RED + "The animal cannot swim or cross the lake!" + JungleKing.RESET);
                return false;
            }
        }
		
		// check if may piece -> then check if can capture (not currentPlayer's)
		if (newTile.isOccupied()) {
			if (newTile.getPiece().getOwner() == currentPlayer) //cannot capture own piece
            {
                System.out.println(JungleKing.RED + "You cannot capture your own piece." + JungleKing.RESET);
                return false;
            } 
				
		}

        return true; // if empty or trap (assuming moving to non occupied trap)
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

		if (rowStart < rowEnd) {
			rowStep = 1;
		} else if (rowStart > rowEnd) {
			rowStep = -1;
		}

		if (colStart < colEnd) {
			colStep = 1;
		} else if (colStart > colEnd) {
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

			if (tile.isLake()) { 
				if (tile.isOccupied() && tile.getPiece().beInWater()) { 
					System.out.println(JungleKing.RED + "You cannot cross the lake because of the rat." + JungleKing.RESET);
					return null;
				}
			} else { 
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
            System.out.println(JungleKing.RED + "You cannot capture your own piece." + JungleKing.RESET);
            return false;
        }

		if (currentTile.isLake() != newTile.isLake()) {// for land and land/ lake and lake 
			if (!selectedPiece.canCrossLake()) { //instance of tiger or lion trying to cross since newTile is current+1
				System.out.println(JungleKing.RED + "You cannot capture a piece on a different terrain!" + JungleKing.RESET);
				return false;
			}
		}
		
		if (newTile.isLake()){

			if(selectedPiece.beInWater() && otherPlayer.beInWater()) { // rat and rat
				return selectedPiece.equals("Rat") && otherPlayer.equals("Rat");
			}
		
			if (!selectedPiece.beInWater()) {
				System.out.println(JungleKing.RED + "No other animal can capture a rat in the lake!" + JungleKing.RESET);
				return false;
			}
			if (!otherPlayer.beInWater()) {
				System.out.println(JungleKing.RED + "A rat in water cannot capture another animal!" + JungleKing.RESET);
				return false;
			}
		}
		
		if (selectedPiece.equals("Rat") && otherPlayer.equals("Elephant")) {
			if (currentTile.isLake() || newTile.isLake()) {
				System.out.println(JungleKing.RED + "A rat cannot capture an Elephant while in water!" + JungleKing.RESET);
				return false;
			}
			return true;
		}

        if (selectedPiece.getStrength() < otherPlayer.getStrength())
        {
            System.out.println(JungleKing.RED + "You cannot capture a stronger animal." + JungleKing.RESET);
        }

        return selectedPiece.getStrength() >= otherPlayer.getStrength();
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
	
	
}
