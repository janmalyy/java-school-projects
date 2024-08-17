package cz.muni.fi.pb162.hw01.impl.utils;

import java.util.HashMap;
import java.util.List;

/**
 * utility class
 * it stores the string representation of possible inputs in HashMap stringNumbers
 * before using the class utils properly, call createStringNumbers()
 * @author Jan Maly
 */
public final class SevenSegmStringNumbers {

    // HashMap with possible inputs and their string representations
    private static final HashMap<String, List<String>> STRING_NUMBERS = new HashMap<>();

    // the height of the display == the height of the string representation of the symbol to be displayed
    private static final int SYMBOL_HEIGHT = 3;

    public static HashMap<String, List<String>> getStringNumbers() {
        return STRING_NUMBERS;
    }

    public static int getSymbolHeight() {
        return SYMBOL_HEIGHT;
    }

    /**
     * private constructor to insure
     *      there is not going to be an instance of the class
     */
    private SevenSegmStringNumbers() {}

    /**
     * fills in the HashMap stringNumbers with
     *      key: the possible inputs, value: their string representation
     */
    public static void createStringNumbers() {
        STRING_NUMBERS.put(" ", List.of("   ", "   ", "   "));
        STRING_NUMBERS.put("E", List.of(" _ ", "|_ ", "|_ "));
        STRING_NUMBERS.put("0", List.of(" _ ", "| |", "|_|"));
        STRING_NUMBERS.put("1", List.of("   ", "  |", "  |"));
        STRING_NUMBERS.put("2", List.of(" _ ", " _|", "|_ "));
        STRING_NUMBERS.put("3", List.of(" _ ", " _|", " _|"));
        STRING_NUMBERS.put("4", List.of("   ", "|_|", "  |"));
        STRING_NUMBERS.put("5", List.of(" _ ", "|_ ", " _|"));
        STRING_NUMBERS.put("6", List.of("   ", "|_ ", "|_|"));
        STRING_NUMBERS.put("7", List.of(" _ ", "  |", "  |"));
        STRING_NUMBERS.put("8", List.of(" _ ", "|_|", "|_|"));
        STRING_NUMBERS.put("9", List.of(" _ ", "|_|", "  |"));
    }
}
