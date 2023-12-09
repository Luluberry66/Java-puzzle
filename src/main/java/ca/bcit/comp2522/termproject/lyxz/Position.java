package ca.bcit.comp2522.termproject.lyxz;

/**
 * The Position class.
 * @version 2023
 * @author Lulu Dong
 */
public class Position {
    private final int x;
    private final int y;

    /**
     * Constructor for Position.
     * @param x x coordinate
     * @param y y coordinate
     */
    public Position(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Get x coordinate.
     * @return x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Get y coordinate.
     * @return y coordinate
     */
    public int getY() {
        return y;
    }
}
