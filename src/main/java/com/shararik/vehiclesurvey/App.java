package com.shararik.vehiclesurvey;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class App {

    private static final String DATA_FILE_NAME = "data.txt";
    public static Stream<String> data;

    public static void main(String[] args) throws IOException {
        System.out.println("Hello World!");
        try (Stream<String> lines = readData(DATA_FILE_NAME)) {
            lines.forEach(System.out::println);
            System.out.println("Data has been loaded");
        }
    }

    public static Stream<String> readData(String dataFileName) throws IOException {
        String filePathStr = App.class.getClassLoader().getResource(dataFileName).getPath();
        Path path = Paths.get(filePathStr);
        Stream<String> lines = Files.lines(path);
        return lines;
    }

    public static long countSouthBound(Stream<String> lines) {
        return lines.filter(line -> line.startsWith("B")).count() / 2;
    }
}
