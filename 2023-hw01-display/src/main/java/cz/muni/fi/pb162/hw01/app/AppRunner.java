package cz.muni.fi.pb162.hw01.app;

import cz.muni.fi.pb162.hw01.cmd.CommandLine;
import cz.muni.fi.pb162.hw01.impl.app.DisplayApp;
import cz.muni.fi.pb162.hw01.impl.app.DisplayAppOptions;

/**
 * Application entry point
 */
public final class AppRunner {

    /**
     * Application entry point
     *
     * @param args command line arguments of the application
     */
    public static void main(String[] args) {
        DisplayAppOptions options = new DisplayAppOptions();
        CommandLine cli = new CommandLine("display", options);
        DisplayApp app = new DisplayApp();
        int status = cli.parseArguments(args);

        exitOnError(status);

        if (options.isShowUsage()) {
            cli.showUsage();
        } else {
            status = app.run(options);
        }

        exitOnError(status);
    }

    /**
     * calls {@link System#exit(int)} on non zero exit status
     * @param status exit status
     */
    private static void exitOnError(int status) {
        if (status != 0) {
            System.exit(status);
        }
    }
}
