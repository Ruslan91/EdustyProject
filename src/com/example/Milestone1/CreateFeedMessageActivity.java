package com.example.Milestone1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.Milestone1.Classes.Response;
import com.example.Milestone1.Classes.SendMessage;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import java.io.InputStreamReader;
import java.util.UUID;

/**
 * Created by Руслан on 11.03.14.
 */
public class CreateFeedMessageActivity extends Activity {
    private EditText etSendMessage;
    private Response result;
    private SendMessage sendMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_create);
        etSendMessage = (EditText) findViewById(R.id.etSendMessage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_message_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_send:
                if (!etSendMessage.getText().toString().equals("")) {
                    sendMessage = new SendMessage();
                    sendMessage.setToken(UUID.fromString(getSharedPreferences("userdetails", MODE_PRIVATE).getString("token", "")));
                    sendMessage.setMessage(etSendMessage.getText().toString());
                    if (getIntent().getStringExtra("groupID") != null)
                        sendMessage.setGroupID(UUID.fromString(getIntent().getStringExtra("groupID")));
                    new SendMessageToFeed().execute();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    //Отправить сообщение в ленту
    public class SendMessageToFeed extends AsyncTask<String, Void, Response> {
        Exception exception;
        private HttpPost request;
        ProgressDialog progressDialog = new ProgressDialog(CreateFeedMessageActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage(getString(R.string.please_wait));
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            if (response.getItem().equals(true)) {
                progressDialog.dismiss();
                finish();
            }
        }

        @Override
        protected Response doInBackground(String... messages) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                if (getIntent().getStringExtra("type").equals("feed")) {
                    request = new HttpPost(getString(R.string.url) + "Feed");
                } else request = new HttpPost(getString(R.string.url) + "GroupMessages");
                StringEntity entity = new StringEntity(new Gson().toJson(sendMessage),
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
