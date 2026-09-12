public class TestBoard {

    public static void main(String[] args) {

        Player player1 = new Player("Player 1");
        Player player2 = new Player("Player 2");

        Board board = new Board(
            player1,
            player2,
            player1
        );

        board.initializePieces();

        System.out.println("BOARD INITIALIZED");
    }
}