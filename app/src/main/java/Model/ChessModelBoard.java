package Model;

import Pieces.*;

import java.util.ArrayList;
import java.util.List;

public class ChessModelBoard {
    ChessModelSquare[][] chessBoard;
    boolean hasGameFinished = false;
    boolean kingUnderCheck = false;
    boolean shortCastleHappened = false;
    boolean longCastleHappened = false;

    public ChessModelBoard(){
        int COLUMNS = 8;
        int ROWS = 8;
        chessBoard = new ChessModelSquare[ROWS][COLUMNS];
        for(int i = 0; i < ROWS; i++){
            for(int j = 0; j < COLUMNS; j++){
                chessBoard[i][j] = new ChessModelSquare(i, j);
            }
        }
    }

    public ChessModelSquare getChessModelSquare(int col, int row) {
        return chessBoard[row][col];
    }

    public void setGameFinished(boolean gameFinished) {
        hasGameFinished = gameFinished;
    }

    public boolean hasGameFinished() {
        return hasGameFinished;
    }

    public boolean didShortCastleHappened(){
        return shortCastleHappened;
    }
    public boolean didLongCastleHappened(){
        return longCastleHappened;
    }
    public void setShortCastleHappened(boolean shortCastleHappened){
        this.shortCastleHappened = shortCastleHappened;
    }

    public void setLongCastleHappened(boolean longCastleHappened) {
        this.longCastleHappened = longCastleHappened;
    }

    public boolean isKingUnderCheck() {
        return kingUnderCheck;
    }

    public void setKingUnderCheck(boolean kingUnderCheck) {
        this.kingUnderCheck = kingUnderCheck;
    }
    //check cannot be proceed when castle's squares are being attack
    private void checkIsCastleAvailable(List<ChessModelSquare> possibleMoves, ColorOfPiece color) {
        int row;
        if(color == ColorOfPiece.BLACK)
            row = 0;
        else
            row = 7;
        if(possibleMoves.contains(chessBoard[row][6])){
            if(!possibleMoves.contains(chessBoard[row][5])){
                possibleMoves.removeIf(square -> square == chessBoard[row][6]);
            }
        }
        if(possibleMoves.contains(chessBoard[row][2])){
            if(!possibleMoves.contains(chessBoard[row][3])){
                possibleMoves.removeIf(square -> square == chessBoard[row][2]);
            }
        }
    }

    public List<ChessModelSquare> getLegalPossibleMoves(ChessModelSquare source) {
        List<ChessModelSquare> possibleMoves;
        if(kingUnderCheck){
            possibleMoves = getMovesStoppingCheck(source);
            return possibleMoves;
        }
        possibleMoves = source.getPiece().checkPossibleMoves(source, chessBoard);
        possibleMoves = removeSquares(source, possibleMoves); //remove squares which lead to illegal moves
        if(source.getPiece() instanceof King){
            if(source.getPiece().getColor() == ColorOfPiece.WHITE) {
                checkIsCastleAvailable(possibleMoves, ColorOfPiece.WHITE);
            }else{
                checkIsCastleAvailable(possibleMoves, ColorOfPiece.BLACK);
            }
        }
        return possibleMoves;
    }

    public void makeMove(ChessModelSquare source, ChessModelSquare destination, ColorOfPiece colorOnMove){
        destination.setPiece(source.getPiece());
        source.setPiece(null);
        if(kingUnderCheck){
            kingUnderCheck = false; //otherwise end of game
        }
        ColorOfPiece colorOfCheckedKing = colorOnMove == ColorOfPiece.WHITE ? ColorOfPiece.BLACK : ColorOfPiece.WHITE;
        ChessModelSquare[] squaresAttackingKing = checkIsKingUnderCheck(colorOfCheckedKing);
        if(squaresAttackingKing.length != 0)
            kingUnderCheck = true;

        //castle handling
        if(destination.getPiece() instanceof King && ((King) destination.getPiece()).isCanCastle()){
            King king = (King)(destination.getPiece());
            int row = king.getColor() == ColorOfPiece.WHITE ? 7 : 0;
            if(destination == chessBoard[row][6]){
                chessBoard[row][5].setPiece(chessBoard[row][7].getPiece());
                chessBoard[row][7].setPiece(null);
                shortCastleHappened = true;
            }
            if(destination == chessBoard[row][2]){ //castle move
                chessBoard[row][3].setPiece(chessBoard[row][0].getPiece());
                chessBoard[row][0].setPiece(null);
                longCastleHappened = true;
            }
            king.setCanCastle(false);
        }

        //move with rook -> lost chance for castle
        if(destination.getPiece() instanceof Rook){
            Rook rook = (Rook)(destination.getPiece());
            rook.setWasMoved(true);
        }

        //segment for en passant
        clearAllEnPassant(colorOnMove);
        if(destination.getPiece() instanceof WhitePawn){
            if(destination.getY() == 4 && source.getY() == 6)
                ((WhitePawn) destination.getPiece()).setEnPassant(true);
        }
        if(destination.getPiece() instanceof BlackPawn){
            if(destination.getY() == 3 && source.getY() == 1)
                ((BlackPawn) destination.getPiece()).setEnPassant(true);
        }
    }

