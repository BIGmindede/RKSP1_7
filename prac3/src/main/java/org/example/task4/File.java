package org.example.task4;

public class File {
    private final String type;
    private final int size;

    public File(String type, int size) {
        this.type = type;
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public int getSize() {
        return size;
    }

}
