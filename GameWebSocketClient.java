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

        System.out.println(
            "GameWebSocketClient created."
        );
    }

    public GameWebSocketClient(
        RoomGUI roomGUI
    ) {

        this.roomGUI = roomGUI;

        connected = false;

        System.out.println(
            "GameWebSocketClient created for RoomGUI."
        );
    }

    public void createRoom(
        String playerName
    ) {

        System.out.println(
            "Create room requested."
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
            "Join room requested."
        );

        if (roomGUI != null) {

            roomGUI.updateStatus(
                "Join room connection pending."
            );
        }
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
            "Remote move received."
        );
    }
}