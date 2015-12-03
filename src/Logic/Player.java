package Logic;

/**
 * Represents a player in a Gomoku game
 *
 * @author Vanji
 */
public class Player {

    public static final int NOPLAYER = 0;
    public static final int PLAYER1 = 1;
    public static final int PLAYER2 = 2;
    public static final int PLAYER3 = 3;
    public static final int PLAYER4 = 4;
    public static final int PLAYER5 = 5;

    /**
     * 0 = Spectator, or no player 1 = Player one 2 = Player two
     */
    private int playerID;

    /**
     * The player shape
     *
     */
    private int shape;

    /** The player name */
    private String name;


    public Player() {
    }

    /**
     * Create a new player
     *
     * @param name
     *            The player name
     * @param id
     */
    public Player(String name, int id) {
        this.name = name;
        setID(id);
        setShape(Board.NOPLAYER);
    }

    public Player(String name, int id, int shape) {
        this.name = name;
        setID(id);
        setShape(shape);
    }

    public int getID() {
        return playerID;
    }

    public void setID(int id) {
        if (id == 0 || id == 1 || id == 2 || id == 3 || id == 4 || id == 5) {
            playerID = id;
        } else {
            throw new IllegalArgumentException("PlayerID set to unknown value " + id);
        }
    }

    /**
     * Returns the player shape
     *
     * @return the player shape
     */
    public int getShape() {
        return shape;
    }

    /**
     * Sets the player shape
     *
     * @param shape
     *            the player shape
     * @throws IllegalArgumentException
     */
    public void setShape(int shape) throws IllegalArgumentException {
        if (shape != Board.PLAYER1
                && shape != Board.PLAYER2
                && shape != Board.PLAYER3
                && shape != Board.PLAYER4
                && shape != Board.PLAYER5
                && shape != Board.NOPLAYER) {
            throw new IllegalArgumentException("Invalid Shape");
        }
        this.shape = shape;
    }

    /**
     * Returns a string representing the shape of the player
     *
     * @return a string representing the shape of the player
     */
    public String getShapeName() {
        if (shape == Board.PLAYER1) {
            return "O";
        }
        else if (shape == Board.PLAYER2) {
            return "X";
        }
        else if (shape == Board.PLAYER3) {
            return "^";
        }
        else if (shape == Board.PLAYER4) {
            return "$";
        }
        else if (shape == Board.PLAYER5) {
            return "#";
        }
        return "None";
    }

    /**
     * Get the player name
     *
     * @return The player name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the player name
     *
     * @param name
     *            The new player name
     * @return The old name
     */
    public String setName(String name) {
        String old = this.name;
        this.name = name;
        return old;
    }

}
