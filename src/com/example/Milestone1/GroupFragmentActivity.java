package com.example.Milestone1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TabHost;

import com.example.Milestone1.Adapters.GroupsTitleAdapter;
import com.example.Milestone1.Adapters.TabsAdapter;

import java.util.UUID;

public class GroupFragmentActivity extends FragmentActivity {

    private int current_fragment;
    private UUID token;
    private TabHost mTabHost;
    private ViewPager mViewPager;
    private String[] titles;
    private TabsAdapter mTabsAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        SharedPreferences userDetails = getSharedPreferences("userdetails", MODE_PRIVATE);
        Bundle extras = getIntent().getExtras();
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
        GroupsTitleAdapter titleAdapter = new GroupsTitleAdapter(getSupportFragmentManager(), this);
        mViewPager.setOffscreenPageLimit(titleAdapter.getCount());
        mViewPager.setCurrentItem(current_fragment);
        mViewPager.setAdapter(titleAdapter);
    }
}