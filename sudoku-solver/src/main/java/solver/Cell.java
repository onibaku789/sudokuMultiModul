package solver;

/**
 * Represents a cell of a sudoku grid.
 */
 enum Cell {
    EMPTY,
    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    NINE;


    /**
     * Returns the instance represented by the value specified.
     * @param value the value represents an instance
     * @return the instance represented by the value specified
     * @throws IllegalArgumentException if the value specified does not represent an instance
     */
    public static Cell of(int value) {
        if (value < 0 || value >= values().length) {
            throw new IllegalArgumentException();
        }
        return values()[value];
    }

    public boolean isMoveable() {
        return isMoveable;
    }

    public void setMoveable(boolean moveable) {
        isMoveable = moveable;
    }

    public int getValue() {
        return ordinal();
    }


    private boolean isMoveable = false;

    public String toString() {
        return Integer.toString(ordinal());
    }
}
