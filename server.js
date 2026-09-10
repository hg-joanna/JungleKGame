const WebSocket = require('ws');

// Use Render's dynamic port, falling back to 8080 for local development
const PORT = process.env.PORT || 8080;
const wss = new WebSocket.Server({ port: PORT });

const rooms = {};

wss.on('connection', (ws) => {
    ws.isAlive = true;
    ws.on('pong', () => { ws.isAlive = true; });

    ws.on('message', (message) => {
        try {
            const data = JSON.parse(message);

            if (data.type === 'JOIN_ROOM') {
                const { roomId, playerName } = data;
                if (!rooms[roomId]) {
                    rooms[roomId] = [];
                }
                
                // Prevent duplicate additions
                if (!rooms[roomId].some(client => client.ws === ws)) {
                    rooms[roomId].push({ ws, playerName });
                }
                
                ws.roomId = roomId;
                ws.playerName = playerName;

                console.log(`${playerName} joined room ${roomId}`);

                if (rooms[roomId].length === 2) {
                    rooms[roomId].forEach((client) => {
                        if (client.ws.readyState === WebSocket.OPEN) {
                            client.ws.send(JSON.stringify({ type: 'ROOM_READY' }));
                        }
                    });
                }
            }

            if (data.type === 'MOVE') {
                const { roomId, payload } = data;
                if (rooms[roomId]) {
                    rooms[roomId].forEach((client) => {
                        if (client.ws !== ws && client.ws.readyState === WebSocket.OPEN) {
                            client.ws.send(JSON.stringify({ type: 'MOVE', payload }));
                        }
                    });
                }
            }
        } catch (err) {
            console.error('Failed to parse incoming message:', err);
        }
    });

    ws.on('close', () => {
        if (ws.roomId && rooms[ws.roomId]) {
            rooms[ws.roomId] = rooms[ws.roomId].filter((client) => client.ws !== ws);
        }
    });
});

// Ping interval to keep Render connections active
const interval = setInterval(() => {
    wss.clients.forEach((ws) => {
        if (ws.isAlive === false) return ws.terminate();
        ws.isAlive = false;
        ws.ping();
    });
}, 30000);

wss.on('close', () => {
    clearInterval(interval);
});

console.log(`Jungle King WebSocket Server running on port ${PORT}`);