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
    private String roomId;
    private String playerName;
    private WebSocket webSocket;

    /**
     * Creates a WebSocket client.
     *
     * @param serverUri WebSocket server URI
     * @param controller game controller
     * @param roomId room ID
     * @param playerName local player name
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
                new WebSocketErrorHandler()
            );
    }

    /**
     * Called after the WebSocket connection opens.
     */
    private void onOpen() {

        StringBuilder joinMessage =
            new StringBuilder();

        joinMessage.append("{");
        joinMessage.append("\"type\":\"JOIN_ROOM\",");
        joinMessage.append("\"roomId\":\"");
        joinMessage.append(roomId);
        joinMessage.append("\",");
        joinMessage.append("\"playerName\":\"");
        joinMessage.append(playerName);
        joinMessage.append("\"");
        joinMessage.append("}");

        send(joinMessage.toString());
    }

    /**
     * Handles messages received from the server.
     *
     * @param webSocket WebSocket connection
     * @param data received data
     * @param last whether this is the final message fragment
     * @return completion stage
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
                "\"type\":\"MOVE\""
            )
        ) {

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

        return WebSocket.Listener.super.onText(
            webSocket,
            data,
            last
        );
    }

    /**
     * Sends a move to the server.
     *
     * @param payload move information
     */
    public void sendMove(
        MovePayload payload
    ) {

        StringBuilder moveMessage =
            new StringBuilder();

        moveMessage.append("{");
        moveMessage.append("\"type\":\"MOVE\",");
        moveMessage.append("\"roomId\":\"");
        moveMessage.append(roomId);
        moveMessage.append("\",");
        moveMessage.append("\"payload\":\"");
        moveMessage.append(payload.serialize());
        moveMessage.append("\"");
        moveMessage.append("}");

        send(moveMessage.toString());
    }

    /**
     * Sends a text message.
     *
     * @param text message
     */
    private void send(String text) {

        if (webSocket != null) {
            webSocket.sendText(
                text,
                true
            );
        }
    }

    /**
     * Determines whether the WebSocket exists.
     *
     * @return true if connected
     */
    public boolean isOpen() {
        return webSocket != null;
    }

    /**
     * Listener used when the WebSocket opens.
     */
    public static class WebSocketOpenHandler
        implements java.util.function.Consumer<WebSocket> {

        private GameWebSocketClient client;

        public WebSocketOpenHandler(
            GameWebSocketClient client
        ) {
            this.client = client;
        }

        public void accept(WebSocket webSocket) {
            client.webSocket = webSocket;
            client.onOpen();
        }
    }

    /**
     * Listener used when the WebSocket connection fails.
     */
    public static class WebSocketErrorHandler
        implements java.util.function.Function<
            Throwable,
            Void
        > {

        public Void apply(Throwable error) {

            error.printStackTrace();

            return null;
        }
    }
}