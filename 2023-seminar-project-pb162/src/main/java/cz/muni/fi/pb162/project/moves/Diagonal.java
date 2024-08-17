package cz.muni.fi.pb162.project.moves;

import cz.muni.fi.pb162.project.Board;
import cz.muni.fi.pb162.project.Color;
import cz.muni.fi.pb162.project.Coordinates;
import cz.muni.fi.pb162.project.Game;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;

/**
 * A generic movement in the diagonal direction.
 *
 * @author Alzbeta Strompova
 */
public class Diagonal implements Move {

    private int maxRange = Board.SIZE;
    private boolean onlyForward = false;

    /**
     * Constructor with the {@code maxRange} set to the size of the game board
     * and the {@code onlyForward} set to {@code false}
     */
    public Diagonal() {
    }

    /**
     * Constructor with the {@code onlyForward} set to {@code false}
     *
     * @param maxRange max distance (number of diagonal squares) to move on
     */
    public Diagonal(int maxRange) {
        this.maxRange = maxRange;
    }

    /**
     * Constructor.
     *
     * @param maxRange Max distance (number of diagonal squares) to move on
     * @param onlyForward If {@core true}, then the only forward movement is considered
     */
    public Diagonal(int maxRange, boolean onlyForward) {
        this(maxRange);
        this.onlyForward = onlyForward;
    }


    @Override
    public Set<Coordinates> getAllowedMoves(Game game, Coordinates position) {
        var board = game.getBoard();
        var result = new HashSet<Coordinates>();
        var piece = board.getPiece(position);
        var color = (piece == null) ? null : piece.getColor();
        Color goal;

        var coordinates = Move.getDiagonalShift(onlyForward, color);
        for (Pair<Integer, Integer> movement : coordinates) {
            for (int i = 1; i <= maxRange; i++) {
                var left = position.letterNumber() + i * movement.getLeft();
                var right = position.number() + i * movement.getRight();
                piece = board.getPiece(left, right);
                goal = (piece == null) ? null : piece.getColor();
                var coordinate = new Coordinates(left, right);
                if (!Board.inRange(coordinate)) {
                    break;
                }
                if (goal == null) {
                    result.add(coordinate);
                    continue;
                }
                if (color.getOppositeColor().equals(goal)) {
                    result.add(coordinate);
                }
                break;
            }
        }
        return result;
    }

}
