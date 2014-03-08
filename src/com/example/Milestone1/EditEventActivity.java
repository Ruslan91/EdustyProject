package com.example.Milestone1;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.Milestone1.Classes.Event;
import com.example.Milestone1.Classes.Response;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Руслан on 26.09.13.
 */
public class EditEventActivity extends Activity {
    private Event eventEdit;
    private Spinner spInterval;
    private CheckBox cbAllDay;
    private EditText etTitle;
    private EditText etDescription;
    private TextView tvEndTime;
    private TextView tvEndDate;
    private EditText etLocation;
    private TextView tvTime;
    private TextView tvDate;
    private Button btnApply;
    private UUID token;
    private String title;
    private String startTime;
    private String endDate;
    private String endTime;
    private int interval;
    private String location;
    private String description;
    private String date;
    private Date eventStartTime;
    private String time;
    private Date eventEndTime;
    private Date eventEndDate;
    private String timeInterval;
    private final static int DIALOG_STARTTIME = 0;
    private final static int DIALOG_STARTDATE = 1;
    private final static int DIALOG_ENDTIME = 2;
    private final static int DIALOG_ENDDATE = 3;
    private int hours;
    private int endHours;
    private int minutes;
    private int endMinutes;
    private int years;
    private int endYears;
    private int months;
    private int endMonths;
    private int days;
    private int endDays;
    private Response result;
    private UUID eventID;
    private Exception exception;
    private UUID groupID;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_edit);
        SharedPreferences sharedPreferences = getSharedPreferences("userdetails", MODE_PRIVATE);
        token = UUID.fromString(sharedPreferences.getString("token", ""));
        Bundle extras = getIntent().getExtras();
        try {
            if (extras != null) {
                title = extras.getString("title");
                startTime = extras.getString("startTime");
                endDate = extras.getString("endDate");
                endTime = extras.getString("endTime");
                interval = extras.getInt("interval");
                location = extras.getString("location");
                description = extras.getString("description");
                eventID = UUID.fromString(extras.getString("eventID"));
                if (extras.getString("groupID") != null) {
                    groupID = UUID.fromString(extras.getString("groupID"));
            } else groupID = null;
            }
        } catch (Exception e) {
            this.exception = e;
        }

        spInterval = (Spinner) findViewById(R.id.spInterval);
        //cbAllDay = (CheckBox) findViewById(R.id.cbAllDay);
        spInterval.setSelection(interval);
        etTitle = (EditText) findViewById(R.id.etTitle);
        etDescription = (EditText) findViewById(R.id.etDescription);
        etLocation = (EditText) findViewById(R.id.etLocation);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvEndTime = (TextView) findViewById(R.id.tvEndTime);
        tvEndDate = (TextView) findViewById(R.id.tvEndDate);

        try {
            etTitle.setText(title);
            etDescription.setText(description);
            etLocation.setText(location);
            eventStartTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(startTime);
            eventEndTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(endTime);
            /*eventEndDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(endDate);
            date = new SimpleDateFormat("yyyy-MM-dd").format(eventStartTime);
            time = new SimpleDateFormat("HH:mm").format(eventStartTime);
            endDate = new SimpleDateFormat("yyyy-MM-dd").format(eventEndTime);
            endTime = new SimpleDateFormat("HH:mm").format(eventEndTime);*/

            years = Integer.parseInt(new SimpleDateFormat("yyyy").format(eventStartTime));
            endYears = Integer.parseInt(new SimpleDateFormat("yyyy").format(eventEndTime));
            months = Integer.parseInt(new SimpleDateFormat("MM").format(eventStartTime)) - 1;
            endMonths = Integer.parseInt(new SimpleDateFormat("MM").format(eventEndTime)) - 1;
            days = Integer.parseInt(new SimpleDateFormat("dd").format(eventStartTime));
            endDays = Integer.parseInt(new SimpleDateFormat("dd").format(eventEndTime));
            hours = Integer.parseInt(new SimpleDateFormat("HH").format(eventStartTime));
            endHours = Integer.parseInt(new SimpleDateFormat("HH").format(eventEndTime));
            minutes = Integer.parseInt(new SimpleDateFormat("mm").format(eventStartTime));
            endMinutes = Integer.parseInt(new SimpleDateFormat("mm").format(eventEndTime));

            updateDisplay();

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void onClickStartTimePick(View v) {
        showDialog(DIALOG_STARTTIME);
    }

    public void onClickStartDatePick(View v) {
        showDialog(DIALOG_STARTDATE);
    }

    public final void onClickEndTimePick(View v) {
        showDialog(DIALOG_ENDTIME);
    }

    public void onClickEndDatePick(View v) {
        showDialog(DIALOG_ENDDATE);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case (DIALOG_STARTTIME):
                return new TimePickerDialog(this, mStartTimeSetListener, hours, minutes, true);

            case DIALOG_STARTDATE:
                return new DatePickerDialog(this, mDateSetListener, years, months, days);
            case (DIALOG_ENDTIME):
                return new TimePickerDialog(this, mEndTimeSetListener, endHours, endMinutes, true);

            case DIALOG_ENDDATE:
                return new DatePickerDialog(this, mEndDateSetListener, endYears, endMonths, endDays);
        }
        return null;
    }

    TimePickerDialog.OnTimeSetListener mStartTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hours = hourOfDay;
            minutes = minute;
            tvTime.setText(new StringBuilder()
                    .append(pad(hours)).append(":")
                    .append(pad(minutes)));
            updateDisplay();
        }
    };
    TimePickerDialog.OnTimeSetListener mEndTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            endHours = hourOfDay;
            endMinutes = minute;
            tvEndTime.setText(new StringBuilder()
                    .append(pad(endHours)).append(":")
                    .append(pad(endMinutes)));
            updateDisplay();
        }
    };
    DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            years = year;
            months = monthOfYear;
            days = dayOfMonth;
            tvDate.setText(new StringBuilder()
                    .append(years).append("-")
                    .append(months + 1).append("-")
                    .append(days));
            updateDisplay();
        }
    };
    DatePickerDialog.OnDateSetListener mEndDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            endYears = year;
            endMonths = monthOfYear;
            endDays = dayOfMonth;
            tvEndDate.setText(new StringBuilder()
                    .append(endYears).append("-")
                    .append(endMonths + 1).append("-")
                    .append(endDays));
            updateDisplay();
        }
    };

    private void updateDisplay() {
        tvTime.setText(new StringBuilder()
                .append(pad(hours)).append(":")
                .append(pad(minutes)));

        tvEndTime.setText(new StringBuilder()
                .append(pad(endHours)).append(":")
                .append(pad(endMinutes)));
        tvDate.setText(new StringBuilder()
                .append(years).append("-")
                .append(months + 1).append("-")
                .append(days));
        tvEndDate.setText(new StringBuilder()
                .append(endYears).append("-")
                .append(endMonths + 1).append("-")
                .append(endDays));
    }

    private static String pad(int c) {
        if (c >= 10) {
            return String.valueOf(c);
        } else {
            return "0" + String.valueOf(c);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        boolean ret = false;
        if (item.getItemId() == R.id.action_apply) {
            try {
                ret = true;
                eventEdit = new Event();
                eventEdit.setToken(token);
                eventEdit.setEventID(eventID);
                eventEdit.setGroupID(groupID);
                eventEdit.setTitle(etTitle.getText().toString());
                eventEdit.setDescription(etDescription.getText().toString());
                eventEdit.setLocation(etLocation.getText().toString());
                if (spInterval.getSelectedItemPosition() != 7) {
                    eventEdit.setTimeInterval(spInterval.getSelectedItemPosition());
                } else eventEdit.setTimeInterval(null);
                eventEdit.setStartTime(tvDate.getText().toString() + "T" + tvTime.getText().toString());
            /*if (years <= endYears) {*/
                eventEdit.setEndDate(tvEndDate.getText().toString() + "T" + tvEndTime.getText().toString());
            /*} else {
                Toast.makeText(this, "Edit date!", Toast.LENGTH_SHORT);
            }*/
                eventEdit.setEndTime(tvEndDate.getText().toString() + "T" + tvEndTime.getText().toString());
                new EventEdit().execute();
            } catch (Exception e) {
                this.exception = e;
            }

        }
        return ret;

    }
    public class EventEdit extends AsyncTask<Void, Void, Response> {
        ProgressDialog progressDialog = new ProgressDialog(EditEventActivity.this);

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
                Intent intent = new Intent(EditEventActivity.this, MainActivity.class);
                intent.putExtra("tab", 1);
                startActivity(intent);
                finish();
            }
        }

        protected Response doInBackground(Void... params) {

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost request = new HttpPost(getString(R.string.url) + "EventEdit");
                StringEntity entity = new StringEntity(new Gson().toJson(eventEdit),
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