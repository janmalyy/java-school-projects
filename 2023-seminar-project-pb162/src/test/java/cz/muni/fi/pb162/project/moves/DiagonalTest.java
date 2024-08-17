package cz.muni.fi.pb162.project.moves;

import cz.muni.fi.pb162.project.Board;
import cz.muni.fi.pb162.project.Chess;
import cz.muni.fi.pb162.project.Color;
import cz.muni.fi.pb162.project.Coordinates;
import cz.muni.fi.pb162.project.Game;
import cz.muni.fi.pb162.project.Piece;
import cz.muni.fi.pb162.project.PieceType;
import cz.muni.fi.pb162.project.helper.BasicRulesTester;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Alzbeta Strompova
 */
class DiagonalTest {

    private final Game game = new Chess(null, null);

    @Test
    void attributesAndMethods() {
        BasicRulesTester.attributesAmount(Diagonal.class, 2);
        BasicRulesTester.methodsAmount(Diagonal.class, 1);
    }

    @Test
    void inheritance() {
        BasicRulesTester.testInheritance(Move.class, Diagonal.class);
    }

    @Test
    void getAllowedMovesStepOne() {
        game.setInitialSet();
        var diagonal = new Diagonal(1);
        var diagonal2 = new Diagonal(1, true);
        Assertions.assertThat(diagonal.getAllowedMoves(game, new Coordinates(1, 1)))
                .containsOnly(new Coordinates(0, 2), new Coordinates(2, 2));
        Assertions.assertThat(diagonal.getAllowedMoves(game, new Coordinates(5, 6)))
                .containsOnly(new Coordinates(6, 5), new Coordinates(4, 5));
        game.putPieceOnBoard(5, 5, new Piece(Color.WHITE, PieceType.BISHOP));
        Assertions.assertThat(diagonal2.getAllowedMoves(game, new Coordinates(5, 5)))
                .containsOnly(new Coordinates(4, 6), new Coordinates(6, 6));
    }

    @Test
    void getAllowedMovesStepBoardSize() {
        game.putPieceOnBoard(3, 3, new Piece(Color.WHITE, PieceType.QUEEN));
        var diagonal = new Diagonal();
        var diagonal2 = new Diagonal(Board.SIZE, true);
        Assertions.assertThat(diagonal.getAllowedMoves(game, new Coordinates(3, 3)))
                .containsOnly(new Coordinates(0, 0),
                        new Coordinates(1, 1),
                        new Coordinates(2, 2),
                        new Coordinates(4, 4),
                        new Coordinates(5, 5),
                        new Coordinates(6, 6),
                        new Coordinates(7, 7),
                        new Coordinates(2, 4),
                        new Coordinates(4, 2),
                        new Coordinates(1, 5),
                        new Coordinates(5, 1),
                        new Coordinates(0, 6),
                        new Coordinates(6, 0));
        Assertions.assertThat(diagonal2.getAllowedMoves(game, new Coordinates(3, 3)))
                .containsOnly(new Coordinates(4, 4),
                        new Coordinates(5, 5),
                        new Coordinates(6, 6),
                        new Coordinates(7, 7),
                        new Coordinates(2, 4),
                        new Coordinates(1, 5),
                        new Coordinates(0, 6));
        game.putPieceOnBoard(3, 3, new Piece(Color.BLACK, PieceType.QUEEN));
        Assertions.assertThat(diagonal2.getAllowedMoves(game, new Coordinates(3, 3)))
                .containsOnly(new Coordinates(0, 0),
                        new Coordinates(1, 1),
                        new Coordinates(2, 2),
                        new Coordinates(4, 2),
                        new Coordinates(5, 1),
                        new Coordinates(6, 0));
        game.setInitialSet();
        Assertions.assertThat(diagonal.getAllowedMoves(game, new Coordinates(0, 0))).isEmpty();
    }

}