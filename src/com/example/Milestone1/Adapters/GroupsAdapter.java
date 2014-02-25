package com.example.Milestone1.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Milestone1.Classes.Groups;
import com.example.Milestone1.GroupsActivity;
import com.example.Milestone1.R;

/**
 * Created by Руслан on 22.10.13.
 */
public class GroupsAdapter extends BaseAdapter {

    private final LayoutInflater lInflater;
    private final String gType;
    private Groups[] results;
    private int level;
    private Activity a;

    public GroupsAdapter(Activity activity, Groups[] groups, String type) {
        results = groups;
        gType = type;
        a = activity;
        lInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {

        return results.length;
    }

    @Override
    public Object getItem(int position) {

        return results[position];
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    public static class ViewHolder {
        TextView tvName;
        TextView tvDescription;
        ImageView image;
        ImageButton btnNextLevel;
    }

    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder viewHolder;
        View v = view;
        if (v == null) {
            if (gType.equals("user_groups")) {
                v = lInflater.inflate(R.layout.user_groups_list_item, null);
            } else if (gType.equals("groups")) {
                v = lInflater.inflate(R.layout.groups_list_item, null);
            }
            viewHolder = new ViewHolder();
            viewHolder.tvName = (TextView) v.findViewById(R.id.tvName);
            viewHolder.tvDescription = (TextView) v.findViewById(R.id.tvDescription);
            viewHolder.image = (ImageView) v.findViewById(R.id.groupImage);
            viewHolder.btnNextLevel = (ImageButton) v.findViewById(R.id.btnNextLevel);

            v.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) v.getTag();

        }
        viewHolder.tvName.setText(results[position].getName());
        viewHolder.tvDescription.setText(results[position].getDescription());
        if (viewHolder.btnNextLevel != null) {
            viewHolder.btnNextLevel.setFocusable(false);
            viewHolder.btnNextLevel.setTag(position);
            viewHolder.btnNextLevel.setId(position);
            viewHolder.btnNextLevel.setOnClickListener(new View.OnClickListener() {
                public Exception exception;

                public void onClick(View view1) {
                    try {
                        int parent = view1.getId();
                        level = results[parent].getGroupLevel();
                        level++;
                        Intent intent = new Intent(a, GroupsActivity.class);
                        intent.putExtra("ParentID", results[parent].getId().toString());
                        intent.putExtra("level", level);
                        a.startActivity(intent);
                    } catch (Exception e) {
                        this.exception = e;
                    }
                }
            });
        }
        return v;
    }
}

