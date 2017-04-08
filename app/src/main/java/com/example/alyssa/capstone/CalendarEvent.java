package com.example.alyssa.capstone;

/**
 * Created by Alyssa on 4/8/2017.
 */

public class CalendarEvent {

    private long start;
    private long end;

    public CalendarEvent(long s, long e) {
        start = s;
        end = e;
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }
}
