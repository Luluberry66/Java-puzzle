package ca.bcit.comp2522.termproject.lyxz;

/**
 * Interface for game logic.
 * @version 2023
 * @author Lulu Dong
 */
public interface IGameLogic {
    /**
     * Initialize the data.
     */
    void initializeData();

    /**
     * Shuffle the data.
     */
    void shuffleData();

    /**
     * Check if the game is solved.
     * @return true if the game is solved, false otherwise.
     */
    boolean isSolved();

    /**
     * Move the tiles.
     * @param direction the direction to move.
     */
    void moveTiles(Direction direction);

    /**
     * Find the empty space.
     * @param targetPosition the target position.
     * @return true if the move is valid, false otherwise.
     */
    boolean isMoveValid(Position targetPosition);

    /**
     * Find the empty space.
     * @param i the row index.
     * @param j the column index.
     * @return the empty space.
     */
    int getTileValue(int i, int j);

}
