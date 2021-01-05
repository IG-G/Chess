package Pieces;

import Model.ChessModelSquare;

import java.util.ArrayList;
import java.util.List;

public class GenericPiece implements ChessPiece{
    ColorOfPiece color;
    private boolean leftDiagonal = false;
    private boolean rightDiagonal = false;
    private boolean down = false;
    private boolean up = false;
    private boolean left = false;
    private boolean right = false;

    JumpMove[] jumpMoves;


    public GenericPiece(ColorOfPiece color) {
        this.color = color;
    }

    @Override
    public ColorOfPiece getColor(){
        return color;
    }


    @Override
    public List<ChessModelSquare> checkPossibleMoves(ChessModelSquare square, ChessModelSquare[][] board) {
        List<ChessModelSquare> possibleMoves = new ArrayList<ChessModelSquare>();
        if(down) {
            checkMovesDown(square, board, possibleMoves);
        }
        if(up) {
            checkMovesUp(square, board, possibleMoves);
        }
        if(left) {
            checkMovesLeft(square, board, possibleMoves);
        }
        if(right) {
            checkMovesRight(square, board, possibleMoves);
        }
        if(leftDiagonal){
            for(int i = square.getX() + 1, j = square.getY() + 1; i < 8 && j < 8; i++, j++)
                if (board[j][i].getPiece() == null)
                    possibleMoves.add(board[j][i]);
                else {
                    if(board[j][i].getPiece().getColor() != getColor())
                        possibleMoves.add(board[j][i]);
                    break;
                }
            for(int i = square.getX() - 1, j = square.getY() - 1; i >= 0 && j >= 0; i--, j--)
                if(board[j][i].getPiece() == null)
                    possibleMoves.add(board[j][i]);
                else {
                    if (board[j][i].getPiece().getColor() != getColor())
                        possibleMoves.add(board[j][i]);
                    break;
                }
        }
        if(rightDiagonal){
            for(int i = square.getX() - 1, j = square.getY() + 1; i >= 0 && j < 8; i--, j++)
                if (board[j][i].getPiece() == null)
                    possibleMoves.add(board[j][i]);
                else{
                    if (board[j][i].getPiece().getColor() != getColor())
                        possibleMoves.add(board[j][i]);
                    break;
                }
            for(int i = square.getX() + 1, j = square.getY() - 1; i < 8 && j >= 0; i++, j--)
                if(board[j][i].getPiece() == null)
                    possibleMoves.add(board[j][i]);
                else {
                    if(board[j][i].getPiece().getColor() != getColor())
                        possibleMoves.add(board[j][i]);
                    break;
                }
        }

        if(jumpMoves != null) {
            for(JumpMove itr: jumpMoves){
                if(square.getY() + itr.shiftY > 7 || square.getY() + itr.shiftY < 0 || square.getX() + itr.shiftX < 0
                || square.getX() + itr.shiftX > 7)
                    continue;
                if(board[square.getY() + itr.shiftY][square.getX() + itr.shiftX].getPiece() == null)
                    possibleMoves.add(board[square.getY() + itr.shiftY][square.getX() + itr.shiftX]);
                if(board[square.getY() + itr.shiftY][square.getX() + itr.shiftX].getPiece() != null)
                    if(board[square.getY() + itr.shiftY][square.getX() + itr.shiftX].getPiece().getColor() != color)
                        possibleMoves.add(board[square.getY() + itr.shiftY][square.getX() + itr.shiftX]);
            }
        }

        return possibleMoves;
    }

    public void setJumpMoves(JumpMove[] jumpMoves) {
        this.jumpMoves = jumpMoves;
    }

    public boolean isLeftDiagonal() {
        return leftDiagonal;
    }

    public void setLeftDiagonal(boolean leftDiagonal) {
        this.leftDiagonal = leftDiagonal;
    }

    public boolean isRightDiagonal() {
        return rightDiagonal;
    }

    public void setRightDiagonal(boolean rightDiagonal) {
        this.rightDiagonal = rightDiagonal;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }
}
