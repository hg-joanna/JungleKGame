
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

        System.out.println(
            "CREATE_ROOM requested."
        );

        System.out.println(
            "Player name: " + playerName
        );

        /*
         * Test only.
         *
         * This confirms that the Java client can
         * receive a request from RoomGUI without
         * using Java's WebSocket classes.
         */

        roomGUI.updateStatus(
            "JavaScript bridge test."
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

        roomGUI.updateStatus(
            "JavaScript bridge test."
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

