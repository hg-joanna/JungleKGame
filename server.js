const WebSocket = require('ws');
const wss = new WebSocket.Server({ port: 8080 });

const rooms = {};

wss.on('connection', (ws) => {
    ws.on('message', (message) => {
        const data = JSON.parse(message);

        if (data.type === 'JOIN_ROOM') {
            const { roomId, playerName } = data;
            if (!rooms[roomId]) {
                rooms[roomId] = [];
            }
            rooms[roomId].push({ ws, playerName });
            ws.roomId = roomId;
            ws.playerName = playerName;

            console.log(`${playerName} joined room ${roomId}`);

            if (rooms[roomId].length === 2) {
                rooms[roomId].forEach((client) => {
                    client.ws.send(JSON.stringify({ type: 'ROOM_READY' }));
                });
            }
        }

        if (data.type === 'MOVE') {
            const { roomId, payload } = data;
            if (rooms[roomId]) {
                rooms[roomId].forEach((client) => {
                    if (client.ws !== ws) {
                        client.ws.send(JSON.stringify({ type: 'MOVE', payload }));
                    }
                });
            }
        }
    });

    ws.on('close', () => {
        if (ws.roomId && rooms[ws.roomId]) {
            rooms[ws.roomId] = rooms[ws.roomId].filter((client) => client.ws !== ws);
        }
    });
});

console.log('Jungle King WebSocket Server running on port 8080');