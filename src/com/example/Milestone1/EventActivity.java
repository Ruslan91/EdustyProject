package com.example.Milestone1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.Milestone1.Classes.Event;
import com.example.Milestone1.Classes.EventDelete;
import com.example.Milestone1.Classes.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


public class EventActivity extends Activity {
    TextView tvTitle, tvStartTime, tvEndTime, tvInterval, tvDescription, tvLocation, tvGroup;
    private String title;
    private String startTime;
    private String endDate;
    private long interval;
    private String description;
    private String endTime;
    private String location;
    private String timeInterval;
    private Date dateTime;
    private String startDateTime;
    private String endDateTime;
    private Date timeDate;
    private UUID eventID;
    private Exception exception;
    private Boolean success;
    private UUID token;
    private EventDelete eventDelete;
    private boolean isReadWrite;
    private Response result;
    GetEventInformation getEventInformation;
    private Event event;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event);
        SharedPreferences sharedPreferences = getSharedPreferences("userdetails", MODE_PRIVATE);
        token = UUID.fromString(sharedPreferences.getString("token", ""));
        Bundle extras = getIntent().getExtras();
        try {
            if (extras != null) {
                eventID = UUID.fromString(extras.getString("eventID"));
            }
            getEventInformation = new GetEventInformation();
            getEventInformation.execute();

        } catch (Exception e) {
            this.exception = e;
        }
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvStartTime = (TextView) findViewById(R.id.tvStartTime);
        tvEndTime = (TextView) findViewById(R.id.tvEndDate);
        tvInterval = (TextView) findViewById(R.id.tvInterval);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        tvGroup = (TextView) findViewById(R.id.tvGroup);


    }

    void setData(Response response) {
        try {
            event = (Event) response.getItem();
            dateTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(event.getStartTime());
            startDateTime = new SimpleDateFormat("dd MMMM yyyy HH:mm").format(dateTime);
            timeDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(event.getEndTime());
            endDateTime = new SimpleDateFormat("dd MMMM yyyy HH:mm").format(timeDate);
            startTime = startDateTime;
            endTime = endDateTime;

            tvTitle.setText(event.getTitle());
            tvStartTime.setText(startTime);
            tvEndTime.setText(endTime);
            String[] intervals = getResources().getStringArray(R.array.intervals);
            if (event.getTimeInterval() != null) {
                tvInterval.setText(intervals[event.getTimeInterval()]);
            } else tvInterval.setText(intervals[7]);
            tvLocation.setText(event.getLocation());
            tvDescription.setText(event.getDescription());
            if (event.getGroupName() != null) {
                tvGroup.setText(event.getGroupName());
            }
        } catch (Exception e) {
            this.exception = e;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.event_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        try {
            event = (Event) result.getItem();
            if (event.getIsReadWrite()) {
                menu.getItem(0).setVisible(true);
                menu.getItem(1).setVisible(true);
            } else {
                menu.getItem(0).setVisible(false);
                menu.getItem(1).setVisible(false);
            }
        } catch (Exception e) {
            this.exception = e;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean ret = false;
        if (item.getItemId() == R.id.action_edit) {
            ret = true;
            try {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), EditEventActivity.class);
                intent.putExtra("title", event.getTitle());
                intent.putExtra("startTime", event.getStartTime());
                intent.putExtra("endTime", event.getEndTime());
                intent.putExtra("endDate", event.getEndDate());
                intent.putExtra("location", event.getLocation());
                intent.putExtra("interval", event.getTimeInterval());
                intent.putExtra("description", event.getDescription());
                intent.putExtra("eventID", eventID.toString());
                if (event.getGroupID() != null)
                    intent.putExtra("groupID", event.getGroupID().toString());
                startActivity(intent);
            } catch (Exception e) {
                this.exception = e;
            }
        }

        if (item.getItemId() == R.id.action_delete) {
            try {
                ret = true;
                new DeleteEvent().execute();
            } catch (Exception e) {
                this.exception = e;
            }
        }
        return ret;
    }

    public class GetEventInformation extends AsyncTask<UUID, Void, Response> {
        public Exception ex;
        ProgressDialog pdLoading = new ProgressDialog(EventActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tЗагрузка...");
            pdLoading.show();
        }

        @Override
        protected void onPostExecute(Response eventResponse) {
            super.onPostExecute(eventResponse);
            setData(eventResponse);
            pdLoading.dismiss();
        }

        @Override
        protected Response doInBackground(UUID... params) {
            try {
                UUID groupID = null;
                HttpClient httpclient = new DefaultHttpClient();
                Gson gson = new Gson();
                HttpGet request = new HttpGet(getString(R.string.url) + "Event?token=" + token + "&eventID=" + eventID);
                HttpResponse response = httpclient.execute(request);
                InputStreamReader reader = new InputStreamReader(response.getEntity()
                        .getContent(), HTTP.UTF_8);
                Type fooType = new TypeToken<Response<Event>>() {
                }.getType();
                result = gson.fromJson(reader, fooType);
            } catch (Exception e) {
                this.ex = e;
            }
            return result;
        }
    }

    public class DeleteEvent extends AsyncTask<Void, Void, Response> {
        ProgressDialog progressDialog = new ProgressDialog(EventActivity.this);
        private Exception ex;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage(getString(R.string.please_wait));
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            if (response.getItem().equals(true)) {
                Intent intent = new Intent(EventActivity.this, OtherMainActivity.class);
                intent.putExtra("tab", 2);
                startActivity(intent);
                finish();
            }
            progressDialog.dismiss();
        }

        protected Response doInBackground(Void... params) {

            try {
                HttpClient httpclient = new DefaultHttpClient();
                Gson gson = new Gson();
                HttpDelete request = new HttpDelete(getString(R.string.url) + "Event?token=" + token + "&eventID=" + eventID);
                HttpResponse response = httpclient.execute(request);
                InputStreamReader reader = new InputStreamReader(response.getEntity()
                        .getContent(), HTTP.UTF_8);
                result = gson.fromJson(reader, Response.class);

            } catch (Exception e) {
                this.ex = e;
            }
            return result;
        }
    }
}