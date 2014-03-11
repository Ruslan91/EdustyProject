package com.example.Milestone1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.Milestone1.Adapters.FeedAdapter;
import com.example.Milestone1.Classes.GroupFeedRead;
import com.example.Milestone1.Classes.Response;
import com.example.Milestone1.Classes.SendGroupMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GroupFeedFragment extends Fragment {
    final String ATTRIBUTE_NAME_TEXTF = "titles";
    final String ATTRIBUTE_NAME_IMAGE = "picture";
    final String ATTRIBUTE_NAME_MESSAGE = "messages";
    final String ATTRIBUTE_NAME_TIME = "times";
    GetGroupFeed getGroupFeed;
    GroupFeedRead[] feed;
    Response result;
    String timeOffset;
    ListView listFeed;
    UUID token, groupID;
    Integer offset;
    Integer count;
    EditText editSendMessage;
    SendGroupMessage send = new SendGroupMessage();
    Response success;
    public Exception exception;
    SimpleAdapter sAdapter;
    Map<String, String> m;
    private ArrayList<Map<String, String>> data;
    private Date[] datetime;
    private UUID messageID;
    private boolean groupOwner;

    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.feed, container, false);

        editSendMessage = (EditText) myView.findViewById(R.id.editSendMessage);
        try {
            SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("userdetails", Context.MODE_PRIVATE);
            token = UUID.fromString(sharedPreferences.getString("token", ""));
            groupID = UUID.fromString(getActivity().getIntent().getStringExtra("groupID"));
            groupOwner = getActivity().getIntent().getExtras().getBoolean("groupOwner");
            getGroupFeed = new GetGroupFeed();
            getGroupFeed.execute();
            listFeed = (ListView) myView.findViewById(R.id.listFeed);
            listFeed.setClickable(true);
            registerForContextMenu(listFeed);
            listFeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                }
            });
        } catch (Exception e) {
            this.exception = e;
        }
        return myView;
    }

    public void setData(Response response) {
        try {
            feed = (GroupFeedRead[]) response.getItem();
            datetime = new Date[feed.length];
            data = new ArrayList<>(
                    feed.length);
            data.clear();
            for (int i = 0; i < feed.length; i++) {
                datetime[i] = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(feed[i].getTime());
                m = new HashMap<>();
                m.put(ATTRIBUTE_NAME_TEXTF, feed[i].getFirstName() + " " + feed[i].getLastName());
                m.put(ATTRIBUTE_NAME_MESSAGE, feed[i].getMessage());
                m.put(ATTRIBUTE_NAME_TIME, new SimpleDateFormat("EEE, dd MMMM yyyy" + " " + "HH:mm").format(datetime[i]));
                if (feed[i].getPictureID() != null && feed[i].getPictureID().compareTo(new UUID(0, 0)) != 0) {
                    m.put("picture", getString(R.string.url) + "File?token=" + token + "&fileID=" + feed[i].getPictureID());
                }
                data.add(m);
            }
            String[] from = {ATTRIBUTE_NAME_TEXTF,
                    ATTRIBUTE_NAME_IMAGE, ATTRIBUTE_NAME_MESSAGE, ATTRIBUTE_NAME_TIME};

            int[] to = {R.id.tvTitle, R.id.imgFeed, R.id.tvMessage, R.id.tvTime};

            sAdapter = new FeedAdapter(getActivity(), data, R.layout.feed_list_item,
                    from, to);

            listFeed.setAdapter(sAdapter);
        } catch (Exception e) {
            this.exception = e;
        }
    }

    public void updateData(Response response) {
        try {
            GroupFeedRead[] newFeed = (GroupFeedRead[]) response.getItem();
            if (newFeed.length == 0) {
                Toast.makeText(getActivity(), "New messages not found", Toast.LENGTH_SHORT).show();
            } else {
                feed = newFeed;
                for (int i = 0; i < feed.length; i++) {
                    datetime[i] = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(feed[i].getTime());
                    m = new HashMap<>();
                    m.put(ATTRIBUTE_NAME_TEXTF, feed[i].getFirstName() + " " + feed[i].getLastName());
                    m.put(ATTRIBUTE_NAME_MESSAGE, feed[i].getMessage());
                    m.put(ATTRIBUTE_NAME_TIME, new SimpleDateFormat("EEE, dd MMMM yyyy" + " " + "HH:mm").format(datetime[i]));
                    if (feed[i].getPictureID() != null && feed[i].getPictureID().compareTo(new UUID(0, 0)) != 0) {
                        m.put("picture", getString(R.string.url) + "File?token=" + token + "&fileID=" + feed[i].getPictureID());
                    }
                    data.add(0, m);
                }
            }
            sAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            this.exception = e;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 1, 0, "Удалить запись");
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
        if (feed[acmi.position].getIsMessageOwner() == Boolean.TRUE || groupOwner == Boolean.TRUE) {
            menu.getItem(0).setEnabled(true);
        } else menu.getItem(0).setEnabled(false);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            // получаем инфу о пункте списка
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            messageID = feed[acmi.position].getId();
            RemoveMessage removeMessage = new RemoveMessage();
            removeMessage.execute();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.feed_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_message:
                Intent intent = new Intent(getActivity(), CreateFeedMessageActivity.class);
                intent.putExtra("type", "groupFeed");
                intent.putExtra("groupID", groupID.toString());
                startActivity(intent);
                return true;
            case R.id.action_update:
                new GetGroupFeed().execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class GetGroupFeed extends AsyncTask<UUID, Void, Response> {
        public Exception ex;
        ProgressDialog pdLoading = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tЗагрузка...");
            pdLoading.show();
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            setData(response);
            pdLoading.dismiss();
        }

        @Override
        protected Response doInBackground(UUID... params) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                Gson gson = new Gson();
                HttpGet request = new HttpGet(getString(R.string.url) + "GroupMessages?token=" + token + "&groupID=" + groupID + "&offset=" + offset + "&count=" + count);
                HttpResponse response = httpclient.execute(request);
                InputStreamReader reader = new InputStreamReader(response.getEntity()
                        .getContent(), HTTP.UTF_8);
                Type fooType = new TypeToken<Response<GroupFeedRead[]>>() {
                }.getType();
                result = gson.fromJson(reader, fooType);
            } catch (Exception e) {
                this.ex = e;
            }
            return result;
        }
    }

    public class GetNewGroupFeed extends AsyncTask<String, Void, Response> {
        Exception exception;
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        String timeOffset = null;

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage(getString(R.string.please_wait));
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Response response) {
            updateData(response);
            progressDialog.dismiss();
        }

        @Override
        protected Response doInBackground(String... times) {
            try {
                timeOffset = times[0];
                HttpClient httpclient = new DefaultHttpClient();
                Gson gson = new Gson();
                HttpGet request = new HttpGet(getString(R.string.url) + "GroupMessages?token=" + token + "&groupID=" + groupID + "&timeOffset=" + timeOffset);
                HttpResponse response = httpclient.execute(request);
                InputStreamReader reader = new InputStreamReader(response.getEntity()
                        .getContent(), HTTP.UTF_8);
                Type fooType = new TypeToken<Response<GroupFeedRead[]>>() {
                }.getType();
                result = gson.fromJson(reader, fooType);
            } catch (Exception e) {
                this.exception = e;
            }
            return result;
        }
    }

    public class RemoveMessage extends AsyncTask<Void, Void, Response> {
        Exception exception;

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            if (result.getItem() == Boolean.TRUE) {
                getGroupFeed = new GetGroupFeed();
                getGroupFeed.execute();
            } else
                Toast.makeText(getActivity(), "Только владелец может выполнить это действие", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Response doInBackground(Void... voids) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                Gson gson = new Gson();
                HttpDelete request = new HttpDelete(getString(R.string.url) + "GroupMessages?token=" + token + "&messageID=" + messageID);
                HttpResponse response = httpclient.execute(request);
                InputStreamReader reader = new InputStreamReader(response.getEntity()
                        .getContent(), HTTP.UTF_8);
                result = gson.fromJson(reader, Response.class);
            } catch (Exception e) {
                this.exception = e;
            }
            return result;
        }
    }
}