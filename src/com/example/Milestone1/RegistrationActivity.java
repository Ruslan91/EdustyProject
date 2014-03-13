package com.example.Milestone1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.Milestone1.Classes.Response;
import com.example.Milestone1.Classes.userRegistration;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import java.io.InputStreamReader;
public class RegistrationActivity extends Activity {
    EditText editEmail, editPasswd, editName, editLastName;
    Response result;
    userRegistration userRegistration = new userRegistration();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPasswd = (EditText) findViewById(R.id.editPasswd);
        editName = (EditText) findViewById(R.id.editName);
        editLastName = (EditText) findViewById(R.id.editLastName);
    }

    public void OnClickBtnReg(View v) {
        userRegistration.EMail = editEmail.getText().toString();
        userRegistration.Password = editPasswd.getText().toString();
        userRegistration.FirstName = editName.getText().toString();
        userRegistration.LastName = editLastName.getText().toString();
        try {
            new Registration().execute();
        } catch (Exception ignored) {

        }
    }

    public void onClickLicenseString(View v) {
        startActivity(new Intent(this, LicenseActivity.class));
    }
    public class Registration extends AsyncTask<userRegistration, Void, Response> {
        public Exception exception;
        ProgressDialog progressDialog = new ProgressDialog(RegistrationActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage(getString(R.string.please_wait));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            try {
                if (response.getItem().equals(true)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.registration_complete),
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegistrationActivity.this, AuthorizationActivity.class);
                    intent.putExtra("email", editEmail.getText().toString());
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(RegistrationActivity.this, getString(R.string.error_please_try_again),
                            Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            } catch (Exception e) {
                this.exception = e;
            }
        }
        protected Response doInBackground(userRegistration... params) {

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost request = new HttpPost(getString(R.string.url) + "Registration");
                Gson gson = new Gson();
                StringEntity entity = new StringEntity(gson.toJson(userRegistration), HTTP.UTF_8);
                entity.setContentType("application/json");
                request.setEntity(entity);
                HttpResponse response = httpclient.execute(request);
                InputStreamReader reader = new InputStreamReader(response.getEntity().getContent());
                result = gson.fromJson(reader, Response.class);

            } catch (Exception e1) {
                this.exception = e1;
            }
            return result;
        }
    }
}