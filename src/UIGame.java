import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UIGame {
    ArrayList<Integer> board;
    Player human;
    Player pc;
    Player currentPlayer;
    PCBrain pcBrain;
    int gameState;
    int humanRepresentInt;
    static final ArrayList<int[]> WINNING_CONDITIONS;

    UIGame(int humanRepresentInt) {
        initializeGame(humanRepresentInt);
    }

    public void initializeGame(int humanRepresentInt) {
        if (board == null) {
            board = new ArrayList<>(Arrays.stream(Main.TEMPLATE).boxed().toList());
        } else {
            for (int i : board) {i = 0;}
        }

        human = new Player(humanRepresentInt, humanRepresentInt == 1 ? "O" : "X", "HUMAN");
        pc = new Player(-humanRepresentInt, -humanRepresentInt == 1 ? "O" : "X", "PC");
        currentPlayer = humanRepresentInt == 1 ? human : pc;

        gameState = 0;
        this.humanRepresentInt = humanRepresentInt;

        pcBrain = new PCBrain();

        //This is done for compatibility.
        Main.pc = pc;
        Main.human = human;
    }

    public void gameProcess(Frame mainFrame, @Nullable Integer targetButtonIndex) {
        System.out.println(++gameState);
        if (gameState > 9) {

        } else {
            if (currentPlayer == human) {
                if (targetButtonIndex != null) {
                    mainFrame.j3x3Matrix.jButtons.get(targetButtonIndex).setText(humanRepresentInt == 1 ? "O" : "X");
                    mainFrame.turnLabel.setText("<html>Turn:<br>PC</html>");

                    board.set(targetButtonIndex, humanRepresentInt);

                    currentPlayer = pc;

                    if (winChecker(targetButtonIndex, board, human) != null) {
                        for (int i : winChecker(targetButtonIndex, board, human)) {
                            mainFrame.j3x3Matrix.jButtons.get(i).setBackground(new Color(0xEEEEE99));
                            mainFrame.turnLabel.setText("<html>HUMAN<br>wins the game!</html>");
                            gameState = 100;
                        }
                    } else if (gameState == 9) {
                        mainFrame.turnLabel.setText("TIE!");
                        gameState = 100;
                    }
                }

                gameProcess(mainFrame, null);

            } else {
                mainFrame.j3x3Matrix.setAllEnabled(false);
                int chosengird = pcBrain.makeMove(board, gameState);
                mainFrame.j3x3Matrix.jButtons.get(chosengird).setText(humanRepresentInt == 1 ? "X" : "O");
                mainFrame.j3x3Matrix.setAllEnabled(true);
                mainFrame.turnLabel.setText("<html>Turn:<br>HUMAN</html>");

                board.set(chosengird, -humanRepresentInt);
                currentPlayer = human;

                if (winChecker(chosengird, board, pc) != null) {
                    for (int i : winChecker(chosengird, board, pc)) {
                        mainFrame.j3x3Matrix.jButtons.get(i).setBackground(new Color(0xEEEEE99));
                        mainFrame.turnLabel.setText("<html>PC<br>wins the game!</html>");
                        gameState = 100;
                    }
                } else if (gameState == 9) {
                    mainFrame.turnLabel.setText("TIE!");
                    gameState = 100;
                }
            }


        }


    }

    public static List<Integer> winChecker(int latestPlacedIndex, ArrayList<Integer> board, Player currentPlayer) {
        int targetRepersentInt = currentPlayer.getRepersentInt();
        ArrayList<Integer> placedIndexes = PCBrain.getSpecific(board, targetRepersentInt);
        return WINNING_CONDITIONS.stream().map(ints -> Arrays.stream(ints).boxed().toList()).filter(placedIndexes::containsAll).findFirst().orElse(null);
    }


    static {
        WINNING_CONDITIONS = new ArrayList<>(Arrays.asList(new int[]{0, 1, 2}, new int[]{3, 4, 5}, new int[]{6, 7, 8},
                                                        new int[]{0, 3, 6}, new int[]{1, 4, 7}, new int[]{2, 5, 8},
                                                        new int[]{0, 4, 8}, new int[]{2, 4, 6}));
    }

}
