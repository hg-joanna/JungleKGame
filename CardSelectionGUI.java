import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;

/**
 * Represents the graphical user interface (view) of the card selection process of the players
 */
public class CardSelectionGUI extends JFrame {

    /** Represents the controller architecture for the card selection process */
    private CardSelectionController controller;

    /** Array of buttons for the cards */
    private JButton[] cardButtons;

    /**
     * Instantiates the GUI for the Card Selection process
     *
     * @param controller controller of the card selection process
     * @param animalOrder the order of animals
     * @param cardBackSide the revealed cards
     */
    public CardSelectionGUI(
        CardSelectionController controller,
        List<Integer> animalOrder,
        ImageIcon cardBackSide
    ) {

        this.controller = controller;

        setTitle("Jungle King - Card Selection");
        setSize(1300, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        JLabel backgroundLabel = new JLabel(
            getResourceIcon("/Resources/bg_darker.png")
        );

        backgroundLabel.setLayout(new BorderLayout());
        add(backgroundLabel);

        setIconImage(
            getResourceIcon("/Resources/logo.png").getImage()
        );

        JPanel cardPanel = new JPanel(
            new GridLayout(2, 4, 10, 10)
        );

        cardPanel.setOpaque(false);

        cardButtons = new JButton[8];

        for (int i = 0; i < 8; i++) {

            int animalIndex = animalOrder.get(i).intValue();

            cardButtons[i] = new CardButton(
                cardBackSide,
                controller,
                i,
                animalIndex,
                this
            );

            cardButtons[i].setBorderPainted(false);
            cardButtons[i].setContentAreaFilled(false);
            cardButtons[i].setFocusPainted(false);
            cardButtons[i].setPreferredSize(
                new Dimension(216, 280)
            );

            cardPanel.add(cardButtons[i]);
        }

        backgroundLabel.add(
            cardPanel,
            BorderLayout.CENTER
        );

        setVisible(true);
    }

    private ImageIcon getResourceIcon(String path) {

        java.net.URL resource =
            CardSelectionGUI.class.getResource(path);

        return new ImageIcon(resource);
    }

    /**
     * Named button class used instead of a lambda ActionListener.
     */
    public static class CardButton extends JButton
        implements ActionListener {

        private CardSelectionController controller;
        private int index;
        private int animalIndex;
        private CardSelectionGUI gui;

        public CardButton(
            ImageIcon icon,
            CardSelectionController controller,
            int index,
            int animalIndex,
            CardSelectionGUI gui
        ) {

            super(icon);

            this.controller = controller;
            this.index = index;
            this.animalIndex = animalIndex;
            this.gui = gui;

            addActionListener(this);
        }

        public void actionPerformed(ActionEvent e) {

            controller.handleCardSelection(
                index,
                animalIndex,
                this
            );
        }
    }
}