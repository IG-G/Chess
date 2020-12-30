package Model;

public class ChessBoardModel {
    private final int ROWS = 8;
    private final int COLUMNS = 8;
    ChessSquare[][] chessBoard;
    boolean isGameFinished = false;

    public ChessBoardModel(){
        chessBoard = new ChessSquare[ROWS][COLUMNS];
        for(int i = 0; i < ROWS; i++){
            for(int j = 0; j < COLUMNS; j++){
                chessBoard[i][j] = new ChessSquare(i, j);
            }
        }
        chessBoard = new ChessSquare[ROWS][COLUMNS];
        initPieces(chessBoard);
    }
    private void initPieces(ChessSquare[][] board){
        ;
    }

    public void setGameFinished(boolean gameFinished) {
        isGameFinished = gameFinished;
    }

    public boolean isGameFinished() {
        return isGameFinished;
    }

    public void makeMove(ChessSquare sourceSquare, ChessSquare destinationSquare){
        //problem z promocja!!!
        if(sourceSquare.getPiece().isMoveValid(sourceSquare, destinationSquare)) {
            sourceSquare.getPiece().makeMove(sourceSquare, destinationSquare);
            //sprawdz szacha
            //sprawdz mata
            //uaktualnij stan roszady
            //uaktualnij stan bicia w przelocie
        }
        //wyślij dane do contollera
    }
}
