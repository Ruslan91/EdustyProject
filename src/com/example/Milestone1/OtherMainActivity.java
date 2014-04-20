package com.example.Milestone1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * Created by Руслан on 15.11.13.
 */
public class OtherMainActivity extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mPlanetTitles;
    private SharedPreferences sharedpreferences;
    Context context;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            sharedpreferences = getSharedPreferences("userdetails", MODE_PRIVATE);
            if (sharedpreferences.getString("token", "").equals("")) {
                startActivity(new Intent(this, AuthorizationActivity.class));
                finish();
            } else {
                mTitle = mDrawerTitle = getTitle();
                mPlanetTitles = getResources().getStringArray(R.array.titles);
                mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                mDrawerList = (ListView) findViewById(R.id.left_drawer);

                // set a custom shadow that overlays the main content when the drawer opens
                mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
                // set up the drawer's list view with items and click listener
                mDrawerList.setAdapter(new ArrayAdapter<>(this,
                        R.layout.drawer_list_item, mPlanetTitles));
                mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

                // enable ActionBar app icon to behave as action to toggle nav drawer
                getActionBar().setDisplayHomeAsUpEnabled(true);
                getActionBar().setHomeButtonEnabled(true);

                // ActionBarDrawerToggle ties together the the proper interactions
                // between the sliding drawer and the action bar app icon
                mDrawerToggle = new ActionBarDrawerToggle(
                        this,                  /* host Activity */
                        mDrawerLayout,         /* DrawerLayout object */
                        R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                        R.string.drawer_open,  /* "open drawer" description for accessibility */
                        R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
                    public void onDrawerClosed(View view) {
                        getActionBar().setTitle(mTitle);
                        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                    }

                    public void onDrawerOpened(View drawerView) {
                        getActionBar().setTitle(mDrawerTitle);
                        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                    }
                };
                mDrawerLayout.setDrawerListener(mDrawerToggle);

                if (savedInstanceState == null) {
                    if (getIntent().getExtras() == null)
                        selectItem(1);
                    else selectItem(getIntent().getIntExtra("tab", 1));
                }
            }
        if (!isOnline()) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setCancelable(false);
            alertDialog.setMessage("Интернет соединение отсутсвует!"); // сообщение
            alertDialog.setPositiveButton("Повторить", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {
                    Intent i = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage(getBaseContext().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            });
            alertDialog.setNegativeButton("Выйти из приложения", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {
                    finish();
                }
            });
            dialog = alertDialog.create();
            dialog.show();
        }
        }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch (item.getItemId()) {
            case R.id.action_profile:
                startActivity(new Intent(this, UserActivity.class));
                return true;
            case R.id.action_quit:
                SharedPreferences.Editor edit = sharedpreferences.edit();
                edit.remove("token");
                edit.commit();
                startActivity(new Intent(this, AuthorizationActivity.class));
                finish();
                return true;
/*            case R.id.action_report:
                AlertDialog.Builder editalert = new AlertDialog.Builder(this);

                editalert.setTitle("Нашли ошибку?");
                editalert.setMessage("Отправьте отчет, чтобы улучшить работу сервиса");
                final EditText input = new EditText(this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                input.setLayoutParams(lp);
                editalert.setView(input);
                editalert.setPositiveButton(getString(R.string.send), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {


                    }
                });
                editalert.show();*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments

        Fragment[] frags = new Fragment[mPlanetTitles.length];
        frags[0] = new SearchActivity();
        frags[1] = new UserFeedFragment();
        frags[2] = new UserEventsFragment();
        frags[3] = new UserFollowsFragment();
        frags[4] = new UserGroupsFragment();
        frags[5] = new UserCoursesFragment();
        frags[6] = new UserMarksFragment();
        frags[7] = new UserJournalsFragment();

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, frags[position]).commit();
        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mPlanetTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}