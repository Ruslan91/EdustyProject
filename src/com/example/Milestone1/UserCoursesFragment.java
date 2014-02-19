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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.Milestone1.Classes.Courses;
import com.example.Milestone1.Classes.Response;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Руслан on 06.12.13.
 */
public class UserCoursesFragment extends Fragment {
    private UUID token;
    private Response result;
    private ListView listCourses;
    private Courses[] courses;
    private GetUserCourses getUserCourses;
    private UUID courseID;

    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.user_courses, container, false);
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("userdetails", Context.MODE_PRIVATE);
        token = UUID.fromString(sharedPreferences.getString("token", ""));
        listCourses = (ListView) myView.findViewById(R.id.listCourses);
        registerForContextMenu(listCourses);
        getUserCourses = new GetUserCourses();
        getUserCourses.execute();

        return myView;
    }

    public void setData(Response response) {

        if (response != null) {

            courses = (Courses[]) response.getItem();
            ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>(
                    courses.length);
            data.clear();
            for (int i = 0; i < courses.length; i++) {
                HashMap<String, String> m = new HashMap<String, String>();
                m.put("titles", courses[i].getTitle());
                data.add(m);
            }
            SimpleAdapter sAdapter = new SimpleAdapter(getActivity(), data, R.layout.user_courses_list_item,
                    new String[]{"titles"}, new int[]{R.id.tvCourseName});

            listCourses.setAdapter(sAdapter);
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 1, 0, "Удалить запись");
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
        if (courses[acmi.position].getIsOwner() == Boolean.FALSE) {
            menu.getItem(0).setEnabled(false);
        } else menu.getItem(0).setEnabled(true);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            // получаем инфу о пункте списка
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            courseID = courses[acmi.position].getId();
            RemoveCourse removeCourse = new RemoveCourse();
            removeCourse.execute();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.user_courses_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean ret;
        if (item.getItemId() == R.id.action_add) {
            ret = true;
            startActivity(new Intent(getActivity(), CreateCourseActivity.class));
            onDestroy();
        } else {
            ret = super.onOptionsItemSelected(item);
        }
        return ret;
    }

    public class GetUserCourses extends AsyncTask<UUID, Void, Response> {
        public Exception ex;
        ProgressDialog pdLoading = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //pdLoading.setMessage("\tЗагрузка...");
            //pdLoading.show();
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            setData(response);
            //pdLoading.dismiss();
        }

        @Override
        protected Response doInBackground(UUID... params) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                Gson gson = new Gson();
                HttpGet request = new HttpGet(getString(R.string.url) + "Courses?token=" + token);
                HttpResponse response = httpclient.execute(request);
                InputStreamReader reader = new InputStreamReader(response.getEntity()
                        .getContent(), HTTP.UTF_8);
                Type fooType = new TypeToken<Response<Courses[]>>() {
                }.getType();
                result = gson.fromJson(reader, fooType);
            } catch (Exception e) {
                this.ex = e;
            }
            return result;
        }
    }

    public class RemoveCourse extends AsyncTask<Void, Void, Response> {
        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            if (result.getItem() == Boolean.TRUE) {
                getUserCourses = new GetUserCourses();
                getUserCourses.execute();
            } else
                Toast.makeText(getActivity(), "Только владелец может выполнить это действие", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Response doInBackground(Void... voids) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                Gson gson = new Gson();
                HttpDelete request = new HttpDelete(getString(R.string.url) + "Course?token=" + token + "&courseID=" + courseID);
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
