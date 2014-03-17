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
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.Milestone1.Classes.CreateEvent;
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
import java.util.Calendar;
import java.util.UUID;

public class CreateEventActivity extends Activity {
    private TextView tvTime;
    private TextView tvDate;

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
    private TextView tvEndTime;
    private TextView tvEndDate;
    private EditText etLocation;
    private EditText etDescription;
    private EditText etTitle;
    private CheckBox cbAllDay;
    private Spinner spInterval;
    private Response result;
    private Intent intent;
    private CreateEvent createEvent;
    private UUID token;
    private Exception exception;
    private UUID groupID;
    private Spinner spGroup;
    private Groups[] groups;
    private String[] group_names;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_create);

        spInterval = (Spinner) findViewById(R.id.spInterval);
        spInterval.setSelection(7);
        spGroup = (Spinner) findViewById(R.id.spGroup);
        spGroup.setSelection(0);
        etTitle = (EditText) findViewById(R.id.etTitle);
        etDescription = (EditText) findViewById(R.id.etDescription);
        etLocation = (EditText) findViewById(R.id.etLocation);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvEndTime = (TextView) findViewById(R.id.tvEndTime);
        tvEndDate = (TextView) findViewById(R.id.tvEndDate);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            try {
                if (extras.getString("groupID") == null) {
                    groupID = null;
                    spGroup.setVisibility(View.VISIBLE);
                } else {
                    groupID = UUID.fromString(extras.getString("groupID"));
                    spGroup.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                this.exception = e;
            }
        }

        SharedPreferences userDetails = getSharedPreferences("userdetails", MODE_PRIVATE);
        token = UUID.fromString(userDetails.getString("token", ""));

        final Calendar calendar = Calendar.getInstance();
        years = calendar.get(Calendar.YEAR);
        endYears = calendar.get(Calendar.YEAR);
        months = calendar.get(Calendar.MONTH);
        endMonths = calendar.get(Calendar.MONTH);
        days = calendar.get(Calendar.DAY_OF_MONTH);
        endDays = calendar.get(Calendar.DAY_OF_MONTH);
        hours = calendar.get(Calendar.HOUR_OF_DAY);
        endHours = calendar.get(Calendar.HOUR_OF_DAY);
        minutes = calendar.get(Calendar.MINUTE);
        endMinutes = calendar.get(Calendar.MINUTE);

        updateDisplay();

        new GetUserGroups().execute();

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
            updateDisplay();
        }
    };
    TimePickerDialog.OnTimeSetListener mEndTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            endHours = hourOfDay;
            endMinutes = minute;
            updateDisplay();
        }
    };
    DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            years = year;
            months = monthOfYear;
            days = dayOfMonth;
            updateDisplay();
        }
    };
    DatePickerDialog.OnDateSetListener mEndDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            endYears = year;
            endMonths = monthOfYear;
            endDays = dayOfMonth;
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
//        if (endYears >= years && endMonths >= months && endDays >= days) {
        tvEndDate.setText(new StringBuilder()
                .append(endYears).append("-")
                .append(endMonths + 1).append("-")
                .append(endDays));
//        } else Toast.makeText(this, getString(R.string.edit_date), Toast.LENGTH_SHORT).show();


        /*tvEndDate.setText(new StringBuilder()
                .append(endYears).append("-")
                .append(endMonths + 1).append("-")
                .append(endDays));*/
    }


    private static String pad(int c) {
        if (c >= 10) {
            return String.valueOf(c);
        } else {
            return "0" + String.valueOf(c);
        }
    }

    public void SetSpinnerGroups(Response response) {
        groups = (Groups[]) response.getItem();
        group_names = new String[groups.length + 1];
        group_names[0] = "Пусто";
        for (int i = 0; i < groups.length; i++) group_names[i + 1] = groups[i].getName();
        ArrayAdapter<String> adapter_groups = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, group_names);
        adapter_groups.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGroup.setAdapter(adapter_groups);
    }

    public void getData() {
        try {
            createEvent = new CreateEvent();
            createEvent.setToken(token);
            //createEvent.setGroupID(groupID);
            if (etTitle.getText().toString().equals("")) {
                Toast.makeText(this, getString(R.string.please_set_text), Toast.LENGTH_SHORT).show();
                //item.setChecked(false);
            } else {
                //item.setChecked(true);
                createEvent.setTitle(etTitle.getText().toString());
                createEvent.setDescription(etDescription.getText().toString());
                createEvent.setLocation(etLocation.getText().toString());
                createEvent.setStartTime(tvDate.getText().toString() + "T" + tvTime.getText().toString());
//                if (years <= endYears && months <= endMonths && days <= endDays) {
                        /*if (hours <= endHours && minutes < endMinutes) {*/
                createEvent.setEndDate(tvEndDate.getText().toString() + "T" + tvEndTime.getText().toString());
                createEvent.setEndTime(tvEndDate.getText().toString() + "T" + tvEndTime.getText().toString());
                if (spInterval.getSelectedItemPosition() != 7) {
                    createEvent.setTimeInterval(spInterval.getSelectedItemPosition());
                } else createEvent.setTimeInterval(null);
                if (groupID == null) {
                    if (spGroup.getSelectedItemPosition() != 0) {
                        createEvent.setGroupID(groups[spGroup.getSelectedItemPosition() - 1].getId());
                    } else createEvent.setGroupID(null);
                } else createEvent.setGroupID(groupID);
                CreateUserEvent createUserEvent = new CreateUserEvent();
                createUserEvent.execute();
/*                } else {
                    Toast.makeText(this, getString(R.string.edit_date), Toast.LENGTH_SHORT).show();
                    //item.setChecked(false);
                }*/
            }


        } catch (Exception e) {
            this.exception = e;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_event_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean ret = false;
        if (item.getItemId() == R.id.action_save) {
            ret = true;
            getData();
        }
        return ret;
    }

    public class CreateUserEvent extends AsyncTask<Void, Void, Response> {

        private Exception ex;

        ProgressDialog pdLoading = new ProgressDialog(CreateEventActivity.this);
        private Exception exception;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage(getString(R.string.please_wait));
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            try {
                pdLoading.dismiss();
                UUID eventID = UUID.fromString((String) result.getItem());
                if (eventID.compareTo(new UUID(0, 0)) != 0) {
                    Intent intent = new Intent(CreateEventActivity.this, OtherMainActivity.class);
                    intent.putExtra("tab", 2);
                    startActivity(intent);
                    finish();
                } else
                    Toast.makeText(CreateEventActivity.this, "Создать событие не удалось. Попробуйте снова", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                this.exception = e;
            }
        }

        protected Response doInBackground(Void... params) {

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost request = new HttpPost(getString(R.string.url) + "Event");
                StringEntity entity = new StringEntity(new Gson().toJson(createEvent),
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

    public class GetUserGroups extends AsyncTask<UUID, Void, Response> {
        public Exception ex;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            SetSpinnerGroups(result);
        }

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
}
