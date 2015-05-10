package com.shararik.vehiclesurvey;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class App {

    private static final String DATA_FILE_NAME = "data.txt";
    public static String[] dataArray;
    public static final int DURATION_1DAY = 86400000;
    public static final int DURATION_15MINS = 900000;

    public static void main(String[] args) throws IOException {
        try (Stream<String> dataStream = readData(DATA_FILE_NAME)) {
            dataArray = dataStream.toArray(String[]::new);
            System.out.println("Data has been loaded");

            int[] timeSeriesData = transformToTimeSeriesData(Arrays.stream(dataArray));

            getDayEndPoints(timeSeriesData).forEach(System.out::println);
            getDayEndPoints(timeSeriesData).forEach(i -> System.out.println(timeSeriesData[i]));
        }
    }

    public static Stream<String> readData(String dataFileName) throws IOException {
        String filePathStr = App.class.getClassLoader().getResource(dataFileName).getPath();
        Path path = Paths.get(filePathStr);
        Stream<String> lines = Files.lines(path);
        return lines;
    }

    // Parse raw data to objects
    public static Record parseRecord(String rawData) {
        return new Record(Hose.valueOf(rawData.substring(0, 1)), Integer.parseInt(rawData.substring(1)));
    }

    // Transform data to time series data
    public static int[] transformToTimeSeriesData(Stream<String> dataStream) {
        return dataStream.mapToInt(l -> Integer.parseInt(l.substring(1))).toArray();
    }

    // Find all the index of last data point of the day
    public static List<Integer> getDayEndPoints(int[] timeSeriesData) {

        List<Integer> dayEndPoints = new ArrayList<Integer>();
        IntStream.range(1, timeSeriesData.length - 1)
                .filter(i -> timeSeriesData[i - 1] > timeSeriesData[i])
                .forEach(i -> dayEndPoints.add(i - 1));

        // The last point provided by the data may not be the actual end point for the day.
        // But we assume it as the last point for analytics purpose.
        dayEndPoints.add(timeSeriesData.length - 1);

        return dayEndPoints;
    }

    public static long countSouthBound(Stream<String> lines) {
        return lines.filter(line -> line.startsWith("B")).count() / 2;
    }
}
