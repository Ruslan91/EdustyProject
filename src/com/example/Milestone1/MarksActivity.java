package com.example.Milestone1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Руслан on 12.12.13.
 */
public class MarksActivity extends Activity {
    UUID journalID;
    private UUID token;
    private String date;
    private Response result;
    private ArrayList<Map<String, String>> data;
    private Exception exception;
    private ListView listMarks;
    private Marks[] marks;
    private UUID groupID;
    private String[] types;
    private int lessonNumber;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marks_list);
        try {
            token = UUID.fromString(getSharedPreferences("userdetails", MODE_PRIVATE).getString("token", ""));
            journalID = UUID.fromString(getIntent().getExtras().getString("journalID"));
            groupID = UUID.fromString(getIntent().getExtras().getString("groupID"));
            date = getIntent().getExtras().getString("date");
            lessonNumber = getIntent().getExtras().getInt("lessonNumber");
            types = getResources().getStringArray(R.array.journal_types);
            new GetMarks().execute();
            listMarks = (ListView) findViewById(R.id.listMarks);

        } catch (Exception e) {
            this.exception = e;
        }
    }

    public void setData(Response response) {
        try {
            marks = (Marks[]) response.getItem();
            data = new ArrayList<Map<String, String>>();
            data.clear();
            for (int i = 0; i < marks.length; i++) {
                HashMap<String, String> m = new HashMap<String, String>();
                m.put("title", marks[i].getUserName());
                //Date datetime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(marks[i].getDate());
                m.put("mark", marks[i].getMark().toString());
                m.put("type", types[marks[i].getType()] + ":");
                m.put("description", marks[i].getDescription());
                data.add(m);
            }
            SimpleAdapter simpleAdapter = new SimpleAdapter(MarksActivity.this, data, R.layout.marks_list_item,
                    new String[]{"title", "mark", "type", "description"},
                    new int[]{R.id.tvTitle, R.id.tvMark, R.id.tvType, R.id.tvDescription});
            listMarks.setAdapter(simpleAdapter);
        } catch (Exception e) {
            this.exception = e;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.marks_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                Intent intent = new Intent(this, EditMarksJournalActivity.class);
                intent.putExtra("journalID", journalID.toString());
                intent.putExtra("groupID", groupID.toString());
                intent.putExtra("date", date);
                intent.putExtra("lessonNumber", lessonNumber);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public class GetMarks extends AsyncTask<Void, Void, Response> {
        public Exception ex;
        ProgressDialog pdLoading = new ProgressDialog(MarksActivity.this);

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
            setData(result);
        }

        @Override
        protected Response doInBackground(Void... params) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                Gson gson = new Gson();
                HttpGet request = new HttpGet(getString(R.string.url) + "Marks?token=" + token + "&journalID=" + journalID + "&date=" + date);
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