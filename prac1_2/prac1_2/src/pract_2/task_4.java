package pract_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class task_4 {
    private static final Map<Path, List<String>> fileContentsMap = new HashMap<>();
    private static final Map<Path, String> fileHashes = new HashMap<>();

    private static List<String> readLinesFromFile(Path filePath) throws IOException, FileSystemException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    private static void detectFileChanges(Path filePath) throws IOException {
        List<String> newFileContents = readLinesFromFile(filePath);
        List<String> oldFileContents = fileContentsMap.get(filePath);

        if (oldFileContents != null) {
            List<String> addedLines = newFileContents.stream()
                    .filter(line -> !oldFileContents.contains(line))
                    .toList();

            List<String> deletedLines = oldFileContents.stream()
                    .filter(line -> !newFileContents.contains(line))
                    .toList();

            if (!addedLines.isEmpty()) {
                System.out.println("added strs" + filePath + ":");
                addedLines.forEach(line -> System.out.println("+ " + line));
            }

            if (!deletedLines.isEmpty()) {
                System.out.println("del str" + filePath + ":");
                deletedLines.forEach(line -> System.out.println("- " + line));
            }
        } else {
            System.out.println("added strs" + filePath + ":");
            newFileContents.forEach(line -> System.out.println("+ " + line));
        }
        calculateFileHash(filePath);
        fileContentsMap.put(filePath, newFileContents);
    }

    private static void firstObserve(Path directory) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory))
        {
            for (Path filePath : stream) {
                if (Files.isRegularFile(filePath)) {
                    fileContentsMap.put(filePath, readLinesFromFile(filePath));
                    calculateFileHash(filePath);
                }
            }
        }
    }
    private static void calculateFileHash(Path filePath) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            try (InputStream is = Files.newInputStream(filePath);
                 DigestInputStream dis = new DigestInputStream(is, md))
            {
                String hash = bytesToHex(md.digest());
                fileHashes.put(filePath, hash);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        Path directory = Paths.get("./observed");
        WatchService watchService = FileSystems.getDefault().newWatchService();
        directory.register(watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY,
                StandardWatchEventKinds.ENTRY_DELETE);
        firstObserve(directory); // предварительно обработаем все, что есть в директории
        while (true) {
            WatchKey key = watchService.take();

            for (WatchEvent<?> event : key.pollEvents()) {
                Path filePath = (Path) event.context();
                if (filePath.toString().endsWith("~")) {
                    continue;
                }
                WatchEvent.Kind<?> kind = event.kind();
                if (kind == StandardWatchEventKinds.ENTRY_CREATE) { // ивент - создание файла

                    System.out.println(event.context());
                    System.out.println("new file: " + filePath);

                    boolean file_avalilable = false;
                    while (!file_avalilable) {
                        try {
                            fileContentsMap.put(filePath, readLinesFromFile(directory.resolve(filePath)));
                            file_avalilable = true;
                        } catch (FileSystemException e) {
                            System.out.println("Failed to obtain lock");
                            Thread.sleep(500);
                        }
                    }
                    fileContentsMap.put(filePath, readLinesFromFile(directory.resolve(filePath)));
                    calculateFileHash(directory.resolve(filePath));
                } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) { // ивент - изменение
                    System.out.println("file changed: " + filePath);
                    detectFileChanges(directory.resolve(filePath));
                } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) { // ивент - удаление
                    System.out.println("file del: " + filePath);
                    String hash = fileHashes.get(directory.resolve(filePath));
                    if (hash != null) System.out.println("hash: " + hash);
                }
            }
            key.reset();
        }
    }
}
