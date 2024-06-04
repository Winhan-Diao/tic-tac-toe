import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.IntStream;

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
            for (Integer i : board) {i = 0;}
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
                            mainFrame.j3x3Matrix.jButtons.get(i).setFont(mainFrame.j3x3Matrix.jButtons.get(i).getFont().deriveFont(Font.BOLD));
                            mainFrame.turnLabel.setText("<html>HUMAN<br>wins the game!</html>");
                            gameState = 100;
                        }
                        return;
                    }
                    if (gameState == 9) {
                        mainFrame.turnLabel.setText("TIE!");
                        IntStream.range(0, 9).mapToObj(i -> mainFrame.j3x3Matrix.jButtons.get(i)).forEach(b -> (new Thread(new AnimationRunnable(null, new Color(0xD1EECA), 5L, b, true, true))).start());
                        gameState = 100;
                        return;
                    }
                    if (mainFrame.analyzerTextArea.isEnabled()) mainFrame.analyzerTextArea.setText("For next: PC\n" + analyzer(true).entrySet().stream().map(Map.Entry::toString).map(s -> (Integer.parseInt(s.split("=")[0])+1)+": "+analyzedCaseParseNumToCase(s.split("=")[1])+"\n").collect(StringBuilder::new, StringBuilder::append, StringBuilder::append));

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
//                        mainFrame.j3x3Matrix.jButtons.get(i).setBackground(new Color(0xEEEEE99));
                        new Thread(new AnimationRunnable(null, new Color(0xEEEEE99), 7L, mainFrame.j3x3Matrix.jButtons.get(i), false, false)).start();
                        mainFrame.j3x3Matrix.jButtons.get(i).setFont(mainFrame.j3x3Matrix.jButtons.get(i).getFont().deriveFont(Font.BOLD));
                        mainFrame.turnLabel.setText("<html>PC<br>wins the game!</html>");
                        gameState = 100;
                    }
                    return;
                }
                if (gameState == 9) {
                    mainFrame.turnLabel.setText("TIE!");
                    IntStream.range(0, 9).mapToObj(i -> mainFrame.j3x3Matrix.jButtons.get(i)).forEach(b -> (new Thread(new AnimationRunnable(null, new Color(0xD1EECA), 5L, b, true, true))).start());
                    gameState = 100;
                    return;
                }
                if (mainFrame.analyzerTextArea.isEnabled()) mainFrame.analyzerTextArea.setText("For next: HUMAN\n" + analyzer(false).entrySet().stream().map(Map.Entry::toString).map(s -> (Integer.parseInt(s.split("=")[0])+1)+": "+analyzedCaseParseNumToCase(s.split("=")[1])+"\n").collect(StringBuilder::new, StringBuilder::append, StringBuilder::append));
            }

        }


    }

    public static List<Integer> winChecker(int latestPlacedIndex, ArrayList<Integer> board, Player currentPlayer) {
        int targetRepersentInt = currentPlayer.getRepersentInt();
        ArrayList<Integer> placedIndexes = PCBrain.getSpecific(board, targetRepersentInt);
        return WINNING_CONDITIONS.stream().map(ints -> Arrays.stream(ints).boxed().toList()).filter(placedIndexes::containsAll).findFirst().orElse(null);
    }

    public TreeMap<Integer, Integer> analyzer(boolean isPC) {
        TreeMap<Integer, Integer> treeMap = new TreeMap<>();
        TreeNodes treeNodes = new TreeNodes(-1, PCBrain.getSpecific(this.board, 0), new ArrayList<>());
        int depth = 9 - gameState;
        int score;
        for (TreeNodes e : treeNodes.children) {
            score = (new PCBrain()).minimaxAlgorithm(this.board, e, isPC, depth);
            treeMap.put(e.data, score);
        }
        return treeMap;
    }

    public static String analyzedCaseParseNumToCase(String num) {
        switch (num) {
            case "1": return "Human wins";
            case "-1": return "PC wins";
            case "0": return "Tie";
            default: return null;
        }
    }


    static {
        WINNING_CONDITIONS = new ArrayList<>(Arrays.asList(new int[]{0, 1, 2}, new int[]{3, 4, 5}, new int[]{6, 7, 8},
                                                        new int[]{0, 3, 6}, new int[]{1, 4, 7}, new int[]{2, 5, 8},
                                                        new int[]{0, 4, 8}, new int[]{2, 4, 6}));
    }

}
