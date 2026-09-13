public class GameWebSocketClient {

    public GameWebSocketClient() {

        System.out.println(
            "GameWebSocketClient loaded."
        );
    }

    public void createRoom(
        String playerName
    ) {

        System.out.println(
            "Create room: "
            + playerName
        );
    }

    public void joinRoom(
        String roomCode,
        String playerName
    ) {

        System.out.println(
            "Join room: "
            + roomCode
            + " "
            + playerName
        );
    }

    public boolean isOpen() {

        return true;
    }
}