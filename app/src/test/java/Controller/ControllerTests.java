package Controller;

import Model.ChessModelBoard;
import Model.ChessModelSquare;
import Pieces.ColorOfPiece;
import Pieces.WhitePawn;
import View.AppGUI;
import View.ChessViewBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ControllerTests {
    private ChessGameController controller;

    @BeforeEach
    public void init() {
        JFrame frame = new JFrame("Test");
        ChessViewBoard view = new ChessViewBoard(frame.getContentPane());
        view.initViewBoard(e -> {
        });
        ChessModelBoard model = new ChessModelBoard();
        model.initPieces();
        controller = new ChessGameController(model, view, new AppGUI(frame, view));
    }

    //@Test
    public void testFirstWhitePawnMoveWithPossibilities() {
        testPossibleFirstMovesForWhitePawn();
        controller.selectedForMove = controller.boardModel.getChessModelSquare(4, 6);
        controller.boardView.cleanPossibleMovesSquares();
        testFirstMoveForWhitePawn();
    }

    private void testPossibleFirstMovesForWhitePawn() {
        controller.selectSquaresToMove(controller.boardModel.getChessModelSquare(4, 6));
        List<ChessModelSquare> expectedSquares = new ArrayList<>();
        expectedSquares.add(controller.boardModel.getChessModelSquare(4, 5));
        expectedSquares.add(controller.boardModel.getChessModelSquare(4, 4));
        //check controller
        assertEquals(2, controller.previousPossibleMoves.length);
        assertTrue(expectedSquares.contains(controller.previousPossibleMoves[0]));
        assertTrue(expectedSquares.contains(controller.previousPossibleMoves[1]));
        //check view
        assertSame(controller.boardView.getChessViewSquare(5, 4).getDisplayingColorOfSquare(),
                controller.boardView.getColorPossibleMove());
        assertSame(controller.boardView.getChessViewSquare(4, 4).getDisplayingColorOfSquare(),
                controller.boardView.getColorPossibleMove());
    }

    private void testFirstMoveForWhitePawn() {
        controller.makeMove(controller.boardModel.getChessModelSquare(4, 4));
        //check if model was correctly update by controller
        assertTrue(controller.boardModel.getChessModelSquare(4, 4).getPiece() instanceof WhitePawn);
        assertNull(controller.boardModel.getChessModelSquare(4, 6).getPiece());
        //check if view is cleaned and updated
        assertNull(controller.boardView.getChessViewSquare(6, 4).getImageIcon());
        assertNotNull(controller.boardView.getChessViewSquare(4, 4).getImageIcon());
        assertSame(controller.boardView.getChessViewSquare(5, 4).getDisplayingColorOfSquare(),
                controller.boardView.getBlackSquareColor());
        assertSame(controller.boardView.getChessViewSquare(4, 4).getDisplayingColorOfSquare(),
                controller.boardView.getWhiteSquareColor());
    }

    //@Test
    public void testGameBetweenBots() {
        ChessBot whiteBot = new ChessBot(controller.boardModel, controller);
        controller.setParamsForNewGameWithBot(whiteBot, ColorOfPiece.BLACK);
        ChessBot blackBot = new ChessBot(controller.boardModel, controller);
        for (int i = 0; i < 60; i++) {
            if (!controller.boardModel.hasGameFinished())
                whiteBot.makeBotMove();
            if (!controller.boardModel.hasGameFinished())
                blackBot.makeBotMove();
        }
    }
}
