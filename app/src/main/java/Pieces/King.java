package Pieces;

import Model.ChessModelSquare;

import java.util.ArrayList;
import java.util.List;

public class King implements ChessPiece {
    private boolean canCastle = true;
    private final ColorOfPiece color;
    private boolean isChecked = false;

    public King(ColorOfPiece color) {
        this.color = color;
    }

    @Override
    public ColorOfPiece getColor() {
        return color;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public void setCanCastle(boolean canCastle) {
        this.canCastle = canCastle;
    }

    public boolean canCastle() {
        return canCastle;
    }

    @Override
    public List<ChessModelSquare> checkPossibleMoves(ChessModelSquare source, ChessModelSquare[][] board) {
        List<ChessModelSquare> possibleMoves = new ArrayList<ChessModelSquare>();
        checkMovesAround(source, board, possibleMoves);
        if (color == ColorOfPiece.WHITE) {
            checkShortCastle(board, possibleMoves, 7);
            checkLongCastle(board, possibleMoves, 7);
        } else {
            checkShortCastle(board, possibleMoves, 0);
            checkLongCastle(board, possibleMoves, 0);
        }
        return possibleMoves;
    }

    private void checkMovesAround(ChessModelSquare source, ChessModelSquare[][] board, List<ChessModelSquare> possibleMoves) {
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (i == 0 && j == 0)
                    continue;
                if (0 <= i + source.getX() && i + source.getX() <= 7 && j + source.getY() >= 0 && j + source.getY() <= 7)
                    possibleMoves.add(board[j + source.getY()][i + source.getX()]);
            }
        }
        possibleMoves.removeIf(square -> square.getPiece() != null && square.getPiece().getColor() == getColor());
    }

    private void checkShortCastle(ChessModelSquare[][] board, List<ChessModelSquare> possibleMoves, int row) {
        if (board[row][7].getPiece() != null && canCastle && board[row][5].getPiece() == null && board[row][6].getPiece() == null) {
            if (board[row][7].getPiece() instanceof Rook && !((Rook) board[row][7].getPiece()).wasMoved() && !isChecked)
                possibleMoves.add(board[row][6]);
        }
    }

    private void checkLongCastle(ChessModelSquare[][] board, List<ChessModelSquare> possibleMoves, int row) {
        if (board[row][0].getPiece() != null && canCastle && board[row][3].getPiece() == null &&
                board[row][2].getPiece() == null && board[row][1].getPiece() == null) {
            if (board[row][0].getPiece() instanceof Rook && !((Rook) board[row][0].getPiece()).wasMoved() && !isChecked)
                possibleMoves.add(board[row][2]);
        }
    }
}
