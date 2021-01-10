package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class AppGUI {
    JFrame mainFrame;
    ChessViewBoard view;
    List<ChessViewSquare> possibleMovesForNewPiece = null;
    boolean[] paramsForNewPiece;
    JFrame newPieceFrame;

    public JFrame getNewPieceFrame() {
        return newPieceFrame;
    }

    public boolean[] getParamsForNewPiece() {
        return paramsForNewPiece;
    }

    public ChessViewSquare[] getPossibleMovesForNewPiece() {
        return possibleMovesForNewPiece.toArray(new ChessViewSquare[0]);
    }

    public AppGUI(JFrame frame, ChessViewBoard view){
        mainFrame = frame;
        this.view = view;
        paramsForNewPiece = new boolean[6];
        for(int i = 0; i < 6; i++)
            paramsForNewPiece[i] = false;
    }

    public void initMainFrame(ActionListener newGameListener, ActionListener newPieceListener, ActionListener createCustomGame){
        if(mainFrame.getSize().height == 0 || mainFrame.getSize().width == 0)
            setMainFrameSize(700, 700);
        setMenuBar(newGameListener, newPieceListener, createCustomGame);
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public Component getFrame(){
        return mainFrame;
    }

    public void setMainFrameSize(int x, int y){
        mainFrame.setSize(x, y);
    }

    private void setMenuBar(ActionListener newGameListener, ActionListener newPieceListener, ActionListener createCustomGame) {
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        JMenu viewMenu = new JMenu("View");
        JMenu pieceMenu = new JMenu("Custom Piece");

        JMenuItem newGameOption = new JMenuItem("Start new game");
        newGameOption.addActionListener(newGameListener);
        gameMenu.add(newGameOption);
        JMenuItem saveGameIntoPGN = new JMenuItem("Save PGN");
        gameMenu.add(saveGameIntoPGN);
        menuBar.add(gameMenu);

        JMenuItem changeWhiteSquareOption = new JMenuItem("Change color of the white squares");
        viewMenu.add(changeWhiteSquareOption);
        changeWhiteSquareOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color newWhiteColor = JColorChooser.showDialog(mainFrame, "Choose white square color", Color.GRAY);
                if (newWhiteColor != null)
                    view.setWhiteSquareColor(newWhiteColor);
            }
        });

        JMenuItem changeBlackSquareOption = new JMenuItem("Change color of the black squares");
        viewMenu.add(changeBlackSquareOption);
        changeBlackSquareOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color newBlackColor = JColorChooser.showDialog(mainFrame, "Choose black square color", Color.DARK_GRAY);
                if (newBlackColor != null)
                    view.setBlackSquareColor(newBlackColor);
            }
        });

        JMenuItem changeSquareColorToDefault = new JMenuItem("Change color of the squares to default");
        viewMenu.add(changeSquareColorToDefault);
        changeSquareColorToDefault.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.resetToDefaultColor(ColorOfSquare.WHITE);
                view.resetToDefaultColor(ColorOfSquare.BLACK);
            }
        });

        JMenuItem changePieceIconsOption = new JMenuItem("Change pieces");
        viewMenu.add(changePieceIconsOption);
        menuBar.add(viewMenu);
        changePieceIconsOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] options = {"Default Pieces", "Another Pieces"};
                int chosenOption = JOptionPane.showOptionDialog(mainFrame,
                        "Choose icons for pieces:",
                        "Piece icons",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]);
                if(chosenOption == 0)
                    view.updateIcons("DefaultPieces");
                else
                    view.updateIcons("NotReallyGoodPieces");
            }
        });

        JMenuItem createNewPiece = new JMenuItem("Create new piece");
        pieceMenu.add(createNewPiece);
        menuBar.add(pieceMenu);
        createNewPiece.addActionListener(newPieceListener);

        JMenuItem createCustom = new JMenuItem("Create custom Game");
        pieceMenu.add(createCustom);
        createCustom.addActionListener(createCustomGame);

        mainFrame.setJMenuBar(menuBar);
    }

    public void createFrameForNewPiece(ActionListener endListener) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("New piece");
                newPieceFrame = frame;
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                JPanel board = new JPanel();
                JRadioButton leftDiagonal = new JRadioButton("Left Diagonal");
                JRadioButton rightDiagonal = new JRadioButton("Right Diagonal");
                JRadioButton up = new JRadioButton("Up");
                JRadioButton down = new JRadioButton("Down");
                JRadioButton left = new JRadioButton("Left");
                JRadioButton right = new JRadioButton("Right");
                JLabel note = new JLabel(" All radio buttons create moves ");
                JLabel note1 = new JLabel(" from one border to another. ");
                JLabel note2 = new JLabel(" These are not jump moves. ");
                JLabel note3 = new JLabel(" Moves selected on board ");
                JLabel note4 = new JLabel(" are jump moves. ");
                JButton end = new JButton("Finish");
                end.addActionListener(endListener);
                JPanel settings = new JPanel();
                settings.add(leftDiagonal);
                settings.add(rightDiagonal);
                settings.add(up);
                settings.add(down);
                settings.add(left);
                settings.add(right);
                settings.add(note);
                settings.add(note1);
                settings.add(note2);
                settings.add(note3);
                settings.add(note4);
                settings.add(end);
                settings.setLayout(new BoxLayout(settings, BoxLayout.Y_AXIS));
                try
                {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                possibleMovesForNewPiece = new ArrayList<>();
                ChessViewBoard boardForNewPiece = new ChessViewBoard(board);
                boardForNewPiece.initViewBoard(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ChessViewSquare source = (ChessViewSquare) e.getSource();
                        for(ChessViewSquare square: possibleMovesForNewPiece) {
                            if (source == square) {
                                Color color = source.getColorOfSquare() == ColorOfSquare.WHITE ?
                                        boardForNewPiece.whiteSquareColor : boardForNewPiece.blackSquareColor;
                                source.setBackground(color);
                                possibleMovesForNewPiece.remove(source);
                                return;
                            }
                        }
                        if(source == boardForNewPiece.getChessViewSquare(3,3))
                            return;
                        source.setBackground(boardForNewPiece.colorPossibleMove);
                        possibleMovesForNewPiece.add(source);
                    }
                });
                boardForNewPiece.clearPieces();
                boardForNewPiece.getChessViewSquare(3,3).setPieceIcon(boardForNewPiece.whitePiecesIcons[5]);

                frame.add(settings, BorderLayout.EAST);
                frame.add(board, BorderLayout.CENTER);
                frame.pack();
                frame.setLocationByPlatform(true);
                frame.setVisible(true);
                frame.setResizable(false);
                frame.setSize(850, 700);

                leftDiagonal.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        paramsForNewPiece[0] = !paramsForNewPiece[0];
                    }
                });
                rightDiagonal.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        paramsForNewPiece[1] = !paramsForNewPiece[1];
                    }
                });
                left.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        paramsForNewPiece[2] = !paramsForNewPiece[2];
                    }
                });
                right.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        paramsForNewPiece[3] = !paramsForNewPiece[3];
                    }
                });
                up.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        paramsForNewPiece[4] = !paramsForNewPiece[4];
                    }
                });
                down.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        paramsForNewPiece[5] = !paramsForNewPiece[5];
                    }
                });
            }
        });
    }

}
