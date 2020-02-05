/*
 * Scotland Yard Game
 * Programmierpraktikum
 * Created by Alexander Löffler
 *
 */
package logic;

/**
 * Class for Saving Positions
 *
 * @author Alexander Löffler
 */
class Position {

    private final double x;
    private final double y;

    /**
     * Class for Saving Positions
     *
     * @param x X coordinate
     * @param y Y coordinate
     */
    Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Getter for X
     *
     * @return X
     */
    double getX() {
        return x;
    }

    /**
     * Getter for X
     *
     * @return Y
     */
    double getY() {
        return y;
    }

}
