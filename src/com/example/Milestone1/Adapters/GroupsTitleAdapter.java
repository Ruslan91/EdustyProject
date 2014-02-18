package com.example.Milestone1.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.example.Milestone1.GroupEventsFragment;
import com.example.Milestone1.GroupFeedFragment;
import com.example.Milestone1.GroupInformationFragment;
import com.example.Milestone1.GroupMembersFragment;
import com.example.Milestone1.R;

public class GroupsTitleAdapter extends FragmentPagerAdapter {
    private final String[] titles;
    private final Fragment[] frags;

    public GroupsTitleAdapter(FragmentManager fm, Context context) {
        super(fm);
        Resources resources = context.getResources();
        titles = resources.getStringArray(R.array.group_titles);
        frags = new Fragment[titles.length];
        frags[0] = new GroupInformationFragment();
        frags[1] = new GroupEventsFragment();
        frags[2] = new GroupFeedFragment();
        frags[3] = new GroupMembersFragment();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Log.v("TitleAdapter - getPageTitle=", titles[position]);
        return titles[position];
    }

    @Override
    public Fragment getItem(int position) {
        Log.v("TitleAdapter - getItem=", String.valueOf(position));
        return frags[position];
    }

    @Override
    public int getCount() {
        return frags.length;
    }
}
