/**
 * Represents a lightweight data structure for transferring move actions over a network or state handler.
 */
public class MovePayload {
    private int fromRow;
    private int fromCol;
    private int toRow;
    private int toCol;
    private String direction;
    private String playerName;

    public MovePayload(int fromRow, int fromCol, int toRow, int toCol, String direction, String playerName) {
        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = toRow;
        this.toCol = toCol;
        this.direction = direction;
        this.playerName = playerName;
    }

    public int getFromRow() { return fromRow; }
    public int getFromCol() { return fromCol; }
    public int getToRow() { return toRow; }
    public int getToCol() { return toCol; }
    public String getDirection() { return direction; }
    public String getPlayerName() { return playerName; }

    /**
     * Converts move details to a simple payload string for networking.
     */
    public String serialize() {
        return fromRow + "," + fromCol + "," + toRow + "," + toCol + "," + direction + "," + playerName;
    }

    /**
     * Deserializes a string payload back into a MovePayload object.
     */
    public static MovePayload deserialize(String data) {
        String[] parts = data.split(",");
        return new MovePayload(
            Integer.parseInt(parts[0]),
            Integer.parseInt(parts[1]),
            Integer.parseInt(parts[2]),
            Integer.parseInt(parts[3]),
            parts[4],
            parts[5]
        );
    }
}