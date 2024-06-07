import java.util.Stack;

public class StringBuilderWithUndo {
    private char[] value;
    private int count;
    private Stack<Snapshot> history;

    public StringBuilderWithUndo() {
        value = new char[16];
        count = 0;
        history = new Stack<>();
    }

    private void saveState() {
        history.push(new Snapshot(value.clone(), count));
    }

    public void undo() {
        if (!history.isEmpty()) {
            Snapshot snapshot = history.pop();
            value = snapshot.getValue();
            count = snapshot.getCount();
        }
    }

    public StringBuilderWithUndo append(String str) {
        saveState();
        if (str == null) {
            str = "null";
        }
        int len = str.length();
        ensureCapacityInternal(count + len);
        str.getChars(0, len, value, count);
        count += len;
        return this;
    }

    public StringBuilderWithUndo insert(int offset, String str) {
        saveState();
        if (offset < 0 || offset > count) {
            throw new StringIndexOutOfBoundsException(offset);
        }
        if (str == null) {
            str = "null";
        }
        int len = str.length();
        ensureCapacityInternal(count + len);
        System.arraycopy(value, offset, value, offset + len, count - offset);
        str.getChars(0, len, value, offset);
        count += len;
        return this;
    }

    public StringBuilderWithUndo delete(int start, int end) {
        saveState();
        if (start < 0) {
            throw new StringIndexOutOfBoundsException(start);
        }
        if (end > count) {
            end = count;
        }
        if (start > end) {
            throw new StringIndexOutOfBoundsException();
        }
        int len = end - start;
        if (len > 0) {
            System.arraycopy(value, start + len, value, start, count - end);
            count -= len;
        }
        return this;
    }

    @Override
    public String toString() {
        return new String(value, 0, count);
    }

    private void ensureCapacityInternal(int minimumCapacity) {
        if (minimumCapacity - value.length > 0) {
            int newCapacity = value.length * 2 + 2;
            if (newCapacity - minimumCapacity < 0)
                newCapacity = minimumCapacity;
            if (newCapacity < 0) {
                if (minimumCapacity < 0) {
                    throw new OutOfMemoryError();
                }
                newCapacity = Integer.MAX_VALUE;
            }
            char[] newValue = new char[newCapacity];
            System.arraycopy(value, 0, newValue, 0, count);
            value = newValue;
        }
    }
}
