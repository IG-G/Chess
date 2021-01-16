package Controller;

import Model.ChessModelBoard;
import Model.ChessModelSquare;
import Pieces.ColorOfPiece;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ChessBot {
    ChessModelBoard model;
    ChessGameController controller;
    ColorOfPiece colorOnMove;

    public ChessBot(ChessModelBoard chessModelBoard, ChessGameController chessGameController) {
        model = chessModelBoard;
        controller = chessGameController;
        colorOnMove = ColorOfPiece.WHITE;
    }

    public void makeBotMove() {
        ColorOfPiece humanColor = controller.humanColor;
        ColorOfPiece botColor = humanColor == ColorOfPiece.WHITE ? ColorOfPiece.BLACK : ColorOfPiece.WHITE;
        List<List<ChessModelSquare>> possibleMoves = new ArrayList<>();
        int tailIndex = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessModelSquare square = model.getChessModelSquare(i, j);
                if (square.getPiece() != null && square.getPiece().getColor() == botColor) {
                    if (controller.selectSquaresToMove(square)) {
                        possibleMoves.add(new ArrayList<>());
                        controller.boardView.cleanPossibleMovesSquares();
                        possibleMoves.get(tailIndex).add(square);
                        possibleMoves.get(tailIndex).addAll(Arrays.<ChessModelSquare>asList(controller.previousPossibleMoves));
                        tailIndex++;
                    }
                }
            }
        }
        Random rand = new Random();
        int i = rand.nextInt(possibleMoves.size());
        int j;
        if (possibleMoves.get(i).size() != 1) {
            j = rand.nextInt(possibleMoves.get(i).size() - 1);
            j++;
        } else {
            j = 1;
        }
        ChessModelSquare source = possibleMoves.get(i).get(0);
        ChessModelSquare dest = possibleMoves.get(i).get(j);
        controller.selectedForMove = source;
        possibleMoves.get(i).remove(0);
        controller.previousPossibleMoves = possibleMoves.get(i).toArray(new ChessModelSquare[0]);
        controller.makeMove(dest);
        controller.selectedForMove = null;
    }
}
