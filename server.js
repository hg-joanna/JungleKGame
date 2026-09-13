
const http = require('http');
const WebSocket = require('ws');

const PORT = process.env.PORT || 8080;

const rooms = {};


/*
 * ---------------------------------------------------------
 * Utility functions
 * ---------------------------------------------------------
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


function sendJson(
    res,
    statusCode,
    data
) {

    res.writeHead(
        statusCode,
        {
            'Content-Type':
                'application/json',
            'Access-Control-Allow-Origin':
                '*',
            'Access-Control-Allow-Methods':
                'GET, POST, OPTIONS',
            'Access-Control-Allow-Headers':
                'Content-Type'
        }
    );

    res.end(
        JSON.stringify(data)
    );
}


function getRequestBody(
    req
) {

    return new Promise(
        (
            resolve,
            reject
        ) => {

            let body = '';

            req.on(
                'data',
                (chunk) => {

                    body +=
                        chunk.toString();
                }
            );

            req.on(
                'end',
                () => {

                    if (
                        body.length === 0
                    ) {

                        resolve({});
                        return;
                    }

                    try {

                        resolve(
                            JSON.parse(body)
                        );

                    } catch (error) {

                        reject(error);
                    }
                }
            );

            req.on(
                'error',
                (error) => {

                    reject(error);
                }
            );
        }
    );
}


function getRoomPlayers(
    room
) {

    return room.players.map(
        (player) => {

            return {
                playerName:
                    player.playerName,

                playerNumber:
                    player.playerNumber,

                cardSelection:
                    player.cardSelection
            };
        }
    );
}


function getRoomState(
    roomId
) {

    const room =
        rooms[roomId];

    if (!room) {

        return null;
    }

    return {
        roomId:
            roomId,

        playerCount:
            room.players.length,

        players:
            getRoomPlayers(room),

        ready:
            room.players.length === 2,

        startingPlayer:
            room.startingPlayer,

        moves:
            room.moves
    };
}


/*
 * ---------------------------------------------------------
 * HTTP API
 * ---------------------------------------------------------
 *
 * These endpoints are used by the future HTTP-based
 * Java/CheerpJ client.
 *
 * WebSocket functionality remains below.
 * ---------------------------------------------------------
 */

