package com.example.Milestone1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Milestone1.Classes.Friend;
import com.example.Milestone1.Classes.Response;
import com.example.Milestone1.Classes.User;
import com.example.Milestone1.ServerConnect.DownloadImagesTask;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserActivity extends Activity {
    private static final int DIALOG_BIRTHDATE = 0;
    UUID token, userID;
    Response result;
    PostFriend followTask;
    GetUserInformation getUser;
    Friend friend = new Friend();
    public Exception exception;
    private Date userDate;
    private String BirthDate;
    private TextView tvName;
    private TextView tvCity;
    private ListView listView;
    private Intent intent;
    private ImageView image;
    private User user;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        tvName = (TextView) findViewById(R.id.tvName);
        tvCity = (TextView) findViewById(R.id.tvCity);
        listView = (ListView) findViewById(R.id.listView);

        SharedPreferences userDetails = getSharedPreferences("userdetails", MODE_PRIVATE);
        token = UUID.fromString(userDetails.getString("token", ""));
        try {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                if (extras.getString("userID") != null)
                    userID = UUID.fromString(extras.getString("userID"));
            } else {
                userID = null;
            }
            getUser = new GetUserInformation();
            getUser.execute();

        } catch (Exception e) {
            this.exception = e;
        }
    }

    public void setData(Response response) {
        user = (User) response.getItem();
        try {
            if (user.getBirthDate() != null) {
                userDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(user.getBirthDate());
                BirthDate = new SimpleDateFormat("dd MMMM yyyy").format(userDate);
            }
            tvName.setText(user.getFirstName() + " " + user.getLastName());
            tvCity.setText(user.getCity());
            String[] titles = {"ДЕНЬ РОЖДЕНИЯ", "ГОРОД", "СТРАНА", "ПОЧТА", "ТЕЛЕФОН"};
            String[] data = {BirthDate, user.getCity(), user.getCountry(), user.getEMail(), user.getPhone()};
            ArrayList<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>(
                    titles.length);

            Map<String, Object> m;
            for (int i = 0; i < titles.length; i++) {
                m = new HashMap<String, Object>();
                m.put("titles", titles[i]);
                m.put("data", data[i]);
                arrayList.add(m);
            }

            SimpleAdapter sAdapter = new SimpleAdapter(this, arrayList, android.R.layout.simple_list_item_2,
                    new String[]{"titles", "data"},
                    new int[]{android.R.id.text1, android.R.id.text2});

            listView.setAdapter(sAdapter);
            String URL = getString(R.string.url) + "File?token=" + token + "&fileID=" + user.getPictureID();
            image = (ImageView) findViewById(R.id.imgUser);
            image.setTag(URL);
            new DownloadImagesTask().execute(image);
        } catch (Exception e) {
            this.exception = e;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            getMenuInflater().inflate(R.menu.profile_menu, menu);
            if (getIntent().getExtras() != null) {
                menu.getItem(0).setVisible(false);
                user = (User) result.getItem();
                if (user.getFollowing() == Boolean.TRUE) {
                    menu.getItem(1).setVisible(true);
                    menu.getItem(1).setTitle(R.string.unfollow);
                    menu.getItem(1).setIcon(R.drawable.ic_action_cancel);
                } else {
                    menu.getItem(1).setVisible(true);
                    menu.getItem(1).setTitle(R.string.follow);
                    menu.getItem(1).setIcon(R.drawable.ic_action_accept);
                }
                menu.getItem(2).setVisible(false);
            } else {
                menu.getItem(0).setVisible(true);
                menu.getItem(1).setVisible(false);
                menu.getItem(2).setVisible(true);
            }
        } catch (Exception e) {
            this.exception = e;
        }


        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean ret;
        if (item.getItemId() == R.id.action_edit) {
            ret = true;
            startActivity(new Intent(this, UserEditActivity.class));
            finish();
        } else if (item.getItemId() == R.id.action_follow) {
            ret = true;
            friend.FriendID = userID;
            friend.Token = token;
            try {
                followTask = new PostFriend();
                followTask.execute();
                result = followTask.get();
                if (result.getItem() == Boolean.TRUE) {
                    if (item.getTitle().toString() == getString(R.string.follow)) {
                        item.setTitle(R.string.unfollow);
                        item.setIcon(R.drawable.ic_action_cancel);
                    } else {
                        item.setTitle(R.string.follow);
                        item.setIcon(R.drawable.ic_action_accept);
                    }
                } else {
                    Toast.makeText(this, "Не удалось подписаться на пользователя", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                this.exception = e;
            }
        } else if (item.getItemId() == R.id.action_quit) {
            ret = true;
            SharedPreferences userDetails = getSharedPreferences("userdetails", MODE_PRIVATE);
            SharedPreferences.Editor edit = userDetails.edit();
            edit.remove("token");
            edit.commit();
            intent = new Intent(this, AuthorizationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            return super.onOptionsItemSelected(item);
        }
        return ret;
    }

    public class GetUserInformation extends AsyncTask<UUID, Void, Response> {
        private Exception exception;
        ProgressDialog pdLoading = new ProgressDialog(UserActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage(getString(R.string.please_wait));
            //pdLoading.setCancelable(false);
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
                HttpGet request = new HttpGet(
                        getString(R.string.url) + "User?token=" + token + "&userID=" + userID);
                HttpResponse response = httpclient.execute(request);
                InputStreamReader reader = new InputStreamReader(response.getEntity()
                        .getContent(), HTTP.UTF_8);
                Type fooType = new TypeToken<Response<User>>() {
                }.getType();
                result = gson.fromJson(reader, fooType);
            } catch (Exception e) {
                this.exception = e;
            }
            return result;
        }
    }

    public class PostFriend extends AsyncTask<Friend, Void, Response> {

        private Exception exception;
        ProgressDialog pdLoading = new ProgressDialog(UserActivity.this);

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
        }

        protected Response doInBackground(Friend... params) {

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost request = new HttpPost(getString(R.string.url) + "Friend");
                StringEntity entity = new StringEntity(new Gson().toJson(friend),
                        HTTP.UTF_8);
                entity.setContentType("application/json");
                request.setEntity(entity);
                HttpResponse response = httpclient.execute(request);
                InputStreamReader reader = new InputStreamReader(response.getEntity().getContent());
                result = new Gson().fromJson(reader, Response.class);

            } catch (Exception e) {
                this.exception = e;
            }
            return result;
        }
    }


}