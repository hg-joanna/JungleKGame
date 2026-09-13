
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
        this.connected = false;

        System.out.println(
            "GameWebSocketClient created for RoomGUI."
        );

        connect();
    }

    private native void nativeConnect();

    private native void nativeSend(
        String message
    );

    public void connect() {

        System.out.println(
            "Connecting to Render server..."
        );

        nativeConnect();
    }

    public void onConnected() {

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

    public void onConnectionFailed() {

        connected = false;

        System.out.println(
            "WebSocket connection failed."
        );

        if (roomGUI != null) {

            roomGUI.updateStatus(
                "Unable to connect to server."
            );
        }
    }

    public void onMessage(
        String message
    ) {

        System.out.println(
            "Server: " + message
        );

        if (roomGUI == null) {
            return;
        }

        if (
            message.startsWith(
                "{\"type\":\"ROOM_CREATED\""
            )
        ) {

            String roomCode =
                extractValue(
                    message,
                    "roomId"
                );

            if (
                roomCode != null
                && roomCode.length() == 4
            ) {

                roomGUI.showCreatedRoom(
                    roomCode
                );
            }

            return;
        }

        if (
            message.startsWith(
                "{\"type\":\"ROOM_WAITING\""
            )
        ) {

            roomGUI.updateStatus(
                "Waiting for another player..."
            );

            return;
        }

        if (
            message.startsWith(
                "{\"type\":\"ROOM_READY\""
            )
        ) {

            String player1Name =
                extractValue(
                    message,
                    "player1Name"
                );

            String player2Name =
                extractValue(
                    message,
                    "player2Name"
                );

            roomGUI.showRoomReady(
                player1Name,
                player2Name
            );

            return;
        }

        if (
            message.startsWith(
                "{\"type\":\"ERROR\""
            )
        ) {

            String error =
                extractValue(
                    message,
                    "message"
                );

            if (error == null) {

                error =
                    "Server returned an error.";
            }

            roomGUI.updateStatus(
                error
            );
        }
    }

    public void createRoom(
        String playerName
    ) {

        if (!connected) {

            if (roomGUI != null) {

                roomGUI.updateStatus(
                    "Not connected to server."
                );
            }

            return;
        }

        StringBuilder message =
            new StringBuilder();

        message.append(
            "CREATE_ROOM:"
        );

        message.append(
            playerName
        );

        System.out.println(
            message.toString()
        );

        nativeSend(
            message.toString()
        );
    }

    public void joinRoom(
        String roomCode,
        String playerName
    ) {

        if (!connected) {

            if (roomGUI != null) {

                roomGUI.updateStatus(
                    "Not connected to server."
                );
            }

            return;
        }

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

        nativeSend(
            message.toString()
        );
    }

    private String extractValue(
        String json,
        String key
    ) {

        StringBuilder search =
            new StringBuilder();

        search.append(
            "\""
        );

        search.append(
            key
        );

        search.append(
            "\":\""
        );

        int start =
            json.indexOf(
                search.toString()
            );

        if (start == -1) {
            return null;
        }

        start +=
            search.length();

        int end =
            json.indexOf(
                "\"",
                start
            );

        if (end == -1) {
            return null;
        }

        return json.substring(
            start,
            end
        );
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

