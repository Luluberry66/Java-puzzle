package ca.bcit.comp2522.termproject.lyxz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class is the game logic of the game.
 * @version 2023
 * @author Lulu Dong
 */
public class GameLogic implements IGameLogic {
    /**
     * The size of the game board.
     */
    public static final int SIZE = 4;
    /**
     * The size of each piece.
     */
    public static final int PIECE_SIZE = 105;
    /**
     * The data of the game board.
     */
    private final int[][] data = new int[SIZE][SIZE];

    /**
     * This method initializes the data of the game board.
     */
    @Override
    public void initializeData() {
        int count = 1;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                data[i][j] = count;
                count++;
            }
        }
        data[SIZE - 1][SIZE - 1] = 0;
    }

    /**
     * This method shuffles the data of the game board.
     */
    @Override
    public void shuffleData() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < SIZE * SIZE; i++) {
            list.add(i + 1);
        }
        list.set(SIZE * SIZE - 1, 0);

        Collections.shuffle(list);

        for (int i = 0; i < SIZE * SIZE; i++) {
            data[i / SIZE][i % SIZE] = list.get(i);
        }
    }

    /**
     * This method checks if the game is solved.
     * @return true if the game is solved, false otherwise.
     */
    @Override
    public boolean isSolved() {
        int count = 1;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (data[i][j] != count) {
                    if (!(i == SIZE - 1 && j == SIZE - 1 && data[i][j] == 0)) {
                        return false;
                    }
                }
                if (count < SIZE * SIZE) {
                    count++;
                }
            }
        }
        return true;
    }

    /**
     * This method moves the tiles.
     * @param direction the direction to move.
     */
    @Override
    public void moveTiles(final Direction direction) {
        Position emptyPosition = findEmptySpace();
        if (emptyPosition == null) {
            return;
        }
        Position targetPosition = getTargetPosition(emptyPosition, direction);
        if (isMoveValid(targetPosition)) {
            performMove(emptyPosition, targetPosition);
        } else {
            GameUtils.playSound("invalid.mp3");
            GameUI.feedbackInvalidMove();
        }
    }

    /**
     * This method checks if the move is valid.
     * @param targetPosition the target position.
     * @return true if the move is valid, false otherwise.
     */
    @Override
    public boolean isMoveValid(final Position targetPosition) {
        return targetPosition.getX() >= 0 && targetPosition.getX() < SIZE
                && targetPosition.getY() >= 0 && targetPosition.getY() < SIZE;
    }

    /**
     * This method finds the empty space.
     * @return the position of the empty space.
     */
    private Position findEmptySpace() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (data[i][j] == 0) {
                    return new Position(j, i);
                }
            }
        }
        return null;
    }

    /**
     * This method gets the target position.
     * @param emptyPosition the position of the empty space.
     * @param direction the direction to move.
     * @return the target position.
     */
    private Position getTargetPosition(final Position emptyPosition, final Direction direction) {
        return switch (direction) {
            case UP -> new Position(emptyPosition.getX() + 1, emptyPosition.getY());
            case DOWN -> new Position(emptyPosition.getX() - 1, emptyPosition.getY());
            case LEFT -> new Position(emptyPosition.getX(), emptyPosition.getY() + 1);
            case RIGHT -> new Position(emptyPosition.getX(), emptyPosition.getY() - 1);
        };
    }

    /**
     * This method performs the move.
     * @param emptyPosition the position of the empty space.
     * @param targetPosition the target position.
     */
    private void performMove(final Position emptyPosition, final Position targetPosition) {
        data[emptyPosition.getY()][emptyPosition.getX()] = data[targetPosition.getY()][targetPosition.getX()];
        data[targetPosition.getY()][targetPosition.getX()] = 0;
        GameUtils.playSound("move.mp3");
        GameUI.incrementMoveCount();
    }

    /**
     * This method gets the value of the tile.
     * @param x the row index.
     * @param y the column index.
     * @return the value of the tile.
     */
    @Override
    public int getTileValue(final int x, final int y) {
        return data[y][x];
    }
}
