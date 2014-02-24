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

import com.example.Milestone1.Adapters.FriendsAdapter;
import com.example.Milestone1.Classes.Members;
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

public class GroupMembersFragment extends Fragment {
    GetGroupMembers getGroupMembers;
    Members[] members;
    Exception exception;
    ListView listMembers;
    UUID token, groupID, memberID;
    Integer offset;
    Integer count;
    private Response result;
    private FriendsAdapter sAdapter;
    private ArrayList<Map<String, String>> data;

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
            members = (Members[]) response.getItem();

            data = new ArrayList<Map<String, String>>(
                    members.length);

            Map<String, String> m;
            for (Members member : members) {
                m = new HashMap<String, String>();
                m.put("name", member.getFirstName());
                m.put("lastName", member.getLastName());
                if (member.getPictureID() != null && member.getPictureID().compareTo(new UUID(0, 0)) != 0) {
                    m.put("picture", getString(R.string.url) + "File?token=" + token + "&fileID=" + member.getPictureID());
                }
                data.add(m);
            }
            String[] from = {"name", "lastName",
                    "picture"};
            int[] to = {R.id.firstName, R.id.lastName, R.id.userImage};

            sAdapter = new FriendsAdapter(getActivity(), data, R.layout.friend_list_item,
                    from, to);

            // определяем список и присваиваем ему адаптер
            listMembers.setAdapter(sAdapter);
            listMembers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public Exception ex;

                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    int pos;

                    try {
                        pos = position;
                        memberID = members[pos].Id;
                        Intent intent = new Intent(getActivity().getApplicationContext(), UserActivity.class);
                        intent.putExtra("memberID", memberID.toString());
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
                Type fooType = new TypeToken<Response<Members[]>>() {
                }.getType();
                result = gson.fromJson(reader, fooType);
            } catch (Exception e) {
                this.ex = e;
            }
            return result;
        }
    }
}
