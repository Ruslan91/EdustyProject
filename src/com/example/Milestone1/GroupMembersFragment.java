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

public class GroupMembersFragment extends Fragment {
    GetGroupMembers getGroupMembers;
    Follows[] members;
    Exception exception;
    ListView listMembers;
    UUID token, groupID;
    Integer offset;
    Integer count;
    private Response result;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.friends, container, false);
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("userdetails", Context.MODE_PRIVATE);
        token = UUID.fromString(sharedPreferences.getString("token", ""));
        groupID = UUID.fromString(getActivity().getIntent().getStringExtra("groupID"));
        listMembers = (ListView) myView.findViewById(R.id.listFriends);

        getGroupMembers = new GetGroupMembers();
        getGroupMembers.execute();
        return myView;
    }

    public void setData(Response response) {
        try {
            members = (Follows[]) response.getItem();

            /*ArrayList<Map<String, String>> data = new ArrayList<>(
                    members.length);

            Map<String, String> m;
            for (Follows member : members) {
                m = new HashMap<>();
                m.put("name", member.getFirstName());
                m.put("lastName", member.getLastName());
                if (member.getPictureID() != null && member.getPictureID().compareTo(new UUID(0, 0)) != 0) {
                    m.put("picture", getString(R.string.url) + "File?token=" + token + "&fileID=" + member.getPictureID());
                }
                data.add(m);
            }
            String[] from = {"name", "lastName",
                    "picture"};
            int[] to = {R.id.firstName, R.id.lastName, R.id.userImage};*/

            FollowsAdapter sAdapter = new FollowsAdapter(getActivity(), members);

            // определяем список и присваиваем ему адаптер
            listMembers.setAdapter(sAdapter);
            listMembers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public Exception ex;

                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    try {
                        Intent intent = new Intent(getActivity(), UserActivity.class);
                        intent.putExtra("userID", members[position].getId().toString());
                        startActivity(intent);
                        onDestroy();
                    } catch (Exception e) {
                        this.ex = e;
                    }
                }
            });
        } catch (Exception e) {
            this.exception = e;
        }
    }

    public class GetGroupMembers extends AsyncTask<UUID, Void, Response> {
        public Exception ex;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            setData(response);
        }

        @Override
        protected Response doInBackground(UUID... params) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                Gson gson = new Gson();
                HttpGet request = new HttpGet(getString(R.string.url) + "GroupMembers?token=" + token + "&groupID=" + groupID + "&offset=" + offset + "&count=" + count);
                HttpResponse response = httpclient.execute(request);
                InputStreamReader reader = new InputStreamReader(response.getEntity()
                        .getContent(), HTTP.UTF_8);
                Type fooType = new TypeToken<Response<Follows[]>>() {
                }.getType();
                result = gson.fromJson(reader, fooType);
            } catch (Exception e) {
                this.ex = e;
            }
            return result;
        }
    }
}
