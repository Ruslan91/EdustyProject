package com.example.Milestone1.ServerConnect;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.example.Milestone1.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Руслан on 23.11.13.
 */
public class DownloadImagesTask extends AsyncTask<ImageView, Void, Bitmap> {

    ImageView imageView = null;

    @Override
    protected Bitmap doInBackground(ImageView... imageViews) {
        this.imageView = imageViews[0];
        return download_Image((String) imageView.getTag());
    }

    /*@Override
    protected void onPostExecute(Bitmap result) {
        //imageView.findViewById(R.id.imgFeed);
        if (result != null) {
            imageView.setImageBitmap(result);
        } else {
            imageView.setImageResource(android.R.drawable.ic_menu_help);
        }
    }*/

    private Bitmap download_Image(String url) {
        Bitmap bm = null;
        BufferedInputStream bis = null;
        InputStream is = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            is = conn.getInputStream();
            bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
/*            bis.close();
            is.close();*/
        } catch (IOException e) {
            Log.e("Hub", "Error getting the image from server : " + e.getMessage().toString());
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bm;
        //---------------------------------------------------
    }
}