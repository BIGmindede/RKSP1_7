package org.example.task2;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.Random;

// Преобразовать поток из 1000 случайных чисел от 0 до 1000
// в поток, содержащий только числа больше 500.

public class First {
    public static void filterNums() {
        Random random = new Random();
        Observable<Integer> randomNumbers = Observable.range(1, 1000)
                .map(i -> random.nextInt(1001)) // 0-1000
                .subscribeOn(Schedulers.computation());

        Observable<Integer> filteredNums = randomNumbers
                .filter(number -> number > 500);

        filteredNums.subscribe(
                filtered -> System.out.print(""),
                Throwable::printStackTrace,
                () -> System.out.println("Success filter")
        );
        try {
            Thread.sleep(5000); // Достаточно времени для выполнения
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
