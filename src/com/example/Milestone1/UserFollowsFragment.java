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

import com.example.Milestone1.Adapters.FollowsAdapter;
import com.example.Milestone1.Classes.Follows;
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

public class UserFollowsFragment extends Fragment {
    Follows[] follows;
    Exception exception;
    ListView listFriends;
    UUID token;
    private Response result;

    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.follows, container, false);
        setRetainInstance(true);
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("userdetails", Context.MODE_PRIVATE);
        token = UUID.fromString(sharedPreferences.getString("token", ""));
        try {
            new GetFriends().execute();
            if (myView != null) {
                listFriends = (ListView) myView.findViewById(R.id.listFriends);
            }
            listFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public Exception ex;

                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    try {
                        try {
                            if (follows[position].get__type().equals("UserFollow")) {
                                Intent intent = new Intent(getActivity(), UserActivity.class);
                                intent.putExtra("userID", follows[position].getId().toString());
                                startActivity(intent);
                            } else if (follows[position].get__type().equals("GroupFollow")) {
                                Intent intent = new Intent(getActivity(), GroupFragmentActivity.class);
                                intent.putExtra("groupID", follows[position].getId().toString());
                                startActivity(intent);
                            }
                        } catch (Exception e) {
                            this.ex = e;
                        }

                    } catch (Exception e) {
                        this.ex = e;
                    }
                }
            });
        } catch (Exception e) {
            this.exception = e;
        }
        return myView;
    }

    void setData(Response response) {
        try {
            follows = (Follows[]) response.getItem();
            /*ArrayList<Map<String, String>> data = new ArrayList<>(follows.length);
            for (Follows friend : follows) {
                HashMap<String, String> m = new HashMap<>();
                m.put("name", friend.getFirstName());
                m.put("lastName", friend.getLastName());
                if (friend.getPictureID() != null && friend.getPictureID().compareTo(new UUID(0, 0)) != 0) {
                    m.put("picture", getString(R.string.url) + "File?token=" + token + "&fileID=" + friend.getPictureID());
                }
                data.add(m);
            }*/
            FollowsAdapter sAdapter = new FollowsAdapter(getActivity(), follows);
            listFriends.setAdapter(sAdapter);
        } catch (Exception e) {
            this.exception = e;
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.user_friends_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean ret;
        if (item.getItemId() == R.id.action_update) {
            ret = true;
            new GetFriends().execute();
        } else {
            ret = super.onOptionsItemSelected(item);
        }
        return ret;
    }

    public class GetFriends extends AsyncTask<UUID, Void, Response> {
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
                HttpGet request = new HttpGet(getString(R.string.url) + "Follows?token=" + token);
                HttpResponse response = httpclient.execute(request);
                InputStreamReader reader = new InputStreamReader(response.getEntity()
                        .getContent(), HTTP.UTF_8);
                Type fooType = new TypeToken<Response<Follows[]>>() {
                }.getType();
                result = gson.fromJson(reader, fooType);

            } catch (Exception e) {
                this.exception = e;
            }
            return result;
        }
    }
}