package Controller;

import Model.ChessModelBoard;
import Model.ChessModelSquare;
import View.ChessViewBoard;
import View.AppGUI;
import View.ChessViewSquare;
import Pieces.ColorOfPiece;

import java.util.ArrayList;
import java.util.List;


public class ChessGameController {
    ChessModelBoard boardModel;
    ChessViewBoard boardView;
    AppGUI frame;
    ChessModelSquare[] previousPossibleMoves = null;
    ChessModelSquare selectedForMove = null;
    ColorOfPiece colorOnMove = ColorOfPiece.WHITE;

    public ChessGameController(ChessModelBoard model, ChessViewBoard view, AppGUI mainFrame){
        boardModel = model;
        boardView = view;
        frame = mainFrame;
    }

    public void initGUI() {
        frame.initMainFrame();
    }

    public void actionOccurred(ChessViewSquare source) {
        ChessModelSquare modelSquare = boardModel.getChessModelSquare(source.getPosXOnBoard(), source.getPosYOnBoard());
        if(selectedForMove == null) {
            if(modelSquare.getPiece() != null && modelSquare.getPiece().getColor() == colorOnMove) {
                selectedForMove = modelSquare;
                selectSquaresToMove(modelSquare);
            }
        }else {
            if(modelSquare == selectedForMove){
                selectedForMove = null;
                boardView.cleanPossibleMovesSquares();
                previousPossibleMoves = null;
                return;
            }
            boolean wasMoveMadeSuccessfully = makeMove(modelSquare);
            selectedForMove = null;
            boardView.cleanPossibleMovesSquares();
            previousPossibleMoves = null;
            if(!wasMoveMadeSuccessfully){
                if(modelSquare.getPiece() != null && modelSquare.getPiece().getColor() == colorOnMove){
                    selectSquaresToMove(modelSquare);
                    selectedForMove = modelSquare;
                }
            }else
                colorOnMove = colorOnMove == ColorOfPiece.WHITE ? ColorOfPiece.BLACK : ColorOfPiece.WHITE;
        }
    }

    public void selectSquaresToMove(ChessModelSquare source) {
        List<ChessModelSquare>possibleMovesFromModel = boardModel.getLegalPossibleMoves(source);

        List<ChessViewSquare> viewSquaresPossibleToMove = new ArrayList<ChessViewSquare>();
        for (ChessModelSquare itr: possibleMovesFromModel) {
            viewSquaresPossibleToMove.add(boardView.getChessViewSquare(itr.getY(), itr.getX()));
        }
        boardView.setPossibleMovesSquares(viewSquaresPossibleToMove.toArray(new ChessViewSquare[0]));
        previousPossibleMoves = possibleMovesFromModel.toArray(new ChessModelSquare[0]);
    }

    public boolean makeMove(ChessModelSquare destinationSquare){
        for (ChessModelSquare square: previousPossibleMoves) {
            if(square == destinationSquare) {
                boardModel.makeMove(selectedForMove, destinationSquare, colorOnMove);
                boardView.makeMove(selectedForMove.getY(), selectedForMove.getX(),
                        destinationSquare.getY(), destinationSquare.getX());
                return true;
            }
        }
        return false;
    }

    public void startNewGame() {
        boardModel.initPieces();
        SquareButtonListener listener = new SquareButtonListener(this);
        boardView.initViewBoard(listener);
    }
}


