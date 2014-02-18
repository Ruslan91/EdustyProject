package com.example.Milestone1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.Milestone1.Classes.Marks;
import com.example.Milestone1.Classes.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Руслан on 07.12.13.
 */
public class MyMarksActivity extends Activity {
    private UUID journalID;
    private UUID token;
    private Response result;
    private String[] types;
    private Exception exception;
    private UUID userID;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marks_list);
        journalID = UUID.fromString(getIntent().getExtras().getString("journalID"));
        if (getIntent().getExtras().getString("userID") != null) {
            userID = UUID.fromString(getIntent().getExtras().getString("userID"));
        }
        token = UUID.fromString(getSharedPreferences("userdetails", MODE_PRIVATE).getString("token", ""));
        getActionBar().setTitle(getIntent().getExtras().getString("title"));
        types = getResources().getStringArray(R.array.journal_types);
        if (userID == null) {
            new GetMyMarks().execute();
        } else new GetUserMarks().execute();
    }

    public void setData(Response response) {
        try {
            Marks[] marks = (Marks[]) response.getItem();
            ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>(
                    marks.length);
            for (int i = 0; i < marks.length; i++) {
                HashMap<String, String> m = new HashMap<String, String>();
                Date datetime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(marks[i].getDate());
                m.put("date", new SimpleDateFormat("dd MMMM yyyy, EEEE").format(datetime) + " " + getString(R.string.lesson) + ": " + marks[i].getLessonNumber());
                m.put("type", types[marks[i].getType()] + ":");
                m.put("mark", marks[i].getMark().toString());
                m.put("description", marks[i].getDescription());
                data.add(m);
            }
            SimpleAdapter simpleAdapter = new SimpleAdapter(
                    this, data, R.layout.my_marks_list_item, new String[]{"date", "type", "mark", "description"}, new int[]{R.id.tvDate, R.id.tvType, R.id.tvMark, R.id.tvDescription
            });
            ListView listMarks = (ListView) findViewById(R.id.listMarks);
            listMarks.setAdapter(simpleAdapter);
        } catch (Exception e) {
            this.exception = e;
        }
    }

    public class GetMyMarks extends AsyncTask<UUID, Void, Response> {
        public Exception ex;

        ProgressDialog pdLoading = new ProgressDialog(MyMarksActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tЗагрузка...");
            pdLoading.show();
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            pdLoading.dismiss();
            pdLoading.setCancelable(false);
            setData(result);
        }

        @Override
        protected Response doInBackground(UUID... params) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                Gson gson = new Gson();
                HttpGet request = new HttpGet(getString(R.string.url) + "MyMarks?token=" + token + "&journalID=" + journalID);
                HttpResponse response = httpclient.execute(request);
                InputStreamReader reader = new InputStreamReader(response.getEntity()
                        .getContent(), HTTP.UTF_8);
                Type fooType = new TypeToken<Response<Marks[]>>() {
                }.getType();
                result = gson.fromJson(reader, fooType);
            } catch (Exception e) {
                this.ex = e;
            }
            return result;
        }
    }

    public class GetUserMarks extends AsyncTask<UUID, Void, Response> {
        public Exception ex;

        ProgressDialog pdLoading = new ProgressDialog(MyMarksActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tЗагрузка...");
            pdLoading.show();
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            pdLoading.dismiss();
            pdLoading.setCancelable(false);
            setData(result);
        }

        @Override
        protected Response doInBackground(UUID... params) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                Gson gson = new Gson();
                HttpGet request = new HttpGet(getString(R.string.url) + "Marks?token=" + token + "&userID=" + userID + "&journalID=" + journalID);
                HttpResponse response = httpclient.execute(request);
                InputStreamReader reader = new InputStreamReader(response.getEntity()
                        .getContent(), HTTP.UTF_8);
                Type fooType = new TypeToken<Response<Marks[]>>() {
                }.getType();
                result = gson.fromJson(reader, fooType);
            } catch (Exception e) {
                this.ex = e;
            }
            return result;
        }
    }
}