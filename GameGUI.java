import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Represents the graphical user interface (view) of the Jungle King game board.
 */
public class GameGUI {
    private static final int ROWS = 7;
    private static final int COLS = 9;
    private JLabel[][] tiles;
    private JLabel currentPlayerLabel;
    private JLabel statusLabel;
    private JFrame frame;
    private JPanel boardPanel;

    private Board board;
    private JungleKingController controller;

    /**
     * Instantiates the graphical user interface of the Jungle King game.
     * @param board Game board model
     * @param controller Game controller instance
     * @param currentPlayer Initial starting player
     */
    public GameGUI(Board board, JungleKingController controller, Player currentPlayer) {
        this.board = board;
        this.controller = controller;

        // Frame formatting
        frame = new JFrame("Jungle King - Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1300, 900);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        // Window icon
        ImageIcon logo = new ImageIcon("Resources/logo.png");
        frame.setIconImage(logo.getImage());

        // Container layer
        JLayeredPane layer = new JLayeredPane();
        layer.setBounds(0, 0, 1300, 900);

        // Background image
        ImageIcon background = new ImageIcon("Resources/bg_darker.png");
        Image bg = background.getImage().getScaledInstance(1300, 900, Image.SCALE_SMOOTH);
        background = new ImageIcon(bg);
        JLabel backgroundLabel = new JLabel(background);
        backgroundLabel.setBounds(0, 0, 1300, 900);
        layer.add(backgroundLabel, Integer.valueOf(0));

        // Board panel grid
        boardPanel = new JPanel(new GridLayout(ROWS, COLS));
        boardPanel.setOpaque(false);
        boardPanel.setBounds(250, 130, 800, 600);
        
        tiles = new JLabel[ROWS][COLS];

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                JLabel tile = new JLabel();
                tile.setHorizontalAlignment(SwingConstants.CENTER);
                tile.setOpaque(true);
                tile.setBackground(Color.WHITE);
                tile.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                final int r = row;
                final int c = col;

                tile.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        controller.clickTile(r, c);
                    }
                });

                tiles[row][col] = tile;
                boardPanel.add(tile);
            }
        }
        
        layer.add(boardPanel, Integer.valueOf(1));

        // Display current turn & client role labels
        currentPlayerLabel = new JLabel();
        currentPlayerLabel.setBounds(20, 20, 250, 60); 
        currentPlayerLabel.setForeground(Color.WHITE);
        currentPlayerLabel.setOpaque(true);
        currentPlayerLabel.setBackground(new Color(0, 102, 0));
        currentPlayerLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        currentPlayerLabel.setHorizontalAlignment(SwingConstants.LEFT);
		
        statusLabel = new JLabel(" Status: Game started.");
        statusLabel.setBounds(280, 20, 350, 60);
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setOpaque(true);
        statusLabel.setBackground(new Color(0, 102, 0));
        statusLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);

        layer.add(currentPlayerLabel, Integer.valueOf(2));
        layer.add(statusLabel, Integer.valueOf(2));

        // Help rules button
        ImageIcon questionMarkIcon = new ImageIcon("Resources/mark.png");
        Image scaledQuestionMarkIcon = questionMarkIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH); 
        questionMarkIcon = new ImageIcon(scaledQuestionMarkIcon);
        
        JLabel questionMarkLabel = new JLabel(questionMarkIcon);
        questionMarkLabel.setBounds(1170, 8, 160, 100);
        questionMarkLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        questionMarkLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
               new RuleFrameGUI(false);
            }
        });
        
        layer.add(questionMarkLabel, Integer.valueOf(2));

        // Add game piece graphics
        addGamePieces();

        frame.add(layer);
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.requestFocusInWindow();
    }

    /**
     * Initializes default pieces and tile overlays on the grid.
     */
    private void addGamePieces() {
        // Player 1 piece icons
        ImageIcon elephant1 = new ImageIcon("Resources/elephant1.png");
        ImageIcon lion1 = new ImageIcon("Resources/lion1.png");
        ImageIcon tiger1 = new ImageIcon("Resources/tiger1.png");
        ImageIcon leopard1 = new ImageIcon("Resources/leopard1.png");
        ImageIcon cat1 = new ImageIcon("Resources/cat1.png");
        ImageIcon dog1 = new ImageIcon("Resources/dog1.png");
        ImageIcon rat1 = new ImageIcon("Resources/rat1.png");
        ImageIcon wolf1 = new ImageIcon("Resources/wolf1.png");

        // Player 2 piece icons
        ImageIcon elephant2 = new ImageIcon("Resources/elephant2.png");
        ImageIcon lion2 = new ImageIcon("Resources/lion2.png");
        ImageIcon tiger2 = new ImageIcon("Resources/tiger2.png");
        ImageIcon leopard2 = new ImageIcon("Resources/leopard2.png");
        ImageIcon cat2 = new ImageIcon("Resources/cat2.png");
        ImageIcon dog2 = new ImageIcon("Resources/dog2.png");
        ImageIcon rat2 = new ImageIcon("Resources/rat2.png");
        ImageIcon wolf2 = new ImageIcon("Resources/wolf2.png");

        // Special tile icons
        ImageIcon house1 = new ImageIcon("Resources/house1.png");
        ImageIcon house2 = new ImageIcon("Resources/house2.png");
        ImageIcon trap1 = new ImageIcon("Resources/trap1.png");
        ImageIcon trap2 = new ImageIcon("Resources/trap2.png");
        ImageIcon lake = new ImageIcon("Resources/lake.png");

        // Player 1 placements
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

        // Player 2 placements
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

        // Lake placements
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

    private void setTileIcon(int row, int col, ImageIcon icon, String iconName) {
        tiles[row][col].setIcon(icon); 
        tiles[row][col].putClientProperty("iconName", iconName); 
    }

    /**
     * Updates board visuals when a move is executed.
     */
    public void updateBoard(Piece selectedPiece, int oldRow, int oldCol, String direction, int playerNo, boolean oldTileLake, boolean newTileLake, 
                            boolean oldTileTrap, boolean newTileTrap, int trapOwner) {
        Object iconName = tiles[oldRow][oldCol].getClientProperty("iconName");
        Icon oldIcon = tiles[oldRow][oldCol].getIcon();
        int newRow = selectedPiece.getRow();
        int newCol = selectedPiece.getCol();

        ImageIcon lake = new ImageIcon("Resources/lake.png"); 
        ImageIcon rat1 = new ImageIcon("Resources/rat1.png");
        ImageIcon rat2 = new ImageIcon("Resources/rat2.png");

        ImageIcon trap1 = new ImageIcon("Resources/trap1.png");
        ImageIcon trap2 = new ImageIcon("Resources/trap2.png");        

        // Updating target tile
        if (newTileLake) {
            if (playerNo == 1) {
                ImageIcon ratInLakeP1 = new ImageIcon("Resources/lake_Rat1.png");
                tiles[newRow][newCol].setIcon(ratInLakeP1);
                tiles[newRow][newCol].putClientProperty("iconName", "lake_Rat1");
            } else {
                ImageIcon ratInLakeP2 = new ImageIcon("Resources/lake_Rat2.png");
                tiles[newRow][newCol].setIcon(ratInLakeP2);
                tiles[newRow][newCol].putClientProperty("iconName", "lake_Rat2");
            }
        } else if (newTileTrap) {
            String fileName = null;
            if (iconName != null) {
                fileName = "Resources/Traps/trap" + trapOwner + "_" + iconName.toString().substring(0,1).toUpperCase() + iconName.toString().substring(1) + ".png"; 
            }
            ImageIcon trappedImage = new ImageIcon(fileName);
            tiles[newRow][newCol].setIcon(trappedImage);
            tiles[newRow][newCol].putClientProperty("iconName", "trap" + trapOwner + "_" + iconName);
        } else {
            if (selectedPiece instanceof Rat && oldTileLake) {    
                if (playerNo == 1) {
                    tiles[newRow][newCol].setIcon(rat1);
                    tiles[newRow][newCol].putClientProperty("iconName", "Rat1");
                } else {
                    tiles[newRow][newCol].setIcon(rat2);
                    tiles[newRow][newCol].putClientProperty("iconName", "Rat2");
                }
            } else if (oldTileTrap) {
                String fileNameAnimal = "Resources/" + selectedPiece.getName() +  playerNo + ".png";
                ImageIcon animal = new ImageIcon(fileNameAnimal);
                tiles[newRow][newCol].setIcon(animal);
                tiles[newRow][newCol].putClientProperty("iconName", selectedPiece.getName() + playerNo);
            } else {
                tiles[newRow][newCol].setIcon(oldIcon);
                tiles[newRow][newCol].putClientProperty("iconName", iconName);
            }
        }

        // Updating source tile
        if (oldTileLake) {
            tiles[oldRow][oldCol].setIcon(lake); 
            tiles[oldRow][oldCol].putClientProperty("iconName", "lake");
        } else if (oldTileTrap) {
            if (oldCol == 0 || oldCol == 1) {
                tiles[oldRow][oldCol].setIcon(trap1);
                tiles[oldRow][oldCol].putClientProperty("iconName", "trap1");
            } else {
                tiles[oldRow][oldCol].setIcon(trap2); 
                tiles[oldRow][oldCol].putClientProperty("iconName", "trap2");
            }
        } else {
            tiles[oldRow][oldCol].setIcon(null);
            tiles[oldRow][oldCol].putClientProperty("iconName", null);
        }
        
        boardPanel.revalidate();
        frame.repaint();
    }

    /**
     * Updates turn status label and highlights whether it is the local client's turn.
     */
    public void updateTurnDisplay(Player activePlayer, Player localAssignedPlayer) {
        boolean isMyTurn = activePlayer.getName().equals(localAssignedPlayer.getName());
        String turnStatus = isMyTurn ? " (YOUR TURN)" : " (Opponent's Turn)";
        
        currentPlayerLabel.setText(" Turn: " + activePlayer.getName() + turnStatus);
        
        if (activePlayer.equals(board.getPlayer1())) {
            currentPlayerLabel.setBackground(new Color(147, 112, 219));
        } else {
            currentPlayerLabel.setBackground(new Color(184, 134, 11));
        }
    }

    public void updateStatusLabel(String statusMessage) {
        statusLabel.setText(" Status: " + statusMessage);
    }

    public void displayMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Notice", JOptionPane.WARNING_MESSAGE);
    }

    public void displayCaptureMessage(String captureMessage) {
        JOptionPane.showMessageDialog(null, captureMessage, "Animal Captured", JOptionPane.INFORMATION_MESSAGE);
    }

    public void closeGame() {
        frame.dispose();
    }
}