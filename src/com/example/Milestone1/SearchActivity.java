package com.example.Milestone1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.Milestone1.Adapters.SearchAdapter;
import com.example.Milestone1.Classes.Feed;
import com.example.Milestone1.Classes.GroupResult;
import com.example.Milestone1.Classes.Groups;
import com.example.Milestone1.Classes.ListSearchResult;
import com.example.Milestone1.Classes.Response;
import com.example.Milestone1.Classes.SearchResult;
import com.example.Milestone1.Classes.User;
import com.example.Milestone1.Classes.UserResult;
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
import java.util.List;
import java.util.UUID;

/**
 * Created by Руслан on 21.02.14.
 */
public class SearchActivity extends Activity {
    private Response result;
    private UUID token;
    private String searchString;
    private EditText etSearch;
    private ListView listResults;
    private SearchResult[] searchresult;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        listResults = (ListView) findViewById(R.id.listResults);
        token = UUID.fromString(getSharedPreferences("userdetails", MODE_PRIVATE).getString("token", ""));
        etSearch = (EditText) findViewById(R.id.etSearch);

    }

    public void onClickBtnSendForSearch(View view) {
        if (etSearch.getText().toString().equals("")) {
            Toast.makeText(this, getString(R.string.please_set_text), Toast.LENGTH_LONG).show();
        } else {
            searchString = etSearch.getText().toString();
            new GetSearchResults().execute();
        }
    }
    public class GetSearchResults extends AsyncTask<Void, Void, Response> {
        public Exception ex;
        ProgressDialog progressDialog = new ProgressDialog(SearchActivity.this);
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
                        getString(R.string.url) + "Search?token=" + token + "&request=" + searchString);
                HttpResponse response = httpclient.execute(request);
                InputStreamReader reader = new InputStreamReader(response.getEntity()
                        .getContent(), HTTP.UTF_8);
                Type fooType = new TypeToken<Response<SearchResult[]>>() {
                }.getType();
                result = gson.fromJson(reader, fooType);
            } catch (Exception e) {
                this.ex = e;
            }
            return result;
        }
    }

    private void setData(Response response) {
        searchresult = (SearchResult[]) response.getItem();

        SearchAdapter searchAdapter = new SearchAdapter(searchresult, this);
        listResults.setAdapter(searchAdapter);
        listResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public Exception ex;

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                try {
                    if (searchresult[position].get__type().equals("UserSearchResult")) {
                        Intent intent = new Intent(SearchActivity.this, UserActivity.class);
                        intent.putExtra("friendID", searchresult[position].getId().toString());
                        startActivity(intent);
                    } else if (searchresult[position].get__type().equals("GroupSearchResult")) {
                        Intent intent = new Intent(SearchActivity.this, GroupFragmentActivity.class);
                        intent.putExtra("groupID", searchresult[position].getId().toString());
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    this.ex = e;
                }
            }
        });
        searchAdapter.notifyDataSetChanged();
    }
}