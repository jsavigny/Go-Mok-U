package Logic;

import static com.sun.corba.se.impl.naming.cosnaming.TransientNameServer.trace;
import static sun.security.jgss.GSSToken.debug;

import Logic.Board.PlacePieceBoardAction;

import java.util.ArrayList;

/**
 * Contains game logic for Gomoku game.
 *
 * @author Vanji
 */
public class GomokuGame {

    /** The board */
    private Board board;

    /** Whose turn it is */
    private Player turn;
    private Player playerOne;
    private Player playerTwo;
    private Player playerThree;
    private Player playerFour;


    private boolean gameOver;

    private GomokuConfig config;

    private ArrayList<GomokuGameListener> listeners;


    static public class PlacePieceGameAction {

        private int player;
        private PlacePieceBoardAction boardAction;
        private boolean done;
        private boolean confirmed;
        private boolean waitForConfirm;


        public PlacePieceGameAction(int player, int x, int y, boolean waitForConfirm) {
            this.player = player;
            this.waitForConfirm = waitForConfirm;
            boardAction = new PlacePieceBoardAction(player, x, y);
            done = false;
            confirmed = false;
        }

        public int getPlayerShape() {
            return player;
        }

        public PlacePieceBoardAction getBoardAction() {
            return boardAction;
        }

        public void doAction(GomokuGame game) throws IllegalActionException {
            if (game.gameOver)
                throw new IllegalActionException("Game over. Cannot place piece.");
            // Biar setelah gameOver tidak bisa diklik
            if (game.turn.getShape() == player && (player != Board.NOPLAYER)) {
                boardAction.doAction(game.getBoard());
                done = true;
                if (!waitForConfirm)
                    confirmAction(game);
            } else {
                throw new IllegalActionException("Not "
                        + game.getPlayerFromShape(player).getShapeName()
                        + "'s turn!");
            }

        }

        public void confirmAction(GomokuGame game) {
            if (confirmed)
                return;
            game.checkBoard(boardAction.getX(), boardAction.getY());
            game.switchTurn();
            confirmed = true;
        }

        public void undoAction(GomokuGame game) {
            if (done) {
                boardAction.undoAction(game.getBoard());
                if (confirmed)
                    game.switchTurn();
            }
        }

    }

    /**
     * Create a new game with set width and height
     */
    public GomokuGame(GomokuConfig config, boolean record) {
        this(new Board(config), config, record);
    }

    /**
     * Create a new game from a board
     *
     * @param board
     */
    public GomokuGame(Board board, GomokuConfig config, boolean record) {
        this.board = board;

        playerOne = new Player("", Player.PLAYER1);
        playerTwo = new Player("", Player.PLAYER2);
        playerThree = new Player("", Player.PLAYER3);
        playerFour = new Player("", Player.PLAYER4);
        turn = playerOne;
        gameOver = false;

        this.config = config;

        listeners = new ArrayList<GomokuGameListener>();
    }

    /**
     * Reset the game and set player turn to red
     */
    public void reset() {
        board.reset();
        turn = playerOne;
        gameOver = false;

        playerOne.setShape(Board.PLAYER1);
        playerTwo.setShape(Board.PLAYER2);
        playerThree.setShape(Board.PLAYER3);
        playerFour.setShape(Board.PLAYER4);
    }

    public GomokuConfig getConfig() {
        return config;
    }

    /**
     * Checks for victory and calls listeners if someone won
     * @param x
     * @param y
     */
    private void checkBoard(int x, int y) {
        /*
         * This method will check four lines for victory
         */
        Player player = getPieceOwner(x, y);
        int shape = player.getShape();
        boolean victory = false;

        // horizontal check
        int length = board.count(shape, x, y, 1, 0);
        if (length == config.getVictoryLength()) {
            victory = true;
        }

        // vertical check
        length = board.count(shape, x, y, 0, 1);
        if (length == config.getVictoryLength()) {
            victory = true;
        }

        // topleft diagonal check
        length = board.count(shape, x, y, 1, 1);
        if (length == config.getVictoryLength()) {
            victory = true;
        }

        // topright diagonal check
        length = board.count(shape, x, y, 1, -1);
        if (length == config.getVictoryLength()) {
            victory = true;
        }

        if (victory) {
            debug("Game detected winner + " + player.getName() + "("
                    + player.getShapeName() + ")" + ". Notifying listeners.");
            gameOver = true;
            for (GomokuGameListener listener : listeners) {
                listener.gameOver(player.getShape());
            }
        }
    }

