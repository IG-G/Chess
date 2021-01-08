/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package pw.proz;

import Controller.ChessGameController;
import Model.ChessModelBoard;
import View.AppGUI;
import View.ChessViewBoard;

import javax.swing.*;


public class App {
    public static void main(String[] args) {

        JFrame frame = new JFrame("Szachy");


        ChessViewBoard boardView = new ChessViewBoard(frame.getContentPane());
        ChessModelBoard boardModel = new ChessModelBoard();
        AppGUI mainFrame = new AppGUI(frame, boardView);
        ChessGameController gameController = new ChessGameController(boardModel, boardView, mainFrame);

        gameController.initGUI();
        gameController.startNewGame();
    }
}