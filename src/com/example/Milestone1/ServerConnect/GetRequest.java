package com.example.Milestone1.ServerConnect;

import android.os.AsyncTask;

import com.example.Milestone1.Classes.Response;

/**
 * Created by Руслан on 08.11.13.
 */
public class GetRequest extends AsyncTask<Void, Void, Response> {
    @Override
    protected Response doInBackground(Void... voids) {
        return null;
    }
    /*public Exception ex;

    @Override
    protected Response<GroupFeedRead[]> doInBackground(UUID... params) {
        try {
            HttpClient httpclient = new DefaultHttpClient();
            Gson gson = new Gson();
            HttpGet request = new HttpGet(getString(R.string.url) + url);
            HttpResponse response = httpclient.execute(request);
            InputStreamReader reader = new InputStreamReader(response.getEntity()
                    .getContent(), HTTP.UTF_8);
            Type fooType = new TypeToken<Response<GroupFeedRead[]>>(){}.getType();
            result = gson.fromJson(reader, fooType);
        } catch (Exception e) {
            this.ex = e;
        }
        return result;
    }*/
}