    /**
     * Place a piece and switch player turn
     *
     * @param x
     *            the x location of the piece
     * @param y
     *            the y location of the piece
     * @param player
     *            the player placing the piece
     * @return true if piece was placed
     */
    public PlacePieceGameAction placePiece(int x, int y, Player player,
            boolean waitForConfirm) throws IllegalActionException {
        if (player == null)
            throw new IllegalArgumentException("Player cannot be null");
        PlacePieceGameAction action = new PlacePieceGameAction(
                player.getShape(), x, y, waitForConfirm);
        action.doAction(this);
        return action;
    }

    /**
     * Get the owner of the piece placed on provided position
     *
     * @param x
     *            The x location of the piece
     * @param y
     *            The y location of the piece
     * @return The player owning the piece on x, y. Null if empty.
     */
    public Player getPieceOwner(int x, int y) {
        int piece = board.getPiece(x, y);
        if (piece == playerOne.getShape())
            return playerOne;
        if (piece == playerTwo.getShape())
            return playerTwo;
        if (piece == playerThree.getShape())
            return playerThree;
        if (piece == playerFour.getShape())
            return playerFour;
        return null;
    }

    public void setShape(int playerID, int shape) {
        debug("Setting shape of " + playerID + " to " + shape);
        int otherShape = Board.PLAYER1;
        if (shape == Board.PLAYER1)
            otherShape = Board.PLAYER2;

        if (playerID == Player.PLAYER1) {
            playerOne.setShape(shape);
            playerTwo.setShape(otherShape);
        } else if (playerID == Player.PLAYER2) {
            playerOne.setShape(otherShape);
            playerTwo.setShape(shape);
        }

    }

    /**
     * Swap turns
     */
    public void switchTurn() {
        if (gameOver)
            return;

        if (turn == playerOne)
            setTurn(playerTwo);
        else if (turn == playerTwo)
            setTurn(playerThree);
        else if (turn == playerThree)
            setTurn(playerFour);
        else if (turn == playerFour)
            setTurn(playerOne);
    }

    /**
     * Set turn to provided player
     *
     * @param player
     *            The player who is going to get the turn
     */
    public void setTurn(Player player) {
        setTurn(player.getID());
    }

    /**
     * Set turn to player with provided shape
     *
     * @param playerID
     *            the provided player shape
     */
    public void setTurn(int playerID) {
        if (gameOver)
            return;
        if (playerID == playerOne.getID()) {
            turn = playerOne;
            trace("Turn set to player " + turn.getID() + "(" + turn.getName()
                    + ")");
        } else if (playerID == playerTwo.getID()) {
            turn = playerTwo;
            trace("Turn set to player " + turn.getID() + "(" + turn.getName()
                    + ")");
        } else if (playerID == playerThree.getID()) {
            turn = playerThree;
            trace("Turn set to player " + turn.getID() + "(" + turn.getName()
                    + ")");
        } else if (playerID == playerFour.getID()) {
            turn = playerFour;
            trace("Turn set to player " + turn.getID() + "(" + turn.getName()
                    + ")");
        }
    }

    /**
     * Get the player who has the turn
     *
     * @return The player who has the turn
     */
    public Player getTurn() {
        return turn;
    }
    public Player getPlayerOne() { return playerOne; }
    public Player getPlayerTwo() {
        return playerTwo;
    }
    public Player getPlayerThree() {
        return playerThree;
    }
    public Player getPlayerFour() { return playerFour; }


    /**
     * Returns a player depending on provided shape
     *
     * @param shape
     *            The player shape
     * @return a player depending on provided shape
     * @throws IllegalArgumentException
     *             Indicates a value other than the shape of player one or
     *             player two.
     */
    public Player getPlayerFromShape(int shape) {
        if (shape == playerOne.getShape())
            return playerOne;
        if (shape == playerTwo.getShape())
            return playerTwo;
        if (shape == playerThree.getShape())
            return playerThree;
        if (shape == playerFour.getShape())
            return playerFour;
        throw new IllegalArgumentException("No player with this shape : \""
                + shape + "\".");
    }

    public Player getPlayer(int id) {
        if (id == playerOne.getID()) {
            return playerOne;
        } else if (id == playerTwo.getID()) {
            return playerTwo;
        } else if (id == playerThree.getID()) {
            return playerThree;
        } else if (id == playerFour.getID()) {
            return playerFour;
        }
        return null;
    }

    /**
     * Replace the current board with the new board
     *
     * @param board
     *            The board to replace the current one
     */
    public void replaceBoard(Board board) {
        trace("Replacing board");
        this.board.replaceBoard(board);
    }

    /**
     * Get the current board
     *
     * @return The current board
     */
    public Board getBoard() {
        return board;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void addListener(GomokuGameListener listener) {
        listeners.add(listener);
    }

    public void removeListener(GomokuGameListener listener) {
        listeners.remove(listener);
    }

}
