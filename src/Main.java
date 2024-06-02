import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;


public class Main {
    public static final int[] TEMPLATE = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    public static Player human;
    public static Player pc;

    public static Player rowDetect(int placed, Player player, ArrayList<Integer> board, boolean simulated) {
        boolean flag = false;
        int row = placed % 3;
        int line = placed / 3;
        if (Objects.equals(board.get(line*3 + 0), board.get(line*3 + 1)) && Objects.equals(board.get(line*3 + 1), board.get(line*3 + 2))) {
            if (!simulated) System.out.println("LINE!");
            flag = true;
        }
        if (Objects.equals(board.get(0*3 + row), board.get(1*3 + row)) && Objects.equals(board.get(1*3 + row), board.get(2*3 + row))) {
            if (!simulated) System.out.println("ROW!");
            flag = true;
        }
        if (placed % 2 == 0) {
            if (placed == 0 || placed == 4 || placed == 8) {
                if (Objects.equals(board.get(0), board.get(4)) && Objects.equals(board.get(4), board.get(8))) {
                    if (!simulated) System.out.println("CROSS!");
                    flag = true;
                }
            }
            if (placed == 2 || placed == 4 || placed == 6) {
                if (Objects.equals(board.get(2), board.get(4)) && Objects.equals(board.get(4), board.get(6))) {
                    if (!simulated) System.out.println("CROSS!");
                    flag = true;
                }
            }
        }
        return flag ? player : null;
    }

    public static boolean placingProcess(int placed, Player player, ArrayList<Integer> board) {
        if (!placingValidityCheck(placed, player, board)) {
            System.out.println("Invalid number.");
            return false;
        }
        board.set(placed, player.getRepersentInt());
        return true;
    }

    public static boolean placingValidityCheck(int placed, Player player, ArrayList<Integer> board) {
        return placed <= 8 && placed >= 0 && board.get(placed) == 0;
    }

    public static int scanNumber(Scanner sc, Player player, ArrayList<Integer> board) {
        Integer chosenGrid;
        do {
            try {
                chosenGrid = Integer.parseInt(sc.nextLine()) - 1;
            } catch (NumberFormatException e) {
                System.out.println("Not a number! ");
                chosenGrid = null;
            }
        } while (chosenGrid == null || !placingProcess(chosenGrid, player, board));
        return chosenGrid;
    }

    public static int calculateNumber(PCBrain pcBrain, ArrayList<Integer> board, int round) {
        int resultGrid = pcBrain.makeMove(board, round);
        System.out.println(String.format("PC choose %d", resultGrid));
        placingProcess(resultGrid, pc, board);
        return resultGrid;
    }

    public static void noUIGameProcess() {
        for (; ; ) {

            System.out.println("Tic Tac Toe!");
            ArrayList<Integer> board = new ArrayList<Integer>(Arrays.stream(TEMPLATE).boxed().toList()) {
                public String getS(int index) {
                    int i = get(index);
                    return switch (i) {
                        case 1 -> "O";
                        case -1 -> "X";
                        default -> ".";
                    };
                }

                @Override
                public String toString() {
                    return String.format("===== \n%s %s %s \n%s %s %s \n%s %s %s \n=====", getS(0), getS(1), getS(2), getS(3), getS(4), getS(5), getS(6), getS(7), getS(8));
                }
            };
            System.out.println(board);

            Scanner sc = new Scanner(System.in);

            for (; ; ) {
                System.out.println("Who play first? (I/U)");
                String text = sc.nextLine();
                if (text.equals("I")) {
                    human = new Player(1, "O", "HUMAN");
                    pc = new Player(-1, "X", "PC");
                    break;
                } else if (text.equals("U")) {
                    pc = new Player(1, "O", "PC");
                    human = new Player(-1, "X", "HUMAN");
                    break;
                }
            }

            int chosenGrid;
            int currentSide = 1;
            int round = 1;
            Player currentPlayer;
            PCBrain pcBrain = new PCBrain();
            currentPlayer = human.getRepersentInt() == 1 ? human : pc;
            System.out.println(String.format("Therefore, %1$s goes first. \n%1$s, please choose a number from 1 to 9.", currentPlayer.getName()));
            if (currentPlayer.equals(human)) {
                scanNumber(sc, currentPlayer, board);
            } else {
                calculateNumber(pcBrain, board, round);
            }
            System.out.println(board);
            for (round = 2; round <= 9; round++) {
                currentSide = currentSide == 1 ? -1 : 1;
                currentPlayer = human.getRepersentInt() == currentSide ? human : pc;
                System.out.println(String.format("Now it's round %d, %s goes.", round, currentPlayer.getName()));
                if (currentPlayer.equals(human)) {
                    chosenGrid = scanNumber(sc, currentPlayer, board);
                } else {
                    chosenGrid = calculateNumber(pcBrain, board, round);
                }
                if (rowDetect(chosenGrid, currentPlayer, board, false) != null) {
                    System.out.println(String.format("%s wins the game!", currentPlayer.getName()));
                    System.out.println(board);
                    break;
                }
                if (round == 9) System.out.println("TIE!");
                System.out.println(board);
            }
            for(;;) {
                System.out.println("Play again? (Y/N)");
                String isReplay = sc.nextLine();
                if (Objects.equals(isReplay, "N")) {
                    System.exit(0);
                } else if (Objects.equals(isReplay, "Y")) {
                    break;
                }
            }
        }
    }


    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        if (args.length != 0 && args[0].equals("ui")) {
            System.out.println(Arrays.toString(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));
            System.out.println(Arrays.toString(UIManager.getInstalledLookAndFeels()));
//            LafManager.setTheme(new IntelliJTheme());
//            LafManager.install();
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new Frame();
                }
            });
        } else noUIGameProcess();

    }
}