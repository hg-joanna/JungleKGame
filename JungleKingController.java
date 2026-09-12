import java.net.URI;
import javax.swing.JOptionPane;

public class JungleKingController {

    private Board board;
    private GameGUI view;
    private Piece selectedPiece = null;
    private Player localAssignedPlayer;
    private GameWebSocketClient webSocketClient;

    public JungleKingController(
        String player1Name,
        String player2Name,
        String startingPlayer,
        String localAssignedPlayerName,
        String roomId,
        String serverUrl
    ) {

        Player player1 = new Player(player1Name);
        Player player2 = new Player(player2Name);

        Player starting;

        if (player1Name.equals(startingPlayer)) {
            starting = player1;
        } else {
            starting = player2;
        }

        this.board = new Board(
            player1,
            player2,
            starting
        );

        this.board.initializePieces();

        if (localAssignedPlayerName.equals(player1Name)) {
            this.localAssignedPlayer = player1;
        } else {
            this.localAssignedPlayer = player2;
        }

        this.view = new GameGUI(
            board,
            this,
            starting
        );

        this.view.updateTurnDisplay(
            starting,
            localAssignedPlayer
        );

        try {

            this.webSocketClient =
                new GameWebSocketClient(
                    new URI(serverUrl),
                    this,
                    roomId,
                    localAssignedPlayerName
                );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void clickTile(int row, int col) {

        Player currentPlayer =
            board.getCurrentPlayer();

        if (
            !localAssignedPlayer.getName()
                .equals(currentPlayer.getName())
        ) {

            view.displayMessage(
                "It is not your turn!"
            );

            return;
        }

        Tile clickedTile =
            board.getTile(row, col);

        if (selectedPiece == null) {

            if (
                clickedTile != null
                && clickedTile.isOccupied()
            ) {

                Piece pieceOnTile =
                    clickedTile.getPiece();

                if (
                    pieceOnTile.getOwner()
                        .getName()
                        .equals(
                            localAssignedPlayer.getName()
                        )
                ) {

                    selectedPiece = pieceOnTile;

                    StringBuilder status =
                        new StringBuilder();

                    status.append("Selected ");
                    status.append(
                        selectedPiece.getName()
                    );
                    status.append(" at (");
                    status.append(row);
                    status.append(", ");
                    status.append(col);
                    status.append(").");

                    view.updateStatusLabel(
                        status.toString()
                    );

                } else {

                    view.displayMessage(
                        "You can only select your own pieces!"
                    );
                }
            }

        } else {

            String direction =
                board.findDirection(
                    selectedPiece,
                    clickedTile
                );

            if (
                direction.equals("INVALID")
                || direction.equals("NC")
                || direction.equals("ADJ")
                || direction.equals("NCL")
            ) {

                view.displayMessage(
                    "Invalid movement choice."
                );

                selectedPiece = null;

                view.updateStatusLabel(
                    "Selection cleared."
                );

            } else {

                MovePayload payload =
                    new MovePayload(
                        selectedPiece.getRow(),
                        selectedPiece.getCol(),
                        row,
                        col,
                        direction,
                        localAssignedPlayer.getName()
                    );

                selectedPiece = null;

                processMove(payload);

                if (
                    webSocketClient != null
                    && webSocketClient.isOpen()
                ) {

                    webSocketClient.sendMove(
                        payload
                    );
                }
            }
        }
    }

    public void processRemoteMove(
        MovePayload payload
    ) {

        processMove(payload);
    }

    public void processMove(
        MovePayload payload
    ) {

        Tile sourceTile =
            board.getTile(
                payload.getFromRow(),
                payload.getFromCol()
            );

        Piece pieceToMove =
            sourceTile.getPiece();

        Player activePlayer =
            board.getCurrentPlayer();

        boolean isOldLake =
            sourceTile.isLake();

        boolean isOldTrap =
            sourceTile.isTrap();

        boolean moved =
            board.movePiece(
                pieceToMove,
                payload.getDirection(),
                activePlayer
            );

        if (moved) {

            Tile targetTile =
                board.getNewTilePosition();

            boolean isNewLake =
                targetTile.isLake();

            boolean isNewTrap =
                targetTile.isTrap();

            int trapOwner = 0;

            if (targetTile.isTrap()) {

                if (targetTile.getCol() <= 1) {
                    trapOwner = 1;
                } else {
                    trapOwner = 2;
                }
            }

            if (board.getCaptureMessage() != null) {

                view.displayCaptureMessage(
                    board.getCaptureMessage()
                );

                board.setCaptureMessage(null);
            }

            int playerNum;

            if (
                activePlayer.equals(
                    board.getPlayer1()
                )
            ) {

                playerNum = 1;

            } else {

                playerNum = 2;
            }

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

                StringBuilder winMessage =
                    new StringBuilder();

                winMessage.append("Game Over! ");
                winMessage.append(
                    activePlayer.getName()
                );
                winMessage.append(" wins!");

                JOptionPane.showMessageDialog(
                    null,
                    winMessage.toString()
                );

                view.closeGame();

                return;
            }

            Player nextPlayer;

            if (
                activePlayer.equals(
                    board.getPlayer1()
                )
            ) {

                nextPlayer =
                    board.getPlayer2();

            } else {

                nextPlayer =
                    board.getPlayer1();
            }

            board.setCurrentPlayer(
                nextPlayer
            );

            view.updateTurnDisplay(
                nextPlayer,
                localAssignedPlayer
            );
        }
    }
}