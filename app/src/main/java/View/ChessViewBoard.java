package View;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;


//Class contains board parameters for GUI
//as well as type of piece on square (in class Square)
public class ChessViewBoard {

    final Color DEFAULT_COLOR_WHITE = Color.GRAY;
    final Color DEFAULT_COLOR_BLACK = Color.DARK_GRAY;
    final Color DEFAULT_COLOR_POSSIBLE_MOVE = Color.LIGHT_GRAY;

    ChessViewSquare[][] chessSquares; //representation of board 8x8
    Icon[] whitePiecesIcons, blackPiecesIcons;
    Color whiteSquareColor = DEFAULT_COLOR_WHITE;
    Color blackSquareColor = DEFAULT_COLOR_BLACK;
    Container frame;
    ChessViewSquare clickedSquare = null;

    public ChessViewBoard(Container frame) {
        this.frame = frame;
        chessSquares = new ChessViewSquare[8][8];
        whitePiecesIcons = new ImageIcon[6];
        blackPiecesIcons = new ImageIcon[6];
    }

    public ChessViewSquare getClickedSquare() {
        return clickedSquare;
    }

    public void setClickedSquare(ChessViewSquare clickedSquare) {
        this.clickedSquare = clickedSquare;
    }

    public ChessViewSquare[][] getChessSquares(){
        return chessSquares;
    }

    public Color getDEFAULT_COLOR_POSSIBLE_MOVE() {
        return DEFAULT_COLOR_POSSIBLE_MOVE;
    }

    public void initViewBoard(ActionListener listener) {
        loadDefaultIcons();

        boolean wasTheLastBlack = true;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                chessSquares[i][j] = new ChessViewSquare();
                initializeSquare(i, j, listener);
                setSquareColor(i, j, wasTheLastBlack);
                wasTheLastBlack = !wasTheLastBlack;
                addSquareToFrame(i, j);
            }
            wasTheLastBlack = !wasTheLastBlack;
        }
        setPiecesImageIcons();
        frame.setLayout(new GridLayout(8, 8));
    }

    private void addSquareToFrame(int i, int j){
        frame.add(chessSquares[i][j]);
    }

    private void setSquareColor(int i, int j, boolean wasTheLastBlack){
        Color squareColor = wasTheLastBlack ?  whiteSquareColor : blackSquareColor;
        chessSquares[i][j].setColorOfTheSquare(squareColor);
    }

    private void setPiecesImageIcons(){
        for(int i = 0; i < 8; i++){
            chessSquares[6][i].setIcon(whitePiecesIcons[0]);
            chessSquares[1][i].setIcon(blackPiecesIcons[0]);
        }
        //setting rooks
        chessSquares[0][0].setIcon(blackPiecesIcons[1]);
        chessSquares[0][7].setIcon(blackPiecesIcons[1]);
        chessSquares[7][0].setIcon(whitePiecesIcons[1]);
        chessSquares[7][7].setIcon(whitePiecesIcons[1]);
        //setting knights
        chessSquares[0][1].setIcon(blackPiecesIcons[2]);
        chessSquares[0][6].setIcon(blackPiecesIcons[2]);
        chessSquares[7][1].setIcon(whitePiecesIcons[2]);
        chessSquares[7][6].setIcon(whitePiecesIcons[2]);
        //setting bishops
        chessSquares[0][2].setIcon(blackPiecesIcons[3]);
        chessSquares[0][5].setIcon(blackPiecesIcons[3]);
        chessSquares[7][2].setIcon(whitePiecesIcons[3]);
        chessSquares[7][5].setIcon(whitePiecesIcons[3]);
        //setting queens
        chessSquares[0][3].setIcon(blackPiecesIcons[5]);
        chessSquares[7][3].setIcon(whitePiecesIcons[5]);
        //setting kings
        chessSquares[0][4].setIcon(blackPiecesIcons[4]);
        chessSquares[7][4].setIcon(whitePiecesIcons[4]);

    }

    private void initializeSquare(int i, int j, ActionListener listener) {

        int posX = chessSquares[i][j].posXInUI + 50 * (i);
        int posY = chessSquares[i][j].posYInUI+ 50 * (j);
        chessSquares[i][j].setBounds(posX, posY, 50, 50);
        chessSquares[i][j].setPosXonBoard(j);
        chessSquares[i][j].setPosYonBoard(i);
        chessSquares[i][j].addActionListener(listener);
    }

    private void loadDefaultIcons(){
        String runtimePath = ChessViewBoard.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        int i = runtimePath.lastIndexOf("/app/");
        String pathToDefaultIcons = runtimePath.substring(0, i);
        pathToDefaultIcons += "/app/images/";


        whitePiecesIcons[0] = new ImageIcon(pathToDefaultIcons + "pawn_white.png");
        whitePiecesIcons[1] = new ImageIcon(pathToDefaultIcons + "rook_white.png");
        whitePiecesIcons[2] = new ImageIcon(pathToDefaultIcons + "knight_white.png");
        whitePiecesIcons[3] = new ImageIcon(pathToDefaultIcons + "bishop_white.png");
        whitePiecesIcons[4] = new ImageIcon(pathToDefaultIcons + "king_white.png");
        whitePiecesIcons[5] = new ImageIcon(pathToDefaultIcons + "queen_white.png");

        blackPiecesIcons[0] = new ImageIcon(pathToDefaultIcons + "pawn_black.png");
        blackPiecesIcons[1] = new ImageIcon(pathToDefaultIcons + "rook_black.png");
        blackPiecesIcons[2] = new ImageIcon(pathToDefaultIcons + "knight_black.png");
        blackPiecesIcons[3] = new ImageIcon(pathToDefaultIcons + "bishop_black.png");
        blackPiecesIcons[4] = new ImageIcon(pathToDefaultIcons + "king_black.png");
        blackPiecesIcons[5] = new ImageIcon(pathToDefaultIcons + "queen_black.png");
    }
}
