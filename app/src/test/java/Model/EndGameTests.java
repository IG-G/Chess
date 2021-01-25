package Model;

import Pieces.ColorOfPiece;
import Pieces.King;
import Pieces.WhitePawn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EndGameTests {
    private ChessModelBoard board;

    @BeforeEach
    public void init() {
        board = new ChessModelBoard();
        board.setPieceOnBoard(4, 7, new King(ColorOfPiece.WHITE));
        board.setPieceOnBoard(4, 0, new King(ColorOfPiece.BLACK));
    }

    @Test
    public void testNotEnoughMaterial() {
        //move to trigger the checker of stale mate
        board.makeMove(board.getChessModelSquare(4, 7), board.getChessModelSquare(5, 7), ColorOfPiece.WHITE);
        assertTrue(board.isStaleMate());
        assertTrue(board.hasGameFinished());
    }

    @Test
    public void testNotEnoughMaterialWithTwoKnights() {
        board.setPieceOnBoard(0, 0, board.createKnight(ColorOfPiece.BLACK));
        board.setPieceOnBoard(7, 7, board.createKnight(ColorOfPiece.WHITE));
        board.makeMove(board.getChessModelSquare(4, 7), board.getChessModelSquare(5, 7), ColorOfPiece.WHITE);
        assertTrue(board.isStaleMate());
        assertTrue(board.hasGameFinished());
    }

    @Test
    public void testPromotion() {
        board.setPieceOnBoard(1, 1, new WhitePawn());
        board.makeMove(board.getChessModelSquare(1, 1), board.getChessModelSquare(1, 0), ColorOfPiece.WHITE);
        //explicit call, because lack of controller
        board.makePromotion(board.getChessModelSquare(1, 0), 0);
        assertTrue(board.isPieceQueen(board.getChessModelSquare(1, 0)));
        assertTrue(board.isKingUnderCheck());
    }

    @Test
    public void testCollisionOfTwoKings() {
        //just for avoiding stale mate
        board.setPieceOnBoard(1, 1, new WhitePawn());
        board.makeMove(board.getChessModelSquare(4, 7), board.getChessModelSquare(4, 6), ColorOfPiece.WHITE);
        board.makeMove(board.getChessModelSquare(4, 0), board.getChessModelSquare(4, 1), ColorOfPiece.BLACK);
        board.makeMove(board.getChessModelSquare(4, 6), board.getChessModelSquare(4, 5), ColorOfPiece.WHITE);
        board.makeMove(board.getChessModelSquare(4, 1), board.getChessModelSquare(4, 2), ColorOfPiece.BLACK);
        board.makeMove(board.getChessModelSquare(4, 5), board.getChessModelSquare(4, 4), ColorOfPiece.WHITE);

        List<ChessModelSquare> actualMoves = board.getLegalPossibleMoves(board.getChessModelSquare(4, 2));
        assertEquals(5, actualMoves.size());
    }
}
