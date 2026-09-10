import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.CompletionStage;

public class GameWebSocketClient implements WebSocket.Listener {
    private JungleKingController controller;
    private String roomId;
    private String playerName;
    private WebSocket webSocket;

    public GameWebSocketClient(URI serverUri, JungleKingController controller, String roomId, String playerName) {
        this.controller = controller;
        this.roomId = roomId;
        this.playerName = playerName;

        HttpClient client = HttpClient.newHttpClient();
        client.newWebSocketBuilder()
              .buildAsync(serverUri, this)
              .thenAccept(ws -> {
                  this.webSocket = ws;
                  onOpen();
              })
              .exceptionally(ex -> {
                  ex.printStackTrace();
                  return null;
              });
    }

    private void onOpen() {
        String joinMsg = String.format("{\"type\":\"JOIN_ROOM\",\"roomId\":\"%s\",\"playerName\":\"%s\"}", roomId, playerName);
        send(joinMsg);
    }

    @Override
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        String message = data.toString();
        if (message.contains("\"type\":\"MOVE\"")) {
            int payloadStart = message.indexOf("\"payload\":\"") + 11;
            int payloadEnd = message.indexOf("\"", payloadStart);
            if (payloadStart > 10 && payloadEnd > payloadStart) {
                String payloadStr = message.substring(payloadStart, payloadEnd);
                MovePayload payload = MovePayload.deserialize(payloadStr);
                controller.processRemoteMove(payload);
            }
        }
        return WebSocket.Listener.super.onText(webSocket, data, last);
    }

    public void sendMove(MovePayload payload) {
        String moveMsg = String.format("{\"type\":\"MOVE\",\"roomId\":\"%s\",\"payload\":\"%s\"}", roomId, payload.serialize());
        send(moveMsg);
    }

    private void send(String text) {
        if (webSocket != null) {
            webSocket.sendText(text, true);
        }
    }

    public boolean isOpen() {
        return webSocket != null;
    }
}