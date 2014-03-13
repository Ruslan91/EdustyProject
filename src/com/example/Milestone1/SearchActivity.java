package com.example.Milestone1;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.Milestone1.Adapters.SearchAdapter;
import com.example.Milestone1.Classes.Response;
import com.example.Milestone1.Classes.SearchResult;
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

/**
 * Created by Руслан on 21.02.14.
 */
public class SearchActivity extends Fragment implements View.OnClickListener {
    private Response result;
    private UUID token;
    private String searchString;
    private EditText etSearch;
    private ListView listResults;
    private SearchResult[] searchresult;
    Exception exception;

    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search, container, false);
        listResults = (ListView) view.findViewById(R.id.listResults);
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("userdetails", Context.MODE_PRIVATE);
        token = UUID.fromString(sharedPreferences.getString("token", ""));
        etSearch = (EditText) view.findViewById(R.id.etSearch);
        ImageButton btnSearch = (ImageButton) view.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);
        return view;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSearch:
                try {
                    if (etSearch.getText().toString().equals("")) {
                        Toast.makeText(getActivity(), getString(R.string.please_set_text), Toast.LENGTH_LONG).show();
                    } else {
                        searchString = etSearch.getText().toString();
                        new GetSearchResults().execute();
                    }
                } catch (Exception e) {
                    this.exception = e;
                }
        }
    }
/*
    public void onClickBtnSendForSearch(View v) {
        if (etSearch.getText().toString().equals("")) {
            Toast.makeText(getActivity(), getString(R.string.please_set_text), Toast.LENGTH_LONG).show();
        } else {
            searchString = etSearch.getText().toString();
            new GetSearchResults().execute();
        }
    }*/

    public class GetSearchResults extends AsyncTask<Void, Void, Response> {
        public Exception ex;
        ProgressDialog progressDialog = new ProgressDialog(getActivity());

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

        SearchAdapter searchAdapter = new SearchAdapter(searchresult, getActivity());
        listResults.setAdapter(searchAdapter);
        listResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public Exception ex;

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                try {
                    if (searchresult[position].get__type().equals("UserSearchResult")) {
                        Intent intent = new Intent(getActivity(), UserActivity.class);
                        intent.putExtra("userID", searchresult[position].getId().toString());
                        startActivity(intent);
                    } else if (searchresult[position].get__type().equals("GroupSearchResult")) {
                        Intent intent = new Intent(getActivity(), GroupFragmentActivity.class);
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
