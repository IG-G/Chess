package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewPieceListener implements ActionListener {
    ChessGameController controller;

    public NewPieceListener(ChessGameController controller) {
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        controller.frame.createFrameForNewPiece(new EndUserInputListener(controller));
    }
}
