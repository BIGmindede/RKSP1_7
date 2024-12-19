package pract_1.task2;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Main {
    public static int square(int n) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return n * n;
    }

    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        Future<Integer> future = null;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите число для возведения в квадрат (или 'exit' для выхода):");
        while (true) {
            String input = scanner.nextLine();
            if ("exit".equals(input)) {
                break;
            }
            try {
                int n = Integer.parseInt(input);
                future = executor.submit(() -> square(n));
                System.out.println("Квадрат числа " + n + " = " + future.get());
            } catch (Exception e) {
                System.out.println("Некорректное значение");
            }
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
        scanner.close();
    }
}
