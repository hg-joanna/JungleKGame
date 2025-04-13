import java.util.*;

/**
 * The main game controller for Jungle King
 * It manages the game loops, player turns, and allows players to make their moves. 
 */
public class JungleKing {
    /**Board containing tiles and pieces */
    private Board board;
    /**Player in the game */
    private Player player1;
    /**Player in the game */
    private Player player2;

    /**Current player of the game */
    private Player currentPlayer;
    /**A list of randomly arranged animal pieces */
    private ArrayList<Piece> randomAnimals;

    Scanner input = new Scanner(System.in);

    // These attributes will not be shown in JavaDoc files 
    /**@hidden */
    public static final String RESET = "\u001B[0m"; /**@hidden */
    public static final String RED = "\u001B[31m"; /**@hidden */
    public static final String GREEN = "\u001B[32m"; /**@hidden */
    public static final String YELLOW = "\u001B[33m"; /**@hidden */
    public static final String BLUE = "\u001B[34m"; /**@hidden */
    public static final String PURPLE = "\u001B[35m"; /**@hidden */
    public static final String CYAN = "\u001B[36m"; 

    /**@hidden */
	public static final String BLUE_BG = "\u001B[44m"; /**@hidden */
	public static final String RED_BG = "\u001B[41m"; 
    //---------------------------------------------------------------

    /**
     * Instantiates the game
     */
    public JungleKing()
    {
        player1 = new Player("Player 1");
        player2 = new Player("Player 2");
        board = new Board(player1, player2);
        this.randomAnimals = new ArrayList<>();
    }

    /**
     *  Displays the game menu of JungleKing 
     */
    public void displayStartGame()
    {
        int menuInput;

        System.out.println("---------------------------------");
        System.out.println(GREEN + "     Welcome to Jungle King!" + RESET);
        System.out.println("---------------------------------");
        System.out.println("[1] Start Game");
        System.out.println("[2] View Rules");
        System.out.println("[3] Exit Game");
        System.out.print("\nEnter: ");

        menuInput = getMenuInput();

        switch(menuInput)
        {
            case 1: 
                startGame();
                break;
            case 2: 
                displayRules();
                break;
            case 3: 
                input.close();
                System.exit(0);
                break;
        }

    }

    /**
     * Returns the user's input for the game menu (either 1, 2, or 3). This method is to ensure that the user only input valid integer values.
     * @return 1 if starting the game, 2 if displaying rules, or 3 if exiting the game
     */
    public int getMenuInput()
    {
        int menuInput;

        while(true)
        {
            if (input.hasNextInt())
            {
                menuInput = input.nextInt();

                if(menuInput >= 1 && menuInput <= 3)
                {
                    input.nextLine();
                    return menuInput;
                }
                    
                else
                    System.out.println("Invalid input. Please try again."); 
            }
            else 
            {
                System.out.println("Invalid input. Please try again."); 
                input.next();
            }
        }
    }

    /**
     * Prints out the rules of the game. Enter is pressed to go back to the game menu.
     */
    public void displayRules()
    {
        System.out.println(CYAN + "\n------------------ JUNGLE KING RULES ------------------" + RESET);
    
        System.out.println(YELLOW + "\n1. Objective of the Game" + RESET);
        System.out.println(" - Capture your opponent's pieces.");
        System.out.println(" - TO WIN, you must reach your opponent's homebase (#). ");

        System.out.println(YELLOW + "\n2. Animal Hierarchy " + RESET);
        System.out.println(" (1) Elephant  > (2) Lion   > (3) Tiger   > (4) Leopard");
        System.out.println(" (5) Wolf      > (6) Dog    > (7) Cat     > (8) Rat\n");
        System.out.println("  - The stronger animal can capture a weaker one. \n   (E.g. The Elephant (1) can capture a Cat (7))");
        System.out.println("  - Exception: The Rat (8) can defeat the Elephant (1) since it can sneak into the elephant's ears.");
		System.out.println("  - Note: The animals are represented by the first letter of their name except the Leopard (denoted by P).");

        System.out.println(YELLOW + "\n3. Movement Rules" + RESET);
        System.out.println(" - Animals can move Up (W), Left (A), Down (S), or Right (D) given that there is an empty tile next to it.");
        System.out.println(" - Tigers & Lions can cross the lakes horizontally or vertically, as long as there is no rat along the way.");
        System.out.println(" - Rats can swim on the lake. When the rat is on the lake, animals on land cannot capture it. \n   However, the rat may still capture the opponent's rat on the lake.");

        System.out.println(YELLOW + "\n4. Tiles" + RESET);
        System.out.println(" - " + RED_BG + "Traps" + RESET + ": Traps limit the opponent's attack, as any player piece may capture the piece on the trap. \n   It can only regain its strength once it leaves the trap.");
        System.out.println(" - " + BLUE_BG + "Rivers" + RESET + ": Only Rats can enter rivers.");

        System.out.println(CYAN + "\n--------------------------------------------------------" + RESET);
        System.out.println("Press Enter to return to the main menu...");
        input.nextLine(); 
		displayStartGame();
        
    }

