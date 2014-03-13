package com.example.Milestone1;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.Milestone1.Adapters.GroupsAdapter;
import com.example.Milestone1.Classes.Groups;
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
import java.util.Map;
import java.util.UUID;

public class UserGroupsFragment extends Fragment {
    final String ATTRIBUTE_NAME_TEXTN = "textN";
    final String ATTRIBUTE_NAME_TEXTD = "textD";
    final String ATTRIBUTE_NAME_IMAGE = "picture";

    public Exception exception;
    ListView listGroups;
    UUID token;
    public Response result;
    Groups[] groups;
    GetUserGroups getUserGroups;
    public UUID groupID;
    Map<String, String> m;


    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.listgroup, container, false);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userdetails", Context.MODE_PRIVATE);
        token = UUID.fromString(sharedPreferences.getString("token", ""));
        try {
            getUserGroups = new GetUserGroups();
            getUserGroups.execute();
            listGroups = (ListView) myView.findViewById(R.id.listGroups);

        } catch (Exception e) {
            this.exception = e;
        }

        return myView;
    }

    void setData(Response response) {
        try {
            groups = (Groups[]) response.getItem();
            GroupsAdapter sAdapter = new GroupsAdapter(getActivity(), groups, "user_groups");
            listGroups.setAdapter(sAdapter);
            listGroups.setClickable(true);
            listGroups.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public Exception exception;

                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    try {
                        Intent intent = new Intent(getActivity().getApplicationContext(), GroupFragmentActivity.class);
                        intent.putExtra("groupID", groups[position].getId().toString());
                        intent.putExtra("groupOwner", groups[position].getIsOwner());
                        startActivity(intent);
                    } catch (Exception e) {
                        this.exception = e;
                    }
                }
            });
        } catch (Exception e) {
            this.exception = e;
        }

    }

    public class GetUserGroups extends AsyncTask<UUID, Void, Response> {
        Exception exception;
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage(getString(R.string.please_wait));
            progressDialog.show();
        }
        @Override
        protected void onPostExecute(Response response) {
            setData(response);
            progressDialog.dismiss();
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
                this.exception = e;
            }
            return result;
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.user_groups_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean ret = false;
        if (item.getItemId() == R.id.action_update) {
            try {
                GetUserGroups updateUserGroups = new GetUserGroups();
                updateUserGroups.execute();
            } catch (Exception ignored) {

            }
        }
        else if (item.getItemId() == R.id.action_catalog) {
            ret = true;
            Intent intent = new Intent(getActivity(), GroupsActivity.class);
            startActivity(intent);
        } else {
            ret = super.onOptionsItemSelected(item);
        }
        return ret;
    }
}