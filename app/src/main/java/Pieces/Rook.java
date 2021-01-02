package Pieces;

import Model.ChessModelSquare;

import java.util.ArrayList;
import java.util.List;

public class Rook implements ChessPiece{
    ColorOfPiece color;
    private boolean wasMoved = false;

    public Rook(ColorOfPiece color){
        this.color = color;
    }

    @Override
    public ColorOfPiece getColor(){
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
        for(int i = square.getX() + 1; i < 8; i++){
            if(board[square.getY()][i].getPiece() == null)
                possibleMoves.add(board[square.getY()][i]);
            else
                break;
        }
        for(int i = square.getX() - 1; i >= 0; i--){
            if(board[square.getY()][i].getPiece() == null)
                possibleMoves.add(board[square.getY()][i]);
            else
                break;
        }
        for(int i = square.getY() + 1; i < 8; i++){
            if(board[i][square.getX()].getPiece() == null)
                possibleMoves.add(board[i][square.getX()]);
            else
                break;
        }
        for(int i = square.getY() - 1; i >= 0; i--){
            if(board[i][square.getX()].getPiece() == null)
                possibleMoves.add(board[i][square.getX()]);
            else
                break;
        }
        return possibleMoves;
    }
}