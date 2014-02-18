package com.example.Milestone1.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.Milestone1.GroupsActivity;
import com.example.Milestone1.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Руслан on 22.10.13.
 */
public class GroupsAdapter extends SimpleAdapter {

    private ArrayList<Map<String, String>> results;
    private Context context;
    public int pos;
    private int level;
    private String parentID;
    public GroupsActivity activity;
    private AsyncTask<ImageView, Void, Bitmap> task;

    public GroupsAdapter(Context context, ArrayList<Map<String, String>> data, int resource, String[] from, int[] to) {
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

    public View getView(int position, View view, ViewGroup parent) {
        pos = position;
        View v = view;
        LayoutInflater vi;
        vi = LayoutInflater.from(getContext());
        if (results.get(position).get("layout") == "user_groups") {
            v = vi.inflate(R.layout.user_groups_list_item, parent, false);
        } else if (results.get(position).get("layout") == "groups")
            v = vi.inflate(R.layout.groups_list_item, parent, false);

        TextView tvName = (TextView) v.findViewById(R.id.groupName);
        tvName.setText(results.get(position).get("textN"));
        TextView tvDescription = (TextView) v.findViewById(R.id.groupDescription);
        tvDescription.setText(results.get(position).get("textD"));
        ImageView image = (ImageView) v.findViewById(R.id.groupImage);
        image.setImageResource(R.drawable.icon);
        if (results.get(position).get("picture") != null) {
            String URL = results.get(position).get("picture");
            image.setTag(URL);
            //task = new DownloadImagesTask().execute(image);
        }
        ImageButton btnNextLevel = (ImageButton) v.findViewById(R.id.btnNextLevel);
        if (btnNextLevel != null) {
            btnNextLevel.setFocusable(false);
            btnNextLevel.setTag(position);
            btnNextLevel.setId(position);
            btnNextLevel.setOnClickListener(new View.OnClickListener() {
                public Exception exception;

                public void onClick(View view1) {
                    try {
                        int parent = view1.getId();
                        parentID = results.get(parent).get("groupID");
                        level = Integer.parseInt(results.get(parent).get("level"));
                        level++;
                        Intent intent = new Intent(getContext().getApplicationContext(), GroupsActivity.class);
                        intent.putExtra("ParentID", parentID);
                        intent.putExtra("level", level);
                        getContext().startActivity(intent);
                        //getContext().
                    } catch (Exception e) {
                        this.exception = e;
                    }
                }
            });
        }
        return v;
    }

    public Context getContext() {
        return context;
    }
}

