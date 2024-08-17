package cz.muni.fi.pb162.project;

import cz.muni.fi.pb162.project.exceptions.MissingPlayerException;
import cz.muni.fi.pb162.project.utils.BoardNotation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Jan Maly
 */
public class Chess extends Game implements GameWritable {

    /**
     * constructor, called at the beginning of the game
     * sets stateOfGame to PLAYING
     *
     * @param playerOne parameter representing the first player of the chess game
     * @param playerTwo parameter representing the second player of the chess game
     */
    public Chess(Player playerOne, Player playerTwo) {
        //calling constructor from the parent class
        super(playerOne, playerTwo);
    }

    /**
     * Private constructor of design pattern builder.
     *
     * @param playerOne first of two players needed to play chess.
     * @param playerTwo second of two players needed to play chess.
     * @param board     is 2-dimensional array to represent board of pieces.
     */
    private Chess(Player playerOne, Player playerTwo, Board board) {
        super(playerOne, playerTwo, board);
    }

    @Override
    public void move(Coordinates first, Coordinates second) {
        super.move(first, second);
        //promotion
        if ((((getBoard().getPiece(second)).getPieceType()).equals(PieceType.PAWN))
                && (second.number() == 0 || second.number() == 7)) {
            Piece queen = new Piece(getBoard().getPiece(second).getColor(), PieceType.QUEEN);
            getBoard().putPieceOnBoard(second.letterNumber(), second.number(), queen);
        }
    }

    @Override
    public void setInitialSet() {
        for (int i = 0; i <= Board.SIZE; i++) {
            getBoard().putPieceOnBoard(i, 6, new Piece(Color.BLACK, PieceType.PAWN));
            getBoard().putPieceOnBoard(i, 1, new Piece(Color.WHITE, PieceType.PAWN));
        }
        getBoard().putPieceOnBoard(0, 7, new Piece(Color.BLACK, PieceType.ROOK));
        getBoard().putPieceOnBoard(1, 7, new Piece(Color.BLACK, PieceType.KNIGHT));
        getBoard().putPieceOnBoard(2, 7, new Piece(Color.BLACK, PieceType.BISHOP));
        getBoard().putPieceOnBoard(3, 7, new Piece(Color.BLACK, PieceType.QUEEN));
        getBoard().putPieceOnBoard(4, 7, new Piece(Color.BLACK, PieceType.KING));
        getBoard().putPieceOnBoard(5, 7, new Piece(Color.BLACK, PieceType.BISHOP));
        getBoard().putPieceOnBoard(6, 7, new Piece(Color.BLACK, PieceType.KNIGHT));
        getBoard().putPieceOnBoard(7, 7, new Piece(Color.BLACK, PieceType.ROOK));

        getBoard().putPieceOnBoard(0, 0, new Piece(Color.WHITE, PieceType.ROOK));
        getBoard().putPieceOnBoard(1, 0, new Piece(Color.WHITE, PieceType.KNIGHT));
        getBoard().putPieceOnBoard(2, 0, new Piece(Color.WHITE, PieceType.BISHOP));
        getBoard().putPieceOnBoard(3, 0, new Piece(Color.WHITE, PieceType.QUEEN));
        getBoard().putPieceOnBoard(4, 0, new Piece(Color.WHITE, PieceType.KING));
        getBoard().putPieceOnBoard(5, 0, new Piece(Color.WHITE, PieceType.BISHOP));
        getBoard().putPieceOnBoard(6, 0, new Piece(Color.WHITE, PieceType.KNIGHT));
        getBoard().putPieceOnBoard(7, 0, new Piece(Color.WHITE, PieceType.ROOK));
    }

    @Override
    public void updateStatus() {
        Piece[] allPieces = getBoard().getAllPiecesFromBoard();
        ArrayList<Piece> kings = new ArrayList<>();
        for (Piece piece : allPieces) {
            if (piece.getPieceType() == PieceType.KING) {
                kings.add(piece);
            }
        }
        if (kings.size() != 2) {
            Color winningColor = kings.get(0).getColor();
            if (winningColor == Color.BLACK) {
                setStateOfGame(StateOfGame.BLACK_PLAYER_WIN);
            } else {
                setStateOfGame(StateOfGame.WHITE_PLAYER_WIN);
            }
        }
    }

