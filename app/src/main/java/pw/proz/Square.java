package pw.proz;

import javax.swing.*;
import java.awt.*;

public class Square extends JButton {
    Color colorOfTheSquare;
    Icon pieceIcon;
    int posXInUI, posYInUI;
    //Piece pieceOnThatSquare;
    public Square(){
        posXInUI = 0;
        posYInUI = 0;
        setSize(50, 50);
    }
    public void setColorOfTheSquare(Color color) {
        colorOfTheSquare = color;
        setBackground(color);
    }

    public void setPieceIcon(Icon icon){

    }
}
