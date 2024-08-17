package cz.muni.fi.pb162.hw01.impl;

import cz.muni.fi.pb162.hw01.impl.displays.Display;

/**
 * cares about the input text, it cuts it when longer than display size
 * and it enables you to modify it, either set it or clear it
 * @author Jan Maly
 */
public class SevenSegmDisplay implements Display {

    private final StringBuilder textToBeStringified;

    /**
     * constructor
     * @param size size of the display
     * param textToBeStringified where the processed input text is stored
     *             this string will be given to Stringifier
     *
     */
    public SevenSegmDisplay(int size) {
        this.textToBeStringified = new StringBuilder((" ").repeat(size));
    }

    @Override
    public StringBuilder getTextToBeStringified() {
        return textToBeStringified;
    }

    @Override
    public void set(String text) {
        set(0, text);
    }

    @Override
    public void set(int pos, String text) {
        if (pos >= 0) {
            for (int i = 0; i < text.length(); i++) {
                if (i + pos < textToBeStringified.length()) {
                    textToBeStringified.setCharAt(i + pos, text.charAt(i));
                } else {
                    break;
                }
            }
            //sets spaces after the given input text to the size of the display
            for (int j = pos+text.length(); j < textToBeStringified.length(); j++) {
                textToBeStringified.setCharAt(j, ' ');
            }
        }
    }

    @Override
    public void clear() {
        for (int i = 0; i < textToBeStringified.length(); i++) {
            clear(i);
        }

    }

    @Override
    public void clear(int pos) {
        if (pos >= 0) {
            textToBeStringified.setCharAt(pos, ' ');
        }
    }
}
