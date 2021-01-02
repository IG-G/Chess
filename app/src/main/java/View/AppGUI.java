package View;

import javax.swing.*;

public class AppGUI {
    JFrame mainFrame;

    public AppGUI(JFrame frame){
        mainFrame = frame;
    }

    public void initMainFrame(){
        if(mainFrame.getSize().height == 0 || mainFrame.getSize().width == 0)
            setMainFrameSize(700, 700);
        setMenuBar();
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void setMainFrameSize(int x, int y){
        mainFrame.setSize(x, y);
    }

    private void setMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        JMenu viewMenu = new JMenu("View");

        JMenuItem newGameOption = new JMenuItem("Start new game");
        gameMenu.add(newGameOption);
        JMenuItem saveGameIntoPGN = new JMenuItem("Save PGN");
        gameMenu.add(saveGameIntoPGN);
        menuBar.add(gameMenu);

        JMenuItem changeBoardColorOption = new JMenuItem("Change color of the board");
        viewMenu.add(changeBoardColorOption);
        JMenuItem changePieceIconsOption = new JMenuItem("Change pieces");
        viewMenu.add(changePieceIconsOption);
        menuBar.add(viewMenu);

        mainFrame.setJMenuBar(menuBar);
    }
}
