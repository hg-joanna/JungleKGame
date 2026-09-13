
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
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

    private GameWebSocketClient roomClient;

    private static final String SERVER_URL =
        "wss://junglekgame.onrender.com/";

    public RoomGUI(
        String playerName
    ) {

        this.playerName =
            playerName;

        setTitle(
            "Jungle King - Room"
        );

        setSize(
            1300,
            900
        );

        setDefaultCloseOperation(
            JFrame.EXIT_ON_CLOSE
        );

        setResizable(false);
        setLocationRelativeTo(null);

        ImageIcon backgroundIcon =
            new ImageIcon(
                getClass().getResource(
                    "/Resources/bg_darker.png"
                )
            );

        JLabel backgroundLabel =
            new JLabel(
                backgroundIcon
            );

        backgroundLabel.setLayout(
            new BorderLayout()
        );

        add(
            backgroundLabel
        );

        ImageIcon logo =
            new ImageIcon(
                getClass().getResource(
                    "/Resources/logo.png"
                )
            );

        setIconImage(
            logo.getImage()
        );

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
            Box.createVerticalStrut(150)
        );

        JLabel titleLabel =
            new JLabel(
                "Jungle King"
            );

        titleLabel.setForeground(
            Color.WHITE
        );

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

        centerPanel.add(
            titleLabel
        );

        centerPanel.add(
            Box.createVerticalStrut(40)
        );

        StringBuilder playerText =
            new StringBuilder();

        playerText.append(
            "Player: "
        );

        playerText.append(
            playerName
        );

        JLabel nameLabel =
            new JLabel(
                playerText.toString()
            );

        nameLabel.setForeground(
            Color.WHITE
        );

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

        centerPanel.add(
            nameLabel
        );

        centerPanel.add(
            Box.createVerticalStrut(40)
        );

        JLabel roomCodeLabel =
            new JLabel(
                "Room Code"
            );

        roomCodeLabel.setForeground(
            Color.WHITE
        );

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

        centerPanel.add(
            roomCodeLabel
        );

        JLabel instructionLabel =
            new JLabel(
                "Enter exactly 4 letters or numbers"
            );

        instructionLabel.setForeground(
            Color.WHITE
        );

        instructionLabel.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                14
            )
        );

        instructionLabel.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        centerPanel.add(
            instructionLabel
        );

        centerPanel.add(
            Box.createVerticalStrut(10)
        );

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
            new Dimension(
                300,
                40
            )
        );

        roomCodeField.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        centerPanel.add(
            roomCodeField
        );

        centerPanel.add(
            Box.createVerticalStrut(20)
        );

        joinRoomButton =
            new JButton(
                "Join Room"
            );

        joinRoomButton.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                18
            )
        );

        joinRoomButton.setPreferredSize(
            new Dimension(
                180,
                45
            )
        );

        joinRoomButton.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        joinRoomButton.addActionListener(
            new JoinRoomButtonListener(this)
        );

        centerPanel.add(
            joinRoomButton
        );

        centerPanel.add(
            Box.createVerticalStrut(25)
        );

        JLabel orLabel =
            new JLabel(
                "OR"
            );

        orLabel.setForeground(
            Color.WHITE
        );

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

        centerPanel.add(
            orLabel
        );

        centerPanel.add(
            Box.createVerticalStrut(25)
        );

        createRoomButton =
            new JButton(
                "Create Room"
            );

        createRoomButton.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                18
            )
        );

        createRoomButton.setPreferredSize(
            new Dimension(
                180,
                45
            )
        );

        createRoomButton.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        createRoomButton.addActionListener(
            new CreateRoomButtonListener(this)
        );

        centerPanel.add(
            createRoomButton
        );

        centerPanel.add(
            Box.createVerticalStrut(35)
        );

        roomStatusLabel =
            new JLabel(
                "Connecting to server..."
            );

        roomStatusLabel.setForeground(
            Color.WHITE
        );

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

        centerPanel.add(
            roomStatusLabel
        );

        backgroundLabel.add(
            centerPanel,
            BorderLayout.CENTER
        );

        setVisible(true);

        connectToServer();
    }

    /**
     * Connects to the WebSocket server.
     */
    private void connectToServer() {

        try {

            roomClient =
                new GameWebSocketClient(this);

        } catch (Exception e) {

            updateStatus(
                "Unable to connect to server."
            );

            e.printStackTrace();
        }
    }

    /**
     * Creates a room.
     */
    private void createRoom() {

        if (
            roomClient == null
            || !roomClient.isOpen()
        ) {

            updateStatus(
                "Not connected to server."
            );

            return;
        }

        updateStatus(
            "Creating room..."
        );

        roomClient.createRoom(
            playerName
        );
    }

    /**
     * Joins a room.
     */
    private void joinRoom() {

        String roomCode =
            roomCodeField.getText()
                .trim()
                .toUpperCase();

        if (
            roomCode.length() != 4
        ) {

            updateStatus(
                "Invalid room code. Use exactly 4 letters or numbers."
            );

            return;
        }

        for (
            int i = 0;
            i < roomCode.length();
            i++
        ) {

            char character =
                roomCode.charAt(i);

            boolean valid =
                (
                    character >= 'A'
                    && character <= 'Z'
                )
                ||
                (
                    character >= '0'
                    && character <= '9'
                );

            if (!valid) {

                updateStatus(
                    "Invalid room code. Use exactly 4 letters or numbers."
                );

                return;
            }
        }

        if (
            roomClient == null
            || !roomClient.isOpen()
        ) {

            updateStatus(
                "Not connected to server."
            );

            return;
        }

        updateStatus(
            "Joining room..."
        );

        roomClient.joinRoom(
            roomCode,
            playerName
        );
    }

    /**
     * Shows the created room code.
     *
     * @param roomCode generated room code
     */
    public void showCreatedRoom(
        String roomCode
    ) {

        roomCodeField.setText(
            roomCode
        );

        StringBuilder status =
            new StringBuilder();

        status.append(
            "Room created: "
        );

        status.append(
            roomCode
        );

        status.append(
            " - Waiting for another player."
        );

        updateStatus(
            status.toString()
        );
    }

    /**
     * Updates the status text.
     *
     * @param message status message
     */
    public void updateStatus(
        String message
    ) {

        roomStatusLabel.setText(
            message
        );
    }

    /**
     * Create Room listener.
     */
    public static class CreateRoomButtonListener
        implements ActionListener {

        private RoomGUI gui;

        public CreateRoomButtonListener(
            RoomGUI gui
        ) {

            this.gui =
                gui;
        }

        public void actionPerformed(
            ActionEvent e
        ) {

            gui.createRoom();
        }
    }

    /**
     * Join Room listener.
     */
    public static class JoinRoomButtonListener
        implements ActionListener {

        private RoomGUI gui;

        public JoinRoomButtonListener(
            RoomGUI gui
        ) {

            this.gui =
                gui;
        }

        public void actionPerformed(
            ActionEvent e
        ) {

            gui.joinRoom();
        }
    }

    public static void main(
        String[] args
    ) {

        new RoomGUI(
            "Test Player"
        );
    }
}
