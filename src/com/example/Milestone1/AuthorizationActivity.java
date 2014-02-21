package com.example.Milestone1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.Milestone1.Classes.Response;
import com.example.Milestone1.Classes.userAuthorization;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import java.io.InputStreamReader;
import java.util.UUID;

public class AuthorizationActivity extends Activity {
    Response result;
    EditText edEmail, edPasswd;
    public Response response;
    Exception exception;
    private userAuthorization user;
    private String[] statusCodes;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authorization);
        //statusCodes = getResources().getStringArray(R.array.status_codes);
        edEmail = (EditText) findViewById(R.id.edEmail);
        edEmail.setText(getIntent().getStringExtra("email"));
        edPasswd = (EditText) findViewById(R.id.edPasswd);
    }

    public void setData(Response response) {
        try {
            if (UUID.fromString((String) response.getItem()).compareTo(new UUID(0, 0)) != 0) {
                SharedPreferences sharedpreferences = getSharedPreferences("userdetails", MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedpreferences.edit();
                assert edit != null;
                edit.putString("token", response.getItem().toString());
                edit.commit();
                startActivity(new Intent(AuthorizationActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(AuthorizationActivity.this, getString(R.string.error_please_try_again),
                        Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            this.exception = e;
            Toast.makeText(AuthorizationActivity.this, response.getItem().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void OnClickBtnLogIn(View view) {

        try {
            user = new userAuthorization();
            if (edEmail.getText().toString().equals("") || edPasswd.getText().toString().equals("")) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            } else {
                user.EMail = edEmail.getText().toString();
                user.Password = edPasswd.getText().toString();
                new Authorization().execute();
            }
        } catch (Exception e1) {
            this.exception = e1;
        }
    }

    public void OnClickBtnSignUp(View view) {
        startActivity(new Intent(this, RegistrationActivity.class));
    }

    public class Authorization extends AsyncTask<userAuthorization, Void, Response> {
        ProgressDialog pdLoading = new ProgressDialog(AuthorizationActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tЗагрузка...");
            pdLoading.show();
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            if (response.getStatusCode().equals(0)) {
            setData(response);
            } else {
                //Toast.makeText(AuthorizationActivity.this, statusCodes[response.getStatusCode()], Toast.LENGTH_LONG).show();
            }
            pdLoading.dismiss();
        }

        protected Response doInBackground(userAuthorization... params) {

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost request = new HttpPost(getString(R.string.url) + "Authorization");
                StringEntity entity = new StringEntity(new Gson().toJson(user),
                        HTTP.UTF_8);
                entity.setContentType("application/json");
                request.setEntity(entity);
                HttpResponse response = httpclient.execute(request);
                InputStreamReader reader = new InputStreamReader(response.getEntity().getContent());
                result = new Gson().fromJson(reader, Response.class);

            } catch (Exception e) {
                Toast.makeText(AuthorizationActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
            return result;
        }
    }
}
