
/**
 * Represents the controller of the Jungle King game
 */
public class JungleKingController {

    private Board model;
    private GameGUI view;

	/**Player 1's information*/
    private Player player1;
	/**Player2's information*/
    private Player player2;
	/**Information about who is currently playing*/
    private Player currentPlayer;
	/**Stores direction chosen to move*/
    private String direction; 
	/**Last tile position of the piece*/
    private Tile lastTilePosition;

    /**
     * Instantiates the controller of the Jungle King game
     * @param player1Name name of player 1
     * @param player2Name name of player 2
     * @param currentPlayer name of current player
     */
    public JungleKingController(String player1Name, String player2Name, String currentPlayer)
    {
        this.player1 = new Player(player1Name);
        this.player2 = new Player(player2Name);

        if (currentPlayer.equals(player1.getName())){
            this.currentPlayer = player1;
        	player1.setCurrentPlayer(true);
			player2.setCurrentPlayer(false);
			
        } else{
            this.currentPlayer = player2;
			player1.setCurrentPlayer(false);
			player2.setCurrentPlayer(true);
		}
		
        this.model = new Board(player1, player2, this.currentPlayer);
        this.view = new GameGUI(model, this, this.currentPlayer);

        startGame();
    }
    
	/**Starts the game by calling different methods*/
    public void startGame()
    {
        model.initializePieces();
		view.displayTurn(currentPlayer);
        view.updateCurrentPlayerLabel(currentPlayer.getName());
        view.updateStatusLabel("Select a piece to move.");
    }
	
	/**Determines if a piece or move is valid
	* @param row row of the current chosen piece
	* @param col col of the current chosen piece
	* @param currentPlayer information of the current player
	*/
	public void clickTile(int row, int col, Player currentPlayer){

        // clicked tile is the old position
        Tile clickedTile = model.getTile(row, col);
		Piece selectedPiece = this.currentPlayer.getSelectedPiece();
        
        if (selectedPiece == null)
        { // No piece is selected, so select a piece
            
			if (clickedTile.getPiece() == null) 
            { 
				view.displayMessage("No piece on this tile. Select a valid piece.");
			}
			
			// Select piece phase
			selectedPiece = clickedTile.getPiece();

            if (selectedPiece.getOwner() == this.currentPlayer) 
            { //owns the piece
			
                selectedPiece = clickedTile.getPiece();
				this.currentPlayer.setSelectedPiece(selectedPiece);
                lastTilePosition = clickedTile;
                view.updateStatusLabel("Selected " + selectedPiece.getName() + ". Click a tile to move.");
            } 
            else 
            {
                view.displayMessage("Select your own piece first.");
            }
        } 
		
        else
        { 
		
        int oldRow = selectedPiece.getRow() ;
        int oldCol = selectedPiece.getCol() ;

            // Move phase
            String direction = model.findDirection(selectedPiece, clickedTile);
			
			if(direction.equals("NC")){
			
				view.displayMessage("Invalid move! You must move from current position.");
			}
			else if (direction.equals("ADJ")) 
            {
				view.displayMessage("Invalid move! You can only move 1 tile in any direction.\nException: Lion or tiger crossing the lake");
			} 
			
			else if(direction.equals("NCL")){
				view.displayMessage("Invalid move! You can only move 2 or 3 tiles across a lake without a rat.");
			}
			
            // successful move - valid piece and movement
            else if (!direction.equals("INVALID") && model.movePiece(selectedPiece, direction, this.currentPlayer)) {
				
                int temp;
                int trapTemp;
				this.currentPlayer.setSelectedPiece(null);
                view.updateStatusLabel("Moved " + selectedPiece.getName() + " successfully!");
				
                if (player1.isCurrentPlayer())
                    temp = 1;
                else
                    temp = 2;

                if(model.getNewTilePosition().getCol() == 0 || model.getNewTilePosition().getCol() == 1)
                    trapTemp = 1;
                else    
                    trapTemp = 2;

                view.updateBoard(selectedPiece, oldRow, oldCol, direction, temp, lastTilePosition.isLake(), model.getNewTilePosition().isLake(), 
                                lastTilePosition.isTrap(), model.getNewTilePosition().isTrap(), trapTemp); 

                                String captureMessage = model.getCaptureMessage();
                if (captureMessage != null)
                {
                    view.displayCaptureMessage(captureMessage);
                    model.setCaptureMessage(null);
                }


				if(model.checkWinningCondition()){ //CHECK WINNING CONDITION 
					view.updateStatusLabel("A player has won!");
					
					view.closeGame();
					 new WinnerGUI(selectedPiece.getOwner().getName());
					player1 = null;
					player2 = null;
                    
				}
				else
                {       
					switchTurns();
				}
				
            } 
            else 
            { 
                view.displayMessage("Invalid. Try again.");
            }
        }
    }

	/**Switches turns of the players and updates labels accordingly*/
    public void switchTurns() {
        
        if (currentPlayer == player1) {
            player1.setCurrentPlayer(false);
            player2.setCurrentPlayer(true);
            currentPlayer = player2;
        } else {
            player1.setCurrentPlayer(true);
            player2.setCurrentPlayer(false);
            currentPlayer = player1;
        }

		view.displayTurn(currentPlayer);
        view.updateCurrentPlayerLabel(currentPlayer.getName());
        view.updateStatusLabel("Waiting for player to select a piece.");
	
    }
	
}

