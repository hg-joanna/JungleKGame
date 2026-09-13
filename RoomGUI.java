import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Represents the room creation and joining interface.
 */
public class RoomGUI extends JFrame {

    private String playerName;

    private JTextField roomCodeField;
    private JButton createRoomButton;
    private JButton joinRoomButton;
    private JLabel roomStatusLabel;

    /**
     * Instantiates the room GUI.
     *
     * @param playerName name of the local player
     */
    public RoomGUI(String playerName) {

        this.playerName = playerName;

        setTitle("Jungle King - Room");
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
            Box.createVerticalStrut(170)
        );

        JLabel titleLabel =
            new JLabel("Jungle King");

        titleLabel.setForeground(Color.WHITE);

        titleLabel.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                32
            )
        );

        titleLabel.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        centerPanel.add(titleLabel);

        centerPanel.add(
            Box.createVerticalStrut(50)
        );

        JLabel nameLabel =
            new JLabel(
                "Player: " + playerName
            );

        nameLabel.setForeground(Color.WHITE);

        nameLabel.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                20
            )
        );

        nameLabel.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        centerPanel.add(nameLabel);

        centerPanel.add(
            Box.createVerticalStrut(50)
        );

        JLabel roomCodeLabel =
            new JLabel("Enter Room Code:");

        roomCodeLabel.setForeground(Color.WHITE);

        roomCodeLabel.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                18
            )
        );

        roomCodeLabel.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        centerPanel.add(roomCodeLabel);

        roomCodeField =
            new JTextField();

        roomCodeField.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                18
            )
        );

        roomCodeField.setMaximumSize(
            new Dimension(300, 40)
        );

        roomCodeField.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        centerPanel.add(roomCodeField);

        centerPanel.add(
            Box.createVerticalStrut(20)
        );

        joinRoomButton =
            new JButton("Join Room");

        joinRoomButton.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                18
            )
        );

        joinRoomButton.setPreferredSize(
            new Dimension(180, 45)
        );

        joinRoomButton.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        joinRoomButton.addActionListener(
            new JoinRoomButtonListener(this)
        );

        centerPanel.add(joinRoomButton);

        centerPanel.add(
            Box.createVerticalStrut(30)
        );

        JLabel orLabel =
            new JLabel("OR");

        orLabel.setForeground(Color.WHITE);

        orLabel.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                18
            )
        );

        orLabel.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        centerPanel.add(orLabel);

        centerPanel.add(
            Box.createVerticalStrut(30)
        );

        createRoomButton =
            new JButton("Create Room");

        createRoomButton.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                18
            )
        );

        createRoomButton.setPreferredSize(
            new Dimension(180, 45)
        );

        createRoomButton.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        createRoomButton.addActionListener(
            new CreateRoomButtonListener(this)
        );

        centerPanel.add(createRoomButton);

        centerPanel.add(
            Box.createVerticalStrut(40)
        );

        roomStatusLabel =
            new JLabel(
                "Create a room or enter a room code."
            );

        roomStatusLabel.setForeground(Color.WHITE);

        roomStatusLabel.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                16
            )
        );

        roomStatusLabel.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        centerPanel.add(roomStatusLabel);

        backgroundLabel.add(
            centerPanel,
            BorderLayout.CENTER
        );

        setVisible(true);
    }

    /**
     * Handles joining a room.
     */
    private void joinRoom() {

        String roomCode =
            roomCodeField.getText().trim();

        if (roomCode.isEmpty()) {

            roomStatusLabel.setText(
                "Please enter a room code."
            );

            return;
        }

        roomStatusLabel.setText(
            "Joining room " + roomCode + "..."
        );

        /*
         * WebSocket connection will be added here next.
         */
    }

    /**
     * Handles creating a room.
     */
    private void createRoom() {

        /*
         * Room creation will be connected to
         * the WebSocket server next.
         */

        roomStatusLabel.setText(
            "Creating room..."
        );
    }

    /**
     * Named listener for Join Room.
     */
    public static class JoinRoomButtonListener
        implements ActionListener {

        private RoomGUI gui;

        public JoinRoomButtonListener(
            RoomGUI gui
        ) {
            this.gui = gui;
        }

        public void actionPerformed(
            ActionEvent e
        ) {
            gui.joinRoom();
        }
    }

    /**
     * Named listener for Create Room.
     */
    public static class CreateRoomButtonListener
        implements ActionListener {

        private RoomGUI gui;

        public CreateRoomButtonListener(
            RoomGUI gui
        ) {
            this.gui = gui;
        }

        public void actionPerformed(
            ActionEvent e
        ) {
            gui.createRoom();
        }
    }

    /**
     * Main method for testing.
     *
     * @param args main method arguments
     */
    public static void main(String[] args) {
        new RoomGUI("Test Player");
    }
}