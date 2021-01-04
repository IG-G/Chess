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
        ChessViewSquare source = (ChessViewSquare) e.getSource();
        controller.actionOccurred(source);
    }

        /*
        ChessViewSquare sourceSquare = controller.boardView.getClickedSquare(); //previously clicked square

        if (sourceSquare == null) { //there is no previously clicked square
            controller.selectSquaresToMove(destinationSquare.getPosXOnBoard(), destinationSquare.getPosYOnBoard());
            sourceSquare = destinationSquare;
        }else{ //now make move or choose another piece or cancel possible moves
            if (sourceSquare.getIcon() != null) { //prev square has piece -> we will make move
                controller.makeMove(destinationSquare.getPosXOnBoard(), destinationSquare.getPosYOnBoard());
                destinationSquare.setIcon(sourceSquare.getIcon());
                sourceSquare.setIcon(null);
                sourceSquare = null;
            }else { //prev square hasn't piece ->
                sourceSquare = destinationSquare;
            }
            controller.boardView.cleanPossibleMovesSquares();
        }
        controller.boardView.setClickedSquare(sourceSquare);
    */
}
