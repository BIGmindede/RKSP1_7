package org.example.task2;

// Дан поток из 10 случайных чисел. Сформировать поток,
// содержащий только первые 5 чисел.

import io.reactivex.rxjava3.core.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Third {
    public static void takeFive() {
        Random random = new Random();
        List<Integer> list = new ArrayList<>();

        Observable<Integer> randomNumbers = Observable.range(1, 10)
                .map(i -> {
                    int num = random.nextInt(100);
                    list.add(num);
                    return num;
                });

        Observable<Integer> firstFiveNumbers = randomNumbers.take(5);

        firstFiveNumbers.subscribe(
                number -> System.out.println(number),
                Throwable::printStackTrace,
                () -> System.out.println(list)
        );
    }
}
