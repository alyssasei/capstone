package com.example.alyssa.capstone;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


/**
 * Created by Alyssa on 3/21/2017.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView duration;

        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.EventName);
            duration = (TextView) v.findViewById(R.id.Duration);
        }
    }

    private List<Event> events;
    private Context context;

    public EventAdapter(List<Event> e, Context c) {
        events = e;
        context = c;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View eventView = inflater.inflate(R.layout.row, parent, false);
        return new ViewHolder(eventView);
    }

    @Override
    public void onBindViewHolder(EventAdapter.ViewHolder holder, int position) {
        Event event = events.get(position);
        holder.title.setText(event.getTitle());
        holder.duration.setText(Integer.toString(event.getMinutes()));
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public void addEvent(Event e) {
        events.add(e);
        notifyItemInserted(events.size() - 1);
    }
}
