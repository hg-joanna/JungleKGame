import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Represents the graphical user interface of the player setup process.
 */
public class PlayerSetupGUI extends JFrame {

    /** Text field for the local player's name. */
    private JTextField playerNameField;

    /** Name of the local player. */
    private String playerName;

    /** Confirmation button for the player name. */
    private JButton confirmButton;

    /**
     * Instantiates the graphical user interface for player setup.
     */
    public PlayerSetupGUI() {

        setTitle("Player Setup");
        setSize(1300, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        ImageIcon backgroundIcon =
            new ImageIcon(
                getClass().getResource(
                    "/Resources/bg_darker.png"
                )
            );

        JLabel backgroundLabel =
            new JLabel(backgroundIcon);

        backgroundLabel.setLayout(
            new BorderLayout()
        );

        add(backgroundLabel);

        ImageIcon logo =
            new ImageIcon(
                getClass().getResource(
                    "/Resources/logo.png"
                )
            );

        setIconImage(logo.getImage());

        JPanel centerPanel =
            new JPanel();

        centerPanel.setLayout(
            new BoxLayout(
                centerPanel,
                BoxLayout.Y_AXIS
            )
        );

        centerPanel.setOpaque(false);

        centerPanel.add(
            Box.createVerticalStrut(200)
        );

        JLabel playerLabel =
            new JLabel("Enter Your Name:");

        playerLabel.setForeground(Color.WHITE);

        playerLabel.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                18
            )
        );

        playerLabel.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        playerNameField =
            new JTextField();

        playerNameField.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                18
            )
        );

        playerNameField.setMaximumSize(
            new Dimension(300, 40)
        );

        playerNameField.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        centerPanel.add(playerLabel);
        centerPanel.add(playerNameField);

        centerPanel.add(
            Box.createVerticalStrut(50)
        );

        confirmButton =
            new JButton("Confirm");

        confirmButton.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                18
            )
        );

        confirmButton.setPreferredSize(
            new Dimension(150, 50)
        );

        confirmButton.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        confirmButton.addActionListener(
            new ConfirmButtonListener(this)
        );

        centerPanel.add(confirmButton);

        backgroundLabel.add(
            centerPanel,
            BorderLayout.CENTER
        );

        setVisible(true);
    }

    /**
     * Handles the Confirm button click.
     */
    private void confirmPlayer() {

        playerName =
            playerNameField.getText().trim();

        if (playerName.isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "Player name cannot be empty!",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        new RoomGUI(playerName);

        dispose();
    }

    /**
     * Named listener class for the Confirm button.
     */
    public static class ConfirmButtonListener
        implements ActionListener {

        private PlayerSetupGUI gui;

        public ConfirmButtonListener(
            PlayerSetupGUI gui
        ) {
            this.gui = gui;
        }

        public void actionPerformed(
            ActionEvent e
        ) {
            gui.confirmPlayer();
        }
    }

    /**
     * Main method.
     *
     * @param args main method arguments
     */
    public static void main(String[] args) {
        new PlayerSetupGUI();
    }
}