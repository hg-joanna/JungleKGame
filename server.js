const http = require('http');
const WebSocket = require('ws');

const PORT = process.env.PORT || 8080;

const server = http.createServer((req, res) => {
    res.writeHead(200, {
        'Content-Type': 'text/plain'
    });

    res.end(
        'Jungle King WebSocket Server is Live!'
    );
});

const wss =
    new WebSocket.Server({
        server
    });

const rooms = {};

/**
 * Generates a random 4-character room code.
 * Uses uppercase letters and numbers.
 */
function generateRoomCode() {

    const characters =
        'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';

    let roomCode = '';

    do {

        roomCode = '';

        for (
            let i = 0;
            i < 4;
            i++
        ) {

            const randomIndex =
                Math.floor(
                    Math.random()
                    * characters.length
                );

            roomCode +=
                characters[randomIndex];
        }

    } while (rooms[roomCode]);

    return roomCode;
}

wss.on(
    'connection',
    (ws) => {

        ws.isAlive = true;

        ws.on(
            'pong',
            () => {
                ws.isAlive = true;
            }
        );

        ws.on(
            'message',
            (message) => {

                try {

                    const data =
                        JSON.parse(message);

                    /**
                     * CREATE ROOM
                     */
                    if (
                        data.type ===
                        'CREATE_ROOM'
                    ) {

                        const playerName =
                            data.playerName;

                        if (
                            !playerName
                            || playerName.trim() === ''
                        ) {

                            ws.send(
                                JSON.stringify({
                                    type:
                                        'ROOM_ERROR',
                                    message:
                                        'Invalid player name.'
                                })
                            );

                            return;
                        }

                        const roomId =
                            generateRoomCode();

                        rooms[roomId] = [];

                        rooms[roomId].push({
                            ws: ws,
                            playerName:
                                playerName
                        });

                        ws.roomId =
                            roomId;

                        ws.playerName =
                            playerName;

                        ws.playerNumber =
                            1;

                        console.log(
                            playerName
                            + ' created room '
                            + roomId
                        );

                        ws.send(
                            JSON.stringify({
                                type:
                                    'ROOM_CREATED',
                                roomId:
                                    roomId,
                                playerNumber:
                                    1
                            })
                        );

                        ws.send(
                            JSON.stringify({
                                type:
                                    'ROOM_WAITING'
                            })
                        );
                    }

                    /**
                     * JOIN ROOM
                     */
                    if (
                        data.type ===
                        'JOIN_ROOM'
                    ) {

                        const roomId =
                            data.roomId
                                .trim()
                                .toUpperCase();

                        const playerName =
                            data.playerName;

                        if (
                            !roomId
                            || roomId.length !== 4
                        ) {

                            ws.send(
                                JSON.stringify({
                                    type:
                                        'ROOM_ERROR',
                                    message:
                                        'Invalid room code.'
                                })
                            );

                            return;
                        }

                        if (
                            !playerName
                            || playerName.trim() === ''
                        ) {

                            ws.send(
                                JSON.stringify({
                                    type:
                                        'ROOM_ERROR',
                                    message:
                                        'Invalid player name.'
                                })
                            );

                            return;
                        }

                        if (
                            !rooms[roomId]
                        ) {

                            ws.send(
                                JSON.stringify({
                                    type:
                                        'ROOM_ERROR',
                                    message:
                                        'Room does not exist.'
                                })
                            );

                            return;
                        }

                        if (
                            rooms[roomId].length >= 2
                        ) {

                            ws.send(
                                JSON.stringify({
                                    type:
                                        'ROOM_ERROR',
                                    message:
                                        'Room is full.'
                                })
                            );

                            return;
                        }

                        if (
                            rooms[roomId].some(
                                (client) =>
                                    client.playerName
                                    === playerName
                            )
                        ) {

                            ws.send(
                                JSON.stringify({
                                    type:
                                        'ROOM_ERROR',
                                    message:
                                        'Player name is already in this room.'
                                })
                            );

                            return;
                        }

                        rooms[roomId].push({
                            ws: ws,
                            playerName:
                                playerName
                        });

                        ws.roomId =
                            roomId;

                        ws.playerName =
                            playerName;

                        ws.playerNumber =
                            2;

                        console.log(
                            playerName
                            + ' joined room '
                            + roomId
                        );

                        /**
                         * The room now has two players.
                         */
                        if (
                            rooms[roomId].length
                            === 2
                        ) {

                            const player1 =
                                rooms[roomId][0];

                            const player2 =
                                rooms[roomId][1];

                            player1.ws.send(
                                JSON.stringify({
                                    type:
                                        'ROOM_READY',
                                    roomId:
                                        roomId,
                                    player1Name:
                                        player1.playerName,
                                    player2Name:
                                        player2.playerName,
                                    playerNumber:
                                        1
                                })
                            );

                            player2.ws.send(
                                JSON.stringify({
                                    type:
                                        'ROOM_READY',
                                    roomId:
                                        roomId,
                                    player1Name:
                                        player1.playerName,
                                    player2Name:
                                        player2.playerName,
                                    playerNumber:
                                        2
                                })
                            );

                            console.log(
                                'Room '
                                + roomId
                                + ' is ready.'
                            );
                        }
                    }

                    /**
                     * MOVE
                     */
                    if (
                        data.type ===
                        'MOVE'
                    ) {

                        const roomId =
                            data.roomId;

                        const payload =
                            data.payload;

                        if (
                            rooms[roomId]
                        ) {

                            rooms[roomId]
                                .forEach(
                                    (client) => {

                                        if (
                                            client.ws !== ws
                                            && client.ws.readyState
                                            === WebSocket.OPEN
                                        ) {

                                            client.ws.send(
                                                JSON.stringify({
                                                    type:
                                                        'MOVE',
                                                    payload:
                                                        payload
                                                })
                                            );
                                        }
                                    }
                                );
                        }
                    }

                } catch (err) {

                    console.error(
                        'Failed to parse incoming message:',
                        err
                    );
                }
            }
        );

        ws.on(
            'close',
            () => {

                if (
                    ws.roomId
                    && rooms[ws.roomId]
                ) {

                    rooms[ws.roomId] =
                        rooms[ws.roomId].filter(
                            (client) =>
                                client.ws !== ws
                        );

                    if (
                        rooms[ws.roomId].length
                        === 0
                    ) {

                        delete rooms[
                            ws.roomId
                        ];
                    }
                }
            }
        );
    }
);

/**
 * Keep-alive ping interval.
 */
const interval =
    setInterval(
        () => {

            wss.clients.forEach(
                (ws) => {

                    if (
                        ws.isAlive === false
                    ) {

                        return ws.terminate();
                    }

                    ws.isAlive = false;

                    ws.ping();
                }
            );
        },
        30000
    );

wss.on(
    'close',
    () => {

        clearInterval(
            interval
        );
    }
);

server.listen(
    PORT,
    () => {

        console.log(
            'Jungle King Server running on port '
            + PORT
        );
    }
);