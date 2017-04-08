package com.example.alyssa.capstone;

/**
 * Created by Alyssa on 3/21/2017.
 */

public class ListEvent {
    private String title;
    private int minutes;

    public ListEvent(String s, int m) {
        title = s;
        minutes = m;
    }

    public String getTitle() {
        return title;
    }

    public int getMinutes() {
        return minutes;
    }
}
