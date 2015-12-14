package Logic;

import java.util.ArrayList;

/**
 * A Board represents a Gomoku board. The board size restrictions is 40x40.
 *
 * @author Vanji
 *
 */
public class Board {

    static public abstract class ChangeListener {
        public abstract void callback(int shape, int x, int y);
    }

    /** The value representing no player */
    public static final int NOPLAYER = 0;
    public static final int PLAYER1 = 1;
    public static final int PLAYER2 = 2;
    public static final int PLAYER3 = 3;
    public static final int PLAYER4 = 4;
    public static final int PLAYER5 = 5;

    /** The structure containing the board data */
    protected int[] board;

    /** The Gomoku game configuration */
    protected GomokuConfig config;

    protected ArrayList<ChangeListener> listeners;

    /**
     * @author Vanji
     */
    static public interface BoardAction {
        public int getPlayer();
        public int getX();
        public int getY();

        /**
         *
         * @param board
         */
        public void doAction(Board board) throws IllegalActionException;

        /**
         *
         * @param board
         */
        public void undoAction(Board board);
    }

    /**
     * Action for placing a piece on the board
     *
     * @author Vanji
     */
    static public class PlacePieceBoardAction implements BoardAction {

        protected int player;
        protected int x;
        protected int y;
        protected boolean done;

        @SuppressWarnings("unused")
        private PlacePieceBoardAction() {
        }

        public PlacePieceBoardAction(int player, int x, int y) {
            this.player = player;
            this.x = x;
            this.y = y;
            done = false;
        }

        @Override
        public int getPlayer() {
            return player;
        }

        @Override
        public int getX() {
            return x;
        }

        @Override
        public int getY() {
            return y;
        }

        @Override
        public void doAction(Board board) throws IllegalActionException {
            if (board.getPiece(x, y) != Board.NOPLAYER)
                throw new IllegalActionException("That position is already occupied!");

            board.setPiece(x, y, player);
            board.fireChangeListeners(player, x, y);
            done = true;
            System.out.println("do Action Board");
        }

        @Override
        public void undoAction(Board board) {
            if (done) {
                board.setPiece(x, y, NOPLAYER);
                board.fireChangeListeners(NOPLAYER, x, y);
                done = false;
            }
        }
    }

    /** Empty constructor */
    @SuppressWarnings("unused")
    private Board() {
        this.config=new GomokuConfig();
        listeners = new ArrayList<ChangeListener>();
        reset();
    }

    public Board(GomokuConfig config) {
        this.config = config;
        listeners = new ArrayList<ChangeListener>();
        reset();
    }

