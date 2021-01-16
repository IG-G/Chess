package Pieces;

import Model.ChessModelSquare;

import java.util.ArrayList;
import java.util.List;

//Rook piece could be made by generic piece class, but rook is used in castling
//so it is needed to control wasMoved parameter
public class Rook implements ChessPiece {
    ColorOfPiece color;
    private boolean wasMoved = false;

    public Rook(ColorOfPiece color) {
        this.color = color;
    }

    @Override
    public ColorOfPiece getColor() {
        return color;
    }

    public boolean wasMoved() {
        return wasMoved;
    }

    public void setWasMoved(boolean wasMoved) {
        this.wasMoved = wasMoved;
    }

    @Override
    public List<ChessModelSquare> checkPossibleMoves(ChessModelSquare square, ChessModelSquare[][] board) {
        List<ChessModelSquare> possibleMoves = new ArrayList<ChessModelSquare>();
        checkMovesUp(square, board, possibleMoves);
        checkMovesDown(square, board, possibleMoves);
        checkMovesLeft(square, board, possibleMoves);
        checkMovesRight(square, board, possibleMoves);

        return possibleMoves;
    }
}