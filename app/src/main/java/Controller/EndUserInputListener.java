package Controller;

import Model.ChessModelSquare;
import View.ChessViewSquare;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class EndUserInputListener implements ActionListener {
    ChessGameController controller;

    public EndUserInputListener(ChessGameController controller) {
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boolean[] var = controller.frame.getParamsForNewPiece();
        ChessViewSquare[] squaresToMove = controller.frame.getPossibleMovesForNewPiece();
        controller.frame.getNewPieceFrame().dispose();
        List<ChessModelSquare> jumpMoves = new ArrayList<>();
        for (ChessViewSquare squares : squaresToMove) {
            int x = squares.getPosXOnBoard();
            int y = squares.getPosYOnBoard();
            jumpMoves.add(controller.boardModel.getChessModelSquare(x, y));
        }
        controller.boardModel.createUserPiece(var, jumpMoves.toArray(new ChessModelSquare[0]));
    }
}
