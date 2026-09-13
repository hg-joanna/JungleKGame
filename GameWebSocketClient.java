
import java.net.URI;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.swing.SwingUtilities;

public class GameWebSocketClient {

    private JungleKingController controller;
    private RoomGUI roomGUI;

    private String roomId;
    private String playerName;

    private boolean connected;

    public GameWebSocketClient(
        JungleKingController controller,
        String roomId,
        String playerName
    ) {

        this.controller = controller;
        this.roomId = roomId;
        this.playerName = playerName;
        this.connected = false;

        System.out.println(
            "GameWebSocketClient created for game."
        );
    }

    public GameWebSocketClient(
        RoomGUI roomGUI
    ) {

        this.roomGUI = roomGUI;
        this.connected = true;

        System.out.println(
            "GameWebSocketClient created for RoomGUI."
        );

        roomGUI.updateStatus(
            "WebSocket client loaded."
        );
    }

    public void createRoom(
        String playerName
    ) {

        System.out.println(
            "CREATE_ROOM:" + playerName
        );

        /*
         * Temporary room creation response.
         *
         * The actual WebSocket connection will be
         * connected in the next networking step.
         *
         * This allows the waiting-room UI to be tested
         * without breaking CheerpJ.
         */

        String roomCode =
            generateRoomCode();

        final String finalRoomCode =
            roomCode;

        SwingUtilities.invokeLater(
            new Runnable() {

                public void run() {

                    if (roomGUI != null) {

                        roomGUI.showCreatedRoom(
                            finalRoomCode
                        );
                    }
                }
            }
        );
    }

    public void joinRoom(
        String roomCode,
        String playerName
    ) {

        StringBuilder message =
            new StringBuilder();

        message.append(
            "JOIN_ROOM:"
        );

        message.append(
            roomCode
        );

        message.append(
            ":"
        );

        message.append(
            playerName
        );

        System.out.println(
            message.toString()
        );

        if (roomGUI != null) {

            roomGUI.updateStatus(
                "Joining room " + roomCode + "..."
            );
        }
    }

    private String generateRoomCode() {

        String characters =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        StringBuilder code =
            new StringBuilder();

        long value =
            System.currentTimeMillis();

        for (
            int i = 0;
            i < 4;
            i++
        ) {

            int index =
                (int)(
                    value
                    % characters.length()
                );

            if (index < 0) {
                index = index * -1;
            }

            code.append(
                characters.charAt(index)
            );

            value =
                value / 7 + 13;
        }

        return code.toString();
    }

    public boolean isOpen() {

        return connected;
    }

    public void sendMove() {

        System.out.println(
            "Send move requested."
        );
    }

    public void processRemoteMove() {

        System.out.println(
            "Remote move requested."
        );
    }
}