    public void addChangeListener(ChangeListener listener) {
        listeners.add(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }

    protected void fireChangeListeners(int shape, int x, int y) {
        for (ChangeListener listener : listeners) {
            listener.callback(shape, x, y);
        }
    }

    /**
     * Reset the board
     */
    public void reset() {
        listeners.clear();
        board = new int[config.getWidth() * config.getHeight()];
    }

    /**
     * Replace the current board with a new board
     * @param board
     */
    public void replaceBoard(Board board) {
        this.board = board.board;
        this.config = board.config;
    }

    public void setBoardData(int[] data) {
        board = data;
    }

    public int[] getBoardData() {
        return board;
    }

    public GomokuConfig getConfig() {
        return config;
    }

    /**
     * Does an action on the board.
     * @param action
     *
     */
    protected void doAction(BoardAction action) throws IllegalActionException {
        try {
            action.doAction(this);
        } catch (IllegalActionException e) {
            throw e;
        }
    }

    /**
     * Place a new piece on the board
     */
    public PlacePieceBoardAction placePiece(Player player, int x, int y)
            throws IllegalActionException {
        return placePiece(player.getShape(), x, y);
    }

    /**
     * Returns true of all the pieces surrounding the line is empty
     * @return
     */
    protected boolean isOpen(int shape, int x, int y, int dirX, int dirY) {
        int xpos, ypos;
        xpos = x + dirX;
        ypos = y + dirY;
        do {
            // kondisi ketika di ujung board
            if (xpos == 0 || xpos == getWidth() - 1) {
                if (getPiece(xpos, ypos) != Board.NOPLAYER)
                    return false;
            }
            if (ypos == 0 || ypos == getHeight() - 1) {
                if (getPiece(xpos, ypos) != Board.NOPLAYER)
                    return false;
            }

            if (getPiece(xpos, ypos) == Board.NOPLAYER) {
                break;
            } else if (getPiece(xpos, ypos) != shape) { // enemy
                return false;
            }

            xpos += dirX;
            ypos += dirY;

        } while (xpos >= 0 && xpos < getWidth() && ypos >= 0
                && ypos < getHeight());

        // cek sisi lain
        xpos = x - dirX;
        ypos = y - dirY;
        do {
            // kondisi ketika di ujung board
            if (xpos == 0 || xpos == getWidth() - 1) {
                if (getPiece(xpos, ypos) != Board.NOPLAYER)
                    return false;
            }
            if (ypos == 0 || ypos == getHeight() - 1) {
                if (getPiece(xpos, ypos) != Board.NOPLAYER)
                    return false;
            }

            if (getPiece(xpos, ypos) == Board.NOPLAYER) {
                break;
            } else if (getPiece(xpos, ypos) != shape) { // enemy
                return false;
            }
            xpos -= dirX;
            ypos -= dirY;

        } while (xpos >= 0 && xpos < getWidth() && ypos >= 0
                && ypos < getHeight());
        return true;
    }

    /**
     * Returns the number of pieces of the provided shape
     *
     * @param shape
     * @param x
     * @param y
     * @param dirX
     * @param dirY
     * @return
     */
    protected int count(int shape, int x, int y, int dirX, int dirY) {
        System.out.println("Masuk Count "+x+"-"+y);
        int ct = 1;
        int xpos, ypos;
        xpos = x + dirX;
        ypos = y + dirY;
        while (xpos >= 0 && xpos < getWidth() && ypos >= 0
                && ypos < getHeight() && getPiece(xpos, ypos) == shape) {
            ct++;
            xpos += dirX;
            ypos += dirY;
        }

        // cek direction sebelah
        xpos = x - dirX;
        ypos = y - dirY;
        while (xpos >= 0 && xpos < getWidth() && ypos >= 0
                && ypos < getHeight() && getPiece(xpos, ypos) == shape) {
            ct++;
            xpos -= dirX;
            ypos -= dirY;
        }
        return ct;
    }

    /**
     * Place a new piece on the board.
     *
     * @param player
     * @param x
     * @param y
     */
    public PlacePieceBoardAction placePiece(int player, int x, int y)
            throws IllegalActionException {
        PlacePieceBoardAction pp = new PlacePieceBoardAction(player, x, y);
        pp.doAction(this);
        return pp;
    }

    /**
     * Returns the player shape for given position on the board. If either of
     * the arguments is invalid, i.e. "x" being larger than board width, the
     * function will return 0.
     *
     * @param x
     *            The x location
     * @param y
     *            The y location
     * @return The player shape for the given position. 0 = Empty, 1 = Player 1,
     *         2 = Player 2.
     */
    public int getPiece(int x, int y) {
        if (x < 0 || x >= config.getWidth() || y < 0 || y >= config.getHeight()) {
            return 0;
        }
        return board[x + config.getWidth() * y];
    }

    /**
     * Place a piece on the board
     *
     * @param x
     *            The x location for the piece
     * @param y
     *            The y location for the piece
     * @param player
     *            The player shape for the piece
     */
    private void setPiece(int x, int y, int player) {
        if (x < 0 || x > config.getWidth() || y < 0 || y > config.getHeight()) {
            throw new IllegalArgumentException("Position out of bounds. X: "
                    + x + ", Y: " + y);
        }
        /*if (player != Board.PLAYER1 && player != Board.PLAYER2
                && player != Board.NOPLAYER) {
            throw new IllegalArgumentException("Unknown value of player: \""
                    + player + "\".");
        }*/

        board[x + config.getWidth() * y] = player;
    }

    /**
     * Returns the current width of the board
     *
     * @return the current width of the board
     */
    public int getWidth() {
        return config.getWidth();
    }

    /**
     * Returns the current height of the board
     *
     * @return the current height of the board
     */
    public int getHeight() {
        return config.getHeight();
    }
}
