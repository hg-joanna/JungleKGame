
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class RoomGUI extends JFrame {

    private String playerName;

    private JTextField roomCodeField;

    private JLabel statusLabel;

    private GameWebSocketClient roomClient;

    private static final String SERVER_URL =
        "wss://junglekgame.onrender.com/";

    public RoomGUI(
        String playerName
    ) {

        this.playerName = playerName;

        setTitle(
            "Jungle King - Join Room"
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

        ImageIcon logo =
            getResourceIcon(
                "/Resources/logo.png"
            );

        setIconImage(
            logo.getImage()
        );

        JLabel backgroundLabel =
            new JLabel(
                getResourceIcon(
                    "/Resources/bg_darker.png"
                )
            );

        backgroundLabel.setLayout(
            null
        );

        setContentPane(
            backgroundLabel
        );

        JLabel titleLabel =
            new JLabel(
                "JOIN ROOM"
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

        titleLabel.setHorizontalAlignment(
            SwingConstants.CENTER
        );

        titleLabel.setBounds(
            400,
            180,
            500,
            50
        );

        backgroundLabel.add(
            titleLabel
        );

        JLabel nameLabel =
            new JLabel(
                "Player: " + playerName
            );

        nameLabel.setForeground(
            Color.WHITE
        );

        nameLabel.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                18
            )
        );

        nameLabel.setHorizontalAlignment(
            SwingConstants.CENTER
        );

        nameLabel.setBounds(
            400,
            250,
            500,
            40
        );

        backgroundLabel.add(
            nameLabel
        );

        JLabel codeLabel =
            new JLabel(
                "Enter Room Code:"
            );

        codeLabel.setForeground(
            Color.WHITE
        );

        codeLabel.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                18
            )
        );

        codeLabel.setHorizontalAlignment(
            SwingConstants.CENTER
        );

        codeLabel.setBounds(
            400,
            320,
            500,
            40
        );

        backgroundLabel.add(
            codeLabel
        );

        roomCodeField =
            new JTextField();

        roomCodeField.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                24
            )
        );

        roomCodeField.setHorizontalAlignment(
            SwingConstants.CENTER
        );

        roomCodeField.setBounds(
            500,
            370,
            300,
            50
        );

        backgroundLabel.add(
            roomCodeField
        );

        JLabel hintLabel =
            new JLabel(
                "4 letters or numbers"
            );

        hintLabel.setForeground(
            Color.WHITE
        );

        hintLabel.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                15
            )
        );

        hintLabel.setHorizontalAlignment(
            SwingConstants.CENTER
        );

        hintLabel.setBounds(
            450,
            425,
            400,
            30
        );

        backgroundLabel.add(
            hintLabel
        );

        JButton createButton =
            new JButton(
                "CREATE ROOM"
            );

        createButton.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                18
            )
        );

        createButton.setBounds(
            390,
            500,
            250,
            60
        );

        createButton.setFocusPainted(
            false
        );

        createButton.addActionListener(
            new CreateRoomButtonListener(
                this
            )
        );

        backgroundLabel.add(
            createButton
        );

        JButton joinButton =
            new JButton(
                "JOIN ROOM"
            );

        joinButton.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                18
            )
        );

        joinButton.setBounds(
            660,
            500,
            250,
            60
        );

        joinButton.setFocusPainted(
            false
        );

        joinButton.addActionListener(
            new JoinRoomButtonListener(
                this
            )
        );

        backgroundLabel.add(
            joinButton
        );

        statusLabel =
            new JLabel(
                "Connecting..."
            );

        statusLabel.setForeground(
            Color.WHITE
        );

        statusLabel.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                18
            )
        );

        statusLabel.setHorizontalAlignment(
            SwingConstants.CENTER
        );

        statusLabel.setBounds(
            300,
            620,
            700,
            40
        );

        backgroundLabel.add(
            statusLabel
        );

        connectToServer();

        setVisible(
            true
        );
    }

    private ImageIcon getResourceIcon(
        String path
    ) {

        java.net.URL resource =
            RoomGUI.class.getResource(
                path
            );

        return new ImageIcon(
            resource
        );
    }

    private void connectToServer() {

        roomClient =
            new GameWebSocketClient(
                this
            );
    }

    private void createRoom() {

        updateStatus(
            "Creating room..."
        );

        if (
            roomClient != null
            && roomClient.isOpen()
        ) {

            roomClient.createRoom(
                playerName
            );

        } else {

            updateStatus(
                "Not connected to server."
            );
        }
    }

    private void joinRoom() {

        String roomCode =
            roomCodeField
                .getText()
                .trim()
                .toUpperCase();

        if (
            roomCode.length() != 4
        ) {

            JOptionPane.showMessageDialog(
                this,
                "Room code must be exactly 4 characters.",
                "Invalid Room Code",
                JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        if (
            !roomCode.matches(
                "[A-Z0-9]{4}"
            )
        ) {

            JOptionPane.showMessageDialog(
                this,
                "Room code can only contain letters and numbers.",
                "Invalid Room Code",
                JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        updateStatus(
            "Joining room..."
        );

        if (
            roomClient != null
            && roomClient.isOpen()
        ) {

            roomClient.joinRoom(
                roomCode,
                playerName
            );

        } else {

            updateStatus(
                "Not connected to server."
            );
        }
    }

    public void showCreatedRoom(
        String roomCode
    ) {

        roomCodeField.setText(
            roomCode
        );

        updateStatus(
            "Waiting for Player 2..."
        );
    }

    public void updateStatus(
        String message
    ) {

        statusLabel.setText(
            message
        );
    }

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
}

