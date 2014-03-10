package com.example.Milestone1.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Milestone1.Classes.Event;
import com.example.Milestone1.Classes.SearchResult;
import com.example.Milestone1.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Руслан on 24.02.14.
 */
public class EventAdapter extends BaseAdapter {
    private final Event[] results;
    private final LayoutInflater lInflater;
    private Exception exception;

    public  EventAdapter(Activity activity, Event[] events) {
        results = events;
        lInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return results.length;
    }

    @Override
    public Object getItem(int i) {
        return results[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    public static class ViewHolder {
        TextView Title;
        TextView StartTime;
        TextView EndTime;
        TextView Location;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        View v = view;
        if (v == null) {
            v = lInflater.inflate(R.layout.event_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.Title = (TextView) v.findViewById(R.id.tvTitle);
            viewHolder.Location = (TextView) v.findViewById(R.id.tvLocation);
            viewHolder.StartTime = (TextView) v.findViewById(R.id.tvStartTime);
            viewHolder.EndTime = (TextView) v.findViewById(R.id.tvEndTime);

            v.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) v.getTag();

        }
        try {
            viewHolder.Title.setText(results[i].getTitle());
            viewHolder.Location.setText(results[i].getLocation());
            Date[] datetime = new Date[results.length];
            datetime[i] = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(results[i].getStartTime());
            viewHolder.StartTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(datetime[i]));
            datetime[i] = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(results[i].getEndTime());
            viewHolder.EndTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(datetime[i]));
        } catch (Exception e) {
            this.exception = e;
        }
        return v;
    }
}
