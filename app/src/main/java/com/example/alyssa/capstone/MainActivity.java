package com.example.alyssa.capstone;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ArrayList<Event> arrayList;

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
                        adapter.addEvent(new Event(s, i));
                        dialogue.dismiss();
                    }
                });
            }
        });

        RecyclerView list = (RecyclerView) findViewById(R.id.list);
        Event e = new Event("homework", 50);
        arrayList.add(e);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this));
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
}
