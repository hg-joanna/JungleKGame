import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.CompletionStage;

/**
 * Handles the WebSocket connection between the game client and server.
 */
public class GameWebSocketClient
    implements WebSocket.Listener {

    private JungleKingController controller;
    private RoomGUI roomGUI;

    private String roomId;
    private String playerName;

    private WebSocket webSocket;

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

        connect(serverUri);
    }

    /**
     * Constructor used by the room GUI.
     */
    public GameWebSocketClient(
        URI serverUri,
        RoomGUI roomGUI
    ) {

        this.roomGUI = roomGUI;

        connect(serverUri);
    }

    /**
     * Connects to the WebSocket server.
     */
    private void connect(
        URI serverUri
    ) {

        HttpClient client =
            HttpClient.newHttpClient();

        client.newWebSocketBuilder()
            .buildAsync(
                serverUri,
                this
            )
            .thenAccept(
                new WebSocketOpenHandler(this)
            )
            .exceptionally(
                new WebSocketErrorHandler(this)
            );
    }

    /**
     * Called when the connection opens.
     */
    private void onOpen() {

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
     * Creates a room.
     *
     * @param playerName local player name
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
     *
     * @param roomCode room code
     * @param playerName local player name
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
     * Handles incoming messages.
     */
    @Override
    public CompletionStage<?> onText(
        WebSocket webSocket,
        CharSequence data,
        boolean last
    ) {

        String message =
            data.toString();

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

        if (
            message.contains(
                "\"type\":\"MOVE\""
            )
        ) {

            handleMove(
                message
            );
        }

        return WebSocket.Listener.super.onText(
            webSocket,
            data,
            last
        );
    }

    /**
     * Handles a room-created message.
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
     * Handles a movement message.
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
     * Sends a movement.
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
     * Sends a message.
     */
    private void send(
        String message
    ) {

        if (webSocket != null) {

            webSocket.sendText(
                message,
                true
            );
        }
    }

    /**
     * Checks whether the socket exists.
     */
    public boolean isOpen() {

        return webSocket != null;
    }

    /**
     * Handles successful WebSocket connections.
     */
    public static class WebSocketOpenHandler
        implements java.util.function.Consumer<WebSocket> {

        private GameWebSocketClient client;

        public WebSocketOpenHandler(
            GameWebSocketClient client
        ) {

            this.client = client;
        }

        public void accept(
            WebSocket socket
        ) {

            client.webSocket =
                socket;

            client.onOpen();
        }
    }

    /**
     * Handles connection errors.
     */
    public static class WebSocketErrorHandler
        implements java.util.function.Function<
            Throwable,
            Void
        > {

        private GameWebSocketClient client;

        public WebSocketErrorHandler(
            GameWebSocketClient client
        ) {

            this.client = client;
        }

        public Void apply(
            Throwable error
        ) {

            if (client.roomGUI != null) {

                client.roomGUI.updateStatus(
                    "Unable to connect to server."
                );
            }

            error.printStackTrace();

            return null;
        }
    }
}