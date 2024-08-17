package cz.muni.fi.pb162.project;
import cz.muni.fi.pb162.project.moves.Diagonal;
import cz.muni.fi.pb162.project.moves.Jump;
import cz.muni.fi.pb162.project.moves.Knight;
import cz.muni.fi.pb162.project.moves.Move;
import cz.muni.fi.pb162.project.moves.Pawn;
import cz.muni.fi.pb162.project.moves.Straight;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * representing a piece on a chessboard
 * @author Jan Maly
 */
public class Piece implements Prototype<Piece> {
    private static final AtomicLong ATOMIC_LONG_ID = new AtomicLong();
    private final long id;
    private final Color color;
    private final PieceType pieceType;
    private final List<Move> moves;

    /** constructor:
     *     creating a unique id for every new Piece object
     *     and allowing the user to set the color and PieceType of the Piece
     *     but only once, later these parameters are not changeable
     *     switch instantiates and stores strategies based on the required piece type.
     * @param color the color of the piece; white/black
     * @param pieceType the PieceType of the piece (e.g. rook)
     */
    public Piece(Color color, PieceType pieceType) {
        id = ATOMIC_LONG_ID.incrementAndGet();
        this.color = color;
        this.pieceType = pieceType;
            switch (pieceType) {
                case KING -> moves = List.of(new Straight(1), new Diagonal(1));
                case QUEEN -> moves = List.of(new Straight(), new Diagonal());
                case BISHOP -> moves = List.of(new Diagonal());
                case ROOK -> moves = List.of(new Straight());
                case KNIGHT -> moves = List.of(new Knight());
                case PAWN -> moves = List.of(new Pawn());
                case DRAUGHTS_KING -> moves = List.of(new Diagonal(1), new Jump());
                case DRAUGHTS_MAN -> moves = List.of(new Diagonal(1, true), new Jump(true));
                default -> throw new IllegalArgumentException("Unknown type in chess.");
            }
        }

    public long getId() {
        return id;
    }
    public PieceType getPieceType() {
        return pieceType;
    }
    public Color getColor() {
        return color;
    }

    public List<Move> getMoves() {
        // z listu udělám list, který se už ale nedá dál měnit, bezpečnější
        return Collections.unmodifiableList(moves);
    }

    @Override
    public Piece makeClone() {
        return new Piece(color, pieceType);
    }

    @Override
    public String toString() {
        return pieceType.getSymbol(color);
        // old version
        // return String.valueOf(String.valueOf(getPieceType()).charAt(0));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Piece piece = (Piece) o;
        return id == piece.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * says where (on which positions) you can move with the piece in the game
     * @param game the current game the piece is a part of
     * @return valid moves (target positions) in the game for the piece
     */
    Set<Coordinates> getAllPossibleMoves(Game game) {
        Set<Coordinates> allPossibleMoves = new HashSet<>();
        Coordinates currentCoordinates = game.getBoard().findCoordinatesOfPieceById(this.getId());
        for (Move move: this.getMoves()) {
            allPossibleMoves.addAll(move.getAllowedMoves(game, currentCoordinates));
        }
        return Collections.unmodifiableSet(allPossibleMoves);
    }
}
