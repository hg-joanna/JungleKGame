
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GameWebSocketClient {

    private JungleKingController controller;
    private RoomGUI roomGUI;

    private boolean connected;

    private static final String SERVER_URL =
        "https://junglekgame.onrender.com";

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
            "HTTP client loaded."
        );
    }

    public void createRoom(
        String playerName
    ) {

        StringBuilder json =
            new StringBuilder();

        json.append(
            "{"
        );

        json.append(
            "\"playerName\":\""
        );

        json.append(
            escapeJson(playerName)
        );

        json.append(
            "\""
        );

        json.append(
            "}"
        );

        sendRequest(
            "/api/create-room",
            json.toString()
        );
    }

    public void joinRoom(
        String roomCode,
        String playerName
    ) {

        StringBuilder json =
            new StringBuilder();

        json.append(
            "{"
        );

        json.append(
            "\"roomId\":\""
        );

        json.append(
            escapeJson(roomCode)
        );

        json.append(
            "\","
        );

        json.append(
            "\"playerName\":\""
        );

        json.append(
            escapeJson(playerName)
        );

        json.append(
            "\""
        );

        json.append(
            "}"
        );

        sendRequest(
            "/api/join-room",
            json.toString()
        );
    }

    private void sendRequest(
        String endpoint,
        String json
    ) {

        Thread requestThread =
            new Thread(
                new HttpRequestTask(
                    endpoint,
                    json
                )
            );

        requestThread.start();
    }

    private String escapeJson(
        String value
    ) {

        StringBuilder result =
            new StringBuilder();

        for (
            int i = 0;
            i < value.length();
            i++
        ) {

            char character =
                value.charAt(i);

            if (character == '\\') {

                result.append(
                    "\\\\"
                );

            } else if (character == '"') {

                result.append(
                    "\\\""
                );

            } else {

                result.append(
                    character
                );
            }
        }

        return result.toString();
    }

    private class HttpRequestTask
        implements Runnable {

        private String endpoint;
        private String json;

        public HttpRequestTask(
            String endpoint,
            String json
        ) {

            this.endpoint =
                endpoint;

            this.json =
                json;
        }

        public void run() {

            HttpURLConnection connection =
                null;

            try {

                StringBuilder urlText =
                    new StringBuilder();

                urlText.append(
                    SERVER_URL
                );

                urlText.append(
                    endpoint
                );

                URL url =
                    new URL(
                        urlText.toString()
                    );

                connection =
                    (HttpURLConnection)
                        url.openConnection();

                connection.setRequestMethod(
                    "POST"
                );

                connection.setDoOutput(
                    true
                );

                connection.setRequestProperty(
                    "Content-Type",
                    "application/json"
                );

                connection.setRequestProperty(
                    "Accept",
                    "application/json"
                );


                OutputStream output =
                    connection.getOutputStream();

                output.write(
                    json.getBytes(
                        "UTF-8"
                    )
                );

                output.flush();
                output.close();


                int responseCode =
                    connection.getResponseCode();


                InputStream input;

                if (
                    responseCode >= 200
                    && responseCode < 400
                ) {

                    input =
                        connection.getInputStream();

                } else {

                    input =
                        connection.getErrorStream();
                }


                BufferedReader reader =
                    new BufferedReader(
                        new InputStreamReader(
                            input,
                            "UTF-8"
                        )
                    );


                StringBuilder response =
                    new StringBuilder();

                String line;

                while (
                    (
                        line =
                            reader.readLine()
                    ) != null
                ) {

                    response.append(
                        line
                    );
                }

                reader.close();


                System.out.println(
                    "HTTP response code:"
                );

                System.out.println(
                    responseCode
                );

                System.out.println(
                    "HTTP response:"
                );

                System.out.println(
                    response.toString()
                );


                handleServerResponse(
                    response.toString()
                );


            } catch (Exception e) {

                System.out.println(
                    "HTTP request failed."
                );

                e.printStackTrace();

                if (
                    roomGUI != null
                ) {

                    roomGUI.updateStatus(
                        "Unable to connect to server."
                    );
                }

            } finally {

                if (
                    connection != null
                ) {

                    connection.disconnect();
                }
            }
        }
    }

    private void handleServerResponse(
        String response
    ) {

        if (
            roomGUI == null
        ) {

            return;
        }


        if (
            response.indexOf(
                "\"type\":\"ROOM_CREATED\""
            ) >= 0
        ) {

            String roomCode =
                extractValue(
                    response,
                    "roomId"
                );

            roomGUI.showCreatedRoom(
                roomCode
            );

            return;
        }


        if (
            response.indexOf(
                "\"type\":\"ROOM_READY\""
            ) >= 0
        ) {

            String player1 =
                extractValue(
                    response,
                    "player1Name"
                );

            String player2 =
                extractValue(
                    response,
                    "player2Name"
                );

            roomGUI.showRoomReady(
                player1,
                player2
            );

            return;
        }


        if (
            response.indexOf(
                "\"type\":\"ROOM_ERROR\""
            ) >= 0
        ) {

            String message =
                extractValue(
                    response,
                    "message"
                );

            roomGUI.updateStatus(
                message
            );

            return;
        }


        roomGUI.updateStatus(
            "Server response received."
        );
    }

    private String extractValue(
        String json,
        String key
    ) {

        StringBuilder search =
            new StringBuilder();

        search.append(
            "\""
        );

        search.append(
            key
        );

        search.append(
            "\":\""
        );


        int start =
            json.indexOf(
                search.toString()
            );


        if (
            start < 0
        ) {

            return "";
        }


        start +=
            search.length();


        int end =
            json.indexOf(
                "\"",
                start
            );


        if (
            end < 0
        ) {

            return "";
        }


        return json.substring(
            start,
            end
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