const server =
    http.createServer(
        async (req, res) => {

            /*
             * CORS
             */

            if (
                req.method === 'OPTIONS'
            ) {

                res.writeHead(
                    204,
                    {
                        'Access-Control-Allow-Origin':
                            '*',

                        'Access-Control-Allow-Methods':
                            'GET, POST, OPTIONS',

                        'Access-Control-Allow-Headers':
                            'Content-Type'
                    }
                );

                res.end();

                return;
            }


            /*
             * Basic server status
             */

            if (
                req.method === 'GET'
                && req.url === '/'
            ) {

                sendJson(
                    res,
                    200,
                    {
                        status:
                            'online',

                        message:
                            'Jungle King Server is Live!'
                    }
                );

                return;
            }


            /*
             * -------------------------------------------------
             * CREATE ROOM
             * POST /api/create-room
             *
             * Body:
             * {
             *     "playerName": "Alice"
             * }
             * -------------------------------------------------
             */

            if (
                req.method === 'POST'
                && req.url === '/api/create-room'
            ) {

                try {

                    const data =
                        await getRequestBody(req);

                    const playerName =
                        data.playerName;

                    if (
                        !playerName
                        || playerName.trim() === ''
                    ) {

                        sendJson(
                            res,
                            400,
                            {
                                type:
                                    'ROOM_ERROR',

                                message:
                                    'Invalid player name.'
                            }
                        );

                        return;
                    }


                    const roomId =
                        generateRoomCode();


                    rooms[roomId] = {

                        players: [],

                        startingPlayer:
                            null,

                        moves: []
                    };


                    rooms[roomId].players.push(
                        {
                            ws:
                                null,

                            playerName:
                                playerName.trim(),

                            playerNumber:
                                1,

                            cardSelection:
                                null
                        }
                    );


                    console.log(
                        playerName
                        + ' created HTTP room '
                        + roomId
                    );


                    sendJson(
                        res,
                        200,
                        {
                            type:
                                'ROOM_CREATED',

                            roomId:
                                roomId,

                            playerNumber:
                                1
                        }
                    );

                } catch (error) {

                    console.error(
                        'Create room error:',
                        error
                    );

                    sendJson(
                        res,
                        500,
                        {
                            type:
                                'ROOM_ERROR',

                            message:
                                'Unable to create room.'
                        }
                    );
                }

                return;
            }


            /*
             * -------------------------------------------------
             * JOIN ROOM
             * POST /api/join-room
             *
             * Body:
             * {
             *     "roomId": "ABCD",
             *     "playerName": "Bob"
             * }
             * -------------------------------------------------
             */

            if (
                req.method === 'POST'
                && req.url === '/api/join-room'
            ) {

                try {

                    const data =
                        await getRequestBody(req);

                    const roomId =
                        data.roomId
                            ? data.roomId
                                .trim()
                                .toUpperCase()
                            : '';

                    const playerName =
                        data.playerName;


                    if (
                        !roomId
                        || roomId.length !== 4
                    ) {

                        sendJson(
                            res,
                            400,
                            {
                                type:
                                    'ROOM_ERROR',

                                message:
                                    'Invalid room code.'
                            }
                        );

                        return;
                    }


                    if (
                        !playerName
                        || playerName.trim() === ''
                    ) {

                        sendJson(
                            res,
                            400,
                            {
                                type:
                                    'ROOM_ERROR',

                                message:
                                    'Invalid player name.'
                            }
                        );

                        return;
                    }


                    if (
                        !rooms[roomId]
                    ) {

                        sendJson(
                            res,
                            404,
                            {
                                type:
                                    'ROOM_ERROR',

                                message:
                                    'Room does not exist.'
                            }
                        );

                        return;
                    }


                    const room =
                        rooms[roomId];


                    if (
                        room.players.length >= 2
                    ) {

                        sendJson(
                            res,
                            400,
                            {
                                type:
                                    'ROOM_ERROR',

                                message:
                                    'Room is full.'
                            }
                        );

                        return;
                    }


                    const duplicate =
                        room.players.some(
                            (player) => {

                                return (
                                    player.playerName
                                    === playerName.trim()
                                );
                            }
                        );


                    if (duplicate) {

                        sendJson(
                            res,
                            400,
                            {
                                type:
                                    'ROOM_ERROR',

                                message:
                                    'Player name is already in this room.'
                            }
                        );

                        return;
                    }


                    room.players.push(
                        {
                            ws:
                                null,

                            playerName:
                                playerName.trim(),

                            playerNumber:
                                2,

                            cardSelection:
                                null
                        }
                    );


                    console.log(
                        playerName
                        + ' joined HTTP room '
                        + roomId
                    );


                    sendJson(
                        res,
                        200,
                        {
                            type:
                                'ROOM_READY',

                            roomId:
                                roomId,

                            player1Name:
                                room.players[0]
                                    .playerName,

                            player2Name:
                                room.players[1]
                                    .playerName,

                            playerNumber:
                                2
                        }
                    );


                } catch (error) {

                    console.error(
                        'Join room error:',
                        error
                    );

                    sendJson(
                        res,
                        500,
                        {
                            type:
                                'ROOM_ERROR',

                            message:
                                'Unable to join room.'
                        }
                    );
                }

                return;
            }


            /*
             * -------------------------------------------------
             * GET ROOM STATUS
             * GET /api/room/ABCD
             * -------------------------------------------------
             */

            if (
                req.method === 'GET'
                && req.url.startsWith(
                    '/api/room/'
                )
            ) {

                const roomId =
                    req.url
                        .substring(
                            '/api/room/'.length
                        )
                        .trim()
                        .toUpperCase();


                if (
                    !rooms[roomId]
                ) {

                    sendJson(
                        res,
                        404,
                        {
                            type:
                                'ROOM_ERROR',

                            message:
                                'Room does not exist.'
                        }
                    );

                    return;
                }


                sendJson(
                    res,
                    200,
                    {
                        type:
                            'ROOM_STATUS',

                        room:
                            getRoomState(
                                roomId
                            )
                    }
                );

                return;
            }


            /*
             * -------------------------------------------------
             * SELECT CARD
             * POST /api/select-card
             *
             * Body:
             * {
             *     "roomId": "ABCD",
             *     "playerNumber": 1,
             *     "animalIndex": 3
             * }
             * -------------------------------------------------
             */

            if (
                req.method === 'POST'
                && req.url === '/api/select-card'
            ) {

                try {

                    const data =
                        await getRequestBody(req);

                    const roomId =
                        data.roomId
                            ? data.roomId
                                .trim()
                                .toUpperCase()
                            : '';

                    const playerNumber =
                        Number(
                            data.playerNumber
                        );

                    const animalIndex =
                        Number(
                            data.animalIndex
                        );


                    if (
                        !rooms[roomId]
                    ) {

                        sendJson(
                            res,
                            404,
                            {
                                type:
                                    'ROOM_ERROR',

                                message:
                                    'Room does not exist.'
                            }
                        );

                        return;
                    }


                    const room =
                        rooms[roomId];


                    const player =
                        room.players.find(
                            (item) => {

                                return (
                                    item.playerNumber
                                    === playerNumber
                                );
                            }
                        );


                    if (!player) {

                        sendJson(
                            res,
                            404,
                            {
                                type:
                                    'ROOM_ERROR',

                                message:
                                    'Player is not in this room.'
                            }
                        );

                        return;
                    }


                    player.cardSelection =
                        animalIndex;


                    console.log(
                        'Player '
                        + playerNumber
                        + ' selected a card in room '
                        + roomId
                    );


                    sendJson(
                        res,
                        200,
                        {
                            type:
                                'CARD_SELECTED',

                            room:
                                getRoomState(
                                    roomId
                                )
                        }
                    );

                } catch (error) {

                    console.error(
                        'Card selection error:',
                        error
                    );

                    sendJson(
                        res,
                        500,
                        {
                            type:
                                'ROOM_ERROR',

                            message:
                                'Unable to select card.'
                        }
                    );
                }

                return;
            }


            /*
             * -------------------------------------------------
             * MOVE
             * POST /api/move
             *
             * Body:
             * {
             *     "roomId": "ABCD",
             *     "playerNumber": 1,
             *     "payload": {...}
             * }
             * -------------------------------------------------
             */

            if (
                req.method === 'POST'
                && req.url === '/api/move'
            ) {

                try {

                    const data =
                        await getRequestBody(req);

                    const roomId =
                        data.roomId
                            ? data.roomId
                                .trim()
                                .toUpperCase()
                            : '';

                    const playerNumber =
                        Number(
                            data.playerNumber
                        );

                    const payload =
                        data.payload;


                    if (
                        !rooms[roomId]
                    ) {

                        sendJson(
                            res,
                            404,
                            {
                                type:
                                    'ROOM_ERROR',

                                message:
                                    'Room does not exist.'
                            }
                        );

                        return;
                    }


                    const room =
                        rooms[roomId];


                    const player =
                        room.players.find(
                            (item) => {

                                return (
                                    item.playerNumber
                                    === playerNumber
                                );
                            }
                        );


                    if (!player) {

                        sendJson(
                            res,
                            404,
                            {
                                type:
                                    'ROOM_ERROR',

                                message:
                                    'Player is not in this room.'
                            }
                        );

                        return;
                    }


                    room.moves.push(
                        {
                            playerNumber:
                                playerNumber,

                            payload:
                                payload,

                            timestamp:
                                Date.now()
                        }
                    );


                    console.log(
                        'Move received from Player '
                        + playerNumber
                        + ' in room '
                        + roomId
                    );


                    sendJson(
                        res,
                        200,
                        {
                            type:
                                'MOVE_RECEIVED',

                            room:
                                getRoomState(
                                    roomId
                                )
                        }
                    );

                } catch (error) {

                    console.error(
                        'Move error:',
                        error
                    );

                    sendJson(
                        res,
                        500,
                        {
                            type:
                                'ROOM_ERROR',

                            message:
                                'Unable to process move.'
                        }
                    );
                }

                return;
            }


            /*
             * Unknown HTTP route
             */

            sendJson(
                res,
                404,
                {
                    type:
                        'NOT_FOUND',

                    message:
                        'Endpoint not found.'
                }
            );
        }
    );


