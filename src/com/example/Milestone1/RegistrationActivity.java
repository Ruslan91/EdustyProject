package com.example.Milestone1;

import android.app.Activity;
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

@SuppressWarnings("ConstantConditions")
public class RegistrationActivity extends Activity {
    EditText editEmail, editPasswd, editName, editLastName;
    userRegistration userRegistration = new userRegistration();
    Registration registration;
    Response response;
    Intent intent;
    Response result;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        //getActionBar().setTitle(getString(R.string.registration));
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
            registration = new Registration();
            registration.execute();

            response = registration.get();
            if (response.getItem() != Boolean.FALSE) {
                Toast.makeText(this, getString(R.string.registration_complete),
                        Toast.LENGTH_SHORT).show();
                intent = new Intent(this, AuthorizationActivity.class);
                intent.putExtra("email", editEmail.getText().toString());
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(this, getString(R.string.error_please_try_again),
                        Toast.LENGTH_SHORT).show();
            }


        } catch (Exception ignored) {

        }
    }

    public void onClickLicenseString(View view) {
        startActivity(new Intent(this, LicenseActivity.class));
    }

    public class Registration extends AsyncTask<userRegistration, Void, Response> {
        public Exception exeption;

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
                this.exeption = e1;
            }
            return result;
        }
    }
}