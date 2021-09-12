package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {

    @Test
    public void testThreeAddThreeRemove() {
        AListNoResizing<Integer> correct = new AListNoResizing<>();
        BuggyAList<Integer> buggy = new BuggyAList<>();
        // add [1, 2, 3]
        correct.addLast(1);
        correct.addLast(2);
        correct.addLast(3);
        buggy.addLast(1);
        buggy.addLast(2);
        buggy.addLast(3);
        assertEquals(correct.size(), buggy.size());
        assertEquals(correct.removeLast(), buggy.removeLast());
        assertEquals(correct.removeLast(), buggy.removeLast());
        assertEquals(correct.removeLast(), buggy.removeLast());
    }
    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> correct = new AListNoResizing<>();
        BuggyAList<Integer> buggy = new BuggyAList<>();
        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                correct.addLast(randVal);
                buggy.addLast(randVal);
                System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // size
                assertEquals(correct.size(), buggy.size());
            }
            else if (operationNumber == 2 && correct.size() > 0) {
                // getLast
                assertEquals(correct.getLast(), buggy.getLast());
            }
            else if (operationNumber == 3 && correct.size() > 0) {
                // removeLast
                assertEquals(correct.removeLast(), buggy.removeLast());
            }
        }
    }
}
