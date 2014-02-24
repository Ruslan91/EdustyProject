package com.example.Milestone1;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.ImageButton;
import android.widget.ImageView;
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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GroupFeedFragment extends Fragment implements View.OnClickListener {
    final String ATTRIBUTE_NAME_TEXTF = "titles";
    final String ATTRIBUTE_NAME_IMAGE = "picture";
    final String ATTRIBUTE_NAME_MESSAGE = "messages";
    final String ATTRIBUTE_NAME_TIME = "times";
    int img = R.drawable.icon;
    GetGroupFeed getGroupFeed;
    GroupFeedRead[] feed;
    Response result;
    GroupFeedRead[] newFeed;
    String timeOffset;
    ListView listFeed;
    UUID token, groupID;
    Integer offset;
    Integer count;
    EditText editSendMessage;
    ImageView userImage;
    SendGroupMessage send = new SendGroupMessage();
    Response success;
    Response<Boolean> sent;
    SendMessageToGroupFeed sendmessage;
    public Exception exception;
    SimpleAdapter sAdapter;
    GetNewGroupFeed getNewFeed;
    Map<String, String> m;
    private ArrayList<Map<String, String>> data;
    private Date[] datetime;
    private GetNewGroupFeed updateFeed;
    private String[] times;
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
            //result = getGroupFeed.get();
            listFeed = (ListView) myView.findViewById(R.id.listFeed);
            listFeed.setClickable(true);
            registerForContextMenu(listFeed);
            listFeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                }
            });
            ImageButton btnSend = (ImageButton) myView.findViewById(R.id.btnSend);
            btnSend.setOnClickListener(this);
        } catch (Exception e) {
            this.exception = e;
        }
        return myView;
    }

    public void setData(Response response) {
        try {
            feed = (GroupFeedRead[]) response.getItem();
            datetime = new Date[feed.length];
            data = new ArrayList<Map<String, String>>(
                    feed.length);
            data.clear();
            for (int i = 0; i < feed.length; i++) {
                datetime[i] = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(feed[i].getTime());
                m = new HashMap<String, String>();
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
        boolean ret;
        if (item.getItemId() == R.id.action_update) {
            ret = true;
            if (feed != null) {
                timeOffset = feed[0].getTime().toString();
            } else timeOffset = null;

            updateFeed = new GetNewGroupFeed();
            updateFeed.execute();
            try {
                result = updateFeed.get();
                newFeed = (GroupFeedRead[]) result.getItem();
                if (newFeed.length == 0) {
                    Toast.makeText(getActivity(), getString(R.string.new_messages_not_found), Toast.LENGTH_SHORT).show();
                } else {
                    feed = newFeed;
                    for (int i = 0; i < feed.length; i++) {
                        m = new HashMap<String, String>();
                        m.put(ATTRIBUTE_NAME_TEXTF, feed[i].getFirstName() + " " + feed[i].getLastName());
                        m.put(ATTRIBUTE_NAME_MESSAGE, feed[i].getMessage());
                        datetime[i] = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(feed[i].getTime());
                        m.put(ATTRIBUTE_NAME_TIME, new SimpleDateFormat("EEE, dd MMMM yyyy" + " " + "HH:mm").format(datetime[i]));
                        if (feed[i].getPictureID() != null) {
                            m.put("picture", getString(R.string.url) + "File?token=" + token + "&fileID=" + feed[i].getPictureID());
                        }
                        data.add(0, m);
                    }
                    // уведомляем, что данные изменились
                    sAdapter.notifyDataSetChanged();
                }
            } catch (Exception e) {

            }

        } else {
            ret = super.onOptionsItemSelected(item);
        }
        return ret;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSend:
                try {
                    send.Message = editSendMessage.getText().toString();
                    editSendMessage.setText("");
                    send.Token = token;
                    send.GroupID = groupID;
                    sendmessage = new SendMessageToGroupFeed();
                    sendmessage.execute();
                    sent = sendmessage.get();
                    sAdapter.notifyDataSetChanged();

                    timeOffset = feed[0].Time.toString();
                    getNewFeed = new GetNewGroupFeed();
                    getNewFeed.execute();
                    result = getNewFeed.get();
                    newFeed = (GroupFeedRead[]) result.getItem();
                    if (newFeed.length == 0) {
                        Toast.makeText(getActivity(), getString(R.string.new_messages_not_found), Toast.LENGTH_SHORT).show();
                    } else {
                        feed = newFeed;
                        for (int i = 0; i < feed.length; i++) {
                            m = new HashMap<String, String>();
                            m.put(ATTRIBUTE_NAME_TEXTF, feed[i].getFirstName() + " " + feed[i].getLastName());
                            m.put(ATTRIBUTE_NAME_IMAGE, getString(R.string.url) + "File?token=" + token + "&fileID=" + feed[i].getPictureID());
                            m.put(ATTRIBUTE_NAME_MESSAGE, feed[i].getMessage());
                            datetime[i] = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(feed[i].getTime());
                            m.put(ATTRIBUTE_NAME_TIME, new SimpleDateFormat("EEE, dd MMMM yyyy" + " " + "HH:mm").format(datetime[i]));
                            data.add(0, m);
                        }
                        // уведомляем, что данные изменились
                        sAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    this.exception = e;
                }
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
            pdLoading.dismiss();
            setData(response);
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

    public class GetNewGroupFeed extends AsyncTask<UUID, Void, Response> {
        public Exception ex;

        @Override
        protected Response doInBackground(UUID... params) {
            try {
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
                this.ex = e;
            }
            return result;
        }
    }

    public class SendMessageToGroupFeed extends AsyncTask<SendGroupMessage, Void, Response> {

        private Exception ex;

        protected Response doInBackground(SendGroupMessage... params) {

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost request = new HttpPost(getString(R.string.url) + "GroupMessages");
                StringEntity entity = new StringEntity(new Gson().toJson(send),
                        HTTP.UTF_8);
                entity.setContentType("application/json");
                request.setEntity(entity);
                HttpResponse response = httpclient.execute(request);
                Reader jsonreader = new InputStreamReader(response.getEntity().getContent());
                success = new Gson().fromJson(jsonreader, Response.class);

            } catch (Exception e) {
                this.ex = e;
            }
            return success;
        }
    }

    public class RemoveMessage extends AsyncTask<Void, Void, Response> {
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

            }
            return result;
        }
    }
}