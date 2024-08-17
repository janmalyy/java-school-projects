package cz.muni.fi.pb162.project.moves;

import cz.muni.fi.pb162.project.Color;
import cz.muni.fi.pb162.project.Coordinates;
import cz.muni.fi.pb162.project.Draughts;
import cz.muni.fi.pb162.project.Game;
import cz.muni.fi.pb162.project.Piece;
import cz.muni.fi.pb162.project.PieceType;
import cz.muni.fi.pb162.project.helper.BasicRulesTester;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Alzbeta Strompova
 */
class JumpTest {

    private final Game game = new Draughts(null, null);

    @Test
    void attributesAndMethods() {
        BasicRulesTester.attributesAmount(Jump.class, 2);
        BasicRulesTester.methodsAmount(Jump.class, 1);
    }

    @Test
    void inheritance() {
        BasicRulesTester.testInheritance(Move.class, Jump.class);
    }

    @Test
    void getAllowedMoves() {
        var jump = new Jump();
        var jump2 = new Jump(true);
        game.putPieceOnBoard(1, 1, new Piece(Color.WHITE, PieceType.DRAUGHTS_MAN));
        game.putPieceOnBoard(0, 0, new Piece(Color.BLACK, PieceType.DRAUGHTS_MAN));
        Assertions.assertThat(jump.getAllowedMoves(game, new Coordinates(0, 0)))
                .containsOnly(new Coordinates(2, 2));
        Assertions.assertThat(jump2.getAllowedMoves(game, new Coordinates(0, 0))).isEmpty();
        game.setInitialSet();
        Assertions.assertThat(jump.getAllowedMoves(game, new Coordinates(1, 1))).isEmpty();
        Assertions.assertThat(jump2.getAllowedMoves(game, new Coordinates(2, 2))).isEmpty();
        game.putPieceOnBoard(5, 3, new Piece(Color.BLACK, PieceType.DRAUGHTS_MAN));
        Assertions.assertThat(jump.getAllowedMoves(game, new Coordinates(6, 2)))
                .containsOnly(new Coordinates(4, 4));
        Assertions.assertThat(jump2.getAllowedMoves(game, new Coordinates(6, 2)))
                .containsOnly(new Coordinates(4, 4));
        Assertions.assertThat(jump2.getAllowedMoves(game, new Coordinates(5, 3))).isEmpty();
    }

}