    private void clearAllEnPassant(ColorOfPiece colorOnMove){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(chessBoard[i][j].getPiece() instanceof WhitePawn && colorOnMove == ColorOfPiece.WHITE) {
                    ((WhitePawn)chessBoard[i][j].getPiece()).setEnPassant(false);
                }
                if(chessBoard[i][j].getPiece() instanceof BlackPawn && colorOnMove == ColorOfPiece.BLACK){
                    ((BlackPawn)chessBoard[i][j].getPiece()).setEnPassant(false);
                }
            }
        }
    }

    //Make move and check whether check occurred -> remove these squares
    private List<ChessModelSquare> removeSquares(ChessModelSquare source, List<ChessModelSquare> possibleMoves){
        List<ChessModelSquare> toRet = new ArrayList<>();
        for(ChessModelSquare destination: possibleMoves){
            //make move
            ChessPiece oldDestPiece = destination.getPiece();
            destination.setPiece(source.getPiece());
            source.setPiece(null);

            //check whether king is still checked
            ChessModelSquare[] checks;
            checks = checkIsKingUnderCheck(destination.getPiece().getColor());

            //undo move
            source.setPiece(destination.getPiece());
            destination.setPiece(oldDestPiece);

            if(checks.length == 0){
                toRet.add(destination);
            }
        }
        return toRet;
    }

    private List<ChessModelSquare> getMovesStoppingCheck(ChessModelSquare source){
        List<ChessModelSquare> possibleMoves;
        possibleMoves = source.getPiece().checkPossibleMoves(source, chessBoard);
        possibleMoves = removeSquares(source, possibleMoves);
        return possibleMoves;
    }

    private List<ChessModelSquare> returnPossibleAttackMoves(ChessModelSquare source, ChessModelSquare[][] board) {
        List<ChessModelSquare> moves = new ArrayList<>();
        if (source.getPiece() != null) {
            ChessPiece piece = source.getPiece();
            if (piece instanceof WhitePawn) {
                ((WhitePawn) piece).checkOnlyAttackMoves(source, board, moves);
            } else if (piece instanceof BlackPawn) {
                ((BlackPawn) piece).checkOnlyAttackMoves(source, board, moves);
            } else {
                moves = piece.checkPossibleMoves(source, board);
            }
            return moves;
        }
        return null;
    }

    private ChessModelSquare[] checkIsKingUnderCheck(ColorOfPiece colorOfKing){
        ChessModelSquare king = getKingPosition(colorOfKing);
        List<ChessModelSquare> squaresAttackingKing = new ArrayList<>();
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                ChessPiece piece = chessBoard[i][j].getPiece();
                if(piece != null && piece.getColor() != colorOfKing){
                    List<ChessModelSquare> attackMoves;
                    attackMoves = returnPossibleAttackMoves(chessBoard[i][j], chessBoard);
                    assert attackMoves != null;
                    for (ChessModelSquare square: attackMoves) {
                        if(square == king){
                            squaresAttackingKing.add(chessBoard[i][j]);
                        }
                    }
                }
            }
        }
        return squaresAttackingKing.toArray(new ChessModelSquare[0]);
    }


    public ChessModelSquare getKingPosition(ColorOfPiece color){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(chessBoard[i][j].getPiece() != null){
                    if(chessBoard[i][j].getPiece() instanceof King && chessBoard[i][j].getPiece().getColor() == color)
                        return chessBoard[i][j];
                }
            }
        }
        return null;
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
