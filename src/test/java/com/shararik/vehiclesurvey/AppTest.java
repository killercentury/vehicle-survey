package com.shararik.vehiclesurvey;

import org.junit.Test;

import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

/**
 * Created by kira on 27/04/15.
 */
public class AppTest {
    @Test
    public void countData() throws IOException {
        Stream<String> lines = App.readData("data.txt");
        long num = lines.count();
        assertEquals(67296, num);
    }
}
