package Controller;

import Pieces.ColorOfPiece;
import Pieces.GenericPiece;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateCustomGameListener implements ActionListener {
    ChessGameController controller;

    public CreateCustomGameListener(ChessGameController controller) {
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        controller.startNewGame();
        GenericPiece userPiece = controller.boardModel.getUserDefinedPiece(ColorOfPiece.WHITE, 0);
        if (userPiece != null)
            controller.boardModel.getChessModelSquare(3, 7).setPiece(userPiece);
    }
}
