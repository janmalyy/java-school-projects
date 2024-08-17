package cz.muni.fi.pb162.project;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Alzbeta Strompova
 */
class ColorTest {

    @Test
    void getOppositeColor() {
        assertEquals(Color.WHITE, Color.BLACK.getOppositeColor());
        assertEquals(Color.BLACK, Color.WHITE.getOppositeColor());
    }

}