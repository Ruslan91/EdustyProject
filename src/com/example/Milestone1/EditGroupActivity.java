package com.example.Milestone1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.Milestone1.Classes.GroupWrite;
import com.example.Milestone1.Classes.Groups;
import com.example.Milestone1.Classes.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * Created by Руслан on 15.11.13.
 */
public class EditGroupActivity extends Activity {
    private Spinner spFree;
    private EditText groupName;
    private EditText groupDescription;
    private EditText groupWebSite;
    private UUID token;
    private int level;
    private UUID parentID;
    private Exception exception;
    private Response result;
    private UUID groupID;
    private Groups groupInformation;
    private GroupWrite editGroup;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_edit);
        spFree = (Spinner) findViewById(R.id.spFree);
        groupName = (EditText) findViewById(R.id.tvName);
        groupDescription = (EditText) findViewById(R.id.tvDescription);
        groupWebSite = (EditText) findViewById(R.id.groupWebSite);

        SharedPreferences sharedPreferences = getSharedPreferences("userdetails", Context.MODE_PRIVATE);
        token = UUID.fromString(sharedPreferences.getString("token", ""));

        Bundle extras = getIntent().getExtras();
        try {
            if (extras != null) {
                groupID = UUID.fromString(extras.getString("groupID"));
            }
            GetGroupInformation getGroupInformation = new GetGroupInformation();
            getGroupInformation.execute();
            result = getGroupInformation.get();
            groupInformation = (Groups) result.getItem();

            groupName.setText(groupInformation.getName());
            groupDescription.setText(groupInformation.getDescription());
            groupWebSite.setText(groupInformation.getWebSite());
            if (groupInformation.getFree() == Boolean.TRUE) {
                spFree.setSelection(0);
            } else spFree.setSelection(1);

        } catch (Exception e) {
            this.exception = e;
        }

    }

    public void onClickBtnEditGroup(View view) {
        editGroup = new GroupWrite();
        editGroup.Name = groupName.getText().toString();
        editGroup.Description = groupDescription.getText().toString();
        editGroup.WebSite = groupWebSite.getText().toString();
        editGroup.Id = groupInformation.getId();
        editGroup.Parent = groupInformation.getParent();
        editGroup.GroupLevel = groupInformation.getGroupLevel();
        editGroup.Token = token;
        editGroup.PictureID = groupInformation.getPictureID();
        if (spFree.getSelectedItemPosition() == 0) {
            editGroup.Free = true;
        } else editGroup.Free = false;

        EditGroup edit = new EditGroup();
        edit.execute();
        try {
            result = edit.get();
            if (result.getStatusCode() == 0) {
                Intent intent = new Intent(this, GroupFragmentActivity.class);
                intent.putExtra("groupID", groupID.toString());
                startActivity(intent);
                finish();
            } else
                Toast.makeText(this, "StatusCode:" + result.getStatusCode(), Toast.LENGTH_SHORT).show();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }

    public class EditGroup extends AsyncTask<GroupWrite, Void, Response> {

        private Exception ex;

        protected Response doInBackground(GroupWrite... params) {

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost request = new HttpPost(getString(R.string.url) + "Group");
                StringEntity entity = new StringEntity(new Gson().toJson(editGroup),
                        HTTP.UTF_8);
                entity.setContentType("application/json");
                request.setEntity(entity);
                HttpResponse response = httpclient.execute(request);
                InputStreamReader reader = new InputStreamReader(response.getEntity().getContent());
                result = new Gson().fromJson(reader, Response.class);

            } catch (Exception e) {
                this.ex = e;
            }
            return result;
        }
    }

    public class GetGroupInformation extends AsyncTask<UUID, Void, Response> {
        public Exception ex;

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
}
