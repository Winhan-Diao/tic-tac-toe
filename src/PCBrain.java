import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PCBrain {
    public static ArrayList<Integer> getSpecific(ArrayList<Integer> board, int specific) {
        return new ArrayList<>(IntStream.range(0, board.size()).filter(i -> board.get(i) == specific).boxed().toList());
    }

    public int pureRandom(ArrayList<Integer> board, Random random) {
        int[] candidate = IntStream.range(0, 9).filter(i -> board.get(i) == 0).toArray();
        return candidate[random.nextInt(0, candidate.length)];
    }

    public ArrayList<Integer> caseCompiling(ArrayList<Integer> history, ArrayList<Integer> board) {
        for (int i = 0; i < history.size(); i++) {
            if (i % 2 == 0) {
                board.set(i, Main.pc.getRepersentInt());
            } else {
                board.set(i, Main.human.getRepersentInt());
            }
        }
        return board;
    }

    public int scoreEvaluating(int finalPlaced, Player finalPlayer, ArrayList<Integer> history, ArrayList<Integer> board) {
//        board = caseCompiling(history, board);
        Player winner = Main.rowDetect(finalPlaced, finalPlayer, board);
        if (winner == null) {
            return 0;
        } else if (winner.equals(Main.pc)) {
            return -1;
        } else return 1;
    }

    public int decisionMakingProcess(int round, ArrayList<Integer> board,  Random random) {
        TreeMap<Integer, Integer> treeMap = new TreeMap<>();
        TreeNodes treeNodes = new TreeNodes(-1, getSpecific(board, 0), new ArrayList<>());
        int depth = 9 - round;
        int score;
        for (TreeNodes e : treeNodes.children) {
            score = minimaxAlgorithm(board, e, true, depth, random);
            treeMap.put(e.data, score);
        }
        int minValue = Collections.min(treeMap.values());
        List<Integer> bests = treeMap.entrySet().stream().filter(entry -> Objects.equals(entry.getValue(), minValue)).map(Map.Entry::getKey).collect(Collectors.toList());
        return bests.get(random.nextInt(0, bests.size()));
    }

    public int minimaxAlgorithm(ArrayList<Integer> board, TreeNodes treeNodes, boolean isPC, int depth, Random random) {
        int score, score1;
        ArrayList<Integer> board1 = (ArrayList<Integer>) board.clone();
        board1.set(treeNodes.data, isPC? Main.pc.getRepersentInt() : Main.human.getRepersentInt());
        if (treeNodes.children.isEmpty() || Main.rowDetect(treeNodes.data, isPC? Main.pc : Main.human, board1) != null) {
            return scoreEvaluating(treeNodes.data, isPC ? Main.pc : Main.human, treeNodes.accumulated, board1);
        }
        if (isPC) {
            score = 10;
            score1 = treeNodes.children.stream().map(child -> minimaxAlgorithm(board1, child, !isPC, depth, random)).min(Integer::compareTo).get();
            score = Math.min(score1, score);
        } else {
            score = -10;
            score1 = treeNodes.children.stream().map(child -> minimaxAlgorithm(board1, child, !isPC, depth, random)).max(Integer::compareTo).get();
            score = Math.max(score1, score);
        }
        return score;
    }
    public int makeMove(ArrayList<Integer> board, int round) {
        Random random = new Random();
        if (round == 9) {
            return getSpecific(board, 0).get(0);
        }
        if (round == 1) {
            if (random.nextInt() % 2 == 0) {
                return 4;
            } else return new int[]{0, 2, 6, 8}[random.nextInt(4)];
        }
//        decisionMakingProcess(round, board, random);
//        TreeNodes treeNodes = new TreeNodes(-1, getSpecific(board, 0), new ArrayList<>());
//        return pureRandom(board, random);
        return decisionMakingProcess(round, board, random);
    }
}
