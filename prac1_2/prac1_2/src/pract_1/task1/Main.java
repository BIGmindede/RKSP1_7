package pract_1.task1;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        List<Integer> array = new ArrayList<Integer>();
        for (int i = 0; i < 10000; i++) {
            array.add((int) (Math.random() * 10000));
        }
        long start;
        long memoryBefore;
        long result;
        long end;
        long memoryAfter;
        start = System.currentTimeMillis();
        result = Consistent.sum(array);
        end = System.currentTimeMillis();
        memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        System.out.println(result);
        System.out.println("Time of Consistent: " + (end - start));
        System.out.println("Memory used: " + (memoryAfter));

        start = System.currentTimeMillis();
        result = Concurrent.sum(array);
        end = System.currentTimeMillis();
        memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        System.out.println(result);
        System.out.println("Time of Concurrent: " + (end - start));
        System.out.println("Memory used: " + (memoryAfter));

        start = System.currentTimeMillis();
        result = ForkJoinSum.sum(array);
        end = System.currentTimeMillis();
        memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        System.out.println(result);
        System.out.println("Time of ForkJoin: " + (end - start));
        System.out.println("Memory used: " + (memoryAfter));
    }
}