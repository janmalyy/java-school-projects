package cz.muni.fi.pb162.hw01.impl.displays;

/**
 * Represents a virtual display
 */
public interface Display {

    /**
     * Sets given text to this display
     * Text is truncated to fit the display
     * Any unused display position following the text should be cleared
     *
     * @param text text to be displayed
     */
    void set(String text);

    /**
     * Sets given text to this display.
     * Text is truncated to fit the display.
     *
     * @param pos starting position on the display
     * @param text text to be displayed
     */
    void set(int pos, String text);

    /**
     * Clears text from display
     */
    void clear();


    /**
     * Clears display position.
     *
     * @param pos position to be cleared
     */
    void clear(int pos);

    /**
     *
     * @return the adjusted input string which is going to be stringified
     */
    StringBuilder getTextToBeStringified();
}
