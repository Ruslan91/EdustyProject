package com.example.Milestone1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.example.Milestone1.Classes.JournalDates;
import com.example.Milestone1.Classes.Members;
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
public class JournalActivity extends Activity {
    private UUID token;
    private UUID journalID;
    private Response result;
    private Exception exception;
    private ArrayList<Map<String, String>> data;

    private ListView listJournalDates;
    private JournalDates[] dates;
    private Spinner spJournalTypes;
    private UUID groupID;
    private GetJournalMembers getJournalMembers;
    private SimpleAdapter simpleAdapter;
    private Members[] members;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.journal_dates_list);
        getActionBar().setTitle(getIntent().getExtras().getString("title"));
        token = UUID.fromString(getSharedPreferences("userdetails", MODE_PRIVATE).getString("token", ""));
        journalID = UUID.fromString(getIntent().getExtras().getString("journalID"));
        groupID = UUID.fromString(getIntent().getExtras().getString("groupID"));
        spJournalTypes = (Spinner) findViewById(R.id.spJournalTypes);
        spJournalTypes.setSelection(0);
        try {
            spJournalTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent,
                                           View itemSelected, int selectedItemPosition, long selectedId) {
                    if (selectedItemPosition == 0) {
                        GetDatesMyJournals getMarksMyJournal = new GetDatesMyJournals();
                        getMarksMyJournal.execute();
                    } else if (selectedItemPosition == 1) {
                        getJournalMembers = new GetJournalMembers();
                        getJournalMembers.execute();

                    }
                }

                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            listJournalDates = (ListView) findViewById(R.id.listJournalDates);
            listJournalDates.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (spJournalTypes.getSelectedItemPosition() == 0) {
                        Intent intent = new Intent(JournalActivity.this, MarksActivity.class);
                        intent.putExtra("date", dates[i].getDate());
                        intent.putExtra("journalID", journalID.toString());
                        intent.putExtra("groupID", groupID.toString());
                        intent.putExtra("lessonNumber", dates[i].getLessonNumber());
                        startActivity(intent);
                    } else if (spJournalTypes.getSelectedItemPosition() == 1) {
                        Intent intent = new Intent(JournalActivity.this, MyMarksActivity.class);
                        intent.putExtra("journalID", journalID.toString());
                        intent.putExtra("userID", members[i].getId().toString());
                        startActivity(intent);
                    }
                }
            });
        } catch (Exception e) {
            this.exception = e;
        }
    }

    public void setData(Response response) {
        try {
            if (spJournalTypes.getSelectedItemPosition() == 0) {
                dates = (JournalDates[]) response.getItem();
                data = new ArrayList<Map<String, String>>();
                data.clear();
                for (int i = 0; i < dates.length; i++) {
                    HashMap<String, String> m = new HashMap<String, String>();
                    Date datetime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(dates[i].getDate());
                    m.put("date", new SimpleDateFormat("EEEE, dd MMMM").format(datetime));
                    m.put("lesson", dates[i].getLessonNumber().toString());
                    data.add(m);
                    simpleAdapter = new SimpleAdapter(JournalActivity.this, data, R.layout.journal_dates_list_item,
                            new String[]{"date", "lesson"},
                            new int[]{R.id.tvDate, R.id.tvLessonNumber});

                }
            } else if (spJournalTypes.getSelectedItemPosition() == 1) {
                members = (Members[]) response.getItem();
                data = new ArrayList<Map<String, String>>();
                data.clear();
                for (int i = 0; i < members.length; i++) {
                    HashMap<String, String> m = new HashMap<String, String>();
                    m.put("name", members[i].getFirstName());
                    m.put("lastname", members[i].getLastName());
                    data.add(m);
                }
                simpleAdapter = new SimpleAdapter(JournalActivity.this, data, R.layout.friend_list_item,
                        new String[]{"name", "lastname"},
                        new int[]{R.id.firstName, R.id.lastName});
            }

            listJournalDates.setAdapter(simpleAdapter);
        } catch (Exception e) {
            this.exception = e;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.journal_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create:
                Intent intent = new Intent(this, CreateMarksJournalActivity.class);
                intent.putExtra("journalID", journalID.toString());
                intent.putExtra("groupID", groupID.toString());
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public class GetDatesMyJournals extends AsyncTask<Void, Void, Response> {
        public Exception ex;

        ProgressDialog pdLoading = new ProgressDialog(JournalActivity.this);

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
                HttpGet request = new HttpGet(getString(R.string.url) + "JournalDates?token=" + token + "&journalID=" + journalID);
                HttpResponse response = httpclient.execute(request);
                InputStreamReader reader = new InputStreamReader(response.getEntity()
                        .getContent(), HTTP.UTF_8);
                Type fooType = new TypeToken<Response<JournalDates[]>>() {
                }.getType();
                result = gson.fromJson(reader, fooType);

            } catch (Exception e) {
                this.ex = e;
            }
            return result;
        }
    }

    public class GetJournalMembers extends AsyncTask<UUID, Void, Response> {
        public Exception ex;
        ProgressDialog pdLoading = new ProgressDialog(JournalActivity.this);

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
        protected Response doInBackground(UUID... params) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                Gson gson = new Gson();
                Integer offset = null;
                Integer count = null;
                HttpGet request = new HttpGet(getString(R.string.url) + "GroupMembers?token=" + token + "&groupID=" + groupID + "&offset=" + offset + "&count=" + count);
                HttpResponse response = httpclient.execute(request);
                InputStreamReader reader = new InputStreamReader(response.getEntity()
                        .getContent(), HTTP.UTF_8);
                Type fooType = new TypeToken<Response<Members[]>>() {
                }.getType();
                result = gson.fromJson(reader, fooType);
            } catch (Exception e) {
                this.ex = e;
            }
            return result;
        }
    }
}