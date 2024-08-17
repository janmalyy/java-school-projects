package cz.muni.fi.pb162.hw01.impl.app;

import cz.muni.fi.pb162.hw01.cmd.Application;
import cz.muni.fi.pb162.hw01.impl.Factory;
import cz.muni.fi.pb162.hw01.impl.displays.Display;
import cz.muni.fi.pb162.hw01.impl.displays.DisplayStringifier;


/**
 * Display application
 */
public class DisplayApp implements Application<DisplayAppOptions> {

    /**
     * Runtime logic of the application
     * initializes the classes and runs the methods
     * @param options input size of the display and input text to be stringified
     * @return exit status code; 0 with normal app run, -1 if the input is somehow incorrect
     */
    public int run(DisplayAppOptions options) {
        if (options.getText() != null || options.getSize() > 0) {
        Factory factory = new Factory();
        Display display = factory.display(options.getSize());
        DisplayStringifier stringifier = factory.stringifier();

        display.set(options.getText());
        System.out.println(stringifier.asString(display));
        return 0;
        }
        return -1;
    }
}
