package com.example.Milestone1;

import android.app.Activity;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GroupsActivity extends Activity {

    final String ATTRIBUTE_NAME_TEXTN = "textN";
    final String ATTRIBUTE_NAME_TEXTD = "textD";
    final String ATTRIBUTE_NAME_GROUPID = "groupID";
    public UUID token, parentID;
    public int level = 0;
    Exception exception;
    ListView listGroups;
    Response result;
    Groups[] groups;
    GetGroups getGroups;
    private UUID groupID;
    SharedPreferences sharedPreferences;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listgroup);
        sharedPreferences = getSharedPreferences("userdetails", MODE_PRIVATE);
        token = UUID.fromString(sharedPreferences.getString("token", ""));
        try {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                level = extras.getInt("level");
                if (level != 0)
                    parentID = UUID.fromString(extras.getString("ParentID"));
            } else {
                level = 0;
            }

            getGroups = new GetGroups();
            getGroups.execute();
            result = getGroups.get();
            groups = (Groups[]) result.getItem();
            String[] names = new String[groups.length];
            String[] descriptions = new String[groups.length];
            String[] groupsID = new String[groups.length];
            String[] pictures = new String[groups.length];
            for (int i = 0; i < groups.length; i++) {
                names[i] = groups[i].Name;
                descriptions[i] = groups[i].Description;
                groupsID[i] = groups[i].getId().toString();
                pictures[i] = getString(R.string.url) + "File?fileID=" + groups[i].getPictureID().toString() + "&token=" + token;
            }

            ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>(
                    groups.length);
            Map<String, String> m;
            for (int i = 0; i < groups.length; i++) {
                m = new HashMap<String, String>();
                m.put(ATTRIBUTE_NAME_TEXTN, names[i]);
                m.put(ATTRIBUTE_NAME_TEXTD, descriptions[i]);
                m.put(ATTRIBUTE_NAME_GROUPID, groupsID[i]);
                m.put("picture", pictures[i]);
                m.put("level", String.valueOf(level));
                m.put("layout", "groups");
                data.add(m);
            }

            String[] from = {ATTRIBUTE_NAME_TEXTN, ATTRIBUTE_NAME_TEXTD};

            int[] to = {R.id.groupName, R.id.groupDescription};

            GroupsAdapter sAdapter = new GroupsAdapter(this, data, R.layout.groups_list_item,
                    from, to);

            listGroups = (ListView) findViewById(R.id.listGroups);
            listGroups.setAdapter(sAdapter);
            sAdapter.notifyDataSetChanged();
            listGroups.setClickable(true);
            listGroups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public Exception ex;

                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    try {
                        groupID = groups[position].getId();
                        Intent intent = new Intent(GroupsActivity.this.getApplicationContext(), GroupFragmentActivity.class);
                        intent.putExtra("groupID", groupID.toString());
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("groupID", groupID.toString());
                        editor.commit();
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
        getMenuInflater().inflate(R.menu.group_menu, menu);
        menu.getItem(1).setVisible(false);
        menu.getItem(2).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean ret;
        if (item.getItemId() == R.id.action_create) {
            ret = true;

            Intent intent = new Intent();
            intent.setClass(GroupsActivity.this.getApplicationContext(), CreateGroupActivity.class);
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