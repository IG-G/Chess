package Model;

import Pieces.ColorOfPiece;
import Pieces.King;
import Pieces.Rook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CastleTests {
    private ChessModelBoard board;

    @BeforeEach
    public void init() {
        board = new ChessModelBoard();
        board.initPieces();
    }

    @Test
    public void testShortCastle() {
        board.makeMove(board.getChessModelSquare(4, 6), board.getChessModelSquare(4, 4), ColorOfPiece.WHITE);
        board.makeMove(board.getChessModelSquare(4, 1), board.getChessModelSquare(4, 3), ColorOfPiece.BLACK);
        board.makeMove(board.getChessModelSquare(5, 7), board.getChessModelSquare(4, 6), ColorOfPiece.WHITE);
        board.makeMove(board.getChessModelSquare(5, 0), board.getChessModelSquare(4, 1), ColorOfPiece.BLACK);
        board.makeMove(board.getChessModelSquare(6, 7), board.getChessModelSquare(5, 5), ColorOfPiece.WHITE);
        board.makeMove(board.getChessModelSquare(6, 0), board.getChessModelSquare(5, 2), ColorOfPiece.BLACK);
        //check if castle possible
        shortCastlePossibilities(7);
        shortCastlePossibilities(0);
        //make castle
        shortCastleMove(7, ColorOfPiece.WHITE);
        shortCastleMove(0, ColorOfPiece.BLACK);
    }

    private void shortCastlePossibilities(int row) {
        List<ChessModelSquare> expectedMoves = new ArrayList<>();
        expectedMoves.add(board.getChessModelSquare(5, row));
        expectedMoves.add(board.getChessModelSquare(6, row));
        List<ChessModelSquare> moves = board.getLegalPossibleMoves(board.getChessModelSquare(4, row));
        assertEquals(2, moves.size());
        assertTrue(moves.contains(expectedMoves.get(0)));
        assertTrue(moves.contains(expectedMoves.get(1)));
    }

    private void shortCastleMove(int row, ColorOfPiece color) {
        board.makeMove(board.getChessModelSquare(4, row), board.getChessModelSquare(6, row), color);
        assertTrue(board.getChessModelSquare(5, row).getPiece() instanceof Rook);
        assertTrue(board.getChessModelSquare(6, row).getPiece() instanceof King);
        assertFalse(((King) board.getChessModelSquare(6, row).getPiece()).canCastle());
    }

    @Test
    public void testLongCastle() {
        board.makeMove(board.getChessModelSquare(3, 6), board.getChessModelSquare(3, 4), ColorOfPiece.WHITE);
        board.makeMove(board.getChessModelSquare(3, 1), board.getChessModelSquare(3, 3), ColorOfPiece.BLACK);
        board.makeMove(board.getChessModelSquare(3, 7), board.getChessModelSquare(3, 5), ColorOfPiece.WHITE);
        board.makeMove(board.getChessModelSquare(3, 0), board.getChessModelSquare(3, 2), ColorOfPiece.BLACK);
        board.makeMove(board.getChessModelSquare(2, 7), board.getChessModelSquare(3, 6), ColorOfPiece.WHITE);
        board.makeMove(board.getChessModelSquare(2, 0), board.getChessModelSquare(3, 1), ColorOfPiece.BLACK);
        board.makeMove(board.getChessModelSquare(1, 7), board.getChessModelSquare(2, 5), ColorOfPiece.WHITE);
        board.makeMove(board.getChessModelSquare(1, 0), board.getChessModelSquare(2, 2), ColorOfPiece.BLACK);
        //check if castle possible
        longCastlePossibilities(7);
        longCastlePossibilities(0);
        //make castle
        longCastleMove(7, ColorOfPiece.WHITE);
        longCastleMove(0, ColorOfPiece.BLACK);
    }

    private void longCastlePossibilities(int row) {
        List<ChessModelSquare> expectedMoves = new ArrayList<>();
        expectedMoves.add(board.getChessModelSquare(3, row));
        expectedMoves.add(board.getChessModelSquare(2, row));
        List<ChessModelSquare> moves = board.getLegalPossibleMoves(board.getChessModelSquare(4, row));
        assertEquals(2, moves.size());
        assertTrue(moves.contains(expectedMoves.get(0)));
        assertTrue(moves.contains(expectedMoves.get(1)));
    }

    private void longCastleMove(int row, ColorOfPiece color) {
        board.makeMove(board.getChessModelSquare(4, row), board.getChessModelSquare(6, row), color);
        assertTrue(board.getChessModelSquare(5, row).getPiece() instanceof Rook);
        assertTrue(board.getChessModelSquare(6, row).getPiece() instanceof King);
        assertFalse(((King) board.getChessModelSquare(6, row).getPiece()).canCastle());
    }

    @Test
    public void testIsCastleForbiddenDuringCheck() {
        board.makeMove(board.getChessModelSquare(4, 6), board.getChessModelSquare(4, 4), ColorOfPiece.WHITE);
        board.makeMove(board.getChessModelSquare(4, 1), board.getChessModelSquare(4, 3), ColorOfPiece.BLACK);
        board.makeMove(board.getChessModelSquare(5, 7), board.getChessModelSquare(3, 5), ColorOfPiece.WHITE);
        board.makeMove(board.getChessModelSquare(5, 0), board.getChessModelSquare(3, 2), ColorOfPiece.BLACK);
        board.makeMove(board.getChessModelSquare(6, 7), board.getChessModelSquare(5, 5), ColorOfPiece.WHITE);
        board.makeMove(board.getChessModelSquare(6, 0), board.getChessModelSquare(7, 2), ColorOfPiece.BLACK);
        board.makeMove(board.getChessModelSquare(0, 6), board.getChessModelSquare(0, 5), ColorOfPiece.WHITE);
        board.makeMove(board.getChessModelSquare(3, 0), board.getChessModelSquare(6, 3), ColorOfPiece.BLACK);
        board.makeMove(board.getChessModelSquare(0, 5), board.getChessModelSquare(0, 4), ColorOfPiece.WHITE);
        board.makeMove(board.getChessModelSquare(6, 3), board.getChessModelSquare(4, 5), ColorOfPiece.BLACK);
        ChessModelSquare kingSquare = board.getChessModelSquare(4, 7);

        assertTrue(board.isKingUnderCheck());

        List<ChessModelSquare> actualMoves = board.getLegalPossibleMoves(kingSquare);
        List<ChessModelSquare> expectedMoves = new ArrayList<>();
        expectedMoves.add(board.getChessModelSquare(5, 7));

        //only one move for king because it's under check (castle forbidden, but only for a while)
        assertTrue(((King) kingSquare.getPiece()).canCastle());
        assertEquals(1, actualMoves.size());
        assertTrue(actualMoves.contains(expectedMoves.get(0)));

        //attacking piece captured
        board.makeMove(board.getChessModelSquare(3, 6), board.getChessModelSquare(4, 5), ColorOfPiece.WHITE);
        assertFalse(board.isKingUnderCheck());
        //dummy move
        board.makeMove(board.getChessModelSquare(0, 1), board.getChessModelSquare(0, 2), ColorOfPiece.BLACK);

        actualMoves = board.getLegalPossibleMoves(kingSquare);
        expectedMoves.add(board.getChessModelSquare(6, 7));

        //castle now available
        assertTrue(((King) kingSquare.getPiece()).canCastle());
        assertEquals(4, actualMoves.size());
        assertTrue(actualMoves.contains(expectedMoves.get(0)));
        assertTrue(actualMoves.contains(expectedMoves.get(1)));
    }
}
