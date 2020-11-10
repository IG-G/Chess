package pw.proz;

import javax.swing.*;
import java.awt.*;

public class Square extends JButton {
    Color colorOfTheSquare;
    int x, y;
    public Square(){
        x = y = 0;
        setSize(50, 50);
    }
}
