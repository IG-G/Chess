package Pieces;

import Model.ChessModelSquare;

import java.util.List;

public interface ChessPiece {
    int ROWS = 8;
    int COLUMNS = 8;

    List<ChessModelSquare> checkPossibleMoves(ChessModelSquare square, ChessModelSquare[][] board);

    default void makeMove(ChessModelSquare sourceSquare, ChessModelSquare destinationSquare) {
        destinationSquare.setPiece(sourceSquare.getPiece());
    }
    ColorOfPiece getColor();
}
