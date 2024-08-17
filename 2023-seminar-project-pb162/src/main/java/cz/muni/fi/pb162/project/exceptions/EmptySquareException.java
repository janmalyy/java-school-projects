package cz.muni.fi.pb162.project.exceptions;

/**
 * checks whether the user wants to move a piece from the empty position
 * or the position which is not on the board.
 * @author Jan Maly
 */
public class EmptySquareException extends Exception{
    /**
     * constructor
     */
    public EmptySquareException() {
        super();
    }

    /**
     * constructor
     * @param message which is displayed when exception is caught
     */
    public EmptySquareException(String message) {
        super(message);
    }
}
