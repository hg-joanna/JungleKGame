public class GameWebSocketClient {

    private JungleKingController controller;
    private RoomGUI roomGUI;

    public GameWebSocketClient(
        JungleKingController controller,
        String roomId,
        String playerName
    ) {

        this.controller = controller;

        System.out.println(
            "GameWebSocketClient loaded for game."
        );
    }

    public GameWebSocketClient(
        RoomGUI roomGUI
    ) {

        this.roomGUI = roomGUI;

        System.out.println(
            "GameWebSocketClient loaded for RoomGUI."
        );

        roomGUI.updateStatus(
            "WebSocket test client loaded."
        );
    }

    public void createRoom(
        String playerName
    ) {

        System.out.println(
            "Create room requested: "
            + playerName
        );
    }

    public void joinRoom(
        String roomCode,
        String playerName
    ) {

        System.out.println(
            "Join room requested: "
            + roomCode
        );
    }

    public boolean isOpen() {

        return true;
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