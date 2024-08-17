package cz.muni.fi.pb162.project.utils;

import cz.muni.fi.pb162.project.Coordinates;
import cz.muni.fi.pb162.project.helper.BasicRulesTester;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Alzbeta Strompova
 */
class BoardNotationTest {

    @Test
    void attributesAndMethodsAmount() {
        BasicRulesTester.attributesAmount(BoardNotation.class, 0);
        BasicRulesTester.methodsAmount(BoardNotation.class, 2);
    }

    @Test
    void getNotationOfCoordinates() {
        assertEquals("b2", BoardNotation.getNotationOfCoordinates(1, 1));
        assertEquals("h1", BoardNotation.getNotationOfCoordinates(7, 0));
        assertEquals("a7", BoardNotation.getNotationOfCoordinates(0, 6));
        assertEquals("c5", BoardNotation.getNotationOfCoordinates(2, 4));
        assertEquals("e4", BoardNotation.getNotationOfCoordinates(4, 3));
        assertEquals("f6", BoardNotation.getNotationOfCoordinates(5, 5));
        assertEquals("d3", BoardNotation.getNotationOfCoordinates(3, 2));
        assertEquals("g8", BoardNotation.getNotationOfCoordinates(6, 7));
    }

    @Test
    void getCoordinatesOfNotation() {
        assertEquals(new Coordinates(1, 0), BoardNotation.getCoordinatesOfNotation('b', 1));
        assertEquals(new Coordinates(7, 1), BoardNotation.getCoordinatesOfNotation('h', 2));
        assertEquals(new Coordinates(0, 2), BoardNotation.getCoordinatesOfNotation('a', 3));
        assertEquals(new Coordinates(2, 3), BoardNotation.getCoordinatesOfNotation('c', 4));
        assertEquals(new Coordinates(4, 4), BoardNotation.getCoordinatesOfNotation('e', 5));
        assertEquals(new Coordinates(5, 5), BoardNotation.getCoordinatesOfNotation('f', 6));
        assertEquals(new Coordinates(3, 6), BoardNotation.getCoordinatesOfNotation('d', 7));
        assertEquals(new Coordinates(6, 7), BoardNotation.getCoordinatesOfNotation('g', 8));
    }

}