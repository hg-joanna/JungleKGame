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
        this.connected = false;

        System.out.println(
            "GameWebSocketClient created for RoomGUI."
        );

        connectNative();
    }

    private static native void connectNative();

    public void createRoom(
        String playerName
    ) {

        if (!connected) {

            roomGUI.updateStatus(
                "Not connected to server."
            );

            return;
        }
    }

    public void joinRoom(
        String roomCode,
        String playerName
    ) {

        if (!connected) {

            roomGUI.updateStatus(
                "Not connected to server."
            );

            return;
        }
    }

    public boolean isOpen() {

        return connected;
    }

    public void setConnected(
        boolean value
    ) {

        connected = value;

        if (roomGUI != null) {

            if (value) {

                roomGUI.updateStatus(
                    "Connected to server."
                );

            } else {

                roomGUI.updateStatus(
                    "Disconnected from server."
                );
            }
        }
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