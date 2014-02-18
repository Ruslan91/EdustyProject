package com.example.Milestone1.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.Milestone1.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Руслан on 25.10.13.
 */
public class FeedAdapter extends SimpleAdapter {
    /*    private final Context ctx;
        private final LayoutInflater lInflater;*/
    private ArrayList<Map<String, String>> results;
    private Context context;
    private int pos;
    private AsyncTask<ImageView, Void, Bitmap> task;
    private ImageView image;

    public FeedAdapter(Context context, ArrayList<Map<String, String>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.results = data;
        this.context = context;
/*        this.ctx = context;
        this.lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);*/
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

    public static class ViewHolder {
        public TextView tvTitle;
        public EditText etDescription;
        public EditText etMark;
        public Spinner spType;
        public CheckBox cbMark;
        public int ref;
    }

    public View getView(int position, View view, ViewGroup parent) {
        pos = position;
        /*View v = view;
        if (v == null) {
            v = lInflater.inflate(R.layout.feed_list_item, parent, false);
        }*/
        View v = view;
        LayoutInflater vi;
        vi = LayoutInflater.from(getContext());
        v = vi.inflate(R.layout.feed_list_item, parent, false);
        TextView tvName = (TextView) v.findViewById(R.id.tvName);
        tvName.setText(results.get(position).get("names"));
        TextView tvMessage = (TextView) v.findViewById(R.id.tvMessage);
        tvMessage.setText(results.get(position).get("messages"));
        TextView tvTime = (TextView) v.findViewById(R.id.tvTime);
        tvTime.setText(results.get(position).get("times"));
        TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
        tvTitle.setText(results.get(position).get("titles"));
        image = (ImageView) v.findViewById(R.id.imgFeed);
        if (results.get(position).get("picture") != null) {
            String URL = results.get(position).get("picture");
            image.setTag(URL);
            //task = new DownloadImagesTask().execute(image);
        }
        /*try {
            Bitmap bit = task.get();
            if (bit != null) {
                image.setImageBitmap(bit);
            } else {
                image.setImageResource(R.drawable.icon);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }*/
        return v;
    }

}



