package com.example.Milestone1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.Milestone1.Classes.Courses;
import com.example.Milestone1.Classes.CreateJournal;
import com.example.Milestone1.Classes.Groups;
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
import java.util.UUID;

/**
 * Created by Руслан on 06.12.13.
 */
public class CreateJournalActivity extends Activity {
    private Groups[] groups;
    private UUID token;
    private EditText etJournalName;
    private Spinner spJournalGroup;
    private Response result, group_result, course_result;
    private Spinner spJournalCourse;
    private Courses[] courses;
    private String[] group_names;
    private String[] course_names;
    private Exception exception;
    private CreateJournal createJournal;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.journal_create);
        SharedPreferences sharedPreferences = getSharedPreferences("userdetails", MODE_PRIVATE);
        token = UUID.fromString(sharedPreferences.getString("token", ""));

        etJournalName = (EditText) findViewById(R.id.etJournalName);
        spJournalGroup = (Spinner) findViewById(R.id.spJournalGroup);
        spJournalCourse = (Spinner) findViewById(R.id.spJournalCourse);
        GetUserCourses getUserCourses = new GetUserCourses();
        getUserCourses.execute();
        GetUserGroups getUserGroups = new GetUserGroups();
        getUserGroups.execute();
        try {
            result = getUserCourses.get();
            courses = (Courses[]) result.getItem();
            course_names = new String[courses.length];
            for (int i = 0; i < courses.length; i++) course_names[i] = courses[i].getTitle();
            result = getUserGroups.get();
            groups = (Groups[]) result.getItem();
            group_names = new String[groups.length];
            for (int i = 0; i < groups.length; i++) group_names[i] = groups[i].getName();
            ArrayAdapter<String> adapter_courses = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, course_names);
            adapter_courses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spJournalCourse.setAdapter(adapter_courses);
            ArrayAdapter<String> adapter_groups = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, group_names);
            adapter_groups.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spJournalGroup.setAdapter(adapter_groups);
        } catch (Exception e) {
            this.exception = e;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_event_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean ret = false;
        if (item.getItemId() == R.id.action_save) {
            ret = true;
            try {
                createJournal = new CreateJournal();
                if (!etJournalName.getText().toString().equals("")) {
                    createJournal.setTitle(etJournalName.getText().toString());

                } else
                    Toast.makeText(this, getString(R.string.please_set_text), Toast.LENGTH_SHORT).show();
                createJournal.setToken(token);
                createJournal.setCourseID(courses[spJournalCourse.getSelectedItemPosition()].getId());
                createJournal.setGroupID(groups[spJournalGroup.getSelectedItemPosition()].getId());

                PostCreateJournal postCreateJournal = new PostCreateJournal();
                postCreateJournal.execute();
            } catch (Exception e) {
                Toast.makeText(this, getString(R.string.error_please_try_again), Toast.LENGTH_SHORT).show();
            }
        }
        return ret;
    }

    public class GetUserGroups extends AsyncTask<UUID, Void, Response> {
        public Exception ex;

        @Override
        protected Response doInBackground(UUID... params) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                Gson gson = new Gson();
                HttpGet request = new HttpGet(getString(R.string.url) + "Groups?token=" + token);
                HttpResponse response = httpclient.execute(request);
                InputStreamReader reader = new InputStreamReader(response.getEntity()
                        .getContent(), HTTP.UTF_8);
                Type fooType = new TypeToken<Response<Groups[]>>() {
                }.getType();
                result = gson.fromJson(reader, fooType);
            } catch (Exception e) {
                this.ex = e;
            }
            return result;
        }
    }

    public class GetUserCourses extends AsyncTask<UUID, Void, Response> {
        public Exception ex;

        @Override
        protected Response doInBackground(UUID... params) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                Gson gson = new Gson();
                HttpGet request = new HttpGet(getString(R.string.url) + "Courses?token=" + token);
                HttpResponse response = httpclient.execute(request);
                InputStreamReader reader = new InputStreamReader(response.getEntity()
                        .getContent(), HTTP.UTF_8);
                Type fooType = new TypeToken<Response<Courses[]>>() {
                }.getType();
                result = gson.fromJson(reader, fooType);
            } catch (Exception e) {
                this.ex = e;
            }
            return result;
        }
    }

    public class PostCreateJournal extends AsyncTask<Void, Void, Response> {

        private Exception ex;
        ProgressDialog progressDialog = new ProgressDialog(CreateJournalActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage(getString(R.string.please_wait));
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            if (response.getItem() != null) {
                progressDialog.dismiss();
                startActivity(new Intent(CreateJournalActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(CreateJournalActivity.this, getString(R.string.error_please_try_again), Toast.LENGTH_SHORT).show();
            }
        }

        protected Response doInBackground(Void... params) {

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost request = new HttpPost(getString(R.string.url) + "Journal");
                StringEntity entity = new StringEntity(new Gson().toJson(createJournal),
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