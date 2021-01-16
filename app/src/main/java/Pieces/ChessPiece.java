package Pieces;

import Model.ChessModelSquare;

import java.util.List;

public interface ChessPiece {
    List<ChessModelSquare> checkPossibleMoves(ChessModelSquare square, ChessModelSquare[][] board);

    ColorOfPiece getColor();

    default void checkMovesDown(ChessModelSquare square, ChessModelSquare[][] board, List<ChessModelSquare> possibleMoves) {
        for (int i = square.getY() - 1; i >= 0; i--)
            if (board[i][square.getX()].getPiece() == null)
                possibleMoves.add(board[i][square.getX()]);
            else {
                if (board[i][square.getX()].getPiece().getColor() != getColor())
                    possibleMoves.add(board[i][square.getX()]);
                break;
            }
    }

    default void checkMovesUp(ChessModelSquare square, ChessModelSquare[][] board, List<ChessModelSquare> possibleMoves) {
        for (int i = square.getY() + 1; i < 8; i++)
            if (board[i][square.getX()].getPiece() == null)
                possibleMoves.add(board[i][square.getX()]);
            else {
                if (board[i][square.getX()].getPiece().getColor() != getColor())
                    possibleMoves.add(board[i][square.getX()]);
                break;
            }
    }

    default void checkMovesLeft(ChessModelSquare square, ChessModelSquare[][] board, List<ChessModelSquare> possibleMoves) {
        for (int i = square.getX() - 1; i >= 0; i--)
            if (board[square.getY()][i].getPiece() == null)
                possibleMoves.add(board[square.getY()][i]);
            else {
                if (board[square.getY()][i].getPiece().getColor() != getColor())
                    possibleMoves.add(board[square.getY()][i]);
                break;
            }
    }

    default void checkMovesRight(ChessModelSquare square, ChessModelSquare[][] board, List<ChessModelSquare> possibleMoves) {
        for (int i = square.getX() + 1; i < 8; i++)
            if (board[square.getY()][i].getPiece() == null)
                possibleMoves.add(board[square.getY()][i]);
            else {
                if (board[square.getY()][i].getPiece().getColor() != getColor())
                    possibleMoves.add(board[square.getY()][i]);
                break;
            }
    }
}