    /**
     * Allows the players to input their names, determine the first player, and loops the game proper.
     */
    public void startGame()
    {
        String name1, name2;
        System.out.println(CYAN + "\nStarting game..." + RESET);
    
        System.out.print(GREEN + "Enter Player 1's Name: " + RESET);
        name1 = input.nextLine();
        player1.setName(name1);

        System.out.print(YELLOW + "Enter Player 2's Name: " + RESET);
        name2 = input.nextLine();
        player2.setName(name2);

        // Selecting an animal piece to determine the first player
        System.out.println();
        System.out.println("Welcome " + GREEN + player1.getName() + RESET + " and " + YELLOW + player2.getName() + RESET + "! \nYou must now select an animal piece. Choose from 1-8.\n");

        randomizeAnimals();
        displayAnimalPieces();

        System.out.println();
        System.out.print(GREEN + "[" + player1.getName() + "] Enter: " + RESET);
        int p1Choice = getAnimalRandomizerInput() - 1;

        System.out.print(YELLOW + "[" + player2.getName() + "] Enter: " + RESET);
        int p2Choice = getAnimalRandomizerInput() - 1;

        Piece p1Animal = randomAnimals.get(p1Choice);
        Piece p2Animal = randomAnimals.get(p2Choice);

        determinePlayer(p1Animal, p2Animal);

        System.out.println(CYAN + "\nDisplaying board..." + RESET);
        gameLoop();
    } 

    /**
     * Loops the game until the winning condition is met.
     */
    public void gameLoop()
    {
        String direction;
        Piece selectedPiece;
        boolean isSuccessfullyMoved;
		boolean gameOver = false;

        while (!gameOver)
        {
            // for color formatting ----------
            String color;

            if (currentPlayer == player1)
                color = GREEN;
            else 
                color = YELLOW;
            // -------------------------------

            board.displayBoard();
            System.out.println(color + currentPlayer.getName() + "'s turn!" + RESET);
		
			displayRemainingPieces(currentPlayer);
		
            selectedPiece = getAnimalPiece();
            direction = getDirection();
            
            isSuccessfullyMoved = board.movePiece(selectedPiece, direction, currentPlayer);

            if (isSuccessfullyMoved)
                switchTurns();
            else 
                System.out.println(RED + "Invalid move! Please enter again." + RESET);
            
			gameOver = checkWinningCondition();
            
        }

        board.displayBoard();
        playAgain();
    }

