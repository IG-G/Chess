package Controller;

import Model.ChessModelBoard;
import Model.ChessModelSquare;
import Pieces.BlackPawn;
import Pieces.WhitePawn;
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
                if(!selectSquaresToMove(modelSquare)){
                    selectedForMove = null;
                }
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
                    if(!selectSquaresToMove(modelSquare)){
                        selectedForMove = null;
                    }else {
                        selectedForMove = modelSquare;
                    }
                }
            }else
                colorOnMove = colorOnMove == ColorOfPiece.WHITE ? ColorOfPiece.BLACK : ColorOfPiece.WHITE;
        }
    }

    public boolean selectSquaresToMove(ChessModelSquare source) {
        List<ChessModelSquare>possibleMovesFromModel = boardModel.getLegalPossibleMoves(source);
        List<ChessViewSquare> viewSquaresPossibleToMove = new ArrayList<ChessViewSquare>();
        if(possibleMovesFromModel != null) {
            for (ChessModelSquare itr : possibleMovesFromModel) {
                viewSquaresPossibleToMove.add(boardView.getChessViewSquare(itr.getY(), itr.getX()));
            }
            boardView.setPossibleMovesSquares(viewSquaresPossibleToMove.toArray(new ChessViewSquare[0]));
            previousPossibleMoves = possibleMovesFromModel.toArray(new ChessModelSquare[0]);

            return true;
        }
        return false;
    }

    public boolean makeMove(ChessModelSquare destinationSquare){
        for (ChessModelSquare square: previousPossibleMoves) {
            if(square == destinationSquare) {
                boardModel.makeMove(selectedForMove, destinationSquare, colorOnMove);
                boardView.makeMove(selectedForMove.getY(), selectedForMove.getX(),
                        destinationSquare.getY(), destinationSquare.getX());
                if(boardModel.didShortCastleHappened()){
                    int row = destinationSquare.getY();
                    ChessModelSquare rookSquare = boardModel.getChessModelSquare(7, row);
                    ChessModelSquare squareAfterCastle = boardModel.getChessModelSquare(5, row);
                    boardView.makeMove(rookSquare.getY(), rookSquare.getX(), squareAfterCastle.getY(), squareAfterCastle.getX());
                    boardModel.setShortCastleHappened(false);
                }
                if(boardModel.didLongCastleHappened()){
                    int row = destinationSquare.getY();
                    ChessModelSquare rookSquare = boardModel.getChessModelSquare(0, row);
                    ChessModelSquare squareAfterCastle = boardModel.getChessModelSquare(3, row);
                    boardView.makeMove(rookSquare.getY(), rookSquare.getX(), squareAfterCastle.getY(), squareAfterCastle.getX());
                    boardModel.setLongCastleHappened(false);
                }
                if(boardModel.isKingUnderCheck()){
                    boardView.cleanKingUnderCheck(); // in case of check during defence
                    ColorOfPiece color = colorOnMove == ColorOfPiece.WHITE ? ColorOfPiece.BLACK : ColorOfPiece.WHITE;
                    ChessModelSquare kingSquare = boardModel.getKingPosition(color);
                    boardView.setKingUnderCheck(kingSquare.getY(), kingSquare.getX());
                }else{
                    boardView.cleanKingUnderCheck();
                }
                //segment en passant
                if(destinationSquare.getPiece() instanceof WhitePawn && destinationSquare.getY() == 2 &&
                        boardModel.getChessModelSquare(destinationSquare.getX(), 3).getPiece() instanceof BlackPawn &&
                        ((BlackPawn) boardModel.getChessModelSquare(destinationSquare.getX(), 3).getPiece()).isEnPassant()){
                    boardView.getChessViewSquare(3, destinationSquare.getX()).setIcon(null);
                    boardModel.getChessModelSquare(destinationSquare.getX(), 3).setPiece(null);
                }
                if(destinationSquare.getPiece() instanceof BlackPawn && destinationSquare.getY() == 5 &&
                        boardModel.getChessModelSquare(destinationSquare.getX(), 4).getPiece() instanceof WhitePawn &&
                        ((WhitePawn) boardModel.getChessModelSquare(destinationSquare.getX(), 4).getPiece()).isEnPassant()){
                    boardView.getChessViewSquare(4, destinationSquare.getX()).setIcon(null);
                    boardModel.getChessModelSquare(destinationSquare.getX(), 4).setPiece(null);
                }
                //promotion of pawn
                if(destinationSquare.getY() == 0 && destinationSquare.getPiece() instanceof WhitePawn){
                    boardView.makePromotion();
                }
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


