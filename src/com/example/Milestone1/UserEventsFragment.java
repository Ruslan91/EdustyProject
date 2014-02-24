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

public class UserEventsFragment extends Fragment {
    UUID token;
    private Response result;
    private GetUserEvents getUserEvents;
    private ListView listEvents;
    EventAdapter sAdapter;
    ArrayList<HashMap<String, String>> data;
    private Event[] events;
    HashMap<String, String> m;


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

        return myView;
    }

    public void setData(Response response) {
        try {
            events = (Event[]) response.getItem();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean ret;
        if (item.getItemId() == R.id.action_update) {
            ret = true;
            new GetUserEvents().execute();
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
