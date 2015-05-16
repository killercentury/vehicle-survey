package com.shararik.vehiclesurvey;

import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

/**
 * Created by kira on 27/04/15.
 */
public class AppTest {
    @Test
    public void countData() throws IOException {
        Stream<String> dataStream = App.readData("data.txt");
        long num = dataStream.count();
        assertEquals(67296, num);
    }

    @Test
    public void testRawDataToObject() throws IOException {
        Record record = App.parseRecord("A98186");
        assertEquals(Hose.A, record.getHose());
        assertEquals(98186, record.getTime());
    }

    @Test
    public void testTimeSeriesDataTransformation() throws IOException {
        Stream<String> dataStream = App.readData("data.txt");
        int[] timeSeriesData = App.transformToTimeSeriesData(dataStream);
        assertEquals(67296, timeSeriesData.length);
    }

    @Test
    public void testDayEndPointsDetection() throws IOException {
        Stream<String> dataStream = App.readData("data.txt");
        List<Record> records = dataStream.map(s -> App.parseRecord(s)).collect(Collectors.toList());
        List<Integer> dayEndPoints = App.getDayEndPoints(records);
        assertEquals(5, App.getDayEndPoints(records).size());
        assertEquals(86351672, records.get(dayEndPoints.get(0)).getTime());
        assertEquals(86381837, records.get(dayEndPoints.get(1)).getTime());
        assertEquals(86382072, records.get(dayEndPoints.get(2)).getTime());
        assertEquals(86174912, records.get(dayEndPoints.get(3)).getTime());
        assertEquals(86389454, records.get(dayEndPoints.get(4)).getTime());
    }

    @Test
    public void testSegmentation() throws IOException {
        Stream<String> dataStream = App.readData("data.txt");
        List<Record> records = dataStream.map(s -> App.parseRecord(s)).collect(Collectors.toList());
        List<List<Record>> segmentedRecordsPer15Mins = App.segmentRecords(records, App.DURATION_15MINS);
        assertEquals(480, segmentedRecordsPer15Mins.size());
        List<List<Record>> segmentedRecordsPer20Mins = App.segmentRecords(records, App.DURATION_20MINS);
        assertEquals(288, segmentedRecordsPer20Mins.size());
        List<List<Record>> segmentedRecordsPer30Mins = App.segmentRecords(records, App.DURATION_30MINS);
        assertEquals(240, segmentedRecordsPer30Mins.size());
        List<List<Record>> segmentedRecordsPerHour = App.segmentRecords(records, App.DURATION_1HOUR);
        assertEquals(120, segmentedRecordsPerHour.size());
    }
}
