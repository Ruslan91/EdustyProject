package com.example.Milestone1.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.example.Milestone1.R;

public class TitleAdapter extends FragmentStatePagerAdapter {
    private final String[] titles;
    private final Fragment[] frags;

    public TitleAdapter(FragmentManager fm, Context context) {
        super(fm);
        Resources resources = context.getResources();
        titles = resources.getStringArray(R.array.titles);
        frags = new Fragment[titles.length];
/*        frags[0] = new UserFollowsFragment();
        frags[1] = new UserEventsFragment();
        frags[2] = new UserFeedFragment();
        frags[3] = new UserGroupsFragment();
        frags[4] = new UserJournalsFragment();
        frags[5] = new UserMarksFragment();
        frags[6] = new UserCoursesFragment();*/
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