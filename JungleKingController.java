import java.net.URI;
import javax.swing.JOptionPane;

public class JungleKingController {
    private Board board;
    private GameGUI view;
    private Piece selectedPiece = null;
    private Player localAssignedPlayer;
    private GameWebSocketClient webSocketClient;

    public JungleKingController(String player1Name, String player2Name, String startingPlayer, String localAssignedPlayerName, String roomId, String serverUrl) {
        Player player1 = new Player(player1Name);
        Player player2 = new Player(player2Name);
        Player starting = player1Name.equals(startingPlayer) ? player1 : player2;
        
        this.board = new Board(player1, player2, starting);
        this.board.initializePieces();
        
        this.localAssignedPlayer = localAssignedPlayerName.equals(player1Name) ? player1 : player2;

        this.view = new GameGUI(board, this, starting);
        this.view.updateTurnDisplay(starting, localAssignedPlayer);

        try {
            this.webSocketClient = new GameWebSocketClient(new URI(serverUrl), this, roomId, localAssignedPlayerName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clickTile(int row, int col) {
        Player currentPlayer = board.getCurrentPlayer();

        if (!localAssignedPlayer.getName().equals(currentPlayer.getName())) {
            view.displayMessage("It is not your turn!");
            return;
        }

        Tile clickedTile = board.getTile(row, col);

        if (selectedPiece == null) {
            if (clickedTile != null && clickedTile.isOccupied()) {
                Piece pieceOnTile = clickedTile.getPiece();
                if (pieceOnTile.getOwner().getName().equals(localAssignedPlayer.getName())) {
                    selectedPiece = pieceOnTile;
                    view.updateStatusLabel("Selected " + selectedPiece.getName() + " at (" + row + ", " + col + ").");
                } else {
                    view.displayMessage("You can only select your own pieces!");
                }
            }
        } else {
            String direction = board.findDirection(selectedPiece, clickedTile);

            if (direction.equals("INVALID") || direction.equals("NC") || direction.equals("ADJ") || direction.equals("NCL")) {
                view.displayMessage("Invalid movement choice.");
                selectedPiece = null;
                view.updateStatusLabel("Selection cleared.");
            } else {
                MovePayload payload = new MovePayload(
                    selectedPiece.getRow(),
                    selectedPiece.getCol(),
                    row,
                    col,
                    direction,
                    localAssignedPlayer.getName()
                );

                selectedPiece = null;
                
                processMove(payload);
                if (webSocketClient != null && webSocketClient.isOpen()) {
                    webSocketClient.sendMove(payload);
                }
            }
        }
    }

    public void processRemoteMove(MovePayload payload) {
        processMove(payload);
    }

    public void processMove(MovePayload payload) {
        Tile sourceTile = board.getTile(payload.getFromRow(), payload.getFromCol());
        Piece pieceToMove = sourceTile.getPiece();
        Player activePlayer = board.getCurrentPlayer();

        boolean isOldLake = sourceTile.isLake();
        boolean isOldTrap = sourceTile.isTrap();

        boolean moved = board.movePiece(pieceToMove, payload.getDirection(), activePlayer);

        if (moved) {
            Tile targetTile = board.getNewTilePosition();
            boolean isNewLake = targetTile.isLake();
            boolean isNewTrap = targetTile.isTrap();
            int trapOwner = targetTile.isTrap() ? (targetTile.getCol() <= 1 ? 1 : 2) : 0;

            if (board.getCaptureMessage() != null) {
                view.displayCaptureMessage(board.getCaptureMessage());
                board.setCaptureMessage(null);
            }

            int playerNum = activePlayer.equals(board.getPlayer1()) ? 1 : 2;

            view.updateBoard(
                pieceToMove,
                payload.getFromRow(),
                payload.getFromCol(),
                payload.getDirection(),
                playerNum,
                isOldLake,
                isNewLake,
                isOldTrap,
                isNewTrap,
                trapOwner
            );

            if (board.checkWinningCondition()) {
                JOptionPane.showMessageDialog(null, "Game Over! " + activePlayer.getName() + " wins!");
                view.closeGame();
                return;
            }

            Player nextPlayer = activePlayer.equals(board.getPlayer1()) ? board.getPlayer2() : board.getPlayer1();
            board.setCurrentPlayer(nextPlayer);
            view.updateTurnDisplay(nextPlayer, localAssignedPlayer);
        }
    }
}