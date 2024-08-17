package cz.muni.fi.pb162.project.exceptions;

/**
 * checks whether there are (at least, more does not matter) two players added to the game
 * when using Builder class to create the game
 * @author Jan Maly
 */
public class MissingPlayerException extends RuntimeException {

    /**
     * constructor
     */
    public MissingPlayerException() {
        super();
    }

    /**
     * constructor
     * @param message which is displayed when exception is caught
     */
    public MissingPlayerException(String message) {
        super(message);
    }
}