/*
 * ---------------------------------------------------------
 * EXISTING WEBSOCKET SERVER
 * ---------------------------------------------------------
 */

const wss =
    new WebSocket.Server({
        server
    });


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


                    /*
                     * -----------------------------------------
                     * CREATE ROOM
                     * -----------------------------------------
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


                        rooms[roomId] = {

                            players: [],

                            startingPlayer:
                                null,

                            moves: []
                        };


                        rooms[roomId].players.push(
                            {
                                ws:
                                    ws,

                                playerName:
                                    playerName.trim(),

                                playerNumber:
                                    1,

                                cardSelection:
                                    null
                            }
                        );


                        ws.roomId =
                            roomId;

                        ws.playerName =
                            playerName.trim();

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


                    /*
                     * -----------------------------------------
                     * JOIN ROOM
                     * -----------------------------------------
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


                        const room =
                            rooms[roomId];


                        if (
                            room.players.length >= 2
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


                        const duplicate =
                            room.players.some(
                                (client) => {

                                    return (
                                        client.playerName
                                        === playerName
                                    );
                                }
                            );


                        if (duplicate) {

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


                        room.players.push(
                            {
                                ws:
                                    ws,

                                playerName:
                                    playerName.trim(),

                                playerNumber:
                                    2,

                                cardSelection:
                                    null
                            }
                        );


                        ws.roomId =
                            roomId;

                        ws.playerName =
                            playerName.trim();

                        ws.playerNumber =
                            2;


                        console.log(
                            playerName
                            + ' joined room '
                            + roomId
                        );


                        if (
                            room.players.length
                            === 2
                        ) {

                            const player1 =
                                room.players[0];

                            const player2 =
                                room.players[1];


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


                    /*
                     * -----------------------------------------
                     * MOVE
                     * -----------------------------------------
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
                                .moves.push(
                                    {
                                        playerNumber:
                                            ws.playerNumber,

                                        payload:
                                            payload,

                                        timestamp:
                                            Date.now()
                                    }
                                );


                            rooms[roomId]
                                .players
                                .forEach(
                                    (client) => {

                                        if (
                                            client.ws !== ws
                                            && client.ws
                                                .readyState
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


        /*
         * ---------------------------------------------
         * WebSocket disconnect
         * ---------------------------------------------
         */

        ws.on(
            'close',
            () => {

                if (
                    ws.roomId
                    && rooms[ws.roomId]
                ) {

                    const room =
                        rooms[ws.roomId];


                    room.players =
                        room.players.filter(
                            (client) => {

                                return (
                                    client.ws !== ws
                                );
                            }
                        );


                    /*
                     * If a WebSocket player disconnects,
                     * keep HTTP players if any remain.
                     */

                    if (
                        room.players.length === 0
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


/*
 * ---------------------------------------------------------
 * WebSocket keep-alive
 * ---------------------------------------------------------
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


/*
 * ---------------------------------------------------------
 * START SERVER
 * ---------------------------------------------------------
 */

server.listen(
    PORT,
    () => {

        console.log(
            'Jungle King Server running on port '
            + PORT
        );
    }
);

