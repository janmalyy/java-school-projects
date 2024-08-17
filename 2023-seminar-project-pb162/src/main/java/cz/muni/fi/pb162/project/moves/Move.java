package cz.muni.fi.pb162.project.moves;


import cz.muni.fi.pb162.project.Color;
import cz.muni.fi.pb162.project.Coordinates;
import cz.muni.fi.pb162.project.Game;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Possible movements of a piece on a board.
 * <p>
 * Each piece type has a different moving direction and rules.
 * This functional interface serves as a "movement strategy" (see the Strategy pattern), where
 * different movement rules are defined as separate classes and then easily extendable.
 * Invocation of the strategy (calling its method) computes and returns possible target
 * places on the board to which the piece can move.
 *
 * @author Alzbeta Strompova
 */
public interface Move {

    /**
     * Returns a set of all possible moves of a piece on a board.
     *
     * @param game A game instance with a board and pieces
     * @param position Source position on the board with a piece to be moved
     * @return A set of possible target positions on the board.
     *         An empty set if the source position is empty, out of the board,
     *         or there is no valid movement possible.
     */
    Set<Coordinates> getAllowedMoves(Game game, Coordinates position);

    /**
     * A helper static method that returns possible diagonal shifts
     * Set of Pairs Integers. These Integers representing Diagonal shifts.
     *
     * @param onlyForward Determines whether it is allows to move back or not.
     * @param color       The color of a piece to be moved.
     * @return A set of pairs of -1 or 1. Each pair represent a possible shift of the piece
     *         (to be added to the piece's current position)
     */
    static Set<Pair<Integer, Integer>> getDiagonalShift(boolean onlyForward, Color color) {
        Set<Pair<Integer, Integer>> coordinates = new HashSet<>();
        // forward movement of a white piece or back movement of a black piece:
        if (!onlyForward || !color.equals(Color.BLACK)) {
            coordinates.add(Pair.of(1, 1));
            coordinates.add(Pair.of(-1, 1));
        }
        // forward movement of a black piece or forward movement of a white piece:
        if (!onlyForward || !color.equals(Color.WHITE)) {
            coordinates.add(Pair.of(-1, -1));
            coordinates.add(Pair.of(1, -1));
        }
        return coordinates;
    }
}
