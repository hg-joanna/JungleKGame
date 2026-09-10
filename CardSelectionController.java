import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.*;

public class CardSelectionController {
    private CardSelectionGUI view;
    private CardSelectionLogic logic;
    private String player1Name;
    private String player2Name;
    private String localAssignedPlayerName;
    private int player1CardSelection = -1;
    private int player2CardSelection = -1;
    private boolean isLocal = false;
    
    public CardSelectionController(String player1Name, String player2Name, String localAssignedPlayerName) {
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.localAssignedPlayerName = localAssignedPlayerName;
        this.logic = new CardSelectionLogic(player1Name, player2Name);
        
        List<Integer> animalOrder = new ArrayList<>();
        for (int i = 0; i < 8; i++) 
            animalOrder.add(i);
        
        Collections.shuffle(animalOrder);
        
        ImageIcon cardBackSide = new ImageIcon("Resources/card.png");
        this.view = new CardSelectionGUI(this, animalOrder, cardBackSide);
    }

    public CardSelectionController(String player1Name, String player2Name) {
        this(player1Name, player2Name, player1Name);
        this.isLocal = true; // Mark as local pass-and-play mode
    }
    
    public void handleCardSelection(int buttonIndex, int animalIndex, JButton button) {
        int strength = logic.getAnimalStrength(animalIndex);

        if (isLocal) {
            // Local sequential turn handling
            if (player1CardSelection == -1) {
                player1CardSelection = strength;
                
                // Reveal Player 1's card temporarily or lock it
                String animalCard = "Resources/" + logic.getAnimalName(animalIndex) + "_card.png";
                ImageIcon revealedCard = new ImageIcon(animalCard);
                button.setIcon(revealedCard);
                button.setDisabledIcon(revealedCard);
                button.setEnabled(false);

                JOptionPane.showMessageDialog(view, player1Name + " has selected a card.\nNow it's " + player2Name + "'s turn to select.");
                return;
            } else if (player2CardSelection == -1) {
                player2CardSelection = strength;
            } else {
                return;
            }
        } else {
            // Network mode validation
            if (localAssignedPlayerName.equals(player1Name) && player1CardSelection != -1) {
                JOptionPane.showMessageDialog(view, "You have already selected your card. Waiting for Player 2.");
                return;
            } else if (localAssignedPlayerName.equals(player2Name) && player2CardSelection != -1) {
                JOptionPane.showMessageDialog(view, "You have already selected your card. Waiting for Player 1.");
                return;
            }

            if (localAssignedPlayerName.equals(player1Name) && player1CardSelection == -1) {  
                player1CardSelection = strength;  
            } else if (localAssignedPlayerName.equals(player2Name) && player2CardSelection == -1) {  
                player2CardSelection = strength;  
            }
        }

        // Reveal card UI update for the second selection (or network selection)
        String animalCard = "Resources/" + logic.getAnimalName(animalIndex) + "_card.png";
        ImageIcon revealedCard = new ImageIcon(animalCard);
        button.setIcon(revealedCard);
        button.setDisabledIcon(revealedCard);
        button.setEnabled(false);

        // Once both players have selected cards
        if (player1CardSelection != -1 && player2CardSelection != -1) {  
            int player1AnimalIndex = logic.getAnimalIndex(player1CardSelection); 
            int player2AnimalIndex = logic.getAnimalIndex(player2CardSelection);
        
            String firstPlayer = logic.compareAnimals(player1CardSelection, player2CardSelection);

            JOptionPane.showMessageDialog(view,
                player1Name + " selected " + logic.getAnimalName(player1AnimalIndex) + "\n" +
                player2Name + " selected " + logic.getAnimalName(player2AnimalIndex) + "\n\n" +
                firstPlayer + " will go first."
            );

            // Launch the main game controller
            new JungleKingController(player1Name, player2Name, firstPlayer, player1Name, "room1", "ws://junglekgame.onrender.com/");
            view.dispose();
        }
    }
}