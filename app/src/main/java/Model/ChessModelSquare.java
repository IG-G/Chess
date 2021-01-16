package Model;

import Pieces.ChessPiece;

public class ChessModelSquare {
    private ChessPiece piece;
    private final int x;
    private final int y;

    public ChessModelSquare(int row, int col) {
        this.piece = null;
        x = col;
        y = row;
    }

    public ChessPiece getPiece() {
        return piece;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setPiece(ChessPiece piece) {
        this.piece = piece;
    }

}
