import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.CompletionStage;

public class GameWebSocketClient
    implements WebSocket.Listener {

    private JungleKingController controller;
    private RoomGUI roomGUI;

    private WebSocket webSocket;
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

        connect();
    }

    private void connect() {

        try {

            URI serverUri =
                new URI(
                    "wss://junglekgame.onrender.com/"
                );

            System.out.println(
                "Creating WebSocket connection."
            );

            HttpClient client =
                HttpClient.newHttpClient();

            client.newWebSocketBuilder()
                .buildAsync(
                    serverUri,
                    this
                );

        } catch (Exception e) {

            System.out.println(
                "WebSocket connection failed."
            );

            if (roomGUI != null) {

                roomGUI.updateStatus(
                    "Unable to connect to server."
                );
            }
        }
    }

    public void createRoom(
        String playerName
    ) {

        if (!connected || webSocket == null) {

            if (roomGUI != null) {

                roomGUI.updateStatus(
                    "Not connected to server."
                );
            }

            return;
        }
    }

    public void joinRoom(
        String roomCode,
        String playerName
    ) {

        if (!connected || webSocket == null) {

            if (roomGUI != null) {

                roomGUI.updateStatus(
                    "Not connected to server."
                );
            }

            return;
        }
    }

    public boolean isOpen() {

        return connected;
    }

    public void sendMove() {

        if (!connected || webSocket == null) {
            return;
        }
    }

    public void processRemoteMove() {

        System.out.println(
            "Remote move requested."
        );
    }

    public void onOpen(
        WebSocket webSocket
    ) {

        this.webSocket = webSocket;
        this.connected = true;

        System.out.println(
            "WebSocket connected."
        );

        if (roomGUI != null) {

            roomGUI.updateStatus(
                "Connected to server."
            );
        }

        webSocket.request(1);
    }

    public CompletionStage<?> onText(
        WebSocket webSocket,
        CharSequence data,
        boolean last
    ) {

        System.out.println(
            "WebSocket message received."
        );

        webSocket.request(1);

        return null;
    }

    public void onError(
        WebSocket webSocket,
        Throwable error
    ) {

        connected = false;

        System.out.println(
            "WebSocket error."
        );

        if (roomGUI != null) {

            roomGUI.updateStatus(
                "WebSocket connection error."
            );
        }
    }

    public CompletionStage<?> onClose(
        WebSocket webSocket,
        int statusCode,
        String reason
    ) {

        connected = false;

        System.out.println(
            "WebSocket closed."
        );

        return null;
    }
}