package Pieces;

import Model.ChessModelSquare;

import java.util.ArrayList;
import java.util.List;

public class WhitePawn implements ChessPiece {
    ColorOfPiece color = ColorOfPiece.WHITE;
    private boolean enPassant = false;

    @Override
    public ColorOfPiece getColor() {
        return color;
    }

    public boolean isEnPassant() {
        return enPassant;
    }

    public void setEnPassant(boolean enPassant) {
        this.enPassant = enPassant;
    }

    public void checkOnlyAttackMoves(ChessModelSquare source, ChessModelSquare[][] board, List<ChessModelSquare> possibleMoves) {
        if (source.getY() == 0)
            return;
        if (source.getX() - 1 >= 0) {
            if (source.getX() != 0 && board[source.getY() - 1][source.getX() - 1].getPiece() != null &&
                    board[source.getY() - 1][source.getX() - 1].getPiece().getColor() != color) //check captures on right
                possibleMoves.add(board[source.getY() - 1][source.getX() - 1]);
        }
        if (source.getX() + 1 < 8) {
            if (source.getX() != 7 && board[source.getY() - 1][source.getX() + 1].getPiece() != null &&
                    board[source.getY() - 1][source.getX() + 1].getPiece().getColor() != color) //check captures on left
                possibleMoves.add(board[source.getY() - 1][source.getX() + 1]);
        }
        if (source.getY() == 3) {
            if (source.getX() + 1 < 8 && board[3][source.getX() + 1].getPiece() != null &&
                    board[3][source.getX() + 1].getPiece() instanceof BlackPawn &&
                    ((BlackPawn) board[3][source.getX() + 1].getPiece()).isEnPassant()) {
                possibleMoves.add(board[2][source.getX() + 1]);
            }
            if (source.getX() - 1 >= 0 && board[3][source.getX() - 1].getPiece() != null &&
                    board[3][source.getX() - 1].getPiece() instanceof BlackPawn &&
                    ((BlackPawn) board[3][source.getX() - 1].getPiece()).isEnPassant()) {
                possibleMoves.add(board[2][source.getX() - 1]);
            }
        }
    }

    @Override
    public List<ChessModelSquare> checkPossibleMoves(ChessModelSquare source, ChessModelSquare[][] board) {
        List<ChessModelSquare> possibleMoves = new ArrayList<ChessModelSquare>();
        checkOnlyAttackMoves(source, board, possibleMoves);
        if (source.getY() != 0 && board[source.getY() - 1][source.getX()].getPiece() == null) //check normal move
            possibleMoves.add(board[source.getY() - 1][source.getX()]);
        if (source.getY() == 6 && board[source.getY() - 1][source.getX()].getPiece() == null
                && board[source.getY() - 2][source.getX()].getPiece() == null) //check first move of the pawn
            possibleMoves.add(board[source.getY() - 2][source.getX()]);

        for (ChessModelSquare square : possibleMoves) {
            if (square.getPiece() != null)
                if (square.getPiece().getColor() == ColorOfPiece.WHITE)
                    possibleMoves.remove(square);
        }
        return possibleMoves;
    }
}
