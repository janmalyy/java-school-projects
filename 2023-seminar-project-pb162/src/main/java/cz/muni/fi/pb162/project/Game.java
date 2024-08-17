package cz.muni.fi.pb162.project;

import cz.muni.fi.pb162.project.exceptions.EmptySquareException;
import cz.muni.fi.pb162.project.exceptions.InvalidFormatOfInputException;
import cz.muni.fi.pb162.project.exceptions.NotAllowedMoveException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * this class represents a game play of two particular players.
 * @author Jan Maly
 */
public abstract class Game implements Playable, GameWritable, GameReadable {

    private final Player playerOne;
    private final Player playerTwo;
    private Board board;
    private StateOfGame stateOfGame;
    private final Stack<Memento> mementoHistory = new Stack<>();

    /**
     * constructor, called at the beginning of the game
     * sets stateOfGame to PLAYING
     *
     * @param playerOne parameter representing the first player of the chess game
     * @param playerTwo parameter representing the second player of the chess game
     * @param board board
     */
    public Game(Player playerOne, Player playerTwo, Board board) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.board = board;
        this.stateOfGame = StateOfGame.PLAYING;
    }


    /**
     * constructor, called at the beginning of the game
     * sets stateOfGame to PLAYING
     *
     * @param playerOne parameter representing the first player of the chess game
     * @param playerTwo parameter representing the second player of the chess game
     */
    public Game(Player playerOne, Player playerTwo) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.board = new Board();
        this.stateOfGame = StateOfGame.PLAYING;
    }

    public Player getPlayerOne() {
        return playerOne;
    }
    public Player getPlayerTwo() {
        return playerTwo;
    }

    public Board getBoard() {
        return board;
    }
    public void setBoard(Board board) {
        this.board = board;
    }

    public Stack<Memento> getMementoHistory() {
        return mementoHistory;
    }

    public StateOfGame getStateOfGame() {
        return stateOfGame;
    }
    public void setStateOfGame(StateOfGame stateOfGame) {
        this.stateOfGame = stateOfGame;
    }

    /**
     * checks whether the status of the game changed
     * (e.g. some player has already won)
     * and then sets the state of game to the updated status
     */
    public abstract void updateStatus();

    @Override
    public void move(Coordinates first, Coordinates second) {
        board.putPieceOnBoard(second.letterNumber(), second.number(), board.getPiece(first));
        // deletion of the piece on the origin position
        board.putPieceOnBoard(first.letterNumber(), first.number(), null);
    }

    @Override
    public void play() throws EmptySquareException, NotAllowedMoveException, InvalidFormatOfInputException {
        while (stateOfGame == StateOfGame.PLAYING) {
            hitSave();
            System.out.printf("Next player is %s%n", getCurrentPlayer());
            //InvalidFormatOfInputException could be thrown here
            // in getInputFromPlayer method called from the interface Playable
            Coordinates first = getInputFromPlayer();

            if (board.getPiece(first) == null) {
                throw new EmptySquareException("You wanted to move a piece " +
                        "from the empty position or the position which is not on the board." +
                        "You inserted these" +
                        first + "coordinates");
            }

            Coordinates second = getInputFromPlayer();

            if (!allPossibleMovesByCurrentPlayer().contains(second)) {
                throw new NotAllowedMoveException("You wanted to make an illegal move." +
                        "You can not move to these" +
                         second + "coordinates.");
            }

            move(first, second);
            updateStatus();
            board.setRound(board.getRound() + 1);

        }
    }

    /**
     * first round is 0, white starts, so every even round the white player plays
     * (and every odd round belongs to the black player).
     * By if conditions the right player is chosen.
     *
     * @return the player whose turn it is
     */
    public Player getCurrentPlayer() {
        if (board.getRound() % 2 == 0) {
            if (playerOne.color() == Color.WHITE) {
                return playerOne;
            }
            return playerTwo;
        }
        if (playerOne.color() == Color.BLACK) {
            return playerOne;
        }
        return playerTwo;
    }

    /**
     * puts the piece on the board at the position of the input integers (replaces the previous one)
     * returns void as it only assigns a piece on the board
     *
     * @param letterNumber column part of coordinates
     * @param number       row part of coordinates
     * @param piece        a Piece object which is wanted to be put on the board
     */
    public void putPieceOnBoard(int letterNumber, int number, Piece piece) {
        board.putPieceOnBoard(letterNumber, number, piece);
    }

    /**
     * @return the set of all possible moves (target positions, class Coordinates)
     * of the current player SORTED IN REVERSE ORDER!
     * don't ask why :shrugging_person:
     */
    Set<Coordinates> allPossibleMovesByCurrentPlayer() {
        TreeSet<Coordinates> allMoves = new TreeSet<>();
        Player currentPlayer = getCurrentPlayer();
        Piece[] allPieces = board.getAllPiecesFromBoard();
        for (Piece piece : allPieces) {
            if (piece.getColor() == currentPlayer.color()) {
                allMoves.addAll(piece.getAllPossibleMoves(this));
            }
        }
        allMoves = allMoves.stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toCollection(TreeSet<Coordinates>::new));
        return allMoves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Game gameOther = (Game) o;
        return (Objects.equals(this.playerOne, gameOther.playerOne)
                && Objects.equals(this.playerTwo, gameOther.playerTwo)
                && Objects.equals(this.board, gameOther.board)
                && this.stateOfGame == gameOther.stateOfGame);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerOne, playerTwo, board, stateOfGame);
    }

    @Override
    public void hitSave() {
        mementoHistory.push(board.save());
    }

    @Override
    public void hitUndo() {
        if (!mementoHistory.empty()) {
            board.restore(mementoHistory.pop());
        }
    }

    @Override
    public void write(OutputStream os) throws IOException {
    }

    @Override
    public void write(File file) throws IOException {

    }

    @Override
    public GameReadable read(InputStream is) throws IOException {
        return null;
    }

    @Override
    public GameReadable read(InputStream is, boolean hasHeader) throws IOException {
        return null;
    }

    @Override
    public GameReadable read(File file) throws IOException {
        return null;
    }

    @Override
    public GameReadable read(File file, boolean hasHeader) throws IOException {
        return null;
    }
}

