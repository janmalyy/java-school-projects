package cz.muni.fi.pb162.project.moves;

import cz.muni.fi.pb162.project.Board;
import cz.muni.fi.pb162.project.Coordinates;
import cz.muni.fi.pb162.project.Game;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;

/**
 * A diagonal jump of a "man" piece in a draughts game.
 * Returned positions are those where we can jump over opponent's pieces in a diagonal direction.
 *
 * @author Alzbeta Strompova
 */
public class Jump implements Move {

    private boolean onlyForward = false;

    /**
     * Constructor with the {@code onlyForward} set to {@code false}
     */
    public Jump() {
    }

    /**
     * Constructor
     *
     * @param onlyForward If {@core true}, then the only forward movement is considered
     */
    public Jump(boolean onlyForward) {
        this.onlyForward = onlyForward;
    }


    @Override
    public Set<Coordinates> getAllowedMoves(Game game, Coordinates position) {
        var board = game.getBoard();
        var result = new HashSet<Coordinates>();
        var piece = board.getPiece(position.letterNumber(), position.number());
        var color = (piece == null) ? null : piece.getColor();
        var coordinates = Move.getDiagonalShift(onlyForward, color);

        for (Pair<Integer, Integer> movement : coordinates) {
            var leftToJump = position.letterNumber() + movement.getLeft();
            var rightToJump = position.number() + movement.getRight();
            var leftGoal = leftToJump + movement.getLeft();
            var rightGoal = rightToJump + movement.getRight();
            piece = board.getPiece(leftToJump, rightToJump);
            if (color.getOppositeColor().equals((piece == null) ? null : piece.getColor())
                    && board.getPiece(leftGoal, rightGoal) == null
                    && Board.inRange(new Coordinates(leftGoal, rightGoal))) {
                result.add(new Coordinates(leftGoal, rightGoal));
            }
        }
        return result;
    }
}
