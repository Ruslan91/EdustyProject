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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Milestone1.Adapters.FeedAdapter;
import com.example.Milestone1.Classes.Feed;
import com.example.Milestone1.Classes.Response;
import com.example.Milestone1.Classes.SendGroupMessage;
import com.example.Milestone1.Classes.SendMessage;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserFeedFragment extends Fragment implements View.OnClickListener {

    GetFeed getFeed;
    Feed[] feed;
    Response result;
    ListView listFeed;
    UUID token, groupID;
    Integer offset;
    Integer count;
    EditText editSendMessage;
    SendMessage send = new SendMessage();
    Boolean sent;
    SendMessageToFeed sendmessage;
    public Exception exception;
    private Date[] datetime;
    private FeedAdapter sAdapter;
    Map<String, String> m;
    String timeOffset;
    ArrayList<Map<String, String>> data;
    private Feed[] newFeed;
    private TextView tvName;
    private UUID messageID;

    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.feed, container, false);
        setRetainInstance(true);
        editSendMessage = (EditText) myView.findViewById(R.id.editSendMessage);
        try {
            SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("userdetails", Context.MODE_PRIVATE);
            token = UUID.fromString(sharedPreferences.getString("token", ""));
            getFeed = new GetFeed();
            getFeed.execute();

            listFeed = (ListView) myView.findViewById(R.id.listFeed);
            listFeed.setFocusable(true);
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
            feed = (Feed[]) response.getItem();
            datetime = new Date[feed.length];
            data = new ArrayList<Map<String, String>>(
                    feed.length);
            data.clear();
            for (int i = 0; i < feed.length; i++) {
                m = new HashMap<String, String>();
                if (feed[i].getTitle() != null) {
                    m.put("names", feed[i].getFirstName() + " " + feed[i].getLastName());
                    m.put("titles", feed[i].getTitle());
                } else {
                    m.put("names", "");
                    m.put("titles", feed[i].getFirstName() + " " + feed[i].getLastName());
                }
                m.put("messages", feed[i].getBody());
                datetime[i] = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(feed[i].getTime());
                m.put("times", new SimpleDateFormat("EEE, dd MMMM yyyy" + " " + "HH:mm").format(datetime[i]));
                if (feed[i].getPictureID() != null) {
                    m.put("picture", getString(R.string.url) + "File?token=" + token + "&fileID=" + feed[i].getPictureID());
                }
                data.add(m);
            }
            sAdapter = new FeedAdapter(getActivity(), data, R.layout.feed_list_item,
                    new String[]{"names", "messages", "times", "titles"},
                    new int[]{R.id.tvName, R.id.tvMessage, R.id.tvTime, R.id.tvTitle}
            );
            listFeed.setAdapter(sAdapter);
        /*if (feed != null) {
            timeOffset = feed[0].getTime().toString();
        } else timeOffset = null;
        GetNewFeed updateFeed = new GetNewFeed();
        updateFeed.execute();
        try {
            result = updateFeed.get();
            newFeed = (Feed[]) result.getItem();
            if (newFeed.length == 0) {
                Toast.makeText(getActivity(), "New messages not found", Toast.LENGTH_SHORT).show();
            } else {
                feed = newFeed;
                for (int i = 0; i < feed.length; i++) {
                    m = new HashMap<String, String>();
                    if (feed[i].getTitle() != null) {
                        m.put("names", feed[i].getFirstName() + " " + feed[i].getLastName());
                        m.put("titles", feed[i].getTitle());
                    } else {
                        m.put("names", "");
                        m.put("titles", feed[i].getFirstName() + " " + feed[i].getLastName());

                    }
                    m.put("messages", feed[i].getBody());
                    datetime[i] = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(feed[i].getTime());
                    m.put("times", new SimpleDateFormat("EEE, dd MMMM yyyy" + " " + "HH:mm").format(datetime[i]));
                    if (feed[i].getPictureID() != null && feed[i].getPictureID().compareTo(new UUID(0, 0)) != 0) {
                        m.put("picture", getString(R.string.url) + "File?token=" + token + "&fileID=" + feed[i].getPictureID());
                    }
                    data.add(0, m);
                }
                sAdapter.notifyDataSetChanged();
            }*/
        } catch (Exception e) {
            this.exception = e;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSend:
                try {
                    send.Message = editSendMessage.getText().toString();
                    editSendMessage.setText("");
                    send.Token = token;
                    sendmessage = new SendMessageToFeed();
                    sendmessage.execute();
                    //setData();
                } catch (Exception e) {
                    this.exception = e;
                }

        }
    }

/*    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 1, 0, "Удалить запись");
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
        if (feed[acmi.position].getIsMessageOwner() == Boolean.FALSE) {
            menu.getItem(0).setEnabled(false);
        } else menu.getItem(0).setEnabled(true);
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
    }*/

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.feed_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean ret;
        if (item.getItemId() == R.id.action_update) {
            ret = true;

            //setData();
        } else {
            ret = super.onOptionsItemSelected(item);
        }
        return ret;
    }

    //Получение сообщений ленты
    public class GetFeed extends AsyncTask<UUID, Void, Void> {
        public Exception ex;
        ProgressDialog pdLoading = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Void v) {
            setData(result);
        }

        @Override
        protected Void doInBackground(UUID... params) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                Gson gson = new Gson();
                HttpGet request = new HttpGet(getString(R.string.url) + "Feed?token=" + token + "&count=" + count + "&offset=" + offset);
                HttpResponse response = httpclient.execute(request);
                InputStreamReader reader = new InputStreamReader(response.getEntity()
                        .getContent(), HTTP.UTF_8);
                Type fooType = new TypeToken<Response<Feed[]>>() {
                }.getType();
                result = gson.fromJson(reader, fooType);


            } catch (Exception e) {
                this.ex = e;
            }
            return null;
        }
    }

    //Получение новых сообщений ленты
    public class GetNewFeed extends AsyncTask<UUID, Void, Response> {
        public Exception ex;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Response response) {

        }

        @Override
        protected Response doInBackground(UUID... params) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                Gson gson = new Gson();
                HttpGet request = new HttpGet(getString(R.string.url) + "Feed?token=" + token + "&timeOffset=" + timeOffset);
                HttpResponse response = httpclient.execute(request);
                InputStreamReader reader = new InputStreamReader(response.getEntity()
                        .getContent(), HTTP.UTF_8);
                Type fooType = new TypeToken<Response<Feed[]>>() {
                }.getType();
                result = gson.fromJson(reader, fooType);
            } catch (Exception e) {
                this.ex = e;
            }
            return result;
        }
    }

    //Отправить сообщение в ленту
    public class SendMessageToFeed extends AsyncTask<SendGroupMessage, Void, Response> {
        public Exception ex;

        @Override
        protected Response doInBackground(SendGroupMessage... params) {

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost request = new HttpPost(getString(R.string.url) + "Feed");
                StringEntity entity = new StringEntity(new Gson().toJson(send),
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

    public class RemoveMessage extends AsyncTask<Void, Void, Response> {
        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            if (result.getItem() == Boolean.TRUE) {
                getFeed = new GetFeed();
                getFeed.execute();
            } else
                Toast.makeText(getActivity(), "Только владелец может выполнить это действие", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Response doInBackground(Void... voids) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                Gson gson = new Gson();
                HttpDelete request = new HttpDelete(getString(R.string.url) + "Feed?token=" + token + "&messageID=" + messageID);
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