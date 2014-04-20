package com.example.Milestone1;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.Milestone1.Adapters.FeedAdapter;
import com.example.Milestone1.Classes.Feed;
import com.example.Milestone1.Classes.Response;
import com.example.Milestone1.Classes.SendMessage;
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

public class UserFeedFragment extends Fragment {

    GetFeed getFeed;
    Feed[] feed;
    Response result;
    ListView listFeed;
    UUID token, groupID;
    Integer offset;
    Integer count;
    EditText editSendMessage;
    SendMessage send = new SendMessage();
    public Exception exception;
    private Date[] datetime;
    private FeedAdapter sAdapter;
    Map<String, String> m;
    String timeOffset;
    ArrayList<Map<String, String>> data;
    private UUID messageID;
    private TextView tvInfo;

    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.feed, container, false);
        setRetainInstance(true);
        try {
            SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("userdetails", Context.MODE_PRIVATE);
            token = UUID.fromString(sharedPreferences.getString("token", ""));
            getFeed = new GetFeed();
            getFeed.execute();

            listFeed = (ListView) myView.findViewById(R.id.listFeed);
            tvInfo = (TextView) myView.findViewById(R.id.tvInfo);
            listFeed.setVisibility(View.INVISIBLE);
            tvInfo.setVisibility(View.INVISIBLE);
            listFeed.setFocusable(true);
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

    @Override
    public void onResume() {
        super.onResume();
    }

    public void setData(Response response) {
        try {
            if (response.getItem() != null) {
                listFeed.setVisibility(View.VISIBLE);
            feed = (Feed[]) response.getItem();
            datetime = new Date[feed.length];
            data = new ArrayList<>(
                    feed.length);
            data.clear();
            for (int i = 0; i < feed.length; i++) {
                m = new HashMap<>();
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
            } else {
                tvInfo.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            this.exception = e;
        }
    }

    public void updateData(Response response) {
        try {
            Feed[] newFeed = (Feed[]) response.getItem();
            if (newFeed.length == 0) {
                Toast.makeText(getActivity(), "New messages not found", Toast.LENGTH_SHORT).show();
            } else {
                feed = newFeed;
                for (int i = 0; i < feed.length; i++) {
                    m = new HashMap<>();
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
            new RemoveMessage().execute();
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
                intent.putExtra("type", "feed");
                startActivity(intent);
                return true;
            case R.id.action_update:
                new GetFeed().execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Получение сообщений ленты
    public class GetFeed extends AsyncTask<UUID, Void, Response> {
        Exception exception;
        ProgressDialog progressDialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage(getString(R.string.please_wait));
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Response response) {
            setData(response);
            progressDialog.dismiss();
        }

        @Override
        protected Response doInBackground(UUID... params) {
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
                this.exception = e;
            }
            return result;
        }
    }

    //Получение новых сообщений ленты
    public class GetNewFeed extends AsyncTask<String, Void, Response> {
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
            timeOffset = times[0];
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
                this.exception = e;
            }
            return result;
        }
    }
}