    /**
     * Returns the piece selected by the player to move. The input must be a valid animal symbol, otherwise, it will ask the user to input again
     * @return Piece
     */
    public Piece getAnimalPiece()
    {
        String selectedPieceSymbol;
		List<String> validSymbols = Arrays.asList("E", "L", "T", "P", "W", "D", "C", "R");

        // for color formatting ----------
        String color;

        if (currentPlayer == player1)
            color = GREEN;
        else 
            color = YELLOW;
        // -------------------------------
        

        while (true)
        {
			
            System.out.print(color + "[" + currentPlayer.getName() + "]" + RESET + " Enter an animal piece to move: ");
            selectedPieceSymbol = input.nextLine().trim().toUpperCase();

            // Check if the input is a valid symbol
			// Check if input is a valid symbol
			if (validSymbols.contains(selectedPieceSymbol)) {
				// Check if player still has the piece
				for (Piece piece : currentPlayer.getPieces()) {
					if (piece.getSymbol().equals(selectedPieceSymbol)) {
						return piece; // Valid piece found, return it
					}
				}
				System.out.println(RED + "The chosen animal has been captured. Please select another." + RESET);
			} else {
				System.out.println(RED + "Invalid input! Please enter a valid animal symbol [E, L, T, P, W, D, C, or R]." + RESET);
			}
        }

    }

    /**
     * Returns the W,A,S, or D input of user as their chosen movement for their selected animal piece. The input must be valid, otherwise it will ask the player for another input.
     * @return "W" if up, "A" if left, "S" if down, or "D" if right
     */
    public String getDirection()
    {
        String direction;

        // for color formatting ----------
        String color;

        if (currentPlayer == player1)
            color = GREEN;
        else 
            color = YELLOW;
        // -------------------------------
        

        while(true)
        {
            System.out.print(color + "[" + currentPlayer.getName() + "]" + RESET + " Enter movement (W, A, S, or D): ");
            direction = input.nextLine().trim().toUpperCase();

            if (direction.equals("W") || direction.equals("A") || direction.equals("S") || direction.equals("D"))
            {
                return direction;
            }
            
            System.out.println(RED + "Invalid input! Please enter W, A, S, or D." + RESET);
        }        
    }

    /**
     * Displays the numerical inputs players will choose from for their animal piece before the game starts.
     */
    public void displayAnimalPieces()
    {
        System.out.println("  ┌───┬───┬───┬───┐");
        System.out.println("  | 1 | 2 | 3 | 4 |");
        System.out.println("  ├───┼───┼───┼───┤");
        System.out.println("  | 5 | 6 | 7 | 8 |");
        System.out.println("  └───┴───┴───┴───┘");
    }

    /**
     * Gets the player's input from 1-8 on their selected random animal piece. The input must be valid, otherwise, it will ask the user to input again.
     * @return an integer value from 1 to 8 depending on the user's choice
     */
    public int getAnimalRandomizerInput()
    {
        int animalInput;

        while(true)
        {
            if (input.hasNextInt())
            {
                animalInput = input.nextInt();

                if(animalInput >= 1 && animalInput <= 8)
                {
                    input.nextLine();
                    return animalInput;
                }
                    
                else
                    System.out.println("Invalid input. Please try again."); 
            }
            else 
            {
                System.out.println("Invalid input. Please try again."); 
                input.next();
            }
        }
    }

    /**
     * Reveals the positions of the animals in the selection board according to the randomizer.
     */
    public void revealAnimalPieces()
    {
        System.out.println(CYAN + "\nRevealing board..." + RESET);
        String row0 = String.format("  | %s | %s | %s | %s |",
            randomAnimals.get(0).getSymbol(), randomAnimals.get(1).getSymbol(),
            randomAnimals.get(2).getSymbol(), randomAnimals.get(3).getSymbol());

        String row1 = String.format("  | %s | %s | %s | %s |",
            randomAnimals.get(4).getSymbol(), randomAnimals.get(5).getSymbol(),
            randomAnimals.get(6).getSymbol(), randomAnimals.get(7).getSymbol());

        System.out.println("  ┌───┬───┬───┬───┐");
        System.out.println(row0);
        System.out.println("  ├───┼───┼───┼───┤");
        System.out.println(row1);
        System.out.println("  └───┴───┴───┴───┘");
    }

    /**
     * Randomizes the animal pieces from the selection board in the method above. It uses the Collections class's .shuffle() method from java.util to shuffle the animals's order in the array list.
     */
    public void randomizeAnimals()
    {
        randomAnimals = new ArrayList<>();

        randomAnimals.add(new Piece("Elephant", 8));
        randomAnimals.add(new Panthera("Lion", 7));
        randomAnimals.add(new Panthera("Tiger", 6));
        randomAnimals.add(new Piece("Leopard", 5));
        randomAnimals.add(new Piece("Wolf", 4));
        randomAnimals.add(new Piece("Dog", 3));
        randomAnimals.add(new Piece("Cat", 2));
        randomAnimals.add(new Piece("Rat", 1));
        
        Collections.shuffle(randomAnimals);
    }

