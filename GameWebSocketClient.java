
public class GameWebSocketClient {

    private JungleKingController controller;
    private RoomGUI roomGUI;

    private boolean connected;

    public GameWebSocketClient(
        JungleKingController controller,
        String roomId,
        String playerName
    ) {

        this.controller = controller;
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

        roomGUI.updateStatus(
            "Sending create room request..."
        );

        /*
         * JavaScript WebSocket connection will be
         * connected separately from Java.
         *
         * This method currently confirms that the
         * Java request reaches this point safely.
         */
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

        roomGUI.updateStatus(
            "Sending join room request..."
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

