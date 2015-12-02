package Logic;

/**
 *  * @author Vanji
 */

public class GomokuConfig {

    private String name;
    private int boardWidth;
    private int boardHeight;
    private int victoryLength;

    public GomokuConfig() {
        name = "Gomoku";
        boardWidth = 20;
        boardHeight = 20;
        victoryLength = 5;
    }

    public GomokuConfig(String name, int width, int height, int victoryLength) {
        this.name = name;
        this.boardWidth = width;
        this.boardHeight = height;
        this.victoryLength = victoryLength;
    }

    public int getVictoryLength() {
        return victoryLength;
    }

    public int getHeight() {
        return boardHeight;
    }

    public int getWidth() { return boardWidth; }

    public String getName() {
        return name;
    }

}
