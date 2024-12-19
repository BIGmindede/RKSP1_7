package pract_1.task1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Concurrent {
    private static long sum(List<Integer> array, int start, int end) {
        long sum = 0;
        for (int j = start; j < end; j++) {
            sum += array.get(j);
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return sum;
    }
    public static long sum(List<Integer> array) {
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        int chunkSize = (int) Math.ceil(array.size() / (double) numThreads);
        List<Future<Long>> futures = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, array.size());
            futures.add(executor.submit(() -> sum(array, start, end)));
        }
        long sum = 0;
        for (Future<Long> future : futures) {
            try {
                sum += future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
        return sum;
    }
}
