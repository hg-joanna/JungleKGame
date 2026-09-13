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

    private RoomWebSocketClient roomClient;

    /**
     * WebSocket server address.
     */
    private static final String SERVER_URL =
        "wss://junglekgame.onrender.com/";

    /**
     * Instantiates the room GUI.
     *
     * @param playerName name of the local player
     */
    public RoomGUI(String playerName) {

        this.playerName = playerName;

        setTitle("Jungle King - Room");
        setSize(1300, 900);
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
            Box.createVerticalStrut(150)
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
            Box.createVerticalStrut(40)
        );

        StringBuilder playerText =
            new StringBuilder();

        playerText.append("Player: ");
        playerText.append(playerName);

        JLabel nameLabel =
            new JLabel(
                playerText.toString()
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
            Box.createVerticalStrut(40)
        );

        JLabel roomCodeLabel =
            new JLabel(
                "Room Code"
            );

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
            Box.createVerticalStrut(25)
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
            Box.createVerticalStrut(25)
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
            Box.createVerticalStrut(35)
        );

        roomStatusLabel =
            new JLabel(
                "Create a room or enter a room code."
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

        centerPanel.add(roomStatusLabel);

        backgroundLabel.add(
            centerPanel,
            BorderLayout.CENTER
        );

        setVisible(true);

        connectToServer();
    }

    /**
     * Connects this client to the WebSocket server.
     */
    private void connectToServer() {

        try {

            roomClient =
                new RoomWebSocketClient(
                    new URI(SERVER_URL),
                    this
                );

        } catch (Exception e) {

            roomStatusLabel.setText(
                "Unable to connect to server."
            );

            e.printStackTrace();
        }
    }

    /**
     * Handles creating a room.
     */
    private void createRoom() {

        if (roomClient == null) {

            roomStatusLabel.setText(
                "Not connected to server."
            );

            return;
        }

        roomStatusLabel.setText(
            "Creating room..."
        );

        roomClient.createRoom(
            playerName
        );
    }

    /**
     * Handles joining a room.
     */
    private void joinRoom() {

        String roomCode =
            roomCodeField.getText()
                .trim()
                .toUpperCase();

        if (
            roomCode.length() != 4
        ) {

            roomStatusLabel.setText(
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

                roomStatusLabel.setText(
                    "Invalid room code. Use exactly 4 letters or numbers."
                );

                return;
            }
        }

        if (roomClient == null) {

            roomStatusLabel.setText(
                "Not connected to server."
            );

            return;
        }

        roomStatusLabel.setText(
            "Joining room..."
        );

        roomClient.joinRoom(
            roomCode,
            playerName
        );
    }

    /**
     * Updates the room status.
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
     * Listener for Create Room.
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
     * Listener for Join Room.
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
     * Temporary room WebSocket client.
     *
     * This will handle room-specific messages.
     */
    public static class RoomWebSocketClient
        implements java.net.http.WebSocket.Listener {

        private URI serverUri;
        private RoomGUI gui;
        private java.net.http.WebSocket socket;

        public RoomWebSocketClient(
            URI serverUri,
            RoomGUI gui
        ) {

            this.serverUri = serverUri;
            this.gui = gui;

            java.net.http.HttpClient client =
                java.net.http.HttpClient.newHttpClient();

            client.newWebSocketBuilder()
                .buildAsync(
                    serverUri,
                    this
                )
                .thenAccept(
                    new SocketHandler(this)
                )
                .exceptionally(
                    new ErrorHandler(gui)
                );
        }

        public void createRoom(
            String playerName
        ) {

            StringBuilder message =
                new StringBuilder();

            message.append("{");
            message.append("\"type\":\"CREATE_ROOM\",");
            message.append("\"playerName\":\"");
            message.append(playerName);
            message.append("\"");
            message.append("}");

            send(message.toString());
        }

        public void joinRoom(
            String roomCode,
            String playerName
        ) {

            StringBuilder message =
                new StringBuilder();

            message.append("{");
            message.append("\"type\":\"JOIN_ROOM\",");
            message.append("\"roomId\":\"");
            message.append(roomCode);
            message.append("\",");
            message.append("\"playerName\":\"");
            message.append(playerName);
            message.append("\"");
            message.append("}");

            send(message.toString());
        }

        private void send(
            String message
        ) {

            if (socket != null) {

                socket.sendText(
                    message,
                    true
                );
            }
        }

        @Override
        public java.util.concurrent.CompletionStage<?> onText(
            java.net.http.WebSocket webSocket,
            CharSequence data,
            boolean last
        ) {

            String message =
                data.toString();

            if (
                message.contains(
                    "\"type\":\"ROOM_CREATED\""
                )
            ) {

                int codeStart =
                    message.indexOf(
                        "\"roomId\":\""
                    ) + 10;

                int codeEnd =
                    message.indexOf(
                        "\"",
                        codeStart
                    );

                if (
                    codeStart > 9
                    && codeEnd > codeStart
                ) {

                    String roomCode =
                        message.substring(
                            codeStart,
                            codeEnd
                        );

                    gui.updateStatus(
                        "Room created: "
                        + roomCode
                        + " - Waiting for another player."
                    );

                    gui.roomCodeField.setText(
                        roomCode
                    );
                }
            }

            if (
                message.contains(
                    "\"type\":\"ROOM_WAITING\""
                )
            ) {

                gui.updateStatus(
                    "Waiting for another player..."
                );
            }

            return java.net.http.WebSocket.Listener
                .super.onText(
                    webSocket,
                    data,
                    last
                );
        }

        public static class SocketHandler
            implements java.util.function.Consumer<
                java.net.http.WebSocket
            > {

            private RoomWebSocketClient client;

            public SocketHandler(
                RoomWebSocketClient client
            ) {
                this.client = client;
            }

            public void accept(
                java.net.http.WebSocket socket
            ) {

                client.socket = socket;

                client.gui.updateStatus(
                    "Connected to server."
                );
            }
        }

        public static class ErrorHandler
            implements java.util.function.Function<
                Throwable,
                Void
            > {

            private RoomGUI gui;

            public ErrorHandler(
                RoomGUI gui
            ) {
                this.gui = gui;
            }

            public Void apply(
                Throwable error
            ) {

                gui.updateStatus(
                    "Unable to connect to server."
                );

                error.printStackTrace();

                return null;
            }
        }
    }

    /**
     * Main method for testing.
     *
     * @param args main method arguments
     */
    public static void main(String[] args) {

        new RoomGUI(
            "Test Player"
        );
    }
}