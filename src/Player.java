public class Player {
    private final int repersentInt;
    private final String mark;
    private final String name;

    public Player(int repersentInt, String mark, String name) {
        this.repersentInt = repersentInt;
        this.mark = mark;
        this.name = name;
    }

    public int getRepersentInt() {
        return repersentInt;
    }

    public String getMark() {
        return mark;
    }
    public String getName() {
        return name;
    }
}
