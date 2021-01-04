package Pieces;

import Model.ChessModelSquare;

import java.util.ArrayList;
import java.util.List;
//TODO BLACK KING DOES NOT WORK PROP WITH CATSLE
public class King implements ChessPiece {
    private boolean canCastle = true;
    private boolean isUnderCheck = false;
    private final ColorOfPiece color;

    public King(ColorOfPiece color){
        this.color = color;
    }

    @Override
    public ColorOfPiece getColor(){
        return color;
    }

    @Override
    public List<ChessModelSquare> checkPossibleMoves(ChessModelSquare source, ChessModelSquare[][] board) {
        List<ChessModelSquare> possibleMoves = new ArrayList<ChessModelSquare>();
        for(int i = -1; i < 2; i++) {
            for(int j = -1; j < 2; j++){
                if(i == 0 && j == 0)
                    continue;
                if(0 <= i + source.getX() && i + source.getX() <= 7 && j + source.getY() >= 0 && j + source.getY() <= 7)
                    possibleMoves.add(board[j + source.getY()][i + source.getX()]);
            }
        }
        possibleMoves.removeIf(square -> square.getPiece() != null && square.getPiece().getColor() == color);
        //TODO check collision -> moze po roszadach aby nie duyblowac sprawdzania
        //short castle TODO check check collision during castle
        if(board[7][7].getPiece() != null && canCastle && board[7][5].getPiece() == null && board[7][6].getPiece() == null){
            if(board[7][7].getPiece() instanceof Rook)
                possibleMoves.add(board[7][6]);
        }
        //long castle TODO check check collision during castle
        if(board[7][0].getPiece() != null && canCastle && board[7][3].getPiece() == null &&
           board[7][2].getPiece() == null && board[7][1].getPiece() == null){
            try{
                Rook rook = (Rook)board[7][0].getPiece();
                if(!rook.wasMoved())
                    possibleMoves.add(board[7][2]);

            } catch (Exception e) {
                //nothing to do here
            }
        }
        return possibleMoves;
    }
}
