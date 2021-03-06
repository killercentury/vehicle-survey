package com.shararik.vehiclesurvey;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class App {

    private static final String DATA_FILE_NAME = "data.txt";
    public static List<String> data;
    public static final long DURATION_1DAY = 86400000;
    public static final long DURATION_1HOUR = 3600000;
    public static final long DURATION_30MINS = 1800000;
    public static final long DURATION_20MINS = 1200000;
    public static final long DURATION_15MINS = 900000;

    public static void main(String[] args) throws IOException {
        try (Stream<String> dataStream = readData(DATA_FILE_NAME)) {
            // convert to list for reuse
            data = dataStream.collect(Collectors.toList());
            System.out.println("Data has been loaded");

            List<Record> recordList = Transform.getRecordList(data.stream());
//            dayEndPoints.forEach(System.out::println);
//            dayEndPoints.forEach(i -> System.out.println(records.get(i).getTime()));

            List<List<Record>> segmentedRecords = segmentRecords(recordList, DURATION_15MINS);
            System.out.println(segmentedRecords.size());
            countSouthBoundPerSegment(segmentedRecords).forEach(System.out::println);
        }
    }

    public static Stream<String> readData(String dataFileName) throws IOException {
        String filePathStr = App.class.getClassLoader().getResource(dataFileName).getPath();
        Path path = Paths.get(filePathStr);
        Stream<String> lines = Files.lines(path);
        return lines;
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

    public static List<List<Record>> segmentRecords(List<Record> records, long duration) {
        List<Integer> dayEndPoints = getDayEndPoints(records);
        int numOfSegments = (int) (DURATION_1DAY / duration);
        List<List<Record>> segmentedRecords = new ArrayList<List<Record>>();

        IntStream.range(0, dayEndPoints.size()).forEach(i -> {
                    // from inclusive index
                    int fromIndex = (i == 0) ? 0 : dayEndPoints.get(i - 1) + 1;
                    // to exclusive index
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

    public static long countSouthBound(List<Record> records) {
        return records.stream().filter(record -> record.getHose() == Hose.B).count() / 2;
    }

    public static long countNorthBound(List<Record> records) {
        return records.size() - countSouthBound(records) * 4;
    }

    public static LongStream countSouthBoundPerSegment(List<List<Record>> segmentedRecords) {
        return segmentedRecords.stream().mapToLong(records -> countSouthBound(records));
    }

    public static LongStream countNorthBoundPerSegment(List<List<Record>> segmentedRecords) {
        return segmentedRecords.stream().mapToLong(records -> countNorthBound(records));
    }
}
