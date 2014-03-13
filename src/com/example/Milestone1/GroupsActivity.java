package com.example.Milestone1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import java.util.UUID;

public class GroupsActivity extends Activity {

    public UUID token, parentID;
    public int level = 0;
    Exception exception;
    ListView listGroups;
    Response result;
    Groups[] groups;
    private UUID groupID;
    SharedPreferences sharedPreferences;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listgroup);
        sharedPreferences = getSharedPreferences("userdetails", MODE_PRIVATE);
        token = UUID.fromString(sharedPreferences.getString("token", ""));
        Bundle extras = getIntent().getExtras();
        try {
            if (extras != null) {
                level = extras.getInt("level");
                if (level != 0)
                    parentID = UUID.fromString(extras.getString("ParentID"));
            } else {
                level = 0;
            }
            listGroups = (ListView) findViewById(R.id.listGroups);
            new GetGroups().execute();

        } catch (Exception e) {
            this.exception = e;
        }
    }

    public void setData(Response response) {
        try {
            groups = (Groups[]) response.getItem();
            GroupsAdapter sAdapter = new GroupsAdapter(GroupsActivity.this, groups, "groups");
            listGroups.setAdapter(sAdapter);
            sAdapter.notifyDataSetChanged();
            listGroups.setClickable(true);
            listGroups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public Exception ex;

                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    try {
                        groupID = groups[position].getId();
                        Intent intent = new Intent(GroupsActivity.this, GroupFragmentActivity.class);
                        intent.putExtra("groupID", groupID.toString());
                        /*SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("groupID", groupID.toString());
                        editor.commit();*/
                        startActivity(intent);
                    } catch (Exception e) {
                        this.ex = e;
                    }
                }
            });
        } catch (Exception e) {
            this.exception = e;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.groups_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean ret;
        if (item.getItemId() == R.id.action_create) {
            ret = true;

            Intent intent = new Intent();
            intent.setClass(GroupsActivity.this, CreateGroupActivity.class);
            intent.putExtra("level", level);
            if (level != 0) {
                intent.putExtra("ParentID", parentID.toString());
            }
            startActivity(intent);
            finish();
        } else {
            ret = super.onOptionsItemSelected(item);
        }
        return ret;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        level--;
    }

    public class GetGroups extends AsyncTask<Void, Void, Response> {
        public Exception ex;
        ProgressDialog progressDialog = new ProgressDialog(GroupsActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage(getString(R.string.please_wait));
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            setData(response);
            progressDialog.dismiss();
        }

        @Override
        protected Response doInBackground(Void... params) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                Gson gson = new Gson();
                HttpGet request = new HttpGet(
                        getString(R.string.url) + "Groups?level=" + level + "&token=" + token + "&parentID=" + parentID);
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

}