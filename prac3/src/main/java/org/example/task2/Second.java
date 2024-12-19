package org.example.task2;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.Random;

// Даны два потока по 1000 элементов. Каждый содержит случайные цифры.
// Сформировать поток, обрабатывающий оба потока последовательно.
// Например, при входных потоках (1, 2, 3) и (4, 5, 6)
// выходной поток — (1, 2, 3, 4, 5, 6).


public class Second {
    public static void mergeFlat() {
        Random random = new Random();

        // Создаем два потока случайных чисел
        Observable<String> stream1 = Observable.range(1, 4)
                .map(i -> "1: " + random.nextInt(4));
        Observable<String> stream2 = Observable.range(1, 4)
                .map(i -> "2: " + random.nextInt(4));

        // Объединяем потоки
        Observable<String> mergedStream = Observable.mergeDelayError(stream1, stream2);

        // Подписываемся на объединенный поток и выводим элементы
        mergedStream.subscribe(number -> System.out.println(number));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}