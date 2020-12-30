package Controller;

import Model.ChessBoardModel;
import View.Board;

public class ChessGameController {
    ChessBoardModel boardModel;
    Board boardView;

    public ChessGameController(ChessBoardModel model, Board view){
        boardModel = model;
        boardView = view;
    }

}
