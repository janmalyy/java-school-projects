package cz.muni.fi.pb162.project;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatIOException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


/**
 * @author Alzbeta Strompova
 */
public class ChessIOTest {

    private static final String GAME_OK_TXT = "game-ok.txt";
    private static final String GAME_ERROR_TXT = "game-error.txt";
    private static final String GAME_OUT_TXT = "game-out.txt";

    private Chess.Builder builder;

    @Before
    public void setUp() {
        builder = new Chess.Builder();
    }

    @Test
    public void readCorrectFileDoesNotThrowException() {
        assertThatCode(() -> builder.read(new File(GAME_OK_TXT), true)).doesNotThrowAnyException();
    }

    @Test
    public void readCorrectStreamDoesNotThrowException() {
        assertThatCode(() -> builder.read(new FileInputStream(GAME_OK_TXT), true)).doesNotThrowAnyException();
    }

    @Test
    public void readInvalidFileThrowsException() {
        assertThatIOException().isThrownBy(() -> builder.read(new File(GAME_ERROR_TXT), true));
    }

    @Test
    public void readInvalidStreamThrowsException() {
        assertThatIOException().isThrownBy(() -> builder.read(new FileInputStream(GAME_ERROR_TXT), true));
    }

    @Test
    public void readInvalidStreamWrongRowSize() throws UnsupportedEncodingException {
        var errorString = """
                ROOK,WHITE;PAWN,WHITE;_;_;_;_;PAWN,BLACK;ROOK,BLACK;
                KNIGHT,WHITE;PAWN,WHITE;_;_;_;_;PAWN,BLACK;KNIGHT,BLACK;
                """;
        testRead(errorString);
    }

    @Test
    public void readInvalidStreamWrongColumnSize() throws UnsupportedEncodingException {
        var errorString = """
                ROOK,WHITE;PAWN,WHITE;_;_;_;_;
                KNIGHT,WHITE;PAWN,WHITE;_;_;_;_;
                BISHOP,WHITE;PAWN,WHITE;_;_;_;_;
                QUEEN,WHITE;PAWN,WHITE;_;_;_;_;
                KING,WHITE;PAWN,WHITE;_;_;_;_;
                BISHOP,WHITE;PAWN,WHITE;_;_;_;_;
                KNIGHT,WHITE;PAWN,WHITE;_;_;_;_;
                ROOK,WHITE;PAWN,WHITE;_;_;_;_;
                """;
        testRead(errorString);
    }

    @Test
    public void readInvalidStreamInvalidFormat() throws UnsupportedEncodingException {
        var errorString = """
                ROOK,WHITE;PAW,WHITE;_;_;_;_;PAWN,BLACK;ROOK,BLACK;
                KNIGHT,BLUE;PAWN,WHITE;_;_;_;_;PAWN,BLACK;KNIGHT,BLACK;
                BISHOP,RED;PAWN,WHITE;_;_;_;_;PAT,BLACK;BISHOP,BLACK;
                QUEEN,WHITE;PAWN,WHITE;_;_;_;_;PAWN,BLACK;QUEEN,BLACK;
                KING,WHITE;PAWN,WHITE;_;_;_;_;PAWN,BLACK;MAT,BLACK;
                BISHOP,WHITE;PAWN,WHITE;_;_;_;_;PAWN,BLACK;BISHOP,BLACK;
                KNIGHT,WHITE;PAWN,WHITE;_;_;_;_;PAWN,BLACK;KNIGHT,BLACK;
                ROOK,WHITE;PAWN,WHITE;_;_;_;_;PAWN,BLACK;ROOK,BLACK;
                """;
        testRead(errorString);
    }

    private void testRead(String errorString) throws UnsupportedEncodingException {
        InputStream stream = new ByteArrayInputStream(errorString.getBytes(StandardCharsets.UTF_8.name()));
        assertThatIOException().isThrownBy(() -> builder.read(stream));
    }

    @Test
    public void readCorrectFile() throws IOException {
        Chess game = builder.read(new FileInputStream(GAME_OK_TXT), true).build();
        var game2 = new Chess(null, null);
        game2.setInitialSet();
        assertEquals(game.getBoard().getAllPiecesFromBoard().length, game2.getBoard().getAllPiecesFromBoard().length);
        assertNotNull(game.getPlayerOne());
        assertNotNull(game.getPlayerTwo());
    }

    @Test
    public void writeAndReadData() throws IOException {
        var player = new Player("Pat", Color.BLACK);
        var player2 = new Player("Mat", Color.WHITE);
        Chess game = new Chess(player, player2);
        var piece = new Piece(Color.WHITE, PieceType.QUEEN);
        game.putPieceOnBoard(4, 2, piece);
        game.write(new File(GAME_OUT_TXT));
        Chess readGame = builder.read(new FileInputStream(GAME_OUT_TXT), true).build();
        assertEquals(1, readGame.getBoard().getAllPiecesFromBoard().length);
        assertEquals(piece.getPieceType(), readGame.getBoard().getAllPiecesFromBoard()[0].getPieceType());
        assertEquals(piece.getColor(), readGame.getBoard().getAllPiecesFromBoard()[0].getColor());
    }
}
