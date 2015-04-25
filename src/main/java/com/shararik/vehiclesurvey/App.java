package com.shararik.vehiclesurvey;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class App {

    private static final String DATA_FILE_NAME = "data.txt";

    public static void main(String[] args) {
        System.out.println("Hello World!");
        readData();
        System.out.println("Data has been loaded");
    }

    public static void readData() {
        String filePathStr = App.class.getClassLoader().getResource(DATA_FILE_NAME).getPath();
        Path path = Paths.get(filePathStr);
        Stream<String> lines;
        try {
            lines = Files.lines(path);
            lines.forEach(System.out::println);
            lines.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
