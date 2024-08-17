package cz.muni.fi.pb162.hw01.impl.app;

import com.beust.jcommander.Parameter;

/**
 * Application command line options.
 */
public final class DisplayAppOptions {
    @Parameter(names = "--help", help = true)
    private boolean showUsage = false;

    @Parameter(names = {"-s", "--size"}, description = "Size of the display (how many symbols)")
    private int size = 1;

    @Parameter(description = "Text to show on the display")
    private  String text = "";


    /**
     * Package private constructor solely for the purpose of testing
     *
     * @param size display size
     * @param text display text
     */
    DisplayAppOptions(int size, String text) {
        this.size = size;
        this.text = text;
    }

    /**
     * Mandatory no-arg constructor
     */
    public DisplayAppOptions() {
    }

    public boolean isShowUsage() {
        return showUsage;
    }

    public int getSize() {
        return size;
    }

    public String getText() {
        return text;
    }
}
