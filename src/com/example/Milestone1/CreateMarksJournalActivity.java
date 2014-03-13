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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.Milestone1.Adapters.CreateMarksJournalAdapter;
import com.example.Milestone1.Classes.EditMark;
import com.example.Milestone1.Classes.Follows;
import com.example.Milestone1.Classes.MarksRead;
import com.example.Milestone1.Classes.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Руслан on 20.12.13.
 */
public class CreateMarksJournalActivity extends Activity {
    private Response result;
    private UUID token;
    private UUID groupID;
    private UUID journalID;
    private ArrayList<Map<String, String>> data;
    private Exception exception;
    private MarksRead[] marksReads;
    private Follows[] members;
    private EditMark createMarks;
    private int item_pos;
    private ListView listMarks;
    private CreateMarksJournalAdapter createMarksJournalAdapter;
    private int spType_pos;
    private HashMap<String, String> m;
    private Spinner spType;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marks_create_list);
        token = UUID.fromString(getSharedPreferences("userdetails", MODE_PRIVATE).getString("token", ""));
        journalID = UUID.fromString(getIntent().getStringExtra("journalID"));
        groupID = UUID.fromString(getIntent().getStringExtra("groupID"));
        new GetJournalMembers().execute();
        Spinner spLessonNumber = (Spinner) findViewById(R.id.spLessonNumber);
        String[] listLessonsNumbers = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listLessonsNumbers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLessonNumber.setAdapter(adapter);
        spLessonNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                item_pos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_journal_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case R.id.action_save:
                    /*int count = listMarks.getCount();
                    marksReads = new MarksRead[count];
                    createMarks = new EditMark();
                    for (int i = 0; i < count; i++) {
                        marksReads[i] = new MarksRead();
                        View v = listMarks.getChildAt(i);
                        ViewGroup row = (ViewGroup) listMarks.getChildAt(i);
                        if (row != null) {
                            //Spinner spType = (Spinner) row.findViewById(R.id.spType);
                            EditText etMark = (EditText) row.findViewById(R.id.etMark);
                            EditText etDescription = (EditText) row.findViewById(R.id.etDescription);
                            //CheckBox cbMark = (CheckBox) row.findViewById(R.id.cbMark);
                            marksReads[i].setType(spType_pos);
                            marksReads[i].setDescription(etDescription.getText().toString());
                            marksReads[i].setMark(Integer.valueOf(etMark.getText().toString()));
                            marksReads[i].setUserID(members[i].getId());
                        }
                    }*/

                    data = createMarksJournalAdapter.getData();
                    marksReads = new MarksRead[data.size()];
                    createMarks = new EditMark();
                    for (int i = 0; i < data.size(); i++) {
                        marksReads[i] = new MarksRead();
                        Map<String, String> m = new HashMap<>();
                        m = (Map<String, String>) data.get(i);
                        for (Map.Entry<String, String> entry : m.entrySet()) {
                            String key = entry.getKey();
                            if (key == "description") {
                                String value = entry.getValue();
                                marksReads[i].setDescription(value);
                            } else if (key == "mark") {
                                String value = entry.getValue();
                                marksReads[i].setMark(Integer.valueOf(value));
                            }
                        }
                        marksReads[i].setType(spType_pos);
                        marksReads[i].setUserID(members[i].getId());
                    }
                    createMarks.setItem(marksReads);
                    createMarks.setJournalID(journalID);
                    createMarks.setToken(token);
                    createMarks.setLessonNumber(item_pos + 1);

                    new CreateMarks().execute();
            }
        } catch (Exception e) {
            this.exception = e;

        }
        return true;
    }

    private void setData(Response response) {
        members = (Follows[]) response.getItem();
        data = new ArrayList<Map<String, String>>();

        listMarks = (ListView) findViewById(R.id.listMarks);
        listMarks.setItemsCanFocus(true);
        for (int i = 0; i < members.length; i++) {
            m = new HashMap<>();
            m.put("title", members[i].getFirstName() + " " + members[i].getLastName());
            m.put("mark", "0");
            m.put("description", "");
            data.add(m);
        }
        createMarksJournalAdapter = new CreateMarksJournalAdapter(this, data);
        listMarks.setAdapter(createMarksJournalAdapter);
        createMarksJournalAdapter.notifyDataSetChanged();

        spType = (Spinner) findViewById(R.id.spType);
        spType.setFocusableInTouchMode(true);
        spType.setSelection(0);
        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                spType_pos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    public class GetJournalMembers extends AsyncTask<UUID, Void, Response> {
        public Exception ex;

        ProgressDialog pdLoading = new ProgressDialog(CreateMarksJournalActivity.this);

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
                Type fooType = new TypeToken<Response<Follows[]>>() {
                }.getType();
                result = gson.fromJson(reader, fooType);
            } catch (Exception e) {
                this.ex = e;
            }
            return result;
        }
    }

    public class CreateMarks extends AsyncTask<Void, Void, Response> {

        private Exception ex;

        ProgressDialog pdLoading = new ProgressDialog(CreateMarksJournalActivity.this);

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
            if (result.getItem() == Boolean.TRUE) {
                Intent intent = new Intent(CreateMarksJournalActivity.this, JournalActivity.class);
                intent.putExtra("journalID", journalID.toString());
                intent.putExtra("groupID", groupID.toString());
                startActivity(intent);
                finish();
            } else
                Toast.makeText(CreateMarksJournalActivity.this, getString(R.string.error_please_try_again), Toast.LENGTH_SHORT).show();
        }

        protected Response doInBackground(Void... params) {

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost request = new HttpPost(getString(R.string.url) + "Marks");
                StringEntity entity = new StringEntity(new Gson().toJson(createMarks),
                        HTTP.UTF_8);
                entity.setContentType("application/json");
                request.setEntity(entity);
                HttpResponse response = httpclient.execute(request);
                InputStreamReader reader = new InputStreamReader(response.getEntity().getContent());
                result = new Gson().fromJson(reader, Response.class);

            } catch (Exception e) {
                this.ex = e;
            }
            return result;
        }
    }
}