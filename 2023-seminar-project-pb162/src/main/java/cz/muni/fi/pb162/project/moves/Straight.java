package cz.muni.fi.pb162.project.moves;

import cz.muni.fi.pb162.project.Board;
import cz.muni.fi.pb162.project.Coordinates;
import cz.muni.fi.pb162.project.Game;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;

/**
 * A generic movement in the straight direction (forth, back, left, and right).
 *
 * @author Alzbeta Strompova
 */
public class Straight implements Move {

    private int maxRange = Board.SIZE;

    /**
     * Constructor with the {@code maxRange} set to the size of the game board
     */
    public Straight() {
    }

    /**
     * Constructor
     *
     * @param maxRange max distance (number of diagonal squares) to move on
     */
    public Straight(int maxRange) {
        this.maxRange = maxRange;
    }

    @Override
    public Set<Coordinates> getAllowedMoves(Game game, Coordinates position) {
        var board = game.getBoard();
        var result = new HashSet<Coordinates>();
        var color = board.getPiece(position).getColor();

        var coordinates = new HashSet<>(Arrays.asList(
                Pair.of(0, 1),
                Pair.of(0, -1),
                Pair.of(1, 0),
                Pair.of(-1, 0)));

        for (Pair<Integer, Integer> movement : coordinates) {
            for (int i = 1; i <= maxRange; i++) {
                var left = position.letterNumber() + i * movement.getLeft();
                var right = position.number() + i * movement.getRight();
                var coordinate = new Coordinates(left, right);
                if (!Board.inRange(coordinate)) {
                    break;
                }
                if (board.getPiece(left, right) == null) {
                    result.add(coordinate);
                } else {
                    if (color.getOppositeColor().equals(board.getPiece(left, right).getColor())) {
                        result.add(coordinate);
                    }
                    break;
                }
            }
        }
        return result;
    }

}
