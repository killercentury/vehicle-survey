package com.shararik.vehiclesurvey;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by kira on 17/05/15.
 */
public class Transform {

    // Parse data from string to object
    public static Record parseRecord(String rawDataItem) {
        return new Record(Hose.valueOf(rawDataItem.substring(0, 1)), Integer.parseInt(rawDataItem.substring(1)));
    }

    public static List<Record> getRecordList(Stream<String> rawDataStream) {
        return rawDataStream.map(rawDataItem -> parseRecord(rawDataItem)).collect(Collectors.toList());
    }
}
