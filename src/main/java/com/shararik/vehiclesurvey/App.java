package com.shararik.vehiclesurvey;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
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

            List<Record> records = Arrays.stream(dataArray).map(s -> parseRecord(s)).collect(Collectors.toList());
            List<Integer> dayEndPoints = getDayEndPoints(records);
//            dayEndPoints.forEach(System.out::println);
//            dayEndPoints.forEach(i -> System.out.println(records.get(i).getTime()));

            List<List<Record>> segmentedRecords = segmentRecords(records, DURATION_15MINS, dayEndPoints.size());
            System.out.println(segmentedRecords.size());
        }
    }

    public static Stream<String> readData(String dataFileName) throws IOException {
        String filePathStr = App.class.getClassLoader().getResource(dataFileName).getPath();
        Path path = Paths.get(filePathStr);
        Stream<String> lines = Files.lines(path);
        return lines;
    }

    // Parse data from string to object
    public static Record parseRecord(String rawData) {
        return new Record(Hose.valueOf(rawData.substring(0, 1)), Integer.parseInt(rawData.substring(1)));
    }

    // Transform data to time series data
    public static int[] transformToTimeSeriesData(Stream<String> dataStream) {
        return dataStream.mapToInt(l -> Integer.parseInt(l.substring(1))).toArray();
    }

    // Find all the index of last data point of the day
    public static List<Integer> getDayEndPoints(List<Record> records) {

        List<Integer> dayEndPoints = new ArrayList<Integer>();
        IntStream.range(1, records.size())
                .filter(i -> records.get(i - 1).getTime() > records.get(i).getTime())
                .forEach(i -> dayEndPoints.add(i - 1));

        // The last point provided by the data may not be the actual end point for the day.
        // But we assume it as the last point for analytics purpose.
        dayEndPoints.add(records.size() - 1);

        return dayEndPoints;
    }

    // The records are only limited to one day due to the time is reset daily in the raw data
    public static List<List<Record>> segmentRecords(List<Record> records, int duration, int days) {
        List<Integer> dayEndPoints = getDayEndPoints(records);
        int numOfSegments = DURATION_1DAY / duration;
        List<List<Record>> segmentedRecords = new ArrayList<List<Record>>();

        IntStream.range(0, dayEndPoints.size()).forEach(i -> {
                    int fromIndex = (i == 0) ? 0 : dayEndPoints.get(i - 1) + 1;
                    int toIndex = dayEndPoints.get(i) + 1;
                    IntStream.range(0, numOfSegments).forEach(n -> {
                        List<Record> recordsByDuration = records.subList(fromIndex, toIndex).stream()
                                .filter(r -> r.getTime() > n * duration && r.getTime() <= (n + 1) * duration)
                                .collect(Collectors.toList());
                        segmentedRecords.add(recordsByDuration);
                    });
                }
        );

        return segmentedRecords;
    }

    public static long countSouthBound(Stream<String> lines) {
        return lines.filter(line -> line.startsWith("B")).count() / 2;
    }
}
