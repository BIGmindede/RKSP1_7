package org.example.task4;

public class FileHandler {
    private final String type;

    public FileHandler(String type) {
        this.type = type;
    }

    public void processFile(File file) {
        if (file.getType().equals(type)) {
            int processingTime = file.getSize() * 7; // Время обработки в миллисекундах
            System.out.println("processing " + file.getType() + " size " + file.getSize() + "...");
            try {
                Thread.sleep(processingTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("File " + file.getType() + " completed processing.");
        } else {
            System.out.println("Handler does not support format " + file.getType());
        }
    }
}
