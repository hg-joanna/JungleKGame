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
    }
    
    public void handleCardSelection(int buttonIndex, int animalIndex, JButton button) {
        if (localAssignedPlayerName.equals(player1Name) && player1CardSelection != -1) {
            JOptionPane.showMessageDialog(view, "You have already selected your card. Waiting for Player 2.");
            return;
        } else if (localAssignedPlayerName.equals(player2Name) && player2CardSelection != -1) {
            JOptionPane.showMessageDialog(view, "You have already selected your card. Waiting for Player 1.");
            return;
        }

        int strength = logic.getAnimalStrength(animalIndex);

        if (localAssignedPlayerName.equals(player1Name) && player1CardSelection == -1) {  
            player1CardSelection = strength;  
        } else if (localAssignedPlayerName.equals(player2Name) && player2CardSelection == -1) {  
            player2CardSelection = strength;  
        }

        String animalCard = "Resources/" + logic.getAnimalName(animalIndex) + "_card.png";
        ImageIcon revealedCard = new ImageIcon(animalCard);
        button.setIcon(revealedCard);
        button.setDisabledIcon(revealedCard);
        button.setEnabled(false);

        if (player1CardSelection != -1 && player2CardSelection != -1) {  
            int player1AnimalIndex = logic.getAnimalIndex(player1CardSelection); 
            int player2AnimalIndex = logic.getAnimalIndex(player2CardSelection);
        
            String firstPlayer = logic.compareAnimals(player1CardSelection, player2CardSelection);

            JOptionPane.showMessageDialog(view,
                player1Name + " selected " + logic.getAnimalName(player1AnimalIndex) + "\n" +
                player2Name + " selected " + logic.getAnimalName(player2AnimalIndex) + "\n\n" +
                firstPlayer + " will go first."
            );

            // Connects to default room "room1" on local server; change port or URL as needed
            new JungleKingController(player1Name, player2Name, firstPlayer, localAssignedPlayerName, "room1", "ws://localhost:8080");
            view.dispose();
        }
    }
}