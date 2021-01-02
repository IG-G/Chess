package Controller;

import View.ChessViewSquare;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SquareButtonListener implements ActionListener {
    private final ChessGameController controller;

    public SquareButtonListener(ChessGameController controller){
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        ChessViewSquare destinationSquare = (ChessViewSquare)e.getSource();
        ChessViewSquare sourceSquare = controller.boardView.getClickedSquare();

        if (sourceSquare == null) { //square was selected for the first time
            controller.selectSquaresToMove(destinationSquare.getPosXOnBoard(), destinationSquare.getPosYOnBoard());
            sourceSquare = destinationSquare;
        }else{ //now make move or choose another piece or cancel possible moves
            if (sourceSquare.getIcon() != null) {
                destinationSquare.setIcon(sourceSquare.getIcon());
                sourceSquare.setIcon(null);
                sourceSquare = null;
            }else {
                sourceSquare = destinationSquare;
            }
        }
        controller.boardView.setClickedSquare(sourceSquare);
    }
}
