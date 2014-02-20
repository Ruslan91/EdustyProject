package com.example.Milestone1.ServerConnect;

import android.app.AlertDialog;
import android.os.AsyncTask;

import com.example.Milestone1.Classes.Response;
import com.example.Milestone1.R;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import java.io.InputStreamReader;


/**
 * Created by Руслан on 20.02.14.
 */
public class GetStatusCode extends AsyncTask<AlertDialog, Void, Response> {

    public Exception ex;
    private Response result;
    Integer statusCode = null;

    @Override
    protected Response doInBackground(AlertDialog... params) {
        try {
            HttpClient httpclient = new DefaultHttpClient();
            Gson gson = new Gson();
            HttpGet request = new HttpGet((R.string.url) + "StatusCode=" + statusCode);
            HttpResponse response = httpclient.execute(request);
            InputStreamReader reader = new InputStreamReader(response.getEntity()
                    .getContent(), HTTP.UTF_8);
            result = gson.fromJson(reader, Response.class);
        } catch (Exception e) {
            this.ex = e;
        }
        return result;
    }
}