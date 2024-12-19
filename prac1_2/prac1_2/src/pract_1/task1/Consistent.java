package pract_1.task1;

import java.util.List;

public class Consistent {
    public static long sum(List<Integer> array) {
        long sum = 0;
        for (int i = 0; i < array.size(); i++) {
            sum += array.get(i);
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return sum;
    }
}
