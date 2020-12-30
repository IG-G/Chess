package Model;

import javax.management.InstanceAlreadyExistsException;

public class ChessSquare {
    ChessPiece piece;
    int x;
    int y;

    public ChessSquare(int i, int j){
        this.piece = null;
        x = j;
        y = i;
    }

    public ChessPiece getPiece() {
        return piece;
    }

    public void setPiece(ChessPiece piece) {
        this.piece = piece;
    }
}
