package com.example.Milestone1;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Milestone1.Classes.Response;
import com.example.Milestone1.Classes.User;
import com.example.Milestone1.Classes.UserWrite;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by Руслан on 19.11.13.
 */
public class UserEditActivity extends Activity {
    private UUID token;
    private Response result;
    private int language;
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etMiddleName;
    private TextView etBirthDate;
    private EditText etEmail;
    private EditText etPhone;
    private EditText etCity;
    private Spinner spCountry;
    private UserWrite userWrite;
    private int year;
    private int month;
    private int day;
    Exception exception;
    static final int GALLERY_REQUEST = 1;
    UUID PictureID;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit);
        SharedPreferences sharedPreferences = getSharedPreferences("userdetails", MODE_PRIVATE);
        token = UUID.fromString(sharedPreferences.getString("token", ""));
        getActionBar().setTitle(R.string.edit);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        GetUserInformation getUserInformation = new GetUserInformation();
        getUserInformation.execute();
        try {
            result = getUserInformation.get();
            User userInfo = (User) result.getItem();
            PictureID = userInfo.getPictureID();
            etFirstName = (EditText) findViewById(R.id.etFirstName);
            etLastName = (EditText) findViewById(R.id.etLastName);
            etMiddleName = (EditText) findViewById(R.id.etMiddleName);
            etBirthDate = (TextView) findViewById(R.id.etBirthDate);
            etEmail = (EditText) findViewById(R.id.etEmail);
            etPhone = (EditText) findViewById(R.id.etPhone);
            etCity = (EditText) findViewById(R.id.etCity);

            spCountry = (Spinner) findViewById(R.id.spCountry);

            etFirstName.setText(userInfo.getFirstName());
            etLastName.setText(userInfo.getLastName());
            etMiddleName.setText(userInfo.getMiddleName());
            if (userInfo.getBirthDate() != null) {
                Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(userInfo.getBirthDate());
                String birthDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
                etBirthDate.setText(birthDate);
                year = Integer.parseInt(new SimpleDateFormat("yyyy").format(date));
                month = Integer.parseInt(new SimpleDateFormat("MM").format(date)) - 1;
                day = Integer.parseInt(new SimpleDateFormat("dd").format(date));
            } else {
                etBirthDate.setText(userInfo.getBirthDate());
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
            }

            etEmail.setText(userInfo.getEMail());
            etPhone.setText(userInfo.getPhone());
            etCity.setText(userInfo.getCity());

            if (Locale.getDefault() != Locale.US) {
                language = 0;
            } else language = 0;
            GetCountry getCountry = new GetCountry();
            getCountry.execute();
            result = getCountry.get();
            List<String> countries = (List<String>) result.getItem();
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, countries);
            spCountry.setAdapter(arrayAdapter);


        } catch (Exception e) {
            this.exception = e;
        }

    }

    public UserEditActivity() {
        super();
    }

    public void updateDisplay() {
        etBirthDate.setText(new StringBuilder()
                .append(year).append("-")
                .append(month + 1).append("-")
                .append(day));
    }

    public void onClickEditBirthDate(View v) {
        showDialog(0);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case (0):
                return new DatePickerDialog(this, birthDateSetListener, year, month, day);
        }
        return null;
    }

    DatePickerDialog.OnDateSetListener birthDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int thisYear, int monthOfYear, int dayOfMonth) {
            year = thisYear;
            month = monthOfYear;
            day = dayOfMonth;
            etBirthDate.setText(new StringBuilder()
                    .append(year).append("-")
                    .append(month + 1).append("-")
                    .append(day));
            updateDisplay();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_edit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_apply:
                userWrite = new UserWrite();
                userWrite.setToken(token);
                userWrite.setFirstName(etFirstName.getText().toString());
                userWrite.setLastName(etLastName.getText().toString());
                userWrite.setMiddleName(etMiddleName.getText().toString());
                userWrite.setBirthDate(etBirthDate.getText().toString());
                userWrite.setEMail(etEmail.getText().toString());
                userWrite.setPhone(etPhone.getText().toString());
                userWrite.setCity(etCity.getText().toString());
                userWrite.setCountry(spCountry.getSelectedItem().toString());
                userWrite.setPictureID(PictureID);
                new PostUserWrite().execute();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickBtnChangePicture(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        Bitmap galleryPic = null;

        switch(requestCode) {
            case GALLERY_REQUEST:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        galleryPic = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        if (galleryPic == null) break;
                        new PostFile().execute(galleryPic);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
        }
    }

    public class PostFile extends AsyncTask<Bitmap, Void, Response> {
        Bitmap file = null;
        Exception exception;
        ProgressDialog pdLoading = new ProgressDialog(UserEditActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage(getString(R.string.please_wait));
            pdLoading.show();
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            if (response.getStatusCode().equals(0)) {
                PictureID = UUID.fromString((String) response.getItem());
            }
            pdLoading.dismiss();
        }

        protected Response doInBackground(Bitmap... bitmaps) {
            String containerName = "avatars";
            file = bitmaps[0];
            String fileName = "cheguevara";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost request = new HttpPost(getString(R.string.url) + "File?fileName=" + fileName + "&token=" + token + "&containerName=" + containerName);
                StringEntity entity = new StringEntity(new Gson().toJson(file),
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

    public class GetUserInformation extends AsyncTask<UUID, Void, Response> {
        private Exception exception;

        @Override
        protected Response doInBackground(UUID... params) {
            UUID userID = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                Gson gson = new Gson();
                HttpGet request = new HttpGet(
                        getString(R.string.url) + "User?token=" + token + "&userID=" + userID);
                HttpResponse response = httpclient.execute(request);
                InputStreamReader reader = new InputStreamReader(response.getEntity()
                        .getContent(), HTTP.UTF_8);
                Type fooType = new TypeToken<Response<User>>() {
                }.getType();
                result = gson.fromJson(reader, fooType);
            } catch (Exception e) {
                this.exception = e;
            }
            return result;
        }
    }

    public class GetCountry extends AsyncTask<Void, Void, Response> {
        private Exception exception;

        @Override
        protected Response doInBackground(Void... params) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                Gson gson = new Gson();
                HttpGet request = new HttpGet(
                        getString(R.string.url) + "Country?language=" + language);
                HttpResponse response = httpclient.execute(request);
                InputStreamReader reader = new InputStreamReader(response.getEntity()
                        .getContent(), HTTP.UTF_8);
                result = gson.fromJson(reader, Response.class);
            } catch (Exception e) {
                this.exception = e;
            }
            return result;
        }
    }

    public class PostUserWrite extends AsyncTask<UserWrite, Void, Response> {
        ProgressDialog progressDialog = new ProgressDialog(UserEditActivity.this);
        private Exception exception;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage(getString(R.string.please_wait));
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            try {
                if (response.getItem() == Boolean.TRUE) {
                    progressDialog.dismiss();
                    startActivity(new Intent(UserEditActivity.this, UserActivity.class));
                    finish();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(UserEditActivity.this, getString(R.string.error_please_try_again), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                this.exception = e;
            }
        }

        protected Response doInBackground(UserWrite... params) {

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost request = new HttpPost(getString(R.string.url) + "User");
                StringEntity entity = new StringEntity(new Gson().toJson(userWrite),
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