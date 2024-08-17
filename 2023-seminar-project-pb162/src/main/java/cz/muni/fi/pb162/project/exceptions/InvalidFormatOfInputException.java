package cz.muni.fi.pb162.project.exceptions;

/**
 * checks whether the input is <char><int> <char><int>.
 * @author Jan Maly
 */
public class InvalidFormatOfInputException extends RuntimeException {
    /**
     * constructor
     */
    public InvalidFormatOfInputException() {
        super();
    }

    /**
     * constructor
     * @param message which is displayed when exception is caught
     */
    public InvalidFormatOfInputException(String message) {
        super(message);
    }
}
