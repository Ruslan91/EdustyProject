package com.example.Milestone1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.Milestone1.Adapters.TitleAdapter;

import java.util.UUID;

public class MainActivity extends FragmentActivity {
    UUID token;
    Exception exception;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        SharedPreferences sharedpreferences = getSharedPreferences("userdetails", MODE_PRIVATE);
        if (sharedpreferences.getString("token", "").equals("")) {
            startActivity(new Intent(this, AuthorizationActivity.class));
            finish();
        } else {
            Bundle extras = getIntent().getExtras();
            int current_fragment;
            if (extras != null) {
                current_fragment = extras.getInt("tab");
            } else {
                current_fragment = 0;
            }
            ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager);
            PagerTabStrip pagerTabStrip = (PagerTabStrip) findViewById(R.id.pagerTabStrip);
            pagerTabStrip.setBackgroundColor(android.R.color.white);
            pagerTabStrip.setDrawFullUnderline(true);
            pagerTabStrip.setTabIndicatorColorResource(android.R.color.white);
            pagerTabStrip.setVisibility(View.VISIBLE);
            TitleAdapter titleAdapter = new TitleAdapter(getSupportFragmentManager(), this);
            mViewPager.setOffscreenPageLimit(titleAdapter.getCount());
            mViewPager.setCurrentItem(current_fragment);
            mViewPager.setAdapter(titleAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean ret;
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

