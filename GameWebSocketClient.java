import java.net.URI;

/**
 * Handles communication with the Jungle King WebSocket server.
 */
public class GameWebSocketClient {

    private JungleKingController controller;
    private RoomGUI roomGUI;

    private String roomId;
    private String playerName;

    private boolean connected = false;

    public GameWebSocketClient(
        URI serverUri,
        JungleKingController controller,
        String roomId,
        String playerName
    ) {

        this.controller = controller;
        this.roomId = roomId;
        this.playerName = playerName;

        System.out.println(
            "GameWebSocketClient created for game."
        );
    }

    public GameWebSocketClient(
        URI serverUri,
        RoomGUI roomGUI
    ) {

        this.roomGUI = roomGUI;

        System.out.println(
            "GameWebSocketClient created for room."
        );

        roomGUI.updateStatus(
            "WebSocket setup pending."
        );
    }

    public void createRoom(
        String playerName
    ) {

        System.out.println(
            "Create room requested: "
            + playerName
        );

        if (roomGUI != null) {

            roomGUI.updateStatus(
                "Create room connection pending."
            );
        }
    }

    public void joinRoom(
        String roomCode,
        String playerName
    ) {

        System.out.println(
            "Join room requested: "
            + roomCode
        );

        if (roomGUI != null) {

            roomGUI.updateStatus(
                "Join room connection pending."
            );
        }
    }

    public void sendMove(
        MovePayload payload
    ) {

        System.out.println(
            "Move sending is pending."
        );
    }

    public boolean isOpen() {

        return connected;
    }

    public void processMessage(
        String message
    ) {

        System.out.println(
            "Server message: "
            + message
        );
    }

    public void processRemoteMove(
        MovePayload payload
    ) {

        if (controller != null) {

            controller.processRemoteMove(
                payload
            );
        }
    }
}