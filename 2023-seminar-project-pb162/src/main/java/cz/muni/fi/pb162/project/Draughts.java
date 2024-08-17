package cz.muni.fi.pb162.project;

/**
 * @author Jan Maly
 */
public class Draughts extends Game{
    /**
     * constructor, called at the beginning of the game
     * sets stateOfGAme to PLAYING
     *
     * @param playerOne parameter representing the first player of the chess game
     * @param playerTwo parameter representing the second player of the chess game
     */
    public Draughts(Player playerOne, Player playerTwo) {
        super(playerOne, playerTwo);
    }

    /**
     * sets the game state to the victory of either a white or black player
     * if there is no piece of the opposite color on the board.
     */
    @Override
    public void updateStatus() {
        Piece[] allPieces = getBoard().getAllPiecesFromBoard();
        int whiteCount = 0;
        int blackCount = 0;
        for (Piece piece: allPieces) {
            if (piece.getColor() == Color.WHITE) {
                whiteCount += 1;
            } else {
                blackCount +=1;
            }
        }
        if (whiteCount == 0) {
            setStateOfGame(StateOfGame.BLACK_PLAYER_WIN);
        }
        if (blackCount == 0) {
            setStateOfGame(StateOfGame.WHITE_PLAYER_WIN);
        }
    }

    @Override
    public void setInitialSet() {
        //even rows
        for (int i = 0; i < Board.SIZE; i+=2) {
            getBoard().putPieceOnBoard(i, 0, new Piece(Color.WHITE, PieceType.DRAUGHTS_MAN));
            getBoard().putPieceOnBoard(i, 2, new Piece(Color.WHITE, PieceType.DRAUGHTS_MAN));
            getBoard().putPieceOnBoard(i, 6, new Piece(Color.BLACK, PieceType.DRAUGHTS_MAN));

        }
        //odd rows
        for (int i = 1; i < Board.SIZE; i+=2) {
            getBoard().putPieceOnBoard(i, 1, new Piece(Color.WHITE, PieceType.DRAUGHTS_MAN));
            getBoard().putPieceOnBoard(i, 5, new Piece(Color.BLACK, PieceType.DRAUGHTS_MAN));
            getBoard().putPieceOnBoard(i, 7, new Piece(Color.BLACK, PieceType.DRAUGHTS_MAN));

        }
    }

    @Override
    public void move(Coordinates first, Coordinates second) {
        super.move(first, second);
        //promotion
        //If the moved piece is a man and is to be placed on the "last square of the board"
        // (i.e., a new position number is 0 for a black piece or 7 for a white piece),
        // then the man is replaced with a king.
        if ((((getBoard().getPiece(second)).getPieceType()).equals(PieceType.DRAUGHTS_MAN))
                && ((second.number() == 0 && getBoard().getPiece(second).getColor().equals(Color.BLACK))
                || (second.number() == 7 && getBoard().getPiece(second).getColor().equals(Color.WHITE)))) {
            Piece king = new Piece(getBoard().getPiece(second).getColor(), PieceType.DRAUGHTS_KING);
            getBoard().putPieceOnBoard(second.letterNumber(), second.number(), king);
        }
    }
}
