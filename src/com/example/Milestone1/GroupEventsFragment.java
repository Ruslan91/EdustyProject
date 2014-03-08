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
import java.util.UUID;

/**
 * Created by Руслан on 03.10.13.
 */
public class GroupEventsFragment extends Fragment {
    private UUID token;
    private Response<Event[]> result;
    private Event[] events;
    private ListView listEvents;
    private UUID groupID;
    Exception exception;

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
            EventAdapter sAdapter = new EventAdapter(getActivity(), events);
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
            this.exception = e;
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

        Exception ex;

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

