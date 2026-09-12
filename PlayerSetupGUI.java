import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Represents the graphical user interface of the player setup process
 */
public class PlayerSetupGUI extends JFrame {

    /** Text field for player1 */
    private JTextField player1NameField;

    /** Text field for player2 */
    private JTextField player2NameField;

    /** Confirmation button for names */
    private JButton confirmButton;

    /** Name of player1 */
    private String player1Name;

    /** Name of player2 */
    private String player2Name;

    /**
     * Instantiates the graphical user interface for the player setup
     */
    public PlayerSetupGUI() {
        setTitle("Player Setup");
        setSize(1300, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        ImageIcon backgroundIcon = new ImageIcon(
            getClass().getResource("/Resources/bg_darker.png")
        );

        JLabel backgroundLabel = new JLabel(backgroundIcon);
        backgroundLabel.setLayout(new BorderLayout());
        add(backgroundLabel);

        ImageIcon logo = new ImageIcon(
            getClass().getResource("/Resources/logo.png")
        );

        setIconImage(logo.getImage());

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        centerPanel.add(Box.createVerticalStrut(150));

        JLabel player1Label = new JLabel("Enter Player 1 Name:");
        player1Label.setForeground(Color.WHITE);
        player1Label.setFont(new Font("Arial", Font.PLAIN, 18));
        player1Label.setAlignmentX(Component.CENTER_ALIGNMENT);

        player1NameField = new JTextField();
        player1NameField.setFont(new Font("Arial", Font.PLAIN, 18));
        player1NameField.setMaximumSize(new Dimension(300, 40));
        player1NameField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel player2Label = new JLabel("Enter Player 2 Name:");
        player2Label.setForeground(Color.WHITE);
        player2Label.setFont(new Font("Arial", Font.PLAIN, 18));
        player2Label.setAlignmentX(Component.CENTER_ALIGNMENT);

        player2NameField = new JTextField();
        player2NameField.setFont(new Font("Arial", Font.PLAIN, 18));
        player2NameField.setMaximumSize(new Dimension(300, 40));
        player2NameField.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(player1Label);
        centerPanel.add(player1NameField);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(player2Label);
        centerPanel.add(player2NameField);

        centerPanel.add(Box.createVerticalStrut(50));

        confirmButton = new JButton("Confirm");
        confirmButton.setFont(new Font("Arial", Font.PLAIN, 18));
        confirmButton.setPreferredSize(new Dimension(150, 50));
        confirmButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        confirmButton.addActionListener(
            new ConfirmButtonListener(this)
        );

        centerPanel.add(confirmButton);
        backgroundLabel.add(centerPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    /**
     * Handles the Confirm button click.
     */
    private void confirmPlayers() {

        player1Name = player1NameField.getText().trim();
        player2Name = player2NameField.getText().trim();

        if (player1Name.isEmpty() || player2Name.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Player names cannot be empty!",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (player1Name.equals(player2Name)) {
            JOptionPane.showMessageDialog(
                this,
                "Player names must be different!",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        JOptionPane.showMessageDialog(
            this,
            "Players confirmed."
        );

        new CardSelectionController(player1Name, player2Name);

        dispose();
    }

    /**
     * Named listener class for the Confirm button.
     */
    public static class ConfirmButtonListener implements ActionListener {

        private PlayerSetupGUI gui;

        public ConfirmButtonListener(PlayerSetupGUI gui) {
            this.gui = gui;
        }

        public void actionPerformed(ActionEvent e) {
            gui.confirmPlayers();
        }
    }

    /**
     * Main method
     * @param args main method
     */
    public static void main(String[] args) {
        new PlayerSetupGUI();
    }
}