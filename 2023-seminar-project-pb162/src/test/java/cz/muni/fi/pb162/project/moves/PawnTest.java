package cz.muni.fi.pb162.project.moves;

import cz.muni.fi.pb162.project.Chess;
import cz.muni.fi.pb162.project.Color;
import cz.muni.fi.pb162.project.Coordinates;
import cz.muni.fi.pb162.project.Piece;
import cz.muni.fi.pb162.project.PieceType;
import cz.muni.fi.pb162.project.helper.BasicRulesTester;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Alzbeta Strompova
 */
class PawnTest {

    @Test
    void attributesAndMethods() {
        BasicRulesTester.attributesAmount(Pawn.class, 0);
        BasicRulesTester.methodsAmount(Pawn.class, 1);
    }

    @Test
    void inheritance() {
        BasicRulesTester.testInheritance(Move.class, Pawn.class);
    }

    @Test
    void getAllowedMoves() {
        var game = new Chess(null, null);
        game.setInitialSet();
        var pawn = new Pawn();
        Assertions.assertThat(pawn.getAllowedMoves(game, new Coordinates(1, 1)))
                .containsOnly(new Coordinates(1, 2), new Coordinates(1, 3));
        Assertions.assertThat(pawn.getAllowedMoves(game, new Coordinates(6, 6)))
                .containsOnly(new Coordinates(6, 4), new Coordinates(6, 5));
        game.putPieceOnBoard(5, 5, new Piece(Color.BLACK, PieceType.PAWN));
        Assertions.assertThat(pawn.getAllowedMoves(game, new Coordinates(5, 5)))
                .containsOnly(new Coordinates(5, 4));
    }

}
