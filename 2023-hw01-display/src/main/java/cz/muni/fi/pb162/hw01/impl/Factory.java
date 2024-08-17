package cz.muni.fi.pb162.hw01.impl;

import cz.muni.fi.pb162.hw01.impl.displays.Display;
import cz.muni.fi.pb162.hw01.impl.displays.DisplayStringifier;

/**
 * A simple factory class use get an instance of implemented interfaces
 */
public final class Factory {

    /**
     * Creates instance of {@link Display}
     *
     * @param size size of the display
     * @return display instance
     */
    public Display display(int size) {
        return new SevenSegmDisplay(size);
    }

    /**
     * Creates instance of {@link DisplayStringifier}
     * Created instance must be compatible with display provided by {@link #display(int)}
     * @return printer instance
     */
    public DisplayStringifier stringifier() {
        return new SevenSegmStringifier();
    }
}
