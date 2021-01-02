package View;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;

public class ChessViewSquare extends JButton {
    Color colorOfTheSquare;
    Icon pieceIcon;
    int posXInUI, posYInUI;
    int posXOnBoard, posYOnBoard;
    public ChessViewSquare() {
        setUI(new BasicButtonUI()); //to clear on-click effects
        posXInUI = 0;
        posYInUI = 0;
        setSize(50, 50);
        pieceIcon = null;
        setBorderPainted(false);
        setFocusPainted(false);
    }

    public void setPosXonBoard(int posXonBoard) {
        this.posXOnBoard = posXonBoard;
    }

    public void setPosYonBoard(int posYonBoard) {
        this.posYOnBoard = posYonBoard;
    }

    public int getPosXOnBoard() {
        return posXOnBoard;
    }

    public int getPosYOnBoard() {
        return posYOnBoard;
    }

    public void setColorOfTheSquare(Color color) {
        colorOfTheSquare = color;
        setBackground(color);
    }

    public void setPieceIcon(Icon icon) {
        pieceIcon = icon;
    }
    public Icon getImageIcon() {
        return pieceIcon;
    }
}
