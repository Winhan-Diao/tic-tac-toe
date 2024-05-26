import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.stream.IntStream;

public class PCBrain {
    public static ArrayList<Integer> getSpecific(ArrayList<Integer> board, int specific) {
        return new ArrayList<>(IntStream.range(0, board.size()).filter(i -> board.get(i) == specific).boxed().toList());
    }

    public int pureRandom(ArrayList<Integer> board, Random random) {
        int[] candidate = IntStream.range(0, 9).filter(i -> board.get(i) == 0).toArray();
        return candidate[random.nextInt(0, candidate.length)];
    }

    public ArrayList<Integer> caseCompiling(ArrayList<Integer> history) {
        return null;
    }

    public int scoreEvaluating() {
        return -1;
    }

    public int decisionMakingProcess(int round, ArrayList<Integer> board,  Random random) {
        HashMap<Integer, Integer> hashMap = new HashMap<>();
        TreeNodes treeNodes = new TreeNodes(-1, getSpecific(board, 0), new ArrayList<>());
        int depth = 9 - round;
        for (TreeNodes e : treeNodes.children) {
            int score = minimaxAlgorithm(e, true, depth, random);
            hashMap.put(e.data, score);
        }
        return -1;
    }

    public int minimaxAlgorithm(TreeNodes treeNodes, boolean isPC, int depth, Random random) {
        if (treeNodes.children.isEmpty()) {
            return scoreEvaluating();
        }
        return -1;
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
        TreeNodes treeNodes = new TreeNodes(-1, getSpecific(board, 0), new ArrayList<>());
        return pureRandom(board, random);
    }
}
