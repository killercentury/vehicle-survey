package com.shararik.vehiclesurvey;

import org.junit.Test;

import java.io.IOException;
import java.util.List;
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
    public void testTimeSeriesDataTransformation() throws IOException {
        Stream<String> dataStream = App.readData("data.txt");
        int[] timeSeriesData = App.transformToTimeSeriesData(dataStream);
        assertEquals(67296, timeSeriesData.length);
    }

    @Test
    public void testDayEndPointsDetection() throws IOException {
        Stream<String> dataStream = App.readData("data.txt");
        int[] timeSeriesData = App.transformToTimeSeriesData(dataStream);
        List<Integer> dayEndPoints = App.getDayEndPoints(timeSeriesData);
        assertEquals(5, App.getDayEndPoints(timeSeriesData).size());
        assertEquals(86351672, timeSeriesData[dayEndPoints.get(0)]);
        assertEquals(86381837, timeSeriesData[dayEndPoints.get(1)]);
        assertEquals(86382072, timeSeriesData[dayEndPoints.get(2)]);
        assertEquals(86174912, timeSeriesData[dayEndPoints.get(3)]);
        assertEquals(86389454, timeSeriesData[dayEndPoints.get(4)]);
    }
}
