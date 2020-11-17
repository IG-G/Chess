package pw.proz;

import javax.swing.*;
import java.awt.*;

//Class contains board parameters for GUI
//as well as type of piece on square (in class Square)
public class Board {
    Square[][] chessSquares; //representation of board 8x8
    PieceIcon[] whitePiecesIcons, blackPiecesIcons;
    Color whiteSquareColor, blackSquareColor;
    Frame frame;

    final String PATH_NAME_DEFAULT_ICONS = "C:\\Users\\IG\\Desktop\\prog\\PROZ\\PROZ-template\\app\\images\\";
    final Color DEFAULT_COLOR_WHITE = Color.GRAY;
    final Color DEFAULT_COLOR_BLACK = Color.BLACK;

    public Board(JFrame frame){
        this.frame = frame;
        chessSquares = new Square[8][8];
        whiteSquareColor = DEFAULT_COLOR_WHITE;
        blackSquareColor = DEFAULT_COLOR_BLACK;
        whitePiecesIcons = new PieceIcon[6];
        blackPiecesIcons = new PieceIcon[6];

        loadDefaultIcons();

        boolean wasTheLastBlack = true;
        for(int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                chessSquares[i][j] = new Square();
                initializeSquare(i, j);
                setSquareColor(i, j, wasTheLastBlack);
                wasTheLastBlack = !wasTheLastBlack;
                setPieceImageIcon(i, j);
                addSquareToFrame(i, j);
            }
            wasTheLastBlack = !wasTheLastBlack;
        }
    }

    private void addSquareToFrame(int i, int j){
        frame.add(chessSquares[i][j]);
    }

    private void setSquareColor(int i, int j, boolean wasTheLastBlack) {
        Color squareColor = wasTheLastBlack ?  whiteSquareColor : blackSquareColor;
        chessSquares[i][j].setColorOfTheSquare(squareColor);
    }

    private void setPieceImageIcon(int i, int j){
        ImageIcon icon = new ImageIcon(PATH_NAME_DEFAULT_ICONS + "pawn_white.png");
        chessSquares[i][j].setIcon(icon);
    }

    private void initializeSquare(int i, int j){
        int posX = chessSquares[i][j].posXInUI + 50 * (i);
        int posY = chessSquares[i][j].posYInUI+ 50 * (j);
        chessSquares[i][j].setBounds(posX, posY, 50, 50);
    }

    private void loadDefaultIcons(){
        whitePiecesIcons[0] = new PieceIcon("pawn", new ImageIcon(PATH_NAME_DEFAULT_ICONS + "pawn_white.png"));
        blackPiecesIcons[0] = new PieceIcon("pawn", new ImageIcon(PATH_NAME_DEFAULT_ICONS + "pawn_black.png"));
        whitePiecesIcons[1] = new PieceIcon("rook", new ImageIcon(PATH_NAME_DEFAULT_ICONS + "rook_white.png"));
        blackPiecesIcons[1] = new PieceIcon("rook", new ImageIcon(PATH_NAME_DEFAULT_ICONS + "rook_black.png"));
        whitePiecesIcons[2] = new PieceIcon("knight", new ImageIcon(PATH_NAME_DEFAULT_ICONS + "knight_white.png"));
        blackPiecesIcons[2] = new PieceIcon("knight", new ImageIcon(PATH_NAME_DEFAULT_ICONS + "knight_black.png"));
        whitePiecesIcons[3] = new PieceIcon("bishop", new ImageIcon(PATH_NAME_DEFAULT_ICONS + "bishop_white.png"));
        blackPiecesIcons[3] = new PieceIcon("bishop", new ImageIcon(PATH_NAME_DEFAULT_ICONS + "bishop_black.png"));
        whitePiecesIcons[4] = new PieceIcon("queen", new ImageIcon(PATH_NAME_DEFAULT_ICONS + "queen_white.png"));
        blackPiecesIcons[4] = new PieceIcon("queen", new ImageIcon(PATH_NAME_DEFAULT_ICONS + "queen_black.png"));
        whitePiecesIcons[5] = new PieceIcon("king", new ImageIcon(PATH_NAME_DEFAULT_ICONS + "king_white.png"));
        blackPiecesIcons[5] = new PieceIcon("king", new ImageIcon(PATH_NAME_DEFAULT_ICONS + "king_black.png"));
    }
}
