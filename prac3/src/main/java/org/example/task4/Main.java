package org.example.task4;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Main {
    public static void main(String[] args) {
        FileGenerator generator = new FileGenerator();
        FileQueue queue = new FileQueue(5); // Вместимость очереди 5
        FileHandler jsonHandler = new FileHandler("JSON");
        FileHandler xmlHandler = new FileHandler("XML");
        FileHandler xlsHandler = new FileHandler("XLS");

        // Генерация файлов в отдельном потоке
        Observable<File> fileObservable = generator.generateFiles()
                .subscribeOn(Schedulers.io())
                .doOnNext(queue::enqueue);

        // Обработка файлов из очереди
        queue.getFiles()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(file -> {
                    jsonHandler.processFile(file);
                    xmlHandler.processFile(file);
                    xlsHandler.processFile(file);
                });

        // Запускаем генерацию файлов
        fileObservable.subscribe();

        // Добавляем задержку, чтобы программа не завершилась сразу
        try {
            Thread.sleep(10000); // Задержка 10 секунд
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
