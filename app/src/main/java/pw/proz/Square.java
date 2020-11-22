package pw.proz;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;

public class Square extends JButton {
    Color colorOfTheSquare;
    Icon pieceIcon;
    int posXInUI, posYInUI;
    //Piece pieceOnThatSquare; in prep
    public Square() {
        setUI(new BasicButtonUI()); //to clear effects on click
        posXInUI = 0;
        posYInUI = 0;
        setSize(50, 50);
        pieceIcon = null;
        setBorderPainted(false);
        setFocusPainted(false);
    }

    public void setColorOfTheSquare(Color color) {
        colorOfTheSquare = color;
        setBackground(color);
    }

    public void setPieceIcon(Icon icon){
        pieceIcon = icon;
    }
    public Icon getImageIcon(){
        return pieceIcon;
    }
}
