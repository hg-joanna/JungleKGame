import java.awt.*;
import java.util.List;
import javax.swing.*;

/**
 * Represents the graphical user interface (view) of the card selection process of the players
 */
public class CardSelectionGUI extends JFrame {
    /**Represents the controller architecture for the card selection process */
    private CardSelectionController controller;
    /**Array of buttons for the cards */
    private JButton[] cardButtons;

    /**
     * Instantiates the GUI for the Card Selection process
     * @param controller controller of the card selection process
     * @param animalOrder the order of animals
     * @param cardBackSide the revealed cards
     */
    public CardSelectionGUI(CardSelectionController controller, List<Integer> animalOrder, ImageIcon cardBackSide) {
        this.controller = controller;
        
        setTitle("Jungle King - Card Selection");
        setSize(1300, 900); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        JLabel backgroundLabel = new JLabel(new ImageIcon("Resources/bg_darker.png"));
        backgroundLabel.setLayout(new BorderLayout()); 
        add(backgroundLabel);
        
        setIconImage(new ImageIcon("Resources/logo.png").getImage());
        
        JPanel cardPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        cardPanel.setOpaque(false);

        cardButtons = new JButton[8];
        
        for (int i = 0; i < 8; i++) {
            int animalIndex = animalOrder.get(i);
            cardButtons[i] = new JButton(cardBackSide);
            cardButtons[i].setBorderPainted(false);
            cardButtons[i].setContentAreaFilled(false);
            cardButtons[i].setFocusPainted(false);
            cardButtons[i].setPreferredSize(new Dimension(216, 280));
            
            final int index = i;
            cardButtons[i].addActionListener(e -> controller.handleCardSelection(index, animalIndex, cardButtons[index]));
            cardPanel.add(cardButtons[i]);
        }
        
        backgroundLabel.add(cardPanel, BorderLayout.CENTER);
        setVisible(true);
    }
}
