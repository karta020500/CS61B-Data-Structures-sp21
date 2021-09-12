package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 1000; i <= Ns.size(); i *= 2) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        SLList<Integer> sListObj = new SLList<Integer>();
        AList<Integer> listNs = new AList<Integer>();
        AList<Double> listTimes = new AList<Double>();
        AList<Integer> listOpCount = new AList<Integer>();
        // Size of addLast()
        int addSize = 64000;
        // How many times to call getLast()
        int getLastOps = 1000;
        for (int i = 0; i <= addSize; i++) {
            sListObj.addLast(i);
            listNs.addLast(i);
            Stopwatch sw = new Stopwatch();
            for (int j = 0; j <= getLastOps; j++) {
                sListObj.getLast();
            }
            double timeInSeconds = sw.elapsedTime();
            listTimes.addLast(timeInSeconds);
            listOpCount.addLast(getLastOps);
        }
        printTimingTable(listNs, listTimes, listOpCount);
    }
}
