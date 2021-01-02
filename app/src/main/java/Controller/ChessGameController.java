package Controller;

import Model.ChessModelBoard;
import Model.ChessModelSquare;
import View.ChessViewBoard;
import View.AppGUI;
import View.ChessViewSquare;

import java.util.List;


public class ChessGameController {
    ChessModelBoard boardModel;
    ChessViewBoard boardView;
    AppGUI frame;

    public ChessGameController(ChessModelBoard model, ChessViewBoard view, AppGUI mainFrame){
        boardModel = model;
        boardView = view;
        frame = mainFrame;
    }

    public void initGUI() {
        frame.initMainFrame();
    }
    public void selectSquaresToMove(int col, int row){
        ChessModelSquare source = boardModel.getChessBoard(col, row);
        List<ChessModelSquare>possibleMovesFromModel = boardModel.getLegalPossibleMoves(source);
        ChessViewSquare[][] chessViewSquares = boardView.getChessSquares();
        for (ChessModelSquare itr: possibleMovesFromModel) {
            chessViewSquares[itr.getY()][itr.getX()].setColorOfTheSquare(boardView.getDEFAULT_COLOR_POSSIBLE_MOVE());
        }
        chessViewSquares[row][col].setColorOfTheSquare(boardView.getDEFAULT_COLOR_POSSIBLE_MOVE());
    }

    public void startNewGame() {
        boardModel.initPieces();
        SquareButtonListener listener = new SquareButtonListener(this);
        boardView.initViewBoard(listener);
    }
        /* jak to ma wyglądać?
            1.inicjujemy nowy model z domyslnymi figurami
            2.inicjujemy nowy widok z domyslnymi figurami
            3.loop
                4.biale wykonuja ruch
                    -kikniecia na figur
                    -przeslanie informajci o kacji do kontrolera
                        -prosi o liste mozliwych ruchow dla tej figury
                        -anuluje poprzednia liste ruchow przeslana do wyswietlenia
                        -wykonuje ruch
                        -znow prosi o liste ruchow (dwa razy klinkiecie na figure na ruchu)
                    -odsyla info do widoku
                    -jezeli ruch sie odbyl aktualizuje model
                    -sprawdza czy gra sie skonczyla
                4.czarne wykonuja ruch
                    -jw
            6.gra sie konczy
            7.okienko dialgowe z informacja i z opcjami
         */
}


