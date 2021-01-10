package Model;

import Pieces.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ChessModelBoard {
    ChessModelSquare[][] chessBoard;
    boolean hasGameFinished = false;
    boolean kingUnderCheck = false;
    boolean shortCastleHappened = false;
    boolean longCastleHappened = false;
    boolean enPassantHappened = false;
    boolean isStaleMate = false;
    List<GenericPiece> userWhiteDefinedPieces;
    List<GenericPiece> userBlackDefinedPieces;
    public ChessModelBoard(){
        chessBoard = new ChessModelSquare[8][8];
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                chessBoard[i][j] = new ChessModelSquare(i, j);
            }
        }
        userWhiteDefinedPieces = new ArrayList<>();
        userBlackDefinedPieces = new ArrayList<>();
    }

    public ChessModelSquare getChessModelSquare(int col, int row) {
        return chessBoard[row][col];
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
    public boolean didEnPassantHappened(){return enPassantHappened;}
    public void setEnPassantHappened(boolean enPassantHappened) {this.enPassantHappened = enPassantHappened; }
    public boolean isKingUnderCheck() {
        return kingUnderCheck;
    }
    public boolean isStaleMate() {
        return isStaleMate;
    }

    public GenericPiece getUserDefinedPiece(ColorOfPiece color, int i){
        if(color == ColorOfPiece.WHITE) {
            if(!userBlackDefinedPieces.isEmpty())
                return userWhiteDefinedPieces.get(i);
        }else{
            if(!userBlackDefinedPieces.isEmpty())
                return userBlackDefinedPieces.get(i);
        }
        return null;
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
            if(possibleMoves.size() == 0)
                return null;
            return possibleMoves;
        }
        possibleMoves = source.getPiece().checkPossibleMoves(source, chessBoard);
        possibleMoves = removeSquares(source, possibleMoves); //remove squares which lead to illegal moves
        if(source.getPiece() instanceof King && ((King)source.getPiece()).isCanCastle()){
            if(source.getPiece().getColor() == ColorOfPiece.WHITE) {
                checkIsCastleAvailable(possibleMoves, ColorOfPiece.WHITE);
            }else{
                checkIsCastleAvailable(possibleMoves, ColorOfPiece.BLACK);
            }
        }
        if(possibleMoves.size() == 0)
            return null;
        return possibleMoves;
    }

    public void makeMove(ChessModelSquare source, ChessModelSquare destination, ColorOfPiece colorOnMove) {
        destination.setPiece(source.getPiece());
        source.setPiece(null);
        if (kingUnderCheck) {
            kingUnderCheck = false; //otherwise end of game
            ChessModelSquare king = getKingPosition(colorOnMove);
            ((King) king.getPiece()).setChecked(false);
        }
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

        //segment for en passant variables
        clearAllEnPassant(colorOnMove);
        if(destination.getPiece() instanceof WhitePawn){
            if(destination.getY() == 4 && source.getY() == 6)
                ((WhitePawn) destination.getPiece()).setEnPassant(true);
        }
        if(destination.getPiece() instanceof BlackPawn){
            if(destination.getY() == 3 && source.getY() == 1)
                ((BlackPawn) destination.getPiece()).setEnPassant(true);
        }

        //segment for en passant moves
        if(makeEnPassantMove(destination)){
            enPassantHappened = true;
        }

        ColorOfPiece colorOfCheckedKing = colorOnMove == ColorOfPiece.WHITE ? ColorOfPiece.BLACK : ColorOfPiece.WHITE;
        ChessModelSquare[] squaresAttackingKing = checkIsKingUnderCheck(colorOfCheckedKing);
        if (squaresAttackingKing.length != 0){
            kingUnderCheck = true;
            ChessModelSquare king = getKingPosition(colorOfCheckedKing);
            ((King)king.getPiece()).setChecked(true);
            if(isKingCheckMated(colorOfCheckedKing)){
                hasGameFinished = true;
            }
        }
        if((!kingUnderCheck && isKingCheckMated(colorOfCheckedKing)) || (isNotEnoughMaterialToMate())){
            isStaleMate = true;
            hasGameFinished = true;
        }
    }

    private boolean isNotEnoughMaterialToMate(){
        boolean isOneAttackingWhitePiece = false;
        boolean isOneAttackingBlackPiece = false;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                ChessPiece piece = chessBoard[i][j].getPiece();
                if(piece != null && !(piece instanceof King)){
                    if(piece instanceof WhitePawn || piece instanceof BlackPawn)
                        return false;
                    if(isPieceKnight(chessBoard[i][j]) || isPieceBishop(chessBoard[i][j])) {
                        if (piece.getColor() == ColorOfPiece.WHITE) {
                            if (isOneAttackingWhitePiece) //we already had these kind of pieces -> checkmate possible
                                return false;
                            else
                                isOneAttackingWhitePiece = true;
                        }else{
                            if (isOneAttackingBlackPiece) //we already had these kind of pieces -> checkmate possible
                                return false;
                            else
                                isOneAttackingBlackPiece = true;
                        }
                    }
                    if(piece instanceof Rook)
                        return false;
                    if(isPieceQueen(chessBoard[i][j]))
                        return false;
                }
            }
        }
        return true;
    }

    private boolean isPieceQueen(ChessModelSquare square){
        if(square.getPiece() == null || !(square.getPiece() instanceof GenericPiece))
            return false;
        GenericPiece piece = (GenericPiece) square.getPiece();
        if(piece.isLeftDiagonal() && piece.isRightDiagonal() && piece.isLeft() &&
        piece.isRight() && piece.isUp() && piece.isDown())
            return true;
        return false;
    }

    private boolean isPieceBishop(ChessModelSquare square){
        if(square.getPiece() == null || !(square.getPiece() instanceof GenericPiece))
            return false;
        GenericPiece piece = (GenericPiece) square.getPiece();
        if(piece.isLeftDiagonal() && piece.isRightDiagonal())
            return true;
        return false;
    }

    private boolean isPieceKnight(ChessModelSquare square){
        if(square.getPiece() == null || !(square.getPiece() instanceof GenericPiece))
            return false;
        GenericPiece piece = (GenericPiece) square.getPiece();
        if(piece.getJumpMoves() != null)
            return true;
        return false;
    }

    private boolean isKingCheckMated(ColorOfPiece color){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(chessBoard[i][j].getPiece() != null && chessBoard[i][j].getPiece().getColor() == color){
                    List<ChessModelSquare> moves = getLegalPossibleMoves(chessBoard[i][j]);
                    if(moves != null)
                        return false;
                }
            }
        }
        return true;
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

    public void clearAll(){
        hasGameFinished = false;
        kingUnderCheck = false;
        shortCastleHappened = false;
        longCastleHappened = false;
        enPassantHappened = false;
        isStaleMate = false;
    }

    //Make move and check whether check occurred -> remove these squares
    private List<ChessModelSquare> removeSquares(ChessModelSquare source, List<ChessModelSquare> possibleMoves){
        List<ChessModelSquare> toRet = new ArrayList<>();
        for(ChessModelSquare destination: possibleMoves){
            //make move
            ChessPiece oldDestPiece = destination.getPiece();
            destination.setPiece(source.getPiece());
            source.setPiece(null);
            boolean wasEnPassantMade = makeEnPassantMove(destination);


            //check whether king is still checked
            ChessModelSquare[] checks;
            checks = checkIsKingUnderCheck(destination.getPiece().getColor());

            //undo move
            if(wasEnPassantMade) {
                undoEnPassant(destination);
            }
            source.setPiece(destination.getPiece());
            destination.setPiece(oldDestPiece);
            if(checks.length == 0){
                toRet.add(destination);
            }
        }
        return toRet;
    }

    private void undoEnPassant(ChessModelSquare destination){
        if(destination.getY() == 2){
            chessBoard[3][destination.getX()].setPiece(new BlackPawn());
            ((BlackPawn)chessBoard[3][destination.getX()].getPiece()).setEnPassant(true);
        }else{
            chessBoard[4][destination.getX()].setPiece(new WhitePawn());
            ((WhitePawn)chessBoard[4][destination.getX()].getPiece()).setEnPassant(true);
        }
    }

    private boolean makeEnPassantMove(ChessModelSquare destination) {
        if(destination.getPiece() instanceof WhitePawn && destination.getY() == 2 &&
                chessBoard[3][destination.getX()].getPiece() instanceof BlackPawn &&
                ((BlackPawn) chessBoard[3][destination.getX()].getPiece()).isEnPassant()) {
            chessBoard[3][destination.getX()].setPiece(null);
            return true;
        }
        if(destination.getPiece() instanceof BlackPawn && destination.getY() == 5 &&
                chessBoard[4][destination.getX()].getPiece() instanceof WhitePawn &&
                ((WhitePawn) chessBoard[4][destination.getX()].getPiece()).isEnPassant()){
            chessBoard[4][destination.getX()].setPiece(null);
            return true;
        }
        return false;
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

    public void makePromotion(ChessModelSquare promotionSquare, int i){
        ColorOfPiece color = promotionSquare.getPiece().getColor();
        switch(i){
            case 0:
                promotionSquare.setPiece(createQueen(color));
                break;
            case 1:
                promotionSquare.setPiece(new Rook(color));
                break;
            case 2:
                promotionSquare.setPiece(createBishop(color));
                break;
            case 3:
                promotionSquare.setPiece(createKnight(color));
                break;
        }
        ColorOfPiece kingColor = color == ColorOfPiece.WHITE ? ColorOfPiece.BLACK : ColorOfPiece.WHITE;
        ChessModelSquare[] squares = checkIsKingUnderCheck(kingColor);
        if(squares.length != 0){
            kingUnderCheck = true;
            ChessModelSquare king = getKingPosition(kingColor);
            ((King)king.getPiece()).setChecked(true);
        }
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

    public void createUserPiece(boolean[] variables, ChessModelSquare[] jumpMoves){
        userWhiteDefinedPieces.add(createColorIndependentUserPiece(ColorOfPiece.WHITE, variables, jumpMoves));
        userBlackDefinedPieces.add(createColorIndependentUserPiece(ColorOfPiece.BLACK, variables, jumpMoves));
    }

    private GenericPiece createColorIndependentUserPiece(ColorOfPiece color, boolean[] var, ChessModelSquare[] jumpMoves){
        GenericPiece userPiece = new GenericPiece(color);
        userPiece.setLeftDiagonal(var[0]);
        userPiece.setRightDiagonal(var[1]);
        userPiece.setLeft(var[2]);
        userPiece.setRight(var[3]);
        userPiece.setUp(var[4]);
        userPiece.setDown(var[5]);
        //originally piece was placed on ChessBoard[3][3] so lets calculate jump moves from that
        List<JumpMove> jumps = new ArrayList<>();
        for(ChessModelSquare square: jumpMoves){
            jumps.add(new JumpMove(square.getX() - 3, square.getY() - 3));
        }
        userPiece.setJumpMoves(jumps.toArray(new JumpMove[0]));
        return userPiece;
    }
}
