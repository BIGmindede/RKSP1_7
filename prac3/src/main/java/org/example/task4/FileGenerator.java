package org.example.task4;

import io.reactivex.rxjava3.core.Observable;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class FileGenerator {
    private final Random random = new Random();

    public Observable<File> generateFiles() {
        return Observable.create(emitter -> {
            while (!emitter.isDisposed()) {
                // Генерация случайного типа файла и размера
                String fileType = randomFileType();
                int fileSize = random.nextInt(91) + 10; // Размер от 10 до 100

                // Создание файла
                File file = new File(fileType, fileSize);
                emitter.onNext(file);

                // Задержка от 100 до 1000 мс
                TimeUnit.MILLISECONDS.sleep(random.nextInt(901) + 100);
            }
        });
    }

    private String randomFileType() {
        String[] types = {"XML", "JSON", "XLS"};
        return types[random.nextInt(types.length)];
    }
}
