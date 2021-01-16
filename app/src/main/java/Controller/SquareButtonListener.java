package Controller;

import View.ChessViewSquare;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SquareButtonListener implements ActionListener {
    private final ChessGameController controller;

    public SquareButtonListener(ChessGameController controller) {
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ChessViewSquare source = (ChessViewSquare) e.getSource();
        if (controller.isGameWithBot) {
            if (controller.colorOnMove == controller.humanColor)
                controller.actionOccurred(source);
        } else {
            controller.actionOccurred(source);
        }
    }
}
