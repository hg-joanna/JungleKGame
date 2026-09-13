
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.*;

public class RoomGUI extends JFrame {

    private JTextField roomCodeField;

    private JLabel titleLabel;
    private JLabel instructionLabel;
    private JLabel roomCodeLabel;
    private JLabel statusLabel;

    private JButton createRoomButton;
    private JButton joinRoomButton;

    private String playerName;

    private GameWebSocketClient roomClient;

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

        titleLabel =
            new JLabel(
                "MULTIPLAYER ROOM"
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
            350,
            150,
            600,
            50
        );

        backgroundLabel.add(
            titleLabel
        );

        instructionLabel =
            new JLabel(
                "Create a room or enter a room code to join."
            );

        instructionLabel.setForeground(
            Color.WHITE
        );

        instructionLabel.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                18
            )
        );

        instructionLabel.setHorizontalAlignment(
            SwingConstants.CENTER
        );

        instructionLabel.setBounds(
            300,
            215,
            700,
            40
        );

        backgroundLabel.add(
            instructionLabel
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
                Font.BOLD,
                18
            )
        );

        nameLabel.setHorizontalAlignment(
            SwingConstants.CENTER
        );

        nameLabel.setBounds(
            450,
            270,
            400,
            35
        );

        backgroundLabel.add(
            nameLabel
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

        roomCodeField.setMaximumSize(
            new Dimension(
                300,
                50
            )
        );

        roomCodeField.setBounds(
            500,
            350,
            300,
            55
        );

        backgroundLabel.add(
            roomCodeField
        );

        JLabel codeHintLabel =
            new JLabel(
                "Enter exactly 4 letters or numbers"
            );

        codeHintLabel.setForeground(
            Color.WHITE
        );

        codeHintLabel.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                15
            )
        );

        codeHintLabel.setHorizontalAlignment(
            SwingConstants.CENTER
        );

        codeHintLabel.setBounds(
            450,
            410,
            400,
            30
        );

        backgroundLabel.add(
            codeHintLabel
        );

        createRoomButton =
            new JButton(
                "CREATE ROOM"
            );

        createRoomButton.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                18
            )
        );

        createRoomButton.setBounds(
            390,
            480,
            250,
            60
        );

        createRoomButton.setFocusPainted(
            false
        );

        createRoomButton.addActionListener(
            new CreateRoomButtonListener(
                this
            )
        );

        backgroundLabel.add(
            createRoomButton
        );

        joinRoomButton =
            new JButton(
                "JOIN ROOM"
            );

        joinRoomButton.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                18
            )
        );

        joinRoomButton.setBounds(
            660,
            480,
            250,
            60
        );

        joinRoomButton.setFocusPainted(
            false
        );

        joinRoomButton.addActionListener(
            new JoinRoomButtonListener(
                this
            )
        );

        backgroundLabel.add(
            joinRoomButton
        );

        roomCodeLabel =
            new JLabel(
                ""
            );

        roomCodeLabel.setForeground(
            Color.WHITE
        );

        roomCodeLabel.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                30
            )
        );

        roomCodeLabel.setHorizontalAlignment(
            SwingConstants.CENTER
        );

        roomCodeLabel.setBounds(
            350,
            575,
            600,
            50
        );

        backgroundLabel.add(
            roomCodeLabel
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
            650,
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

        String roomCode =
            generateRoomCode();

        roomCodeField.setText(
            roomCode
        );

        roomCodeField.setEditable(
            false
        );

        roomCodeLabel.setText(
            "ROOM CODE: " + roomCode
        );

        statusLabel.setText(
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

            statusLabel.setText(
                "Room created. Waiting for server connection..."
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
                "Room code must be exactly 4 letters or numbers.",
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

        roomCodeField.setText(
            roomCode
        );

        statusLabel.setText(
            "Joining room " + roomCode + "..."
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

            statusLabel.setText(
                "Not connected to server."
            );
        }
    }

    private String generateRoomCode() {

        String characters =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        Random random =
            new Random();

        StringBuilder code =
            new StringBuilder();

        for (
            int i = 0;
            i < 4;
            i++
        ) {

            int index =
                random.nextInt(
                    characters.length()
                );

            code.append(
                characters.charAt(
                    index
                )
            );
        }

        return code.toString();
    }

    public void showCreatedRoom(
        String roomCode
    ) {

        roomCodeField.setText(
            roomCode
        );

        roomCodeLabel.setText(
            "ROOM CODE: " + roomCode
        );

        statusLabel.setText(
            "Waiting for another player..."
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

    public static void main(
        String[] args
    ) {

        new RoomGUI(
            "Player 1"
        );
    }
}

