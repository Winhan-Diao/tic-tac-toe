import java.util.ArrayList;

public class TreeNodes {
    int data;
    ArrayList<TreeNodes> children;

    ArrayList<Integer> accumulated;

    TreeNodes(int data, ArrayList<Integer> remains, ArrayList<Integer> preAccumulated) {
        this.data = data;
        this.children = new ArrayList<>();
        this.accumulated = ((ArrayList<Integer>) preAccumulated.clone());
        if (data != -1) this.accumulated.add(data);
        for (int i : remains) {
            ArrayList<Integer> remains1 = (ArrayList<Integer>) remains.clone();
            remains1.remove(Integer.valueOf(i));
            this.children.add(new TreeNodes(i, remains1, accumulated));
        }
    }
}
