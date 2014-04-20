package com.example.Milestone1.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Milestone1.Classes.Follows;
import com.example.Milestone1.R;
import com.example.Milestone1.ServerConnect.DownloadImagesTask;

public class FollowsAdapter extends BaseAdapter {

    private final Follows[] results;
    private final LayoutInflater lInflater;

    ViewHolder viewHolder = null;

    public FollowsAdapter(Context context, Follows[] follows) {
        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        results = follows;
    }

    @Override
    public int getCount() {
        return results.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public static class ViewHolder {
        public TextView Title;
        public TextView Description;
        public ImageView image;
    }

    public View getView(int position, View view, ViewGroup parent) {
        View v = view;
        if (v == null) {
            v = lInflater.inflate(R.layout.follows_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.Title = (TextView) v.findViewById(R.id.tvTitle);
            viewHolder.Description = (TextView) v.findViewById(R.id.tvDescription);
            viewHolder.image = (ImageView) v.findViewById(R.id.image);
            v.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) v.getTag();
        }
        if (results[position].getTitle() == null) {
            viewHolder.Title.setText(results[position].getFirstName() + " " + results[position].getLastName());
            viewHolder.Description.setText(results[position].getDescription());
        } else /*if (results[position].get__type().equals("GroupJoin") || results[position].get__type() == null)*/ {
            viewHolder.Title.setText(results[position].getTitle());
            viewHolder.Description.setText(results[position].getDescription());
        }
        if (results[position].getPictureID() != null) {
            String URL = v.getResources().getString(R.string.url) + "File?token=" + results[position].getId().toString() + "&fileID=" + results[position].getPictureID().toString();
            viewHolder.image.setTag(URL);
            new DownloadImagesTask().execute(viewHolder.image);
        }
        return v;
    }
}
