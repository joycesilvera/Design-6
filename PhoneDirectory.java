import java.util.HashSet;
import java.util.Queue;
import java.util.LinkedList;

// Time complexity: O(1) for all operations
// Space complexity: O(N) where N is the maxNumbers
public class PhoneDirectory {
    HashSet<Integer> set;
    Queue<Integer> q;

    public PhoneDirectory(int maxNumbers) {
        this.set = new HashSet<>();
        this.q = new LinkedList<>();

        for (int i = 0; i < maxNumbers; i++) {
            set.add(i);
            q.add(i);
        }
    }

    public int get() {
        if (set.isEmpty())
            return -1;
        int curr = q.poll();
        set.remove(curr);
        return curr;
    }

    public boolean check(int number) {
        return set.contains(number);
    }

    public void release(int number) {
        if (!set.contains(number)) {
            set.add(number);
            q.add(number);
        }
    }
}