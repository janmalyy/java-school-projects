package cz.muni.fi.pb162.project;

import java.util.Arrays;
import java.util.Objects;

/**
 * class for representing the board, the board consists of a two-dimensional array of pieces
 * @author Jan Maly
 * board is a two-dimensional array of pieces
 * round counts the number of played rounds of the game
 */
public class Board implements Originator<Memento>{
    public static final int SIZE = 8;
    private int round = 0;
    private Piece[][] pieces = new Piece[SIZE][SIZE];

    public int getRound() {
        return round;
    }
    public void setRound(int round) {
        this.round = round;
    }

    public Piece[][] getPieces() {
        return pieces;
    }

    /**
     * checks whether the given coordinates are not out of the bounds of the board
     * @param row row part of coordinates
     * @param column column part of coordinates
     * @return true when both row and column are in the bounds of the board, otherwise false
     */
    public static boolean inRange(int row, int column) {
        return row < SIZE && column < SIZE && row >=0 && column >= 0;
    }

    /**
     * works in the same way as the first inRange method,
     * the only difference is that as an input parameter the Coordinates object is given
     * @param coordinates Coordinates record object with two parameters letterNumber and number
     *                    representing the two parts of the coordinates
     * @return true when both letterNumber and number are in the bounds of the board, otherwise false
     */
    public static boolean inRange(Coordinates coordinates) {
        return coordinates.letterNumber() < SIZE && coordinates.number() < SIZE
                && coordinates.letterNumber() >= 0 && coordinates.number() >= 0;
    }

    /**
     * checks whether the given position on the board is empty (meaning there is no piece) or not
     * @param row row part of coordinates
     * @param column column part of coordinates
     * @return true if is empty or out of the bounds of the board
     *     (to check this it is using inRange method); otherwise false
     */
    public boolean isEmpty (int row, int column) {
        return !inRange(row, column) || pieces[row][column] == null;
    }

    /**
     * takes two integers as input parameters (a row and a column coordinates)
     * and returns the piece at the position of the coordinates.
     * Checks whether all conditions for correct return are satisfied by inRange and isEmpty methods
     * @param row row part of coordinates
     * @param column column part of coordinates
     * @return the piece at the position of the coordinates or null when some condition fails
     */
    public Piece getPiece (int row, int column) {
        if (inRange(row, column) && !isEmpty(row, column)) {
            return pieces[row][column];
        }
        return null;
    }

    /**
     * overloaded method; get piece on the given coordinates
     * @param coordinates where the desired piece is
     * @return the piece on the given coordinates
     */
    public Piece getPiece (Coordinates coordinates) {
        return getPiece(coordinates.letterNumber(), coordinates.number());
    }

    /**
     * puts the piece on the board at the position of the input integers (replaces the previous one)
     * returns void as it only assigns a piece on the board
     * @param row row part of coordinates
     * @param column column part of coordinates
     * @param piece a Piece object which is wanted to be put on the board
     */
    public void putPieceOnBoard (int row, int column, Piece piece) {
        if (inRange(row, column)) {
            pieces[row][column] = piece;
        }
    }

    /**
     * the name of the method says everything
     * @param inputId Id which you want to find on the board
     * @return a Piece object with id == inputId if such inputId found on the board, otherwise null
     */
    public Coordinates findCoordinatesOfPieceById(long inputId) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (pieces[i][j] != null && inputId == pieces[i][j].getId()) {
                    return new Coordinates(i, j);
                }
            }
        }
        return null;
    }

    /**
     * first, it computes the number of pieces on the board,
     * then it creates an array of such length and
     * then adds the pieces to the array
     * it is not the nicest code but it works
     * @return an array of all pieces on the board
     */
    public Piece[] getAllPiecesFromBoard() {
        int counter = 0;
        for ( int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (getPiece(i, j) != null) {
                    counter += 1;
                }
            }
        }
        Piece[] allPieces = new Piece[counter];
       int counter2 = 0;
        for ( int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (getPiece(i, j) != null) {
                    allPieces[counter2] = getPiece(i, j);
                    counter2 += 1;
                }
            }
        }
        return allPieces;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String dashSeparator = "  --------------------------------";
        String[] letters = new String[SIZE];
        for (int i = 0; i < SIZE; i++) {
            letters[i] = Character.toString((char)('A' + i));
        }
        sb.append("    1   2   3   4   5   6   7   8 ");
        sb.append(System.lineSeparator());
        sb.append(dashSeparator);
        sb.append(System.lineSeparator());
        for (int i = 0; i < SIZE; i++) {
            sb.append(letters[i]);
            for (int j = 0; j < SIZE; j++) {
                sb.append(" | ");
                if (getPiece(i, j) != null) {
                    sb.append(getPiece(i, j).toString());
                }   else {
                    sb.append(" ");
                }
            }
            sb.append(" |");
            sb.append(System.lineSeparator());
            sb.append(dashSeparator);
            if (i != SIZE-1) {
                sb.append(System.lineSeparator());
            }
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Board boardOther = (Board) o;
        return round == boardOther.round && Arrays.deepEquals(pieces, boardOther.pieces);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(round);
        result = 31 * result + Arrays.deepHashCode(pieces);
        return result;
    }

    @Override
    public Memento save() {
        return new Memento(pieces, round);
    }

    @Override
    public void restore(Memento save) {
        pieces = save.board();
        round = save.round();
    }
}
