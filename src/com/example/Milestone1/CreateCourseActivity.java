package com.example.Milestone1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.Milestone1.Classes.CreateCourse;
import com.example.Milestone1.Classes.Response;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import java.io.InputStreamReader;
import java.util.UUID;

/**
 * Created by Руслан on 06.12.13.
 */
public class CreateCourseActivity extends Activity {
    private UUID token;
    private EditText etCourseName;
    private Response result;
    private CreateCourse createCourse;
    Exception exception;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_create);
        SharedPreferences sharedPreferences = getSharedPreferences("userdetails", MODE_PRIVATE);
        token = UUID.fromString(sharedPreferences.getString("token", ""));

        etCourseName = (EditText) findViewById(R.id.etCourseName);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_event_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean ret = false;
        if (item.getItemId() == R.id.action_apply) {
            ret = true;
            try {
                if (etCourseName.getText().toString().equals("")) {
                    Toast.makeText(this, getString(R.string.please_set_text), Toast.LENGTH_SHORT).show();
                } else {
                    createCourse = new CreateCourse();
                    createCourse.setTitle(etCourseName.getText().toString());
                    createCourse.setToken(token);
                    PostCreateCourse postCreateCourse = new PostCreateCourse();
                    postCreateCourse.execute();
                }
            } catch (Exception e) {
                this.exception = e;
            }
        }
        return ret;
    }

    public class PostCreateCourse extends AsyncTask<Void, Void, Response> {
        ProgressDialog progressDialog = new ProgressDialog(CreateCourseActivity.this);
        Exception exception;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage(getString(R.string.please_wait));
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            if (result.getItem() != null) {
                Intent intent = new Intent(CreateCourseActivity.this, OtherMainActivity.class);
                intent.putExtra("tab", 5);
                startActivity(intent);
                finish();
            } else Toast.makeText(CreateCourseActivity.this, getString(R.string.error_please_try_again), Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }

        protected Response doInBackground(Void... params) {

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost request = new HttpPost("http://89.222.166.108/api/" + "Course");
                StringEntity entity = new StringEntity(new Gson().toJson(createCourse),
                        HTTP.UTF_8);
                entity.setContentType("application/json");
                request.setEntity(entity);
                HttpResponse response = httpclient.execute(request);
                InputStreamReader reader = new InputStreamReader(response.getEntity().getContent());
                result = new Gson().fromJson(reader, Response.class);

            } catch (Exception e) {
                this.exception = e;
            }
            return result;
        }
    }
}
