package org.example.task4;

import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class FileQueue {
    private final BlockingQueue<File> queue; // Блокирующая очередь
    private final int capacity;

    public FileQueue(int capacity) {
        this.capacity = capacity;
        this.queue = new LinkedBlockingQueue<>(capacity); // Устанавливаем вместимость
    }

    // Добавление файла в очередь
    public void enqueue(File file) {
        try {
            queue.put(file); // Блокирует, если очередь полная
            System.out.println("Added to queue");
        } catch (InterruptedException e) {
            System.err.println("Error adding: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    // Извлечение файлов из очереди в виде Observable
    public Observable<File> getFiles() {
        return Observable.create(emitter -> {
            try {
                while (!emitter.isDisposed()) {
                    File file = queue.take(); // Блокирует, если очередь пуста
                    System.out.println("Taken from queue");
                    emitter.onNext(file); // Отправляем файл в Observable
                }
            } catch (InterruptedException e) {
                emitter.onError(e); // Сообщаем об ошибке
                Thread.currentThread().interrupt(); // Восстанавливаем статус прерывания
            }
        });
    }
}

