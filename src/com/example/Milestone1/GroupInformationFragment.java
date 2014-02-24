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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Milestone1.Classes.GroupFollow;
import com.example.Milestone1.Classes.Groups;
import com.example.Milestone1.Classes.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.UUID;

public class GroupInformationFragment extends Fragment {
    UUID token, groupID;
    Response result;
    public Groups groupInfo;
    GetGroupInformation getGroupInformation;
    ImageButton btnJoin;
    TextView groupName, groupDescription, groupWebSite;
    Exception exception;
    private GroupFollow groupFollow;
    private Response followResult;
    private PostGroupFollow postGroupFollow;
    private TextView textViewWebSite;

    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.group_information, container, false);
        groupName = (TextView) myView.findViewById(R.id.tvGroupName);
        groupDescription = (TextView) myView.findViewById(R.id.tvGroupDescription);
        groupWebSite = (TextView) myView.findViewById(R.id.tvGroupWebSite);
        textViewWebSite = (TextView) myView.findViewById(R.id.textViewWebSite);
        try {
            SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("userdetails", Context.MODE_PRIVATE);
            token = UUID.fromString(sharedPreferences.getString("token", ""));
            groupID = UUID.fromString(getActivity().getIntent().getStringExtra("groupID"));
            getGroupInformation = new GetGroupInformation();
            getGroupInformation.execute();

        } catch (Exception e) {
            this.exception = e;
        }
        return myView;
    }

    public void setData(Response response) {
        groupInfo = (Groups) response.getItem();

        groupName.setText(groupInfo.getName());
        groupDescription.setText(groupInfo.getDescription());
        if (groupInfo.getWebSite() == null) {
            groupWebSite.setVisibility(View.GONE);
            textViewWebSite.setVisibility(View.GONE);
        } else {
            groupWebSite.setText(groupInfo.WebSite);
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.group_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    public void onPrepareOptionsMenu(Menu menu) {
        try {
            groupInfo = (Groups) result.getItem();
            menu.getItem(0).setVisible(false);
            if (groupInfo.getMember() == Boolean.FALSE || groupInfo.getMember() == null) {
                menu.getItem(1).setVisible(false);
                menu.getItem(2).setTitle(R.string.join);
                menu.getItem(2).setIcon(R.drawable.ic_action_accept);
            } else {
                menu.getItem(1).setVisible(true);
                if (groupInfo.getIsOwner()) {
                    menu.getItem(2).setTitle(getString(R.string.delete));
                } else menu.getItem(2).setTitle(R.string.leave);
                menu.getItem(2).setIcon(R.drawable.ic_action_cancel);
            }
        } catch (Exception e) {
            this.exception = e;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean ret;
        if (item.getItemId() == R.id.action_edit) {
            ret = true;
            Intent intent = new Intent(getActivity().getApplicationContext(), EditGroupActivity.class);
            intent.putExtra("groupID", groupID.toString());
            startActivity(intent);
            getActivity().finish();
        } else if (item.getItemId() == R.id.action_follow) {
            ret = true;
            if (groupInfo.getIsOwner()) {
                new RemoveGroup().execute();
            } else {
                groupFollow = new GroupFollow();
                groupFollow.setToken(token);
                groupFollow.setGroupID(groupID);

                postGroupFollow = new PostGroupFollow();
                postGroupFollow.execute();
                try {
                    result = postGroupFollow.get();
                    if (result.getItem() == Boolean.TRUE) {
                        if (item.getTitle().toString() == getString(R.string.join)) {
                            item.setTitle(R.string.leave);
                            item.setIcon(R.drawable.ic_action_cancel);
                        } else {
                            item.setTitle(R.string.join);
                            item.setIcon(R.drawable.ic_action_accept);
                        }

                    }
                } catch (Exception ignored) {

                }
            }
        } else {
            ret = super.onOptionsItemSelected(item);
        }
        return ret;
    }

    public class PostGroupFollow extends AsyncTask<Void, Void, Response> {

        ProgressDialog pdLoading = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /*pdLoading.setMessage("\tЗагрузка...");
           pdLoading.show();*/
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            //pdLoading.dismiss();
        }

        protected Response doInBackground(Void... params) {

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost request = new HttpPost(getString(R.string.url) + "GroupJoin");
                StringEntity entity = new StringEntity(new Gson().toJson(groupFollow),
                        HTTP.UTF_8);
                entity.setContentType("application/json");
                request.setEntity(entity);
                HttpResponse response = httpclient.execute(request);
                InputStreamReader reader = new InputStreamReader(response.getEntity().getContent());
                result = new Gson().fromJson(reader, Response.class);

            } catch (Exception e) {

            }
            return result;
        }
    }

    public class GetGroupInformation extends AsyncTask<UUID, Void, Response> {
        public Exception ex;

        ProgressDialog pdLoading = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*pdLoading.setMessage("\tЗагрузка...");
            pdLoading.show();*/
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            setData(response);
            //pdLoading.dismiss();
        }


        @Override
        protected Response doInBackground(UUID... params) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                Gson gson = new Gson();
                HttpGet request = new HttpGet(getString(R.string.url) + "Group?token=" + token + "&groupID=" + groupID);
                HttpResponse response = httpclient.execute(request);
                InputStreamReader reader = new InputStreamReader(response.getEntity()
                        .getContent(), HTTP.UTF_8);
                Type fooType = new TypeToken<Response<Groups>>() {
                }.getType();
                result = gson.fromJson(reader, fooType);
            } catch (Exception e) {
                this.ex = e;
            }
            return result;
        }
    }

    public class RemoveGroup extends AsyncTask<Void, Void, Response> {
        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            if (result.getItem() == Boolean.TRUE) {
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            } else
                Toast.makeText(getActivity(), "Только владелец может выполнить это действие", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Response doInBackground(Void... voids) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                Gson gson = new Gson();
                HttpDelete request = new HttpDelete(getString(R.string.url) + "Group?token=" + token + "&groupID=" + groupID);
                HttpResponse response = httpclient.execute(request);
                InputStreamReader reader = new InputStreamReader(response.getEntity()
                        .getContent(), HTTP.UTF_8);
                result = gson.fromJson(reader, Response.class);
            } catch (Exception e) {

            }
            return result;
        }
    }
}