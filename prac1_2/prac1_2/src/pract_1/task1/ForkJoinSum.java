package pract_1.task1;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ForkJoinSum {

    private static class SumTask extends RecursiveTask<Long> {

        private final List<Integer> array;
        private final int start;
        private final int end;
        private static final int THRESHOLD = 10;

        public SumTask(List<Integer> array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            if (end - start <= THRESHOLD) {
                long sum = 0;
                for (int i = start; i < end; i++) {
                    sum += array.get(i);
                }
                return sum;
            } else {
                int middle = start + (end - start) / 2;
                SumTask left = new SumTask(array, start, middle);
                SumTask right = new SumTask(array, middle, end);
                left.fork();
                try {
                    Thread.currentThread().sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return right.compute() + left.join();
            }
        }
    }

    public static long sum(List<Integer> array) {
        ForkJoinPool pool = new ForkJoinPool();
        return pool.invoke(new SumTask(array, 0, array.size()));
    }
}