    @Override
    /**
     * writes the state of the game (meaning where is which piece on the board) into given OutputStream
     */
    public void write(OutputStream os) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os))) {
            bw.write(getPlayerOne().name() + "-" + getPlayerOne().color() + ";" +
                    getPlayerTwo().name() + "-" + getPlayerTwo().color());
            bw.newLine();
            for (Piece[] row : getBoard().getPieces()) {
                for (int i = 0; i < row.length; i++) {
                    if (row[i] == null) {
                        bw.write("_");
                    } else {
                        bw.write(row[i].getPieceType() + "," + row[i].getColor());
                    }
                    if (i != row.length - 1) {
                        bw.write(";");
                    }
                }
                bw.newLine();
            }
            //does not know at all what flush does, but it is usually places at the end of writing block
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("something wrong about writing into file/os", e);
        }
    }

    @Override
    public void write(File file) throws IOException {
        OutputStream os = new FileOutputStream(file);
        write(os);
    }

    /**
     * uses Builder design pattern (or at least I hope so)
     * to build new chess game
     * @author Jan Maly
     */
    public static class Builder implements Buildable<Chess>, GameReadable {
        private final Board board = new Board();
        private Player playerOne;
        private Player playerTwo;

        /**
         * adds step by step a player to the game
         * adding more than 2 players does not change anything
         * @param player to be added/set to the game
         * @return building game object in progress
         */
        public Builder addPlayer(Player player) {
            if (playerOne == null) {
                playerOne = player;
            } else if (playerTwo == null) {
                playerTwo = player;
            }
            return this;
        }

        /**
         * takes a piece and a position in the chess notation as input parameters.
         * It puts the piece on the board.
         * @param piece to be added
         * @param letterNumber its first coordinate
         * @param number its second coordinate
         * @return building game object in progress
         */
        public Builder addPieceToBoard(Piece piece, char letterNumber, int number) {
            Coordinates coordinates = BoardNotation.getCoordinatesOfNotation(letterNumber, number);
            board.putPieceOnBoard(coordinates.letterNumber(), coordinates.number(), piece);
            return this;
        }

        @Override
        public Chess build() throws MissingPlayerException {
            if (playerOne == null || playerTwo == null) {
                throw new MissingPlayerException("You must have two players to play");
            }
            return new Chess(playerOne, playerTwo, board);
        }

        @Override
        /**
         * the input is expected to be the board with some pieces in the correct format
         * the method checks the format and if correct, sets board according to the input
         */
        public Builder read(InputStream is) throws IOException {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                for (int i=0; i< Board.SIZE; i++) {
                    String line = reader.readLine();
                    String[] row = line.split(";", Board.SIZE);
                    for (int j=0; j<row.length; j++) {
                        if (!Objects.equals(row[j], "_")) {
                            String[] toBePiece = row[j].split(",", 2);
                            Piece piece = new Piece(Color.valueOf(toBePiece[1]), PieceType.valueOf(toBePiece[0]));
                            board.putPieceOnBoard(i, j, piece);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new IOException("Something wrong about InputStream" + is, e);
            }
            return this;
        }

        @Override
        /**
         * header includes information about players, their names and colors,
         * these are set to the game to (if in correct format)
         */
        public Builder read(InputStream is, boolean hasHeader) throws IOException {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                if (hasHeader) {
                    String header = reader.readLine();
                    String[] toBePlayers = header.split(";", 2);
                    for (String a : toBePlayers) {
                        String[] toBePlayer = a.split("-", 2);
                        Player player = new Player(toBePlayer[0], Color.valueOf(toBePlayer[1]));
                        addPlayer(player);
                    }
                    //creates a reader without header which is then used as input into read(InputStream is) method
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    int nextByte;
                    while ((nextByte = reader.read()) != -1) {
                        outputStream.write(nextByte);
                    }
                    InputStream withoutFirstLine = new ByteArrayInputStream(outputStream.toByteArray());
                    return read(withoutFirstLine);
                } else {
                    return read(is);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new IOException("Something wrong about InputStream" + is, e);
            }
        }

        @Override
        public Builder read(File file) throws IOException {
            InputStream fis = new FileInputStream(file);
            return read(fis);
        }

        @Override
        public Builder read(File file, boolean hasHeader) throws IOException {
            InputStream fis = new FileInputStream(file);
            return read(fis, hasHeader);
        }
    }
}
