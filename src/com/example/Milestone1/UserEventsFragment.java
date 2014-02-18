package com.example.Milestone1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.Milestone1.Classes.Event;
import com.example.Milestone1.Classes.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class UserEventsFragment extends Fragment {
    UUID token;
    private Response result;
    private GetUserEvents getUserEvents;
    private String ATTRIBUTE_NAME_TITLE = "title";
    private String ATTRIBUTE_NAME_STARTTIME = "starttime";
    private String ATTRIBUTE_NAME_ENDTIME = "endtime";
    private String ATTRIBUTE_NAME_LOCATION = "location";
    private ListView listEvents;
    private String[] titles;
    private String[] startTimes;
    private String[] endTimes;
    private String[] locations;
    private Date[] datetime;
    SimpleAdapter sAdapter;
    private Date[] timedate;
    ArrayList<HashMap<String, String>> data;
    private String eventID;
    private String[] eventsID;
    private Exception exception;
    private Event[] events;
    HashMap<String, String> m;
    private GetUserEvents updateUserEvents;
    private Comparator<HashMap<String, String>> comparator;
    private TextView tvTitle;


    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View myView = inflater.inflate(R.layout.listevents, container, false);
        setRetainInstance(true);
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("userdetails", Context.MODE_PRIVATE);
        token = UUID.fromString(sharedPreferences.getString("token", ""));
        getUserEvents = new GetUserEvents();
        getUserEvents.execute();
        listEvents = (ListView) myView.findViewById(R.id.listEvents);
        listEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public Exception exception;

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                try {
                    eventID = data.get(position).get("eventID");
                    Intent intent = new Intent(getActivity().getApplicationContext(), EventActivity.class);
                    intent.putExtra("eventID", eventID);
                    startActivity(intent);
                    onDestroy();
                } catch (Exception e) {
                    this.exception = e;
                }
            }
        });
        return myView;
    }

    public void setData(Response response) {
        try {
            events = (Event[]) response.getItem();
            datetime = new Date[events.length];
            timedate = new Date[events.length];
            data = new ArrayList<HashMap<String, String>>(
                    events.length);
            data.clear();
            for (int i = 0; i < events.length; i++) {
                m = new HashMap<String, String>();
                m.put(ATTRIBUTE_NAME_TITLE, events[i].getTitle());
                datetime[i] = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(events[i].getStartTime());
                m.put(ATTRIBUTE_NAME_STARTTIME, new SimpleDateFormat("yyyy-MM-dd HH:mm").format(datetime[i]));
                timedate[i] = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(events[i].getEndTime());
                m.put(ATTRIBUTE_NAME_ENDTIME, new SimpleDateFormat("yyyy-MM-dd HH:mm").format(timedate[i]));
                m.put(ATTRIBUTE_NAME_LOCATION, events[i].getLocation());
                m.put("eventID", events[i].getEventID().toString());
                data.add(m);
            }
            comparator = new Comparator<HashMap<String, String>>() {
                @Override
                public int compare(HashMap<String, String> object1, HashMap<String, String> object2) {
                    return object1.get(ATTRIBUTE_NAME_STARTTIME).compareToIgnoreCase(object2.get(ATTRIBUTE_NAME_STARTTIME));
                }
            };
            Collections.sort(data, comparator);

            sAdapter = new SimpleAdapter(getActivity(), data, R.layout.event_list_item,
                    new String[]{ATTRIBUTE_NAME_TITLE, ATTRIBUTE_NAME_STARTTIME, ATTRIBUTE_NAME_ENDTIME, ATTRIBUTE_NAME_LOCATION},
                    new int[]{R.id.tvTitle, R.id.tvStartTime, R.id.tvEndTime, R.id.tvLocation}
            );
            listEvents.setAdapter(sAdapter);
        } catch (Exception e) {

        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.user_events_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean ret;
        if (item.getItemId() == R.id.action_update) {
            ret = true;
            updateUserEvents = new GetUserEvents();
            updateUserEvents.execute();
            /*try {
                //result = updateUserEvents.get();
                events = (Event[]) result.getItem();
                data.clear();
                for (int i = 0; i < events.length; i++) {
                    m = new HashMap<String, String>();
                    m.put(ATTRIBUTE_NAME_TITLE, events[i].getTitle());
                    datetime[i] = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(events[i].getStartTime());
                    timedate[i] = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(events[i].getEndTime());
                    m.put(ATTRIBUTE_NAME_STARTTIME, new SimpleDateFormat("yyyy-MM-dd HH:mm").format(datetime[i]));
                    m.put(ATTRIBUTE_NAME_ENDTIME, new SimpleDateFormat("yyyy-MM-dd HH:mm").format(timedate[i]));
                    m.put(ATTRIBUTE_NAME_LOCATION, events[i].getLocation());
                    m.put("eventID", events[i].getEventID().toString());
                    data.add(m);
                }
                Collections.sort(data, comparator);
                sAdapter.notifyDataSetChanged();
                return ret;
            } catch (Exception ignored) {

            }*/
        }
        if (item.getItemId() == R.id.action_add) {
            ret = true;
            String token_string = token.toString();
            Intent intent = new Intent(getActivity().getApplicationContext(), CreateEventActivity.class);
            intent.putExtra("token", token_string);
            startActivity(intent);
            onDestroy();
        } else {
            ret = super.onOptionsItemSelected(item);
        }
        return ret;
    }

    public class GetUserEvents extends AsyncTask<Void, Void, Void> {
        public Exception ex;

        ProgressDialog pdLoading = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*pdLoading.setMessage("\tЗагрузка...");
            pdLoading.setCancelable(false);
            pdLoading.show();*/
        }

        @Override
        protected void onPostExecute(Void v) {
            //pdLoading.dismiss();
            setData(result);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                Gson gson = new Gson();
                HttpGet request = new HttpGet(getString(R.string.url) + "Events?token=" + token);
                HttpResponse response = httpclient.execute(request);
                InputStreamReader reader = new InputStreamReader(response.getEntity()
                        .getContent(), HTTP.UTF_8);
                Type fooType = new TypeToken<Response<Event[]>>() {
                }.getType();
                result = gson.fromJson(reader, fooType);

            } catch (Exception e) {
                this.ex = e;
            }
            return null;
        }
    }
}
