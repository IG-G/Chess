package Model;

import Pieces.*;

import java.util.Arrays;
import java.util.List;

public class ChessModelBoard {
    private final int ROWS = 8;
    private final int COLUMNS = 8;
    ChessModelSquare[][] chessBoard;
    boolean hasGameFinished = false;

    public ChessModelBoard(){
        chessBoard = new ChessModelSquare[ROWS][COLUMNS];
        for(int i = 0; i < ROWS; i++){
            for(int j = 0; j < COLUMNS; j++){
                chessBoard[i][j] = new ChessModelSquare(i, j);
            }
        }
    }

    public ChessModelSquare getChessBoard(int col, int row) {
        return chessBoard[row][col];
    }

    public void setGameFinished(boolean gameFinished) {
        hasGameFinished = gameFinished;
    }

    public boolean hasGameFinished() {
        return hasGameFinished;
    }


    private boolean isPinned(ChessModelSquare source) {
        //TODO
        return false;
    }

    public List<ChessModelSquare> getLegalPossibleMoves(ChessModelSquare source) {
        List<ChessModelSquare> possibleMoves;
        if(isPinned(source))
            return null;
        possibleMoves = source.getPiece().checkPossibleMoves(source, chessBoard);
        return possibleMoves;
    }
    public void initPieces(){
        for(int i = 0; i < 8; i++){
            chessBoard[1][i].setPiece(new BlackPawn());
            chessBoard[6][i].setPiece(new WhitePawn());
        }
        for(int i = 2; i < 6; i++) {
            for (int j = 0; j < 8; j++)
                chessBoard[i][j].setPiece(null);
        }

        chessBoard[0][0].setPiece(new Rook(ColorOfPiece.BLACK));
        chessBoard[0][1].setPiece(createKnight(ColorOfPiece.BLACK));
        chessBoard[0][2].setPiece(createBishop(ColorOfPiece.BLACK));
        chessBoard[0][3].setPiece(createQueen(ColorOfPiece.BLACK));
        chessBoard[0][4].setPiece(new King(ColorOfPiece.BLACK));
        chessBoard[0][5].setPiece(createBishop(ColorOfPiece.BLACK));
        chessBoard[0][6].setPiece(createKnight(ColorOfPiece.BLACK));
        chessBoard[0][7].setPiece(new Rook(ColorOfPiece.BLACK));

        chessBoard[7][0].setPiece(new Rook(ColorOfPiece.WHITE));
        chessBoard[7][1].setPiece(createKnight(ColorOfPiece.WHITE));
        chessBoard[7][2].setPiece(createBishop(ColorOfPiece.WHITE));
        chessBoard[7][3].setPiece(createQueen(ColorOfPiece.WHITE));
        chessBoard[7][4].setPiece(new King(ColorOfPiece.WHITE));
        chessBoard[7][5].setPiece(createBishop(ColorOfPiece.WHITE));
        chessBoard[7][6].setPiece(createKnight(ColorOfPiece.WHITE));
        chessBoard[7][7].setPiece(new Rook(ColorOfPiece.WHITE));
    }

    private ChessPiece createBishop(ColorOfPiece color){
        GenericPiece bishop = new GenericPiece(color);
        bishop.setLeftDiagonal(true);
        bishop.setRightDiagonal(true);
        return bishop;
    }

    private ChessPiece createQueen(ColorOfPiece color){
        GenericPiece queen = new GenericPiece(color);
        queen.setRightDiagonal(true);
        queen.setLeftDiagonal(true);
        queen.setRight(true);
        queen.setLeft(true);
        queen.setDown(true);
        queen.setUp(true);
        return queen;
    }

    private ChessPiece createKnight(ColorOfPiece color) {
        GenericPiece knight = new GenericPiece(color);
        JumpMove[] moves = {
                new JumpMove(2, 1),
                new JumpMove(1, 2),
                new JumpMove(2, -1),
                new JumpMove(1, -2),
                new JumpMove(-2, 1),
                new JumpMove(-1, 2),
                new JumpMove(-2, -1),
                new JumpMove(-1, -2)};
        knight.setJumpMoves(moves);
    return knight;
    }
}
