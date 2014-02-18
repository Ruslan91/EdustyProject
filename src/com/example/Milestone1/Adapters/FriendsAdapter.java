package com.example.Milestone1.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.Milestone1.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Руслан on 26.11.13.
 */
public class FriendsAdapter extends SimpleAdapter {
    private ArrayList<Map<String, String>> results;
    private Context context;
    private int pos;
    private AsyncTask<ImageView, Void, Bitmap> task;
    private ImageView image;

    public FriendsAdapter(Context context, ArrayList<Map<String, String>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.results = data;
        this.context = context;
    }

    @Override
    public int getCount() {

        return results.size();
    }

    @Override
    public Object getItem(int position) {

        return results.get(position);
    }

    public Object getAppName(int position) {

        return results.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    public Context getContext() {
        return context;
    }

    public View getView(int position, View view, ViewGroup parent) {
        pos = position;
        View v = view;
        LayoutInflater vi;
        vi = LayoutInflater.from(getContext());
        v = vi.inflate(R.layout.friend_list_item, parent, false);
        TextView tvFirstName = (TextView) v.findViewById(R.id.firstName);
        tvFirstName.setText(results.get(position).get("name"));
        TextView tvLastName = (TextView) v.findViewById(R.id.lastName);
        tvLastName.setText(results.get(position).get("lastName"));
        image = (ImageView) v.findViewById(R.id.userImage);
        image.setImageResource(R.drawable.social_person);
        if (results.get(position).get("picture") != null) {
            String URL = results.get(position).get("picture");
            image.setTag(URL);
            //task = new DownloadImagesTask().execute(image);
        }
        return v;
    }
}
