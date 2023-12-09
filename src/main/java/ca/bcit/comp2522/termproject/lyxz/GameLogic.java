package ca.bcit.comp2522.termproject.lyxz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameLogic implements IGameLogic{
    public static final int SIZE = 4;
    public static final int PIECE_SIZE = 105;
    private int[][] data = new int[SIZE][SIZE];

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

    @Override
    public void moveTiles(Direction direction) {
        Position emptyPosition = findEmptySpace();
        if (emptyPosition == null) return;

        Position targetPosition = getTargetPosition(emptyPosition, direction);
        if (isMoveValid(targetPosition)) {
            performMove(emptyPosition, targetPosition);
        } else {
            GameUtils.playSound("invalid.mp3");
            GameUI.feedbackInvalidMove();
        }
    }

    @Override
    public boolean isMoveValid(Position targetPosition) {
        return targetPosition.getX() >= 0 && targetPosition.getX() < SIZE &&
                targetPosition.getY() >= 0 && targetPosition.getY() < SIZE;
    }

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

    private Position getTargetPosition(Position emptyPosition, Direction direction) {
        return switch (direction) {
            case UP -> new Position(emptyPosition.getX() + 1, emptyPosition.getY());
            case DOWN -> new Position(emptyPosition.getX() - 1, emptyPosition.getY());
            case LEFT -> new Position(emptyPosition.getX(), emptyPosition.getY() + 1);
            case RIGHT -> new Position(emptyPosition.getX(), emptyPosition.getY() - 1);
            default -> emptyPosition;
        };
    }

    private void performMove(Position emptyPosition, Position targetPosition) {
        data[emptyPosition.getY()][emptyPosition.getX()] = data[targetPosition.getY()][targetPosition.getX()];
        data[targetPosition.getY()][targetPosition.getX()] = 0;
        GameUtils.playSound("move.mp3");
        GameUI.incrementMoveCount();
    }
    @Override
    public int getTileValue(int x, int y) {
        return data[y][x];
    }
}
