package com.example.Milestone1.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
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
import com.example.Milestone1.ServerConnect.DownloadImagesTask;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Руслан on 25.10.13.
 */
public class FeedAdapter extends SimpleAdapter {
    private final LayoutInflater lInflater;
    /*    private final Context ctx;
            private final LayoutInflater lInflater;*/
    private ArrayList<Map<String, String>> results;
    private Context context;
    ViewHolder viewHolder = null;

    public FeedAdapter(Context context, ArrayList<Map<String, String>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.results = data;
        this.context = context;
        this.lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        public TextView tvName;
        public TextView tvMessage;
        public TextView tvTime;
        public TextView tvTitle;
        public ImageView image;
        public int ref;
    }

    public View getView(int position, View view, ViewGroup parent) {

        View v = view;
        if (v == null) {
            v = lInflater.inflate(R.layout.feed_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tvName = (TextView) v.findViewById(R.id.tvName);
            viewHolder.tvMessage = (TextView) v.findViewById(R.id.tvMessage);
            viewHolder.tvTitle = (TextView) v.findViewById(R.id.tvTitle);
            viewHolder.tvTime = (TextView) v.findViewById(R.id.tvTime);
            viewHolder.image = (ImageView) v.findViewById(R.id.imgFeed);
            v.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) v.getTag();
        }
        viewHolder.tvName.setText(results.get(position).get("names"));
        viewHolder.tvMessage.setText(results.get(position).get("messages"));
        viewHolder.tvTime.setText(results.get(position).get("times"));
        viewHolder.tvTitle.setText(results.get(position).get("titles"));
        if (results.get(position).get("picture") != null) {
            String URL = results.get(position).get("picture");
/*            image.setImageBitmap(download_Image(URL));*/

            viewHolder.image.setTag(URL);
            new DownloadImagesTask().execute(viewHolder.image);
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
/*    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            publishProgress(new Void[]{});

            String url = "";
            if( params.length > 0 ){
                url = params[0];
            }

            InputStream input = null;

            try {
                URL urlConn = new URL(url);
                input = urlConn.openStream();
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            return BitmapFactory.decodeStream(input);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            viewHolder.image.setImageBitmap(result);
        }
    }*/
}



