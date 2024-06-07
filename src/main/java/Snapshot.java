public class Snapshot {
    private final char[] value;
    private final int count;

    public Snapshot(char[] value, int count) {
        this.value = value;
        this.count = count;
    }

    public char[] getValue() {
        return value;
    }

    public int getCount() {
        return count;
    }
}
