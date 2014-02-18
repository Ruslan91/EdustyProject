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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserGroupsFragment extends Fragment {
    final String ATTRIBUTE_NAME_TEXTN = "textN";
    final String ATTRIBUTE_NAME_TEXTD = "textD";
    final String ATTRIBUTE_NAME_IMAGE = "picture";

    Exception exception;
    ListView listGroups;
    ImageView groupImage;
    UUID token;
    Response result;
    Groups[] groups;
    GetUserGroups getUserGroups;
    public UUID groupID;
    private SharedPreferences sharedPreferences;
    private SimpleAdapter sAdapter;
    private GetUserGroups updateUserGroups;
    private ArrayList<Map<String, String>> data;
    Map<String, String> m;


    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.listgroup, container, false);
        sharedPreferences = getActivity().getSharedPreferences("userdetails", Context.MODE_PRIVATE);
        token = UUID.fromString(sharedPreferences.getString("token", ""));
        try {
            getUserGroups = new GetUserGroups();
            getUserGroups.execute();
            listGroups = (ListView) myView.findViewById(R.id.listGroups);
            listGroups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // When clicked, show a toast with the TextView text;
                    int pos;

                    try {
                        pos = position;
                        groupID = groups[pos].Id;
                        Intent intent = new Intent(getActivity().getApplicationContext(), GroupFragmentActivity.class);
                        intent.putExtra("groupID", groupID.toString());
                        intent.putExtra("groupOwner", groups[position].getIsOwner());
                        startActivity(intent);
                    } catch (Exception e) {

                    }
                }
            });
        } catch (Exception e) {
            this.exception = e;
        }

        return myView;
    }

    void SetData(Response response) {
        try {
            groups = (Groups[]) response.getItem();

            data = new ArrayList<Map<String, String>>(
                    groups.length);
            data.clear();
            for (int i = 0; i < groups.length; i++) {
                m = new HashMap<String, String>();
                m.put(ATTRIBUTE_NAME_TEXTN, groups[i].getName());
                m.put(ATTRIBUTE_NAME_TEXTD, groups[i].getDescription());
                if (groups[i].getPictureID() != null && groups[i].getPictureID().compareTo(new UUID(0, 0)) != 0) {
                    m.put("picture", getString(R.string.url) + "File?token=" + token + "&fileID=" + groups[i].getPictureID());
                }
                m.put("layout", "user_groups");
                data.add(m);
            }
            sAdapter = new GroupsAdapter(getActivity(), data, R.layout.user_groups_list_item,
                    new String[]{ATTRIBUTE_NAME_TEXTN, ATTRIBUTE_NAME_TEXTD, ATTRIBUTE_NAME_IMAGE},
                    new int[]{R.id.groupName, R.id.groupDescription, R.id.groupImage}
            );
            listGroups.setAdapter(sAdapter);
        } catch (Exception e) {
        }

    }

    public class GetUserGroups extends AsyncTask<UUID, Void, Response> {
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
        protected void onPostExecute(Response response) {
            SetData(result);
            //pdLoading.dismiss();

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

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.user_groups_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean ret;
        if (item.getItemId() == R.id.action_update) {
            ret = true;
            try {
                updateUserGroups = new GetUserGroups();
                updateUserGroups.execute();
            } catch (Exception ignored) {

            }
        }
        if (item.getItemId() == R.id.action_search) {
            ret = true;
            Intent intent = new Intent(getActivity(), GroupsActivity.class);
            startActivity(intent);
        } else {
            ret = super.onOptionsItemSelected(item);
        }
        return ret;
    }
}