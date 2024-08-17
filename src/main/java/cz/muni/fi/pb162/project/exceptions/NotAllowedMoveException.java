package cz.muni.fi.pb162.project.exceptions;

/**
 * checks whether the user wants to make an illegal move
 * (checked by AllPossibleMovesByCurrentPlayer method in Game class).
 * @author Jan Maly
 */
public class NotAllowedMoveException extends Exception {
    /**
     * constructor
     */
    public NotAllowedMoveException() {
        super();
    }

    /**
     * constructor
     * @param message which is displayed when exception is caught
     */
    public NotAllowedMoveException(String message) {
        super(message);
    }
}
