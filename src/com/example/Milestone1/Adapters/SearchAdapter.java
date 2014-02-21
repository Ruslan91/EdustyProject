package com.example.Milestone1.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Milestone1.Classes.SearchResult;
import com.example.Milestone1.R;

/**
 * Created by Руслан on 21.02.14.
 */
public class SearchAdapter extends BaseAdapter {
    private final SearchResult[] results;
    private final LayoutInflater lInflater;

    public SearchAdapter(SearchResult[] searchResult, Activity activity) {
        results = searchResult;
        Activity a = activity;
        lInflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return results.length;
    }

    @Override
    public Object getItem(int i) {
        return results[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public static class ViewHolder {
        TextView Title;

        TextView DescriptionCity;
        TextView WebSiteCountry;

        ImageView Picture;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        View v = view;
        if (v == null) {
            v = lInflater.inflate(R.layout.search_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.Title = (TextView) v.findViewById(R.id.tvTitle);
            viewHolder.Picture = (ImageView) v.findViewById(R.id.image);

            viewHolder.DescriptionCity = (TextView) v.findViewById(R.id.tvDescriptionCity);
            viewHolder.WebSiteCountry = (TextView) v.findViewById(R.id.tvWebSiteCountry);

            v.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) v.getTag();

        }
        if (results[i].get__type().equals("UserSearchResult")) {
            viewHolder.Title.setText(results[i].getFirstName() + " " + results[i].getLastName());
            viewHolder.DescriptionCity.setText(results[i].getCity());
            viewHolder.WebSiteCountry.setText(results[i].getCountry());
        } else if (results[i].get__type().equals("GroupSearchResult")) {
            viewHolder.Title.setText(results[i].getName());
            viewHolder.DescriptionCity.setText(results[i].getDescription());
            viewHolder.WebSiteCountry.setText(results[i].getWebSite());
        }
        return v;
    }
}
