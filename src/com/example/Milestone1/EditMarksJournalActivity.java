package com.example.Milestone1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.Milestone1.Adapters.EditMarksJournalAdapter;
import com.example.Milestone1.Classes.EditMark;
import com.example.Milestone1.Classes.Marks;
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
 * Created by Руслан on 13.12.13.
 */
public class EditMarksJournalActivity extends Activity {

    private UUID token;
    private UUID journalID;
    private String date;
    private ListView listMarks;
    private Exception exception;
    private Marks[] marks;
    private ArrayList<Map<String, String>> data;
    private Response result;
    private EditMarksJournalAdapter simpleAdapter;
    private static MarksRead[] marksReads;
    private EditMark editMark;
    private UUID groupID;
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
            new GetMarks().execute();
            listMarks = (ListView) findViewById(R.id.listMarks);
            listMarks.setItemsCanFocus(true);

        } catch (Exception e) {
            this.exception = e;
        }
    }

    public void setData(Response response) {
        try {
            marks = (Marks[]) response.getItem();
            data = new ArrayList<Map<String, String>>();
            //data.clear();
            for (int i = 0; i < marks.length; i++) {
                HashMap<String, String> m = new HashMap<String, String>();
                m.put("title", marks[i].getUserName());
                m.put("mark", marks[i].getMark().toString());
                m.put("type", marks[i].getType().toString());
                m.put("description", marks[i].getDescription());
                data.add(m);
            }
            simpleAdapter = new EditMarksJournalAdapter(this, data);
            listMarks.setAdapter(simpleAdapter);
            simpleAdapter.notifyDataSetChanged();

        } catch (Exception e) {
            this.exception = e;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case R.id.action_apply:
/*                    int count = listMarks.getCount();
                    marksReads = new MarksRead[count];
                    editMark = new EditMark();
                    for (int i = 0; i < count; i++) {
                        marksReads[i] = new MarksRead();
                        View v = (View) listMarks.getAdapter().getView(i,null,listMarks);
                        ViewGroup row = (ViewGroup) listMarks.getChildAt(i);
                        if (v != null) {
                            Spinner spType = (Spinner) v.findViewById(R.id.spType);
                            EditText etMark = (EditText) v.findViewById(R.id.etMark);
                            EditText etDescription = (EditText) v.findViewById(R.id.etDescription);
                            CheckBox cbMark = (CheckBox) v.findViewById(R.id.cbMark);
                            marksReads[i].setType(spType.getSelectedItemPosition());
                            marksReads[i].setDescription(etDescription.getText().toString());
                            marksReads[i].setMark(Integer.valueOf(etMark.getText().toString()));
                            marksReads[i].setId(marks[i].getId());
                            marksReads[i].setUserID(marks[i].getUserID());
                        }
                    }*/
                    data = simpleAdapter.getData();
                    marksReads = new MarksRead[data.size()];
                    editMark = new EditMark();
                    //Object[] objects = new Object[data.size()];
                    for (int i = 0; i < data.size(); i++) {
                        marksReads[i] = new MarksRead();
                        Map<String, String> m = new HashMap<>();
                        m = (Map<String, String>) data.get(i);
                        for (Map.Entry<String, String> entry : m.entrySet()) {
                            String key = entry.getKey();
                            if (key == "type") {
                                String value = entry.getValue();
                                marksReads[i].setType(Integer.valueOf(value));
                            } else if (key == "description") {
                                String value = entry.getValue();
                                marksReads[i].setDescription(value);
                            } else if (key == "mark") {
                                String value = entry.getValue();
                                marksReads[i].setMark(Integer.valueOf(value));
                            }
                        }
                        marksReads[i].setId(marks[i].getId());
                        marksReads[i].setUserID(marks[i].getUserID());
                    }
                    editMark.setItem(marksReads);
                    editMark.setToken(token);
                    editMark.setJournalID(journalID);
                    editMark.setLessonNumber(lessonNumber);

                    EditMarks editMarks = new EditMarks();
                    editMarks.execute();
            }
        } catch (Exception e) {
            this.exception = e;
        }
        return super.onOptionsItemSelected(item);
    }

    public class GetMarks extends AsyncTask<Void, Void, Response> {
        public Exception ex;
        ProgressDialog pdLoading = new ProgressDialog(EditMarksJournalActivity.this);

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
            simpleAdapter.getItem(0);
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

    public class EditMarks extends AsyncTask<Void, Void, Response> {

        private Exception ex;

        ProgressDialog pdLoading = new ProgressDialog(EditMarksJournalActivity.this);

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
            if (result.getItem() == Boolean.TRUE /*&& result.getStatusCode() == 3*/) {
                Intent intent = new Intent(EditMarksJournalActivity.this, JournalActivity.class);
                intent.putExtra("journalID", journalID.toString());
                intent.putExtra("groupID", groupID.toString());
                startActivity(intent);
                finish();
            } else
                Toast.makeText(EditMarksJournalActivity.this, getString(R.string.error_please_try_again), Toast.LENGTH_SHORT).show();
        }

        protected Response doInBackground(Void... params) {

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost request = new HttpPost(getString(R.string.url) + "MarksEdit");
                StringEntity entity = new StringEntity(new Gson().toJson(editMark),
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