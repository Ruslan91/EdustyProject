package com.example.Milestone1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.Toast;

import com.example.Milestone1.Adapters.TabsAdapter;

import java.util.UUID;

/**
 * Created by Руслан on 15.11.13.
 */
public class OtherMainActivity extends FragmentActivity {
    private ViewPager mViewPager;
    private TabHost mTabHost;
    private TabsAdapter mTabsAdapter;
    private String[] titles;
    private UUID token;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences userDetails = getSharedPreferences("userdetails", MODE_PRIVATE);
        int tab = 0;
        if (getIntent().getExtras() != null) {
            tab = getIntent().getExtras().getInt("tab");
        }
        //getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        token = UUID.fromString(userDetails.getString("token", ""));
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();
        mViewPager = (ViewPager) findViewById(R.id.pager);
        titles = getResources().getStringArray(R.array.titles);
        mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);
        mTabsAdapter.addTab(mTabHost.newTabSpec("friends").setIndicator(titles[0]), UserFriendsFragment.class, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec("events").setIndicator(titles[1]), UserEventsFragment.class, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec("feed").setIndicator(titles[2]), UserFeedFragment.class, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec("groups").setIndicator(titles[3]), UserGroupsFragment.class, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec("journals").setIndicator(titles[4]), UserJournalsFragment.class, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec("courses").setIndicator(titles[5]), UserCoursesFragment.class, null);

        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }
        mViewPager.setCurrentItem(tab);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean ret;
        /*if (item.getItemId() == R.id.action_update) {
            Intent intent = new Intent(getIntent());
            intent.putExtra("tab", mViewPager.getCurrentItem());
            startActivity(intent);
            finish();
        }*/
        if (item.getItemId() == R.id.action_profile) {
            ret = true;
            startActivity(new Intent(this, UserActivity.class));
        } else if (item.getItemId() == R.id.action_quit) {
            ret = true;
            SharedPreferences userDetails = getSharedPreferences("userdetails", MODE_PRIVATE);
            SharedPreferences.Editor edit = userDetails.edit();
            edit.remove("token");
            edit.commit();
            Intent intent = new Intent(this, AuthorizationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else if (item.getItemId() == R.id.action_settings) {
            ret = true;
            Toast.makeText(this, getString(R.string.secIsDev), Toast.LENGTH_SHORT).show();
        } else {
            ret = super.onOptionsItemSelected(item);
        }
        return ret;
    }
}