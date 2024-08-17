package cz.muni.fi.pb162.project.utils;

import cz.muni.fi.pb162.project.Coordinates;

/**
 * MISSING JAVADOC cause I am not sure what BoardNotation is good for
 * utility class
 * @author Jan Maly
 */
public final class BoardNotation {
    private static final int ASCII_A_LETTER_VALUE = 97;
    private static final int ASCII_ZERO_VALUE = 48;
    /**
     * way to ensure there will be just one instance of BoardNotation class
     * by using private constructor
     */
    private BoardNotation() {}

    /**
     * transform the coordinates of the board to the chess notation
     * @param letterNumber first part of coordinates (a number 1-8 representing a letter a-h)
     * @param number second part of coordinates (a number 0-7 representing a number 1-8)
     * @return two chars long string made of a letter a-h and a number 1-8
     */
    public static String getNotationOfCoordinates(int letterNumber, int number) {
        char letter = (char) (letterNumber+97);
        char number1 = (char) (number+ASCII_ZERO_VALUE+1);
        return String.valueOf(letter)+number1;
    }

    /**
     * transform the chess notation to the coordinates of the board
     * the same process as in getNotationofCoordinates but vice versa
     * @param letter a char representing the first part of coordinates of the board, a-h
     * @param number a number representing the second part of coordinates of the board, 1-8
     * @return Coordinates object
     */
    public static Coordinates getCoordinatesOfNotation(char letter, int number) {
        return new Coordinates((int) letter-ASCII_A_LETTER_VALUE, number-1);
    }
}
