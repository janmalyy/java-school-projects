package cz.muni.fi.pb162.project;

import cz.muni.fi.pb162.project.exceptions.EmptySquareException;
import cz.muni.fi.pb162.project.exceptions.InvalidFormatOfInputException;
import cz.muni.fi.pb162.project.exceptions.NotAllowedMoveException;
import cz.muni.fi.pb162.project.utils.BoardNotation;

import java.util.Scanner;
import java.util.Set;

/**
 * @author Jan Maly
 */
public interface Playable extends Caretaker{
    /**
     * The aim of the method is to set the initial layout of the pieces on the board.
     * Note, that the board is watched from the position of the judge
     * e.g. white starts on the left side and goes right
     */
    void setInitialSet();

    /**
     * The aim of the move method is to take two Coordinates as input parameters
     *       and move the piece from the first position to the second position on the board.
     * This method also implements so-called promotion: If the moved piece is a pawn
     *       and it moves to the last square of the board,
     *       it the piece turns into a queen.
     * If there is no piece at the source position or positions are wrong, then the method does nothing.
     * @param first where the piece was
     * @param second where the piece is planned to move
     */
    void move(Coordinates first, Coordinates second);

    /**
     * The aim of the play method without parameters is to demonstrate the game.
     *      The method loops until the end of the game.
     * In each loop, it finds out which player is next, gets input from the player (from standard input),
     *      increases the round by one, and makes a move.
     * Also, the state of the game is updated.
     *
     * throws some exceptions, check their meaning in the javadoc.
     */
    void play() throws EmptySquareException, NotAllowedMoveException;

    /**
     * The input from the user consists of a coordinate from which (s)he wants to move the piece
     * and a coordinate to which (s)he wants to move the piece.
     * Copied code from the assignment
     * @return  Coordinates object, already converted to ints from alphanumeric input
     */

    default Coordinates getInputFromPlayer() {
        Set<Character> possibleCharValues = Set.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
        Set<Integer> possibleIntValues = Set.of(1, 2, 3, 4, 5, 6, 7, 8);
        String position = new Scanner(System.in).next().trim();
        int parsedInt;
        try {
            parsedInt = Integer.parseInt(String.valueOf(position.charAt(1)));
            // dalo by se catch (Exception e) - ta chytne všechny výjimky
        } catch (NumberFormatException exception) {
            throw new InvalidFormatOfInputException("The second provided symbol should be some of 1-8. " +
                    "You inserted" + position.charAt(1));
        }
        if (position.length() != 2
                || !possibleCharValues.contains(position.charAt(0))
                || !possibleIntValues.contains(parsedInt)) {
            throw new InvalidFormatOfInputException("The correct format of input should be [a-h][1-8], " +
                    "you inserted " + position.charAt(0) + parsedInt + ".");
        }
        char letterNumber = position.charAt(0);
        return BoardNotation.getCoordinatesOfNotation(letterNumber, parsedInt);

    }
}

