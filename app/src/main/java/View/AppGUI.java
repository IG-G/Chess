package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AppGUI {
    JFrame mainFrame;
    ChessViewBoard view;

    public AppGUI(JFrame frame, ChessViewBoard view){
        mainFrame = frame;
        this.view = view;
    }

    public void initMainFrame(ActionListener listener){
        if(mainFrame.getSize().height == 0 || mainFrame.getSize().width == 0)
            setMainFrameSize(700, 700);
        setMenuBar(listener);
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public Component getFrame(){
        return mainFrame;
    }

    public void setMainFrameSize(int x, int y){
        mainFrame.setSize(x, y);
    }

    private void setMenuBar(ActionListener listener) {
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        JMenu viewMenu = new JMenu("View");
        JMenu pieceMenu = new JMenu("Custom Piece");

        JMenuItem newGameOption = new JMenuItem("Start new game");
        newGameOption.addActionListener(listener);
        gameMenu.add(newGameOption);
        JMenuItem saveGameIntoPGN = new JMenuItem("Save PGN");
        gameMenu.add(saveGameIntoPGN);
        menuBar.add(gameMenu);

        JMenuItem changeBoardColorOption = new JMenuItem("Change color of the board");
        viewMenu.add(changeBoardColorOption);
        changeBoardColorOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color newWhiteColor = JColorChooser.showDialog(mainFrame, "Choose white square color", Color.LIGHT_GRAY);
                if (newWhiteColor != null)
                    view.setWhiteSquareColor(newWhiteColor);
            }
        });
        JMenuItem changePieceIconsOption = new JMenuItem("Change pieces");
        viewMenu.add(changePieceIconsOption);
        menuBar.add(viewMenu);

        JMenuItem createNewPiece = new JMenuItem("Create new piece");
        pieceMenu.add(createNewPiece);
        menuBar.add(pieceMenu);
        createNewPiece.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createFrame();
            }
        });

        mainFrame.setJMenuBar(menuBar);
    }

    public static void createFrame()
    {
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                JFrame frame = new JFrame("Test");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                try
                {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                frame.setVisible(true);
            }
        });
    }

}
