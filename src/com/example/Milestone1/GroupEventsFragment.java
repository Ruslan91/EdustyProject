package com.example.Milestone1;

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

import com.example.Milestone1.Adapters.EventAdapter;
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

/**
 * Created by Руслан on 03.10.13.
 */
public class GroupEventsFragment extends Fragment {
    private static final String TITLE = "title";
    private static final String STARTTIME = "starttime";
    private static final String ENDTIME = "endtime";
    private static final String LOCATION = "location";
    private UUID token;
    private Response<Event[]> result;
    private EventAdapter sAdapter;
    private Event[] events;
    private String[] titles, startTimes, endTimes, locations;
    private Date[] datetime, timedate;
    private ListView listEvents;
    private UUID groupID;
    private Exception exception;
    private ArrayList<HashMap<String, String>> data;
    private Comparator<HashMap<String, String>> comparator;
    private String eventID;

    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.listevents, container, false);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userdetails", Context.MODE_PRIVATE);
        token = UUID.fromString(sharedPreferences.getString("token", ""));
        groupID = UUID.fromString(getActivity().getIntent().getStringExtra("groupID"));
        GetGroupEvents getGroupEvents = new GetGroupEvents();
        getGroupEvents.execute();
        listEvents = (ListView) myView.findViewById(R.id.listEvents);


        return myView;
    }

    public void setData(Response response) {
        try {
            events = (Event[]) response.getItem();
            /*datetime = new Date[events.length];
            timedate = new Date[events.length];
            data = new ArrayList<HashMap<String, String>>(
                    events.length);
            data.clear();
            for (int i = 0; i < events.length; i++) {
                HashMap<String, String> m = new HashMap<String, String>();
                m.put(TITLE, events[i].getTitle());
                datetime[i] = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(events[i].getStartTime());
                m.put(STARTTIME, new SimpleDateFormat("yyyy-MM-dd HH:mm").format(datetime[i]));
                timedate[i] = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(events[i].getEndTime());
                m.put(ENDTIME, new SimpleDateFormat("yyyy-MM-dd HH:mm").format(timedate[i]));
                m.put(LOCATION, events[i].getLocation());
                m.put("eventID", events[i].getEventID().toString());
                data.add(m);
            }
            comparator = new Comparator<HashMap<String, String>>() {
                @Override
                public int compare(HashMap<String, String> object1, HashMap<String, String> object2) {
                    return object1.get(STARTTIME).compareToIgnoreCase(object2.get(STARTTIME));
                }
            };
            Collections.sort(data, comparator);*/
            sAdapter = new EventAdapter(getActivity(), events);
            listEvents.setAdapter(sAdapter);
            listEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public Exception exception;

                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    try {
                        Intent intent = new Intent(getActivity().getApplicationContext(), EventActivity.class);
                        intent.putExtra("eventID", events[position].getEventID().toString());
                        startActivity(intent);
                        onDestroy();
                    } catch (Exception e) {
                        this.exception = e;
                    }
                }
            });
        } catch (Exception e) {

        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.user_events_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        boolean ret = false;
        if (item.getItemId() == R.id.action_add) {
            ret = true;
            Intent intent = new Intent(getActivity().getApplicationContext(), CreateEventActivity.class);
            intent.putExtra("groupID", groupID.toString());
            startActivity(intent);
            onDestroy();
        }
        return ret;
    }

    public class GetGroupEvents extends AsyncTask<UUID, Void, Response<Event[]>> {

        private Exception ex;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Response<Event[]> response) {
            super.onPostExecute(response);
            setData(response);
        }

        @Override
        protected Response<Event[]> doInBackground(UUID... params) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                Gson gson = new Gson();
                HttpGet request = new HttpGet(getString(R.string.url) + "Events?token=" + token + "&groupID=" + groupID);
                HttpResponse response = httpclient.execute(request);
                InputStreamReader reader = new InputStreamReader(response.getEntity()
                        .getContent(), HTTP.UTF_8);
                Type fooType = new TypeToken<Response<Event[]>>() {
                }.getType();
                result = gson.fromJson(reader, fooType);
            } catch (Exception e) {
                this.ex = e;
            }
            return result;
        }

    }
}

