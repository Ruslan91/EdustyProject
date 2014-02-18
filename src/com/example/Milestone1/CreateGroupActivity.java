package com.example.Milestone1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.Milestone1.Classes.GroupWrite;
import com.example.Milestone1.Classes.Response;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import java.io.InputStreamReader;
import java.util.UUID;

public class CreateGroupActivity extends Activity {
    public UUID parentID, token;
    public int level;
    GroupWrite create = new GroupWrite();
    public UUID success, groupID;
    EditText groupName, groupDescription, groupWebSite;
    CreateGroup createGroup;
    Exception exception;
    private Response result;
    private Spinner spFree;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_create);
        spFree = (Spinner) findViewById(R.id.spFree);
        groupName = (EditText) findViewById(R.id.groupName);
        groupDescription = (EditText) findViewById(R.id.groupDescription);
        groupWebSite = (EditText) findViewById(R.id.groupWebSite);

        SharedPreferences sharedPreferences = getSharedPreferences("userdetails", MODE_PRIVATE);
        token = UUID.fromString(sharedPreferences.getString("token", ""));
        Bundle extras = getIntent().getExtras();
        try {
            if (extras != null) {
                level = extras.getInt("level");
                if (level != 0) {
                    parentID = UUID.fromString(extras.getString("ParentID"));
                }
            }


        } catch (Exception e) {
            this.exception = e;
        }

    }

    public void onClickBtnCreateGroup(View v) {
        try {
            if (groupName.getText().toString().equals("")) {
                Toast.makeText(this, "Name can't be null", Toast.LENGTH_SHORT).show();
            }
            create.Name = groupName.getText().toString();
            create.Description = groupDescription.getText().toString();
            create.WebSite = groupWebSite.getText().toString();
            create.Token = token;
            create.Parent = parentID;
            create.GroupLevel = level;
            if (spFree.getSelectedItemPosition() == 0) {
                create.Free = true;
            } else create.Free = false;

            createGroup = new CreateGroup();
            createGroup.execute();
            result = createGroup.get();
            if (result.getItem() != null) {

                Intent intent = new Intent(this.getApplicationContext(), GroupsActivity.class);
                if (level != 0) {
                    intent.putExtra("level", level);
                    intent.putExtra("ParentID", parentID.toString());
                } else {
                    intent.putExtra("level", level);
                }

                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, getString(R.string.please_set_text), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {

        }
    }

    public class CreateGroup extends AsyncTask<GroupWrite, Void, Response> {

        private Exception ex;

        protected Response doInBackground(GroupWrite... params) {

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost request = new HttpPost(getString(R.string.url) + "Group");
                StringEntity entity = new StringEntity(new Gson().toJson(create),
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
}