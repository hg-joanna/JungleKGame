import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.*;

/**
 * Represents the controller architecture of the card selection process of players
 */
public class CardSelectionController {
	/**View architecture of the card selection process */
    private CardSelectionGUI view;
	/**Model architecture of the card selection process */
    private CardSelectionLogic logic;
    /**Player1's name */
    private String player1Name;
    /**Player2's name */
    private String player2Name;
    /**Strength of player1's chosen card*/
    private int player1CardSelection = -1;
	/**Strength of player2's chosen card*/
    private int player2CardSelection = -1;
    
	/**
	 * Instantiates the controller for the card selection process
	 * @param player1Name name of the first player as a String
	 * @param player2Name name of the second player as a String
	 */
    public CardSelectionController(String player1Name, String player2Name) {
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.logic = new CardSelectionLogic(player1Name, player2Name);
        
        List<Integer> animalOrder = new ArrayList<>();
        for (int i = 0; i < 8; i++) 
		animalOrder.add(i);
        
		Collections.shuffle(animalOrder);
		
        ImageIcon cardBackSide = new ImageIcon("Resources/card.png");
        this.view = new CardSelectionGUI(this, animalOrder, cardBackSide);
    }
    
	/**
	 * Handles the selection of the card when a player clicks on a button. This acts as an intermediate between the view and model part of the architecture. 
	 * @param buttonIndex index of the chosen card 
	 * @param animalIndex index of the selected animal behind the card
	 * @param button button corresponding to the selected card
	 */
    public void handleCardSelection(int buttonIndex, int animalIndex, JButton button) {
		int strength = logic.getAnimalStrength(animalIndex);  // Correctly get strength

		if (player1CardSelection == -1) {  
			player1CardSelection = strength;  
		} else if (player2CardSelection == -1) {  
			player2CardSelection = strength;  
		}

		// Reveal the selected card
		String animalCard = "Resources/" + logic.getAnimalName(animalIndex) + "_card.png";
		ImageIcon revealedCard = new ImageIcon(animalCard);
		button.setIcon(revealedCard);
		button.setDisabledIcon(revealedCard);
		button.setEnabled(false);

		// Check if both players have selected
		if (player1CardSelection != -1 && player2CardSelection != -1) {  
			
			// Find the index of the selected strength in the animalStrength array
			int player1AnimalIndex = logic.getAnimalIndex(player1CardSelection); 
			int player2AnimalIndex = logic.getAnimalIndex(player2CardSelection);
		
			String firstPlayer = logic.compareAnimals(player1CardSelection, player2CardSelection); //determine the first player

			JOptionPane.showMessageDialog(view,
				player1Name + " selected " + logic.getAnimalName(player1AnimalIndex) + "\n" +
				player2Name + " selected " + logic.getAnimalName(player2AnimalIndex) + "\n\n" +
				firstPlayer + " will go first."
			);

			new JungleKingController(player1Name, player2Name, firstPlayer);
			view.dispose();
		}
	}

}
