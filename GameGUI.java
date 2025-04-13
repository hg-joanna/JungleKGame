import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Represents the graphical user interface (view) of the Jungle King game board
 */
public class GameGUI {
    private static final int ROWS = 7;
    private static final int COLS = 9;
    private JLabel[][] tiles;  // Declare tiles as an instance variable
    private JLabel currentPlayerLabel;  // Label for current player
    private JLabel statusLabel;         // Label for status
    private JFrame frame; // Reference to the frame
	private JPanel boardPanel; //for board choosing tile/animal and display board

    private Board board;
    private JungleKingController controller;
	private Player currentPlayer;
	
    /**
     * Instantiates the graphical user interface (view) of the Jungle King game
     * @param board board of the game
     * @param controller controller of the game
     * @param currentPlayer current player of the game
     */
    public GameGUI(Board board, JungleKingController controller, Player currentPlayer) {
		
        this.board = board;
        this.controller = controller;
		this.currentPlayer = currentPlayer; 

        // frame formatting
        frame = new JFrame("Jungle King - Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1300, 900);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        // icon beside title
        ImageIcon logo = new ImageIcon("Resources/logo.png");
        frame.setIconImage(logo.getImage());

        // layer for the contents
        JLayeredPane layer = new JLayeredPane();
        layer.setBounds(0, 0, 1300, 900);

        // Background
        ImageIcon background = new ImageIcon("Resources/bg_darker.png");
        Image bg = background.getImage().getScaledInstance(1300, 900, Image.SCALE_SMOOTH);
        background = new ImageIcon(bg);
        JLabel backgroundLabel = new JLabel(background);
        backgroundLabel.setBounds(0, 0, 1300, 900);
        layer.add(backgroundLabel, Integer.valueOf(0));

        // Board panel (tiles)
        boardPanel = new JPanel(new GridLayout(ROWS, COLS));
        boardPanel.setOpaque(false);
        boardPanel.setBounds(250, 130, 800, 600); 
        
        // TILES
        tiles = new JLabel[ROWS][COLS]; // Initialize the tiles array

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                JLabel tile = new JLabel();
                tile.setHorizontalAlignment(SwingConstants.CENTER);
                tile.setOpaque(true);
                tile.setBackground(Color.WHITE);
                tile.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                int r = row;
                int c = col;

				tile.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e)
                    {
                        controller.clickTile(r, c, currentPlayer);
                    }
                });

                tiles[row][col] = tile; // Store the tile in the array
                boardPanel.add(tile);
            }
        }
        
        layer.add(boardPanel, Integer.valueOf(1));

        // Create and position the labels for current player and status
        currentPlayerLabel = new JLabel();  // Show who goes first
		
        currentPlayerLabel.setBounds(20, 20, 175, 60); 
        currentPlayerLabel.setForeground(Color.WHITE);
        currentPlayerLabel.setOpaque(true);
        currentPlayerLabel.setBackground(new Color(0, 102, 0));  // green color
		currentPlayerLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); //2px border
        currentPlayerLabel.setHorizontalAlignment(SwingConstants.LEFT);
		updateCurrentPlayerLabel(currentPlayer.getName()); // Update the turn label in the view
		
        statusLabel = new JLabel();
        statusLabel.setBounds(210, 20, 350, 60);
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setOpaque(true);
        statusLabel.setBackground(new Color(0, 102, 0));  // green bg color
		statusLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);

        layer.add(currentPlayerLabel, Integer.valueOf(2));
        layer.add(statusLabel, Integer.valueOf(2));

        // question mark for rules
        ImageIcon questionMarkIcon = new ImageIcon("Resources/mark.png");
        Image scaledQuestionMarkIcon = questionMarkIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH); 
        questionMarkIcon = new ImageIcon(scaledQuestionMarkIcon);
        
        JLabel questionMarkLabel = new JLabel(questionMarkIcon);
        questionMarkLabel.setBounds(1170, 8, 160, 100);  //maybe make it bigger pa
        questionMarkLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // rules frame
        questionMarkLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
               new RuleFrameGUI(false);
            }
        });
        
        layer.add(questionMarkLabel, Integer.valueOf(2));

        // adding game pieces icon to board
        addGamePieces();

        frame.add(layer);
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.requestFocusInWindow();

    }

    /**
     * Adds the game pieces to the board using the images in the Resources folder
     */
    private void addGamePieces() {
        // player1 icons
        ImageIcon elephant1 = new ImageIcon("Resources/elephant1.png");
        ImageIcon lion1 = new ImageIcon("Resources/lion1.png");
        ImageIcon tiger1 = new ImageIcon("Resources/tiger1.png");
        ImageIcon leopard1 = new ImageIcon("Resources/leopard1.png");
        ImageIcon cat1 = new ImageIcon("Resources/cat1.png");
        ImageIcon dog1 = new ImageIcon("Resources/dog1.png");
        ImageIcon rat1 = new ImageIcon("Resources/rat1.png");
        ImageIcon wolf1 = new ImageIcon("Resources/wolf1.png");

        // player2 icons
        ImageIcon elephant2 = new ImageIcon("Resources/elephant2.png");
        ImageIcon lion2 = new ImageIcon("Resources/lion2.png");
        ImageIcon tiger2 = new ImageIcon("Resources/tiger2.png");
        ImageIcon leopard2 = new ImageIcon("Resources/leopard2.png");
        ImageIcon cat2 = new ImageIcon("Resources/cat2.png");
        ImageIcon dog2 = new ImageIcon("Resources/dog2.png");
        ImageIcon rat2 = new ImageIcon("Resources/rat2.png");
        ImageIcon wolf2 = new ImageIcon("Resources/wolf2.png");

        // tiles icons
        ImageIcon house1 = new ImageIcon("Resources/house1.png");
        ImageIcon house2 = new ImageIcon("Resources/house2.png");
        ImageIcon trap1 = new ImageIcon("Resources/trap1.png");
        ImageIcon trap2 = new ImageIcon("Resources/trap2.png");
        ImageIcon lake = new ImageIcon("Resources/lake.png");

            // Placement for player1
        setTileIcon(0, 2, elephant1, "elephant1");
        setTileIcon(6, 0, lion1, "lion1");
        setTileIcon(0, 0, tiger1, "tiger1");
        setTileIcon(4, 2, leopard1, "leopard1");
        setTileIcon(1, 1, cat1, "cat1");
        setTileIcon(5, 1, dog1, "dog1");
        setTileIcon(6, 2, rat1, "rat1");
        setTileIcon(2, 2, wolf1, "wolf1");

        setTileIcon(2, 0, trap1, "trap1");
        setTileIcon(3, 1, trap1, "trap1");
        setTileIcon(4, 0, trap1, "trap1");
        setTileIcon(3, 0, house1, "house1");

        // Placement for player2
        setTileIcon(6, 6, elephant2, "elephant2");
        setTileIcon(0, 8, lion2, "lion2");
        setTileIcon(6, 8, tiger2, "tiger2");
        setTileIcon(2, 6, leopard2, "leopard2");
        setTileIcon(5, 7, cat2, "cat2");
        setTileIcon(1, 7, dog2, "dog2");
        setTileIcon(0, 6, rat2, "rat2");
        setTileIcon(4, 6, wolf2, "wolf2");

        setTileIcon(2, 8, trap2, "trap2");
        setTileIcon(3, 7, trap2, "trap2");
        setTileIcon(4, 8, trap2, "trap2");
        setTileIcon(3, 8, house2, "house2");

        // Lake positions
        int[][] lakePositions = {
            {1, 3}, {1, 4}, {1, 5},
            {2, 3}, {2, 4}, {2, 5},
            {4, 3}, {4, 4}, {4, 5},
            {5, 3}, {5, 4}, {5, 5},
        };

        for (int[] pos : lakePositions) {
            setTileIcon(pos[0], pos[1], lake, "lake");
        }

    }

    /**
     * Sets the icon on the tile and stores the name of the icon
     */
    private void setTileIcon(int row, int col, ImageIcon icon, String iconName) {
        tiles[row][col].setIcon(icon); 
        tiles[row][col].putClientProperty("iconName", iconName); 
    }

    /**
     * Returns the ImageIcon on the specified row and column
     * @param row specified row
     * @param col specified column
     * @return image on the tile
     */
    public ImageIcon getTileIcon(int row, int col) {
        if (row < 0 || row >= ROWS || col < 0 || col >= COLS) {
            return null; // Return null for invalid positions
        }
    
        Icon icon = tiles[row][col].getIcon(); // Get the icon from the tile
        if (icon instanceof ImageIcon imageIcon) {
            return imageIcon; // Return the ImageIcon if it exists
        }
    
        return null; // Return null if no icon is set
    }

    /**
     * Updates the board game
     * @param selectedPiece the piece selected by the player
     * @param oldRow the row where the piece is currently located
     * @param oldCol the column where the piece is currently located
     * @param direction direction of the movement
     * @param playerNo the current player
     * @param oldTileLake true if the old tile is a lake, otherwise false
     * @param newTileLake true if the new tile is a lake, otherwise false
     * @param oldTileTrap true if the old tile contains a trap, otherwise false
     * @param newTileTrap true if the new tile contains a trap, otherwise false
     * @param trapOwner owner of the trap the piece is on or the piece will move to
     */
    public void updateBoard(Piece selectedPiece, int oldRow, int oldCol, String direction, int playerNo, boolean oldTileLake, boolean newTileLake, 
                            boolean oldTileTrap, boolean newTileTrap, int trapOwner)
    {
       Object iconName = tiles[oldRow][oldCol].getClientProperty("iconName");
	   
        Icon oldIcon = tiles[oldRow][oldCol].getIcon();
        int newRow = selectedPiece.getRow();
        int newCol = selectedPiece.getCol();

        ImageIcon lake = new ImageIcon("Resources/lake.png"); 
        ImageIcon rat1 = new ImageIcon("Resources/rat1.png");
        ImageIcon rat2 = new ImageIcon("Resources/rat2.png");

        ImageIcon trap1 = new ImageIcon("Resources/trap1.png");
        ImageIcon trap2 = new ImageIcon("Resources/trap2.png");        


        // CHANGING NEW TILE
        if (newTileLake) // if new tile is a lake
        {

            if (playerNo == 1)
            {
                ImageIcon ratInLakeP1 = new ImageIcon("Resources/lake_Rat1.png");
                tiles[newRow][newCol].setIcon(ratInLakeP1);
                tiles[newRow][newCol].putClientProperty("iconName", "lake_Rat1");
            }
            else
            {
                ImageIcon ratInLakeP2 = new ImageIcon("Resources/lake_Rat2.png");
                tiles[newRow][newCol].setIcon(ratInLakeP2);
                tiles[newRow][newCol].putClientProperty("iconName", "lake_Rat2");
            }
                
        }
        // TO IMPLEMENT
        else if (newTileTrap)
        {
            // get the icon name and replace with its rat equivalent
            // check owner as well
            String fileName = null;
         
            // debugging
            if (iconName != null) {
                fileName = "Resources/Traps/trap" + trapOwner + "_" + iconName.toString().substring(0,1).toUpperCase() + iconName.toString().substring(1) + ".png"; 
            } else {
                System.out.println("iconName is null, cannot generate fileName.");
            }

 

            ImageIcon trappedImage = new ImageIcon(fileName);

            tiles[newRow][newCol].setIcon(trappedImage);
            tiles[newRow][newCol].putClientProperty("iconName", "trap" + trapOwner + "_" + iconName);

        }
        else
        {
            // getting out of lake
            if (selectedPiece instanceof Rat && oldTileLake)
            {    
                if (playerNo == 1)
                {
                    tiles[newRow][newCol].setIcon(rat1);
                    tiles[newRow][newCol].putClientProperty("iconName", "Rat1");
                }
                else
                {
                    tiles[newRow][newCol].setIcon(rat2);
                    tiles[newRow][newCol].putClientProperty("iconName", "Rat2");
                }

            }

            else if (oldTileTrap)
            {

                String fileNameAnimal = "Resources/" + selectedPiece.getName() +  playerNo + ".png";
                //System.out.println("FILE NAME ANIMAL " + fileNameAnimal);
                ImageIcon animal = new ImageIcon(fileNameAnimal);
                tiles[newRow][newCol].setIcon(animal);
                tiles[newRow][newCol].putClientProperty("iconName", selectedPiece.getName() + playerNo);
            }



            // normal empty tile
            else
            {
                tiles[newRow][newCol].setIcon(oldIcon);
                tiles[newRow][newCol].putClientProperty("iconName", iconName);
            }
      
        }

        // CHANGING OLD TILE
        if (oldTileLake)
        {
            tiles[oldRow][oldCol].setIcon(lake); 
            tiles[oldRow][oldCol].putClientProperty("iconName", "lake");
        }
        else if (oldTileTrap)
        {
            if (oldCol == 0 || oldCol == 1)
            {
                tiles[oldRow][oldCol].setIcon(trap1);
                tiles[oldRow][oldCol].putClientProperty("iconName", "trap1");
            }    
            else 
            {
                tiles[oldRow][oldCol].setIcon(trap2); 
                tiles[oldRow][oldCol].putClientProperty("iconName", "trap2");
            }
                 

        }
        else
        {
            tiles[oldRow][oldCol].setIcon(null); // clear old
            tiles[oldRow][oldCol].putClientProperty("iconName", null);
        }
        
        boardPanel.revalidate(); // updates board gui
        frame.repaint();
	}
	
	/**
     * Updates the status label
     * @param statusMessage message to be displayed oon label
     */
	public void updateStatusLabel(String statusMessage) {
		statusLabel.setText(" Status: " + statusMessage);
	}

	/**
     * Updates the current player label
     * @param name name of the player
     */
	public void updateCurrentPlayerLabel(String name)
	{
		currentPlayerLabel.setText(" Current Player: " + name);
	}

	/**Creates a JOptionPane to notify user
	* @param message string which has the warning
	*/
    public void displayMessage(String message)
    {
        JOptionPane.showMessageDialog(null, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }

	/**Displays the animal capture message
	* @param captureMessage string that holds the animal pieces captured
	*/
    public void displayCaptureMessage(String captureMessage)
    {
        JOptionPane.showMessageDialog(null, captureMessage, "Animal Captured",JOptionPane.INFORMATION_MESSAGE);
    }

	/**
     * Changes the color of the currentPlayerLabel based on the current player
     * @param currentPlayer current player of the game
     */
    public void displayTurn(Player currentPlayer)
    {
        if (currentPlayer.getName().equals(board.getPlayer1().getName()))
        {
            currentPlayerLabel.setBackground(new Color(147,112,219));
        }
        else
        {
            currentPlayerLabel.setBackground(new Color(184,134,11));
        }
    }
	
	/**diposes the GameGUI frame*/
	public void closeGame(){
		frame.dispose();
	}

	
 }
	
 