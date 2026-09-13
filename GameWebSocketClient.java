import java.net.URI;

/**
 * Handles the WebSocket connection between the game client and server.
 *
 * The actual browser WebSocket is implemented through CheerpJ JavaScript
 * native methods so that the Java application does not depend on
 * java.net.http.WebSocket.
 */
public class GameWebSocketClient {

    private JungleKingController controller;
    private RoomGUI roomGUI;

    private String roomId;
    private String playerName;

    private boolean connected = false;

    /**
     * Constructor used by the game.
     */
    public GameWebSocketClient(
        URI serverUri,
        JungleKingController controller,
        String roomId,
        String playerName
    ) {

        this.controller = controller;
        this.roomId = roomId;
        this.playerName = playerName;

        connect(
            serverUri.toString()
        );
    }

    /**
     * Constructor used by the room GUI.
     */
    public GameWebSocketClient(
        URI serverUri,
        RoomGUI roomGUI
    ) {

        this.roomGUI = roomGUI;

        connect(
            serverUri.toString()
        );
    }

    /**
     * Connects to the browser WebSocket.
     *
     * The implementation is provided by JavaScript through CheerpJ.
     */
    private native void connect(
        String serverUri
    );

    /**
     * Sends a message through the browser WebSocket.
     *
     * The implementation is provided by JavaScript through CheerpJ.
     */
    private native void sendNative(
        String message
    );

    /**
     * Returns whether the browser WebSocket is connected.
     */
    private native boolean isNativeOpen();

    /**
     * Called by JavaScript when the WebSocket connection opens.
     */
    public void onOpen() {

        connected = true;

        if (roomGUI != null) {

            roomGUI.updateStatus(
                "Connected to server."
            );
        }

        if (
            controller != null
            && roomId != null
        ) {

            StringBuilder message =
                new StringBuilder();

            message.append("{");
            message.append("\"type\":\"JOIN_ROOM\",");
            message.append("\"roomId\":\"");
            message.append(roomId);
            message.append("\",");
            message.append("\"playerName\":\"");
            message.append(playerName);
            message.append("\"");
            message.append("}");

            send(
                message.toString()
            );
        }
    }

    /**
     * Called by JavaScript when the WebSocket connection fails.
     */
    public void onError(
        String errorMessage
    ) {

        connected = false;

        if (roomGUI != null) {

            roomGUI.updateStatus(
                "Unable to connect to server."
            );
        }

        System.out.println(
            "WebSocket error: "
            + errorMessage
        );
    }

    /**
     * Called by JavaScript when the WebSocket closes.
     */
    public void onClose() {

        connected = false;

        if (roomGUI != null) {

            roomGUI.updateStatus(
                "Disconnected from server."
            );
        }
    }

    /**
     * Called by JavaScript whenever a message is received.
     */
    public void onMessage(
        String message
    ) {

        if (
            message == null
            || message.length() == 0
        ) {
            return;
        }

        if (
            message.contains(
                "\"type\":\"ROOM_CREATED\""
            )
        ) {

            handleRoomCreated(
                message
            );
        }

        if (
            message.contains(
                "\"type\":\"ROOM_WAITING\""
            )
        ) {

            if (roomGUI != null) {

                roomGUI.updateStatus(
                    "Waiting for another player..."
                );
            }
        }

        if (
            message.contains(
                "\"type\":\"ROOM_READY\""
            )
        ) {

            if (roomGUI != null) {

                roomGUI.updateStatus(
                    "Room is ready!"
                );
            }

            handleRoomReady(
                message
            );
        }

        if (
            message.contains(
                "\"type\":\"ROOM_ERROR\""
            )
        ) {

            if (roomGUI != null) {

                roomGUI.updateStatus(
                    "Room error."
                );
            }
        }

        if (
            message.contains(
                "\"type\":\"MOVE\""
            )
        ) {

            handleMove(
                message
            );
        }
    }

    /**
     * Creates a room.
     */
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

        send(
            message.toString()
        );
    }

    /**
     * Joins an existing room.
     */
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

        send(
            message.toString()
        );
    }

    /**
     * Handles a ROOM_CREATED message.
     */
    private void handleRoomCreated(
        String message
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

            if (roomGUI != null) {

                roomGUI.showCreatedRoom(
                    roomCode
                );
            }
        }
    }

    /**
     * Handles a ROOM_READY message.
     */
    private void handleRoomReady(
        String message
    ) {

        if (roomGUI != null) {

            roomGUI.updateStatus(
                "Both players are ready!"
            );
        }
    }

    /**
     * Handles a MOVE message.
     */
    private void handleMove(
        String message
    ) {

        if (controller == null) {
            return;
        }

        int payloadStart =
            message.indexOf(
                "\"payload\":\""
            ) + 11;

        int payloadEnd =
            message.indexOf(
                "\"",
                payloadStart
            );

        if (
            payloadStart > 10
            && payloadEnd > payloadStart
        ) {

            String payloadStr =
                message.substring(
                    payloadStart,
                    payloadEnd
                );

            MovePayload payload =
                MovePayload.deserialize(
                    payloadStr
                );

            controller.processRemoteMove(
                payload
            );
        }
    }

    /**
     * Sends a movement to the server.
     */
    public void sendMove(
        MovePayload payload
    ) {

        StringBuilder message =
            new StringBuilder();

        message.append("{");
        message.append("\"type\":\"MOVE\",");
        message.append("\"roomId\":\"");
        message.append(roomId);
        message.append("\",");
        message.append("\"payload\":\"");
        message.append(payload.serialize());
        message.append("\"");
        message.append("}");

        send(
            message.toString()
        );
    }

    /**
     * Sends a message through the browser WebSocket.
     */
    private void send(
        String message
    ) {

        if (!connected) {

            System.out.println(
                "WebSocket is not connected."
            );

            if (roomGUI != null) {

                roomGUI.updateStatus(
                    "Not connected to server."
                );
            }

            return;
        }

        sendNative(
            message
        );
    }

    /**
     * Returns whether the WebSocket is open.
     */
    public boolean isOpen() {

        if (!connected) {
            return false;
        }

        return isNativeOpen();
    }
}