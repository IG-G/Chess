package Controller;

import View.ChessViewSquare;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class NewPieceListener implements ActionListener {
    ChessGameController controller;

    public NewPieceListener(ChessGameController controller){
        this.controller = controller;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        controller.frame.createFrameForNewPiece(new EndUserInputListener(controller));
    }
}
