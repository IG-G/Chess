package Model;

public interface ChessPiece {
    int ROWS = 8;
    int COLUMNS = 8;

    boolean isMoveValid(ChessSquare sourceSquare, ChessSquare destinationSquare);

    default void makeMove(ChessSquare sourceSquare, ChessSquare destinationSquare) {
        destinationSquare.setPiece(sourceSquare.getPiece());
    }

}
