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

        connected = false;
    }

    public GameWebSocketClient(
        RoomGUI roomGUI
    ) {

        this.roomGUI = roomGUI;

        connected = false;

        connect(
            "wss://junglekgame.onrender.com/"
        );
    }

    private native void connect(
        String serverUrl
    );

    private native void sendNative(
        String message
    );

    private native boolean isNativeOpen();

    public void onOpen() {

        connected = true;

        System.out.println(
            "WebSocket connected."
        );

        if (roomGUI != null) {

            roomGUI.updateStatus(
                "Connected to server."
            );
        }
    }

    public void onMessage(
        String message
    ) {

        System.out.println(
            "Server message: "
            + message
        );

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
    }

    public void onError(
        String message
    ) {

        connected = false;

        System.out.println(
            "WebSocket error: "
            + message
        );

        if (roomGUI != null) {

            roomGUI.updateStatus(
                "Unable to connect to server."
            );
        }
    }

    public void onClose() {

        connected = false;

        if (roomGUI != null) {

            roomGUI.updateStatus(
                "Disconnected from server."
            );
        }
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

        send(
            message.toString()
        );
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

        send(
            message.toString()
        );
    }

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

    private void send(
        String message
    ) {

        if (!connected) {

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

    public boolean isOpen() {

        if (!connected) {
            return false;
        }

        return isNativeOpen();
    }

    public void sendMove() {

        // Movement networking will be added after
        // room creation and joining are working.
    }
}