package cz.muni.fi.pb162.hw01.cmd;

/**
 * Runnable application
 *
 * @param <T> type representing command line options of this application
 */
public interface Application<T> {

    /**
     * Runs this application
     *
     * @param options command line options object
     * @return exit status code
     */
    int run(T options);
}
