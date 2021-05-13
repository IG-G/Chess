package View;

import Pieces.ColorOfPiece;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ChessViewBoard {

    final Color DEFAULT_COLOR_WHITE = Color.GRAY;
    final Color DEFAULT_COLOR_BLACK = Color.DARK_GRAY;
    final Color DEFAULT_COLOR_POSSIBLE_MOVE = Color.LIGHT_GRAY;
    final Color DEFAULT_COLOR_CHECK_SQUARE = Color.ORANGE;

    ChessViewSquare[][] chessSquares; //representation of board 8x8
    Icon[] whitePiecesIcons, blackPiecesIcons;
    Color whiteSquareColor = DEFAULT_COLOR_WHITE;
    Color blackSquareColor = DEFAULT_COLOR_BLACK;
    Color colorPossibleMove = DEFAULT_COLOR_POSSIBLE_MOVE;
    Container frame;

    ChessViewSquare[] possibleMovesSquares = null;
    ChessViewSquare kingUnderCheck;


    public ChessViewBoard(Container frame) {
        this.frame = frame;
        chessSquares = new ChessViewSquare[8][8];
        whitePiecesIcons = new ImageIcon[6];
        blackPiecesIcons = new ImageIcon[6];
    }

    public Icon[] getBlackPiecesIcons() {
        return blackPiecesIcons;
    }

    public Icon[] getWhitePiecesIcons() {
        return whitePiecesIcons;
    }

    public ChessViewSquare getChessViewSquare(int row, int col) {
        return chessSquares[row][col];
    }

    public Color getWhiteSquareColor() {
        return whiteSquareColor;
    }

    public Color getBlackSquareColor() {
        return blackSquareColor;
    }

    public void setWhiteSquareColor(Color white) {
        whiteSquareColor = white;
        updateColorOfSquares(ColorOfSquare.WHITE, white);
    }

    public void setBlackSquareColor(Color black) {
        blackSquareColor = black;
        updateColorOfSquares(ColorOfSquare.BLACK, black);
    }

    public Color getColorPossibleMove() {
        return colorPossibleMove;
    }

    public void setColorPossibleMove(Color colorPossibleMove) {
        this.colorPossibleMove = colorPossibleMove;
    }

    public void resetToDefaultColor(ColorOfSquare color) {
        if (color == ColorOfSquare.WHITE) {
            whiteSquareColor = DEFAULT_COLOR_WHITE;
            updateColorOfSquares(ColorOfSquare.WHITE, DEFAULT_COLOR_WHITE);
        } else {
            blackSquareColor = DEFAULT_COLOR_BLACK;
            updateColorOfSquares(ColorOfSquare.BLACK, DEFAULT_COLOR_BLACK);
        }
    }

    public void setKingUnderCheck(int row, int col) {
        chessSquares[row][col].setBackground(DEFAULT_COLOR_CHECK_SQUARE);
        kingUnderCheck = chessSquares[row][col];
    }

    private void updateColorOfSquares(ColorOfSquare color, Color newColor) {
        for (ChessViewSquare[] i : chessSquares) {
            for (ChessViewSquare j : i) {
                if (j.getColorOfSquare() == color)
                    j.setBackground(newColor);
            }
        }
    }

    public void setPossibleMovesSquares(ChessViewSquare[] possibleMovesSquares) {
        this.possibleMovesSquares = possibleMovesSquares;
        for (ChessViewSquare square : possibleMovesSquares) {
            square.setDisplayingColorOfSquare(colorPossibleMove);
        }
    }

    public void cleanPossibleMovesSquares() {
        for (ChessViewSquare square : possibleMovesSquares) {
            if (square.getColorOfSquare() == ColorOfSquare.WHITE)
                square.setDisplayingColorOfSquare(whiteSquareColor);
            else
                square.setDisplayingColorOfSquare(blackSquareColor);
        }
        possibleMovesSquares = null;
    }

    public void makeMove(int sourceRow, int sourceCol, int destRow, int destCol) {
        ChessViewSquare viewSquareSource = getChessViewSquare(sourceRow, sourceCol);
        ChessViewSquare viewSquareDest = getChessViewSquare(destRow, destCol);
        viewSquareDest.setPieceIcon(viewSquareSource.getIcon());
        viewSquareSource.setPieceIcon(null);
    }

    public int makePromotion(ChessViewSquare promotionSquare) {
        Object[] options = {
                "Queen",
                "Rook",
                "Bishop",
                "Knight"};
        int chosenOption;
        if (promotionSquare.getPosYOnBoard() == 0)
            chosenOption = makeColorPromotion(promotionSquare, options, whitePiecesIcons);
        else
            chosenOption = makeColorPromotion(promotionSquare, options, blackPiecesIcons);
        return chosenOption;
    }

    public int makeColorPromotion(ChessViewSquare promotionSquare, Object[] options, Icon[] icons) {

        int chosenOption = JOptionPane.showOptionDialog(frame,
                "Promote your pawn to:",
                "Promotion",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                icons[0],
                options,
                options[0]);
        switch (chosenOption) {
            case 0:
                promotionSquare.setPieceIcon(icons[5]);
                break;
            case 1:
                promotionSquare.setPieceIcon(icons[1]);
                break;
            case 2:
                promotionSquare.setPieceIcon(icons[3]);
                break;
            case 3:
                promotionSquare.setPieceIcon(icons[2]);
                break;
        }
        return chosenOption;
    }

    public void cleanKingUnderCheck() {
        if (kingUnderCheck == null)
            return;
        if (kingUnderCheck.getColorOfSquare() == ColorOfSquare.BLACK)
            kingUnderCheck.setBackground(blackSquareColor);
        else
            kingUnderCheck.setBackground(whiteSquareColor);
        kingUnderCheck = null;
    }

    public void showEndGameInfo(ColorOfPiece winningColor) {
        JOptionPane.showMessageDialog(frame,
                "CHECK MATE!\n" + winningColor + " is the winner!");
    }

    public void showTieGameInfo() {
        JOptionPane.showMessageDialog(frame, "THE GAME IS TIED");
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

    public void clearPieces() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                chessSquares[i][j].setPieceIcon(null);
            }
        }
    }

    private void addSquareToFrame(int i, int j) {
        frame.add(chessSquares[i][j]);
    }

    private void setSquareColor(int i, int j, boolean wasTheLastBlack) {
        Color squareColor = wasTheLastBlack ? whiteSquareColor : blackSquareColor;
        chessSquares[i][j].setDisplayingColorOfSquare(squareColor);
        chessSquares[i][j].setColorOfSquare(wasTheLastBlack ? ColorOfSquare.WHITE : ColorOfSquare.BLACK);
    }

    private void setPiecesImageIcons() {
        for (int i = 0; i < 8; i++) {
            chessSquares[6][i].setPieceIcon(whitePiecesIcons[0]);
            chessSquares[1][i].setPieceIcon(blackPiecesIcons[0]);
        }
        //setting rooks
        chessSquares[0][0].setPieceIcon(blackPiecesIcons[1]);
        chessSquares[0][7].setPieceIcon(blackPiecesIcons[1]);
        chessSquares[7][0].setPieceIcon(whitePiecesIcons[1]);
        chessSquares[7][7].setPieceIcon(whitePiecesIcons[1]);
        //setting knights
        chessSquares[0][1].setPieceIcon(blackPiecesIcons[2]);
        chessSquares[0][6].setPieceIcon(blackPiecesIcons[2]);
        chessSquares[7][1].setPieceIcon(whitePiecesIcons[2]);
        chessSquares[7][6].setPieceIcon(whitePiecesIcons[2]);
        //setting bishops
        chessSquares[0][2].setPieceIcon(blackPiecesIcons[3]);
        chessSquares[0][5].setPieceIcon(blackPiecesIcons[3]);
        chessSquares[7][2].setPieceIcon(whitePiecesIcons[3]);
        chessSquares[7][5].setPieceIcon(whitePiecesIcons[3]);
        //setting queens
        chessSquares[0][3].setPieceIcon(blackPiecesIcons[5]);
        chessSquares[7][3].setPieceIcon(whitePiecesIcons[5]);
        //setting kings
        chessSquares[0][4].setPieceIcon(blackPiecesIcons[4]);
        chessSquares[7][4].setPieceIcon(whitePiecesIcons[4]);

    }

    private void initializeSquare(int i, int j, ActionListener listener) {

        int posX = chessSquares[i][j].posXInUI + 50 * (i);
        int posY = chessSquares[i][j].posYInUI + 50 * (j);
        chessSquares[i][j].setBounds(posX, posY, 50, 50);
        chessSquares[i][j].setPosXonBoard(j);
        chessSquares[i][j].setPosYonBoard(i);
        chessSquares[i][j].addActionListener(listener);
    }

    private void loadDefaultIcons() {
        loadIcons("DefaultPieces");
    }

    public void updateIcons(String dir) {
        Icon[] prevWhite = new Icon[6];
        Icon[] prevBlack = new Icon[6];
        System.arraycopy(whitePiecesIcons, 0, prevWhite, 0, 6);
        System.arraycopy(blackPiecesIcons, 0, prevBlack, 0, 6);
        loadIcons(dir);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (chessSquares[i][j].getImageIcon() != null) {
                    for (int k = 0; k < 6; k++) {
                        if (chessSquares[i][j].getImageIcon().equals(prevWhite[k])) {
                            chessSquares[i][j].setPieceIcon(whitePiecesIcons[k]);
                            break;
                        }
                        if (chessSquares[i][j].getImageIcon().equals(prevBlack[k])) {
                            chessSquares[i][j].setPieceIcon(blackPiecesIcons[k]);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void loadIcons(String directory) {

        whitePiecesIcons[0] = new ImageIcon(this.getClass().getResource("/" + directory + "/pawn_white.png"));
        whitePiecesIcons[1] = new ImageIcon(this.getClass().getResource("/" + directory + "/rook_white.png"));
        whitePiecesIcons[2] = new ImageIcon(this.getClass().getResource("/" + directory + "/knight_white.png"));
        whitePiecesIcons[3] = new ImageIcon(this.getClass().getResource("/" + directory + "/bishop_white.png"));
        whitePiecesIcons[4] = new ImageIcon(this.getClass().getResource("/" + directory + "/king_white.png"));
        whitePiecesIcons[5] = new ImageIcon(this.getClass().getResource("/" + directory + "/queen_white.png"));

        blackPiecesIcons[0] = new ImageIcon(this.getClass().getResource("/" + directory + "/pawn_black.png"));
        blackPiecesIcons[1] = new ImageIcon(this.getClass().getResource("/" + directory + "/rook_black.png"));
        blackPiecesIcons[2] = new ImageIcon(this.getClass().getResource("/" + directory + "/knight_black.png"));
        blackPiecesIcons[3] = new ImageIcon(this.getClass().getResource("/" + directory + "/bishop_black.png"));
        blackPiecesIcons[4] = new ImageIcon(this.getClass().getResource("/" + directory + "/king_black.png"));
        blackPiecesIcons[5] = new ImageIcon(this.getClass().getResource("/" + directory + "/queen_black.png"));
    }
}
