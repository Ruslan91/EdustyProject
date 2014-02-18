package com.example.Milestone1;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.Milestone1.Classes.Journals;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Руслан on 19.12.13.
 */
public class UserMarksFragment extends Fragment {
    private View myView;
    private UUID token;
    private Journals[] journals;
    private ArrayList<Map<String, String>> data;
    private SimpleAdapter sAdapter;
    private ListView listJournals;
    private Response result;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.user_journals, container, false);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userdetails", Context.MODE_PRIVATE);
        token = UUID.fromString(sharedPreferences.getString("token", ""));

        new GetJournalsUser().execute();

        listJournals = (ListView) myView.findViewById(R.id.listJournals);
        listJournals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public Exception ex;

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                try {
                    Intent intent = new Intent(getActivity(), MyMarksActivity.class);
                    intent.putExtra("journalID", journals[position].getId().toString());
                    intent.putExtra("title", journals[position].getCourseName());
                    startActivity(intent);
                    onDestroy();

                } catch (Exception e) {
                    this.ex = e;
                }
            }
        });
        return myView;
    }

    public void SetJournals(Response response) {
        journals = (Journals[]) response.getItem();
        data = new ArrayList<Map<String, String>>(
                journals.length);

        for (int i = 0; i < journals.length; i++) {
            HashMap<String, String> m = new HashMap<String, String>();
            m.put("titles", journals[i].getTitle());
            m.put("courses", journals[i].getCourseName());
            m.put("groups", journals[i].getGroupName());
            m.put("owners", journals[i].getOwnerName());
            data.add(m);
        }
        sAdapter = new SimpleAdapter(getActivity(), data, R.layout.user_journals_list_item,
                new String[]{"titles", "courses", "groups", "owners"},
                new int[]{R.id.tvJournalTitle, R.id.tvJournalCourse, R.id.tvJournalGroup, R.id.tvJournalOwner}
        );
        listJournals.setAdapter(sAdapter);

    }

    public class GetJournalsUser extends AsyncTask<Void, Void, Response> {

        private Exception exception;

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            SetJournals(result);
        }

        @Override
        protected Response doInBackground(Void... params) {
            HttpClient httpClient = new DefaultHttpClient();
            Gson gson = new Gson();
            HttpGet request = new HttpGet(getString(R.string.url) + "JournalsUser?token=" + token);
            try {
                HttpResponse response = httpClient.execute(request);
                InputStreamReader reader = new InputStreamReader(response.getEntity()
                        .getContent(), HTTP.UTF_8);
                Type fooType = new TypeToken<Response<Journals[]>>() {
                }.getType();
                result = gson.fromJson(reader, fooType);
            } catch (Exception e) {
                this.exception = e;
            }
            return result;
        }
    }
}