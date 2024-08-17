package cz.muni.fi.pb162.hw01.cmd;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
/**
 * Command Line Interface
 */
public final class CommandLine {

    private final JCommander commander;

    /**
     * Constructs new CLI instance
     *
     * @param name name of the application
     * @param options options object to be populated with arguments
     */
    public CommandLine(String name, Object options) {
        commander = JCommander.newBuilder()
                .addObject(options)
                .build();

        commander.setProgramName(name);
    }

    /**
     * Parses command line arguments (terminates on error)
     *
     * @param args command line arguments of the application
     *
     * @return exit status code (0 on success, 1 on fail)
     */
    public int parseArguments(String[] args) {
        try {
            commander.parse(args);
            return 0;
        } catch (ParameterException ex) {
            showUsage();
            return 1;
        }
    }

    /**
     * Shows usage of the application and terminates
     */
    public void showUsage() {
        commander.usage();
    }
}