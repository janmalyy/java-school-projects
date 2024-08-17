package cz.muni.fi.pb162.project.moves;

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
class StraightTest {

    private final Game game = new Chess(null, null);

    @Test
    void inheritance() {
        BasicRulesTester.testInheritance(Move.class, Straight.class);
    }

    @Test
    void attributesAndMethods() {
        BasicRulesTester.attributesAmount(Straight.class, 1);
        BasicRulesTester.methodsAmount(Straight.class, 1);
    }

    @Test
    void getAllowedMovesStepOne() {
        game.setInitialSet();
        var straight = new Straight(1);
        Assertions.assertThat(straight.getAllowedMoves(game, new Coordinates(1, 1)))
                .containsOnly(new Coordinates(1, 2));
        Assertions.assertThat(straight.getAllowedMoves(game, new Coordinates(5, 6)))
                .containsOnly(new Coordinates(5, 5));
        game.putPieceOnBoard(4, 4, new Piece(Color.WHITE, PieceType.ROOK));
        Assertions.assertThat(straight.getAllowedMoves(game, new Coordinates(4, 4)))
                .containsOnly(new Coordinates(4, 5), new Coordinates(4, 3),
                        new Coordinates(5, 4), new Coordinates(3, 4));
    }

    @Test
    void getAllowedMovesStepBoardSize() {
        game.putPieceOnBoard(3, 3, new Piece(Color.WHITE, PieceType.QUEEN));
        var straight = new Straight();
        Assertions.assertThat(straight.getAllowedMoves(game, new Coordinates(3, 3)))
                .containsOnly(new Coordinates(3, 0),
                        new Coordinates(3, 1),
                        new Coordinates(3, 2),
                        new Coordinates(3, 4),
                        new Coordinates(3, 5),
                        new Coordinates(3, 6),
                        new Coordinates(3, 7),
                        new Coordinates(0, 3),
                        new Coordinates(1, 3),
                        new Coordinates(2, 3),
                        new Coordinates(4, 3),
                        new Coordinates(5, 3),
                        new Coordinates(6, 3),
                        new Coordinates(7, 3));
        game.setInitialSet();
        Assertions.assertThat(straight.getAllowedMoves(game, new Coordinates(7, 1)))
                .containsOnly(new Coordinates(7, 2),
                        new Coordinates(7, 3),
                        new Coordinates(7, 4),
                        new Coordinates(7, 5),
                        new Coordinates(7, 6));
        Assertions.assertThat(straight.getAllowedMoves(game, new Coordinates(0, 0))).isEmpty();
    }
}