    /**
     * Determines which player is the first to play depending on the selected animal pieces's strengths.
     * @param p1 represents the animal piece selected by player 1
     * @param p2 represents the animal piece selected by player 2
     */
    public void determinePlayer(Piece p1, Piece p2)
    {
        revealAnimalPieces();

        System.out.println(GREEN + player1.getName() + " got " + p1.getName() + RESET + " and " + YELLOW + player2.getName() + " got " + p2.getName() + "." + RESET);
        if (p1.getStrength() > p2.getStrength())
        {
            currentPlayer = player1;
            player1.setCurrentPlayer(true);
            System.out.println(GREEN + player1.getName() + " plays first!" + RESET);
        }
        else
        {
            currentPlayer = player2;
            player2.setCurrentPlayer(true);
            System.out.println(YELLOW + player2.getName() + " plays first!" + RESET);
        }
    }

    /**
     * Used in the game loop to switch the turns of players after they have made their move. 
     */
    public void switchTurns()
    {
        if (currentPlayer == player1)
        {
            // player2 will be the current player
            player1.setCurrentPlayer(false);
            player2.setCurrentPlayer(true);
            currentPlayer = player2;
        }
        else
        {
            // player1 will be the current player
            player1.setCurrentPlayer(true);
            player2.setCurrentPlayer(false);
            currentPlayer = player1;
        }
    }

    /**
     * Displays the remaining pieces a player has during their respective turns on the game.
     * @param player - represents the current player
     */
	public void displayRemainingPieces(Player player) {
		
		// for color formatting ----------
        String color;

        if (currentPlayer == player1)
            color = GREEN;
        else 
            color = YELLOW;
        // -------------------------------
        
		System.out.print(color + "Remaining Pieces:"+ RESET);

		for (Piece piece : currentPlayer.getPieces()) {
			System.out.print(color + piece.getSymbol() + " " + RESET);
		}
		System.out.println();
	}

    /**
     * Checks if any player is already in their opponent's homebase. It prints out the winning message.
     * @return true if any player has won, otherwise it returns false
     */
    public boolean checkWinningCondition() {
        if (player1.isInOpponentHomeBase()) {
            System.out.println(GREEN + player1.getName() + " wins!"+ RESET);
            return true;
        }
        if (player2.isInOpponentHomeBase()) {
            System.out.println(YELLOW + player2.getName() + " wins!"+ RESET);
            return true;
        }
        if (player1.getPieces().isEmpty()) {
            System.out.println(YELLOW + player2.getName() + " wins as " + player1.getName() + " has no pieces left!" + RESET);
            return true;
        }
        if (player2.getPieces().isEmpty()) {
            System.out.println(GREEN + player1.getName() + " wins as " + player2.getName() + " has no pieces left!" + RESET);
            return true;
        }
        return false;
    }

    /**
     * Asks if the players want to play another round of JungleKing. If yes, it will reset the game. Otherwise, it will exit the game.
     */
    public void playAgain()
    {
        System.out.print("\n\nDo you want to play again (Y/N)? ");
        String ans = input.nextLine().trim().toUpperCase();

        if (ans.equals("Y"))
        {
            resetGame();
            startGame();
        }
        else
        {
            System.out.println(CYAN + "Exiting game..." + RESET);
            input.close();
            System.exit(0);
        }
    }

    /**
     * Resets the game if another round is played.
     */
    public void resetGame(){
        player1 = new Player("Player 1");
        player2 = new Player("Player 2");
        board = new Board(player1, player2);
        randomAnimals.clear();
        currentPlayer = null;
    }

    /**
     * Main method of the game
     * @param args
     */
    public static void main(String[] args) {
        JungleKing game = new JungleKing();
        game.displayStartGame();
    }
	
}
