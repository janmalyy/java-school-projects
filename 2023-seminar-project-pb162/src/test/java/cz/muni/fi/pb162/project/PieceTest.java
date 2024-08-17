package cz.muni.fi.pb162.project;

import cz.muni.fi.pb162.project.helper.BasicRulesTester;
import java.util.HashSet;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * @author Alzbeta Strompova
 */
class PieceTest {

    private final Piece piece = new Piece(Color.WHITE, PieceType.KING);
    private final Piece piece2 = new Piece(Color.WHITE, PieceType.QUEEN);
    private final Piece piece3 = new Piece(Color.BLACK, PieceType.ROOK);

    @Test
    void attributesAndMethodsAmount() {
        BasicRulesTester.attributesAmount(Piece.class, 4);
        //BasicRulesTester.methodsAmount(Piece.class, 9);
        BasicRulesTester.attributesFinal(Piece.class, 3);
    }

    @Test
    void inheritance() {
        BasicRulesTester.testInheritance(Prototype.class, Piece.class);
    }

    @Test
    void getId() {
        var pieceIds = new HashSet<Long>();
        for (int i = 0; i < 42; i++) {
            pieceIds.add(new Piece(Color.WHITE, PieceType.KING).getId());
        }
        assertEquals(42, pieceIds.size(), "Instances of piece should have different ids.");
    }

    @Test
    void getColor() {
        assertEquals(Color.WHITE, piece.getColor());
        assertEquals(Color.WHITE, piece2.getColor());
        assertEquals(Color.BLACK, piece3.getColor());
    }

    @Test
    void getAndSetTypeOfPiece() {
        assertEquals(PieceType.KING, piece.getPieceType());
        assertEquals(PieceType.QUEEN, piece2.getPieceType());
        assertEquals(PieceType.ROOK, piece3.getPieceType());
        //piece.setPieceType(PieceType.KNIGHT);
        //piece2.setPieceType(PieceType.BISHOP);
        //piece3.setPieceType(PieceType.PAWN);
        //assertEquals(PieceType.KNIGHT, piece.getPieceType());
        //assertEquals(PieceType.BISHOP, piece2.getPieceType());
        //assertEquals(PieceType.PAWN, piece3.getPieceType());
    }

    @Test
    void testToString() {
        assertEquals("\u2654", piece.toString());
        assertEquals("\u2655", piece2.toString());
        assertEquals("\u265C", piece3.toString());
        //piece.setPieceType(PieceType.KNIGHT);
        //piece2.setPieceType(PieceType.BISHOP);
        //piece3.setPieceType(PieceType.PAWN);
        //assertEquals("\u2658", piece.toString());
        //assertEquals("\u2657", piece2.toString());
        //assertEquals("\u265F", piece3.toString());
    }

    @Test
    void makeClone() {
        var pieceClone = piece.makeClone();
        var pieceClone2 = piece.makeClone();
        var piece2Clone = piece2.makeClone();
        var piece3Clone = piece3.makeClone();
        assertEquals(piece.getColor(), pieceClone.getColor());
        assertEquals(piece.getPieceType(), pieceClone.getPieceType());
        assertNotEquals(piece.getId(), pieceClone.getId());

        assertEquals(piece.getColor(), pieceClone2.getColor());
        assertEquals(piece.getPieceType(), pieceClone2.getPieceType());
        assertNotEquals(piece.getId(), pieceClone2.getId());

        assertEquals(piece2.getColor(), piece2Clone.getColor());
        assertEquals(piece2.getPieceType(), piece2Clone.getPieceType());
        assertNotEquals(piece.getId(), piece2Clone.getId());

        assertEquals(piece3.getColor(), piece3Clone.getColor());
        assertEquals(piece3.getPieceType(), piece3Clone.getPieceType());
        assertNotEquals(piece3.getId(), piece3Clone.getId());
    }

    @Test
    void testEquals() {
        assertThat(piece).isNotEqualTo(piece2);
        assertThat(piece).isNotEqualTo(piece3);
        assertThat(piece2).isNotEqualTo(piece3);
        assertThat(piece).isNotEqualTo(new Piece(Color.WHITE, PieceType.KING));
        assertThat(piece).isEqualTo(piece);
        assertThat(piece2).isEqualTo(piece2);
        assertThat(piece3).isEqualTo(piece3);
    }

    @Test
    void testHashCode() {
        assertNotEquals(piece.hashCode(), piece2.hashCode());
        assertNotEquals(piece.hashCode(), piece3.hashCode());
        assertNotEquals(piece2.hashCode(), piece3.hashCode());
        assertNotEquals(piece.hashCode(), new Piece(Color.WHITE, PieceType.KING).hashCode());
        assertEquals(piece.hashCode(), piece.hashCode());
        assertEquals(piece2.hashCode(), piece2.hashCode());
        assertEquals(piece3.hashCode(), piece3.hashCode());
    }

    @Test
    void getMoves() {
        int expectedSize = piece.getMoves().size();
        try {
            piece.getMoves().clear();
            Assertions.assertThat(piece.getMoves())
                    .as("Method returns modifiable collection - return new or unmodifiable.")
                    .hasSize(expectedSize);
        } catch (UnsupportedOperationException e) {
            // ok (unmodifiable)
        }
    }

    @Test
    void getAllPossibleMoves() {
        var game = new Chess(null, null);
        game.setInitialSet();
        var whiteKing = game.getBoard().getPiece(4, 0);
        Assertions.assertThat(whiteKing.getAllPossibleMoves(game)).isEmpty();
        var blackPawn = game.getBoard().getPiece(3, 6);
        Assertions.assertThat(blackPawn.getAllPossibleMoves(game))
                .containsOnly(new Coordinates(3, 5), new Coordinates(3, 4));
        var whiteQueen = new Piece(Color.WHITE, PieceType.QUEEN);
        game.putPieceOnBoard(7, 1, whiteQueen);
        Assertions.assertThat(whiteQueen.getAllPossibleMoves(game))
                .containsOnly(new Coordinates(7, 2),
                        new Coordinates(7, 3),
                        new Coordinates(7, 4),
                        new Coordinates(7, 5),
                        new Coordinates(7, 6),
                        new Coordinates(6, 2),
                        new Coordinates(5, 3),
                        new Coordinates(4, 4),
                        new Coordinates(3, 5),
                        new Coordinates(2, 6));
        game.move(new Coordinates(3, 1), new Coordinates(3, 3));
        Assertions.assertThat(whiteKing.getAllPossibleMoves(game)).containsOnly(new Coordinates(3, 1));
    }
}
