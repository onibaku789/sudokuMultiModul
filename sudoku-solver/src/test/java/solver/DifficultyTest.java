package solver;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class DifficultyTest {

    @Test
    void testGetValues() {
        assertEquals( Difficulty.TEST.getValue(),1 );
        assertEquals( Difficulty.EASY.getValue(),30 );
        assertEquals( Difficulty.MEDIUM.getValue(),50 );
        assertEquals( Difficulty.HARD.getValue(),75 );



    }
}
