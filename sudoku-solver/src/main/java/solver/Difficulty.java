package solver;

public enum Difficulty {
    TEST( 1 ),
    TEST2( 81 ),
    TEST3(3),
    EASY( 30 ),
    MEDIUM( 50 ),
    HARD( 75 );

    Difficulty(int numberToRemove) {
        this.numberToRemove = numberToRemove;
    }

    int numberToRemove;


    public int getValue() {
        return numberToRemove;
    }


}
