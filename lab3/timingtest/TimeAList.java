package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeAList {
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
        timeAListConstruction();
    }

    public static void timeAListConstruction() {
        AList<Integer> listNs = new AList<Integer>();
        AList<Double> listTimes = new AList<Double>();
        AList<Integer> listOpCount = new AList<Integer>();
        Stopwatch sw = new Stopwatch();
        // Size of addLast()
        int addSize = 128000;
        for (int i = 0; i <= addSize; i++){
            listNs.addLast(i);
            double timeInSeconds = sw.elapsedTime();
            listTimes.addLast(timeInSeconds);
            listOpCount.addLast(i);
        }
        printTimingTable(listNs, listTimes, listOpCount);
    }
}
