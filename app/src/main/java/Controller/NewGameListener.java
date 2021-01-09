package Controller;

import Pieces.ColorOfPiece;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewGameListener implements ActionListener {
    final ChessGameController controller;
    final Component frame;
    boolean isBotSelected = false;
    boolean isWhiteSelected = false;

    NewGameListener(ChessGameController controller, Component frame){
        this.controller = controller;
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JRadioButton botOption = new JRadioButton("Bot");
        botOption.setSelected(true);
        JRadioButton humanOption = new JRadioButton("Player");
        ButtonGroup opponentOption = new ButtonGroup();
        opponentOption.add(botOption);
        opponentOption.add(humanOption);

        JRadioButton white = new JRadioButton("White");
        white.setSelected(true);
        JRadioButton black = new JRadioButton("Black");
        ButtonGroup colorOption = new ButtonGroup();
        colorOption.add(white);
        colorOption.add(black);

        humanOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    white.setVisible(false);
                    black.setVisible(false);
            }
        });
        botOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                white.setVisible(true);
                black.setVisible(true);
                isBotSelected = !isBotSelected;
                isWhiteSelected = white.isSelected();
            }
        });

        Object[] option = {botOption, humanOption, white, black, "Start"};
        JOptionPane.showOptionDialog(frame,
                "Choose opponent and your piece color:",
                "Game settings",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                option,
                option[0]);

        if(isBotSelected){
            if(isWhiteSelected)
                controller.startNewGameWithBot(ColorOfPiece.WHITE);
            else
                controller.startNewGameWithBot(ColorOfPiece.BLACK);
        }
        controller.startNewGame();
    }
}
