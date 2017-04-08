package com.example.alyssa.capstone;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.everything.providers.android.calendar.Calendar;
import me.everything.providers.android.calendar.Event;
import me.everything.providers.android.calendar.CalendarProvider;

public class MainActivity extends AppCompatActivity {
    ArrayList<ListEvent> arrayList;
    ArrayList<CalendarEvent> calendarEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        arrayList = new ArrayList<>();
        final EventAdapter adapter = new EventAdapter(arrayList, this);

        adapter.setOnItemClickListener(new EventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, final int position) {
                new AlertDialog.Builder(MainActivity.this).setItems(new CharSequence[]{"Add to calendar", "Delete"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 1) {
                            adapter.deleteEvent(position);
                        } else {
                            long duration = adapter.getEvent(position).getMinutes() * 60000;
                            long startTime = findFreeTime(duration);
                            Intent intent = new Intent(Intent.ACTION_INSERT)
                                    .setData(CalendarContract.Events.CONTENT_URI)
                                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime)
                                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, startTime + duration)
                                    .putExtra(CalendarContract.Events.TITLE, adapter.getEvent(position).getTitle());
                            startActivity(intent);
                        }
                        dialog.dismiss();
                    }
                }).show();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final CreateDialogue dialogue = new CreateDialogue(MainActivity.this);
                dialogue.show();
                dialogue.setOnSaveListener(new CreateDialogue.DialogueDismissListener() {
                    @Override
                    public void onDismiss(String s, int i) {
                        Snackbar.make(view, "Item added", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        adapter.addEvent(new ListEvent(s, i));
                        dialogue.dismiss();
                    }
                });
            }
        });

        RecyclerView list = (RecyclerView) findViewById(R.id.list);
        ListEvent e = new ListEvent("homework", 50);
        arrayList.add(e);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onStart() {
        super.onStart();
        checkForPermission();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkForPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR}, 1);
        } else {
            importCalendar();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == 1) {
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED) {
               new AlertDialog.Builder(this).setMessage("Please give calendar permission").setNegativeButton("OK", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                   }
               }).show();
            } else {
                importCalendar();
            }
        }
    }

    private void importCalendar() {
        calendarEvents = new ArrayList<>();
        CalendarProvider provider = new CalendarProvider(this);
        for(Calendar c: provider.getCalendars().getList()) {
            if(!c.visible) {
                continue;
            }

            for(Event e: provider.getEvents(c.id).getList()) {
                if(e.allDay || !DateUtils.isToday(e.dTStart)) {
                    continue;
                }

                Date eStart = new Date(e.dTStart);
                Date eEnd = new Date(e.dTend);
                Date startLimit = new Date();
                Date endLimit = new Date();

                startLimit.setHours(9);
                startLimit.setMinutes(0);
                endLimit.setHours(22);
                endLimit.setMinutes(0);

                if(eEnd.before(startLimit) || eStart.after(endLimit)) {
                    continue;
                }

                calendarEvents.add(new CalendarEvent(e.dTStart, e.dTend));
            }
        }
    }

    private long findFreeTime(long duration) {
        for(int x = 0; x < calendarEvents.size() - 1; x++) {
            CalendarEvent e1 = calendarEvents.get(x);
            CalendarEvent e2 = calendarEvents.get(x + 1);
            long difference = e2.getStart() - e1.getEnd();
            if(difference >= duration) {
                return e1.getEnd();
            }
        }
        return System.currentTimeMillis();
    }
}
