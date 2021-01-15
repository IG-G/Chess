package Controller;

import Model.ChessModelBoard;
import Model.ChessModelSquare;
import Pieces.BlackPawn;
import Pieces.Rook;
import Pieces.WhitePawn;
import View.ChessViewBoard;
import View.AppGUI;
import View.ChessViewSquare;
import Pieces.ColorOfPiece;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class ChessGameController {
    ChessModelBoard boardModel;
    ChessViewBoard boardView;
    AppGUI frame;
    ChessModelSquare[] previousPossibleMoves = null;
    ChessModelSquare selectedForMove = null;
    ColorOfPiece colorOnMove = ColorOfPiece.WHITE;
    boolean isGameWithBot = false;
    ColorOfPiece humanColor = null;

    public ChessGameController(ChessModelBoard model, ChessViewBoard view, AppGUI mainFrame){
        boardModel = model;
        boardView = view;
        frame = mainFrame;
    }

    public void initGUI() {
        NewGameListener listener = new NewGameListener(this, frame.getFrame());
        NewPieceListener listener1 = new NewPieceListener(this);
        CreateCustomGameListener listener2 = new CreateCustomGameListener(this);
        frame.initMainFrame(listener, listener1, listener2);
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
            }else{
                if(isGameWithBot) {
                    makeBotMove();
                    colorOnMove = humanColor;
                } else {
                    colorOnMove = colorOnMove == ColorOfPiece.WHITE ? ColorOfPiece.BLACK : ColorOfPiece.WHITE;
                }
            }
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
                    ((Rook)boardModel.getChessModelSquare(5, row).getPiece()).setWasMoved(true);
                    boardModel.setShortCastleHappened(false);
                }
                if(boardModel.didLongCastleHappened()){
                    int row = destinationSquare.getY();
                    ChessModelSquare rookSquare = boardModel.getChessModelSquare(0, row);
                    ChessModelSquare squareAfterCastle = boardModel.getChessModelSquare(3, row);
                    boardView.makeMove(rookSquare.getY(), rookSquare.getX(), squareAfterCastle.getY(), squareAfterCastle.getX());
                    ((Rook)boardModel.getChessModelSquare(3, row).getPiece()).setWasMoved(true);
                    boardModel.setLongCastleHappened(false);
                }

                //segment en passant
                if(boardModel.didEnPassantHappened()){
                    if(destinationSquare.getY() == 2){
                        boardView.getChessViewSquare(3, destinationSquare.getX()).setPieceIcon(null);
                    }else{
                        boardView.getChessViewSquare(4, destinationSquare.getX()).setPieceIcon(null);
                    }
                    boardModel.setEnPassantHappened(false);
                }
                //promotion of pawn
                if((destinationSquare.getY() == 0 && destinationSquare.getPiece() instanceof WhitePawn) ||
                        (destinationSquare.getY() == 7 && destinationSquare.getPiece() instanceof BlackPawn)){
                    ChessViewSquare promotionSquare = boardView.getChessViewSquare(destinationSquare.getY(), destinationSquare.getX());
                    int chosenOption = boardView.makePromotion(promotionSquare);
                    boardModel.makePromotion(destinationSquare, chosenOption);
                }

                //check segment
                if(boardModel.isKingUnderCheck()){
                    boardView.cleanKingUnderCheck(); // in case of checking the other king during defence
                    ColorOfPiece color = colorOnMove == ColorOfPiece.WHITE ? ColorOfPiece.BLACK : ColorOfPiece.WHITE;
                    ChessModelSquare kingSquare = boardModel.getKingPosition(color);
                    boardView.setKingUnderCheck(kingSquare.getY(), kingSquare.getX());
                }else{
                    boardView.cleanKingUnderCheck();
                }
                if(boardModel.hasGameFinished()){
                    endGame();
                }
                return true;
            }
        }
        return false;
    }

    private void endGame(){
        if(!boardModel.isStaleMate()) {
            boardView.showEndGameInfo(colorOnMove);
        }else{
            boardView.showTieGameInfo();
        }
        isGameWithBot = false;
    }

    public void startNewGame() {
        ((JFrame) (frame.getFrame())).getContentPane().removeAll();
        boardModel.initPieces();
        boardModel.clearAll();
        SquareButtonListener listener = new SquareButtonListener(this);
        boardView.initViewBoard(listener);
        colorOnMove = ColorOfPiece.WHITE;
        if (isGameWithBot && humanColor == ColorOfPiece.BLACK){
            makeBotMove();
            colorOnMove = ColorOfPiece.BLACK;
        }
    }

    public void setParamsForNewGameWithBot(ColorOfPiece humanColor){
        isGameWithBot = true;
        this.humanColor = humanColor;
    }

    private void makeBotMove(){
        ColorOfPiece botColor = humanColor == ColorOfPiece.WHITE ? ColorOfPiece.BLACK : ColorOfPiece.WHITE;
        List<List<ChessModelSquare>> possibleMoves = new ArrayList<>();
        int tailIndex = 0;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                ChessModelSquare square = boardModel.getChessModelSquare(i, j);
                if(square.getPiece() != null && square.getPiece().getColor() == botColor) {
                    if (selectSquaresToMove(square)) {
                        possibleMoves.add(new ArrayList<>());
                        boardView.cleanPossibleMovesSquares();
                        possibleMoves.get(tailIndex).add(square);
                        possibleMoves.get(tailIndex).addAll(Arrays.<ChessModelSquare>asList(previousPossibleMoves));
                        tailIndex++;
                    }
                }
            }
        }
        Random rand = new Random();
        int i = rand.nextInt(possibleMoves.size());
        int j;
        if(possibleMoves.get(i).size() != 1){
             j = rand.nextInt(possibleMoves.get(i).size() - 1);
             j++;
        }else{
            j = 1;
        }
        ChessModelSquare source = possibleMoves.get(i).get(0);
        ChessModelSquare dest = possibleMoves.get(i).get(j);
        selectedForMove = source;
        possibleMoves.get(i).remove(0);
        previousPossibleMoves = possibleMoves.get(i).toArray(new ChessModelSquare[0]);
        makeMove(dest);
        selectedForMove = null;
    }
}


