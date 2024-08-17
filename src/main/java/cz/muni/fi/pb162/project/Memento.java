package cz.muni.fi.pb162.project;

/**
 * stores the current state of a board
 * @param board 2D array of pieces representing the board
 * @param round int, number of current round
 * @author Jan Maly
 */
public record Memento(Piece[][] board, int round) {
    /**
     * Constructor
     * @param board 2D array of pieces representing the board
     * @param round int, number of current round
     */
    public Memento {
        Piece[][] savedBoard = new Piece[Board.SIZE][Board.SIZE];
        for (int i = 0; i < Board.SIZE; i++) {
            //don't know at all why there can not be system.arraycopy
            //but with clone() it works correctly while with system.arraycoppy not
            savedBoard[i] = board[i].clone();
        }
        board = savedBoard;
    }
}


