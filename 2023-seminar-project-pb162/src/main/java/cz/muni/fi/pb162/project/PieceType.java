package cz.muni.fi.pb162.project;

import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jan Maly
 */
public enum PieceType {
    KING, QUEEN, ROOK, BISHOP, KNIGHT, PAWN,
    DRAUGHTS_KING, DRAUGHTS_MAN;

    private static final Map<Pair<PieceType, Color>, String> FANCY_ICONS = new HashMap<>() {{
        // píšu jen takhle put bez ničeho, protože je jasný,
        // že to patří přímo k tomu fancyIcons, vlastně to dělám
        // uprostřed vyrábění toho fancyIcons
        put(Pair.of(PieceType.KING, Color.WHITE), "\u2654");
        put(Pair.of(PieceType.QUEEN, Color.WHITE), "\u2655");
        put(Pair.of(PieceType.BISHOP, Color.WHITE), "\u2657");
        put(Pair.of(PieceType.ROOK, Color.WHITE), "\u2656");
        put(Pair.of(PieceType.KNIGHT, Color.WHITE), "\u2658");
        put(Pair.of(PieceType.PAWN, Color.WHITE), "\u2659");
        put(Pair.of(PieceType.KING, Color.BLACK), "\u265A");
        put(Pair.of(PieceType.QUEEN, Color.BLACK), "\u265B");
        put(Pair.of(PieceType.BISHOP, Color.BLACK), "\u265D");
        put(Pair.of(PieceType.ROOK, Color.BLACK), "\u265C");
        put(Pair.of(PieceType.KNIGHT, Color.BLACK), "\u265E");
        put(Pair.of(PieceType.PAWN, Color.BLACK), "\u265F");
        put(Pair.of(PieceType.DRAUGHTS_MAN, Color.WHITE), "\u26C0");
        put(Pair.of(PieceType.DRAUGHTS_KING, Color.WHITE), "\u26C1");
        put(Pair.of(PieceType.DRAUGHTS_MAN, Color.BLACK), "\u26C2");
        put(Pair.of(PieceType.DRAUGHTS_KING, Color.BLACK), "\u26C3");
        }};

    /**
     *
     * @param color of the piece
     *        which representation will be changed into one of the symbols/icons
     * @return fancy unicode representation of the piece from the fancyIcons map
     */
    String getSymbol(Color color) {
        if (FANCY_ICONS.containsKey(Pair.of(this, color))) {
            return FANCY_ICONS.get(Pair.of(this, color));
        }
        return "";
    }
}

