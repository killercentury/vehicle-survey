package com.shararik.vehiclesurvey;

/**
 * Created by kira on 10/05/15.
 */
public class Record {
    private Hose hose;
    // the number of milliseconds since midnight
    private int time;

    public Record(Hose hose, int time) {
        this.hose = hose;
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public Hose getHose() {
        return hose;
    }

    public void setHose(Hose hose) {
        this.hose = hose;
    }
}
