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

    public CardSelectionController(
        String player1Name,
        String player2Name,
        String localAssignedPlayerName
    ) {
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.localAssignedPlayerName = localAssignedPlayerName;
        this.logic = new CardSelectionLogic(player1Name, player2Name);

        List<Integer> animalOrder = new ArrayList<Integer>();

        for (int i = 0; i < 8; i++) {
            animalOrder.add(Integer.valueOf(i));
        }

        Collections.shuffle(animalOrder);

        ImageIcon cardBackSide = new ImageIcon(
            CardSelectionController.class.getResource(
                "/Resources/card.png"
            )
        );

        this.view = new CardSelectionGUI(
            this,
            animalOrder,
            cardBackSide
        );
    }

    public CardSelectionController(
        String player1Name,
        String player2Name
    ) {
        this(player1Name, player2Name, player1Name);
        this.isLocal = true;
    }

    public void handleCardSelection(
        int buttonIndex,
        int animalIndex,
        JButton button
    ) {

        int strength = logic.getAnimalStrength(animalIndex);

        if (isLocal) {

            if (player1CardSelection == -1) {

                player1CardSelection = strength;

                String animalCard = buildAnimalCardPath(animalIndex);

                ImageIcon revealedCard = new ImageIcon(
                    CardSelectionController.class.getResource(
                        animalCard
                    )
                );

                button.setIcon(revealedCard);
                button.setDisabledIcon(revealedCard);
                button.setEnabled(false);

                String message = buildPlayerTurnMessage();

                JOptionPane.showMessageDialog(
                    view,
                    message
                );

                return;

            } else if (player2CardSelection == -1) {

                player2CardSelection = strength;

            } else {

                return;
            }

        } else {

            if (
                localAssignedPlayerName.equals(player1Name)
                && player1CardSelection != -1
            ) {

                JOptionPane.showMessageDialog(
                    view,
                    "You have already selected your card. Waiting for Player 2."
                );

                return;

            } else if (
                localAssignedPlayerName.equals(player2Name)
                && player2CardSelection != -1
            ) {

                JOptionPane.showMessageDialog(
                    view,
                    "You have already selected your card. Waiting for Player 1."
                );

                return;
            }

            if (
                localAssignedPlayerName.equals(player1Name)
                && player1CardSelection == -1
            ) {

                player1CardSelection = strength;

            } else if (
                localAssignedPlayerName.equals(player2Name)
                && player2CardSelection == -1
            ) {

                player2CardSelection = strength;
            }
        }

        String animalCard = buildAnimalCardPath(animalIndex);

        ImageIcon revealedCard = new ImageIcon(
            CardSelectionController.class.getResource(
                animalCard
            )
        );

        button.setIcon(revealedCard);
        button.setDisabledIcon(revealedCard);
        button.setEnabled(false);

        if (
            player1CardSelection != -1
            && player2CardSelection != -1
        ) {

            int player1AnimalIndex =
                logic.getAnimalIndex(player1CardSelection);

            int player2AnimalIndex =
                logic.getAnimalIndex(player2CardSelection);

            String firstPlayer =
                logic.compareAnimals(
                    player1CardSelection,
                    player2CardSelection
                );

            String message = buildFinalSelectionMessage(
                player1AnimalIndex,
                player2AnimalIndex,
                firstPlayer
            );

            JOptionPane.showMessageDialog(
                view,
                message
            );

            new JungleKingController(
                player1Name,
                player2Name,
                firstPlayer,
                player1Name,
                "room1",
                "ws://junglekgame.onrender.com/"
            );

            view.dispose();
        }
    }

    private String buildAnimalCardPath(int animalIndex) {

        StringBuilder path = new StringBuilder();

        path.append("/Resources/");
        path.append(logic.getAnimalName(animalIndex));
        path.append("_card.png");

        return path.toString();
    }

    private String buildPlayerTurnMessage() {

        StringBuilder message = new StringBuilder();

        message.append(player1Name);
        message.append(" has selected a card.");
        message.append("\nNow it's ");
        message.append(player2Name);
        message.append("'s turn to select.");

        return message.toString();
    }

    private String buildFinalSelectionMessage(
        int player1AnimalIndex,
        int player2AnimalIndex,
        String firstPlayer
    ) {

        StringBuilder message = new StringBuilder();

        message.append(player1Name);
        message.append(" selected ");
        message.append(logic.getAnimalName(player1AnimalIndex));

        message.append("\n");

        message.append(player2Name);
        message.append(" selected ");
        message.append(logic.getAnimalName(player2AnimalIndex));

        message.append("\n\n");

        message.append(firstPlayer);
        message.append(" will go first.");

        return message.toString();
    }
}