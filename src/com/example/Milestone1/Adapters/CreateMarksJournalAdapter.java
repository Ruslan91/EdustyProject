package com.example.Milestone1.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.Milestone1.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Руслан on 21.12.13.
 */
public class CreateMarksJournalAdapter extends BaseAdapter {
    private final Activity activity;
    private ArrayList<Map<String, String>> results;
    private Context ctx;
    private int pos;
    private AsyncTask<ImageView, Void, Bitmap> task;
    private ImageView image;

    LayoutInflater lInflater;
    private View v;


    public CreateMarksJournalAdapter(Activity a, ArrayList<Map<String, String>> data) {
        results = data;
        activity = a;
        lInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public static class ViewHolder {
        public TextView tvTitle;
        public EditText etDescription;
        public EditText etMark;
        public Spinner spType;
        public CheckBox cbMark;
    }

    @Override
    public int getCount() {
        return results.size();
    }

    @Override
    public Object getItem(int position) {

        return results.get(position);
    }

    public Object getAppName(int position) {

        return results.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    public Context getContext() {
        return ctx;
    }

    public ArrayList<Map<String, String>> getData() {
        return results;
    }

    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder viewHolder;
        pos = position;
        v = view;
        if (v == null) {
            viewHolder = new ViewHolder();
            v = lInflater.inflate(R.layout.marks_create_list_item, parent, false);
            viewHolder.tvTitle = (TextView) v.findViewById(R.id.tvTitle);
            viewHolder.etDescription = (EditText) v.findViewById(R.id.etDescription);
            viewHolder.etMark = (EditText) v.findViewById(R.id.etMark);
            viewHolder.cbMark = (CheckBox) v.findViewById(R.id.cbMark);
            v.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) v.getTag();
        }
/*
        viewHolder.spType = (Spinner) v.findViewById(R.id.spType);
        if (results.get(position).get("type") != null) {
            viewHolder.spType.setSelection(Integer.parseInt(results.get(position).get("type")));
        } else viewHolder.spType.setSelection(0);
*/
        viewHolder.tvTitle.setText(results.get(position).get("title"));
        /*if (results.get(position).get("type").equals("0")) {
            viewHolder.etMark.setVisibility(View.GONE);
            viewHolder.cbMark.setVisibility(View.VISIBLE);
        } else {
            viewHolder.etMark.setVisibility(View.VISIBLE);
            viewHolder.cbMark.setVisibility(View.GONE);
        }*/
        viewHolder.etMark.setId(position);
        if (results.get(position).get("mark") != null) {
            viewHolder.etMark.setText(results.get(position).get("mark"));
        } else viewHolder.etMark.setText("");
        viewHolder.etMark.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    final int position = v.getId();
                    final EditText Mark = (EditText) v;
                    String value = Mark.getText().toString();

                    if (value.compareTo("") != 0) {
                        String mark = "";
                        if (Mark.getTag() != null) {
                            mark = Mark.getTag().toString();
                        }
                    }
                    results.get(position).put("mark", value);
                }
            }
        });
        viewHolder.etDescription.setId(position);
        if (results.get(position).get("description") != null) {
            viewHolder.etDescription.setText(results.get(position).get("description"));
        } else viewHolder.etDescription.setText("");
        viewHolder.etDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    final int position = v.getId();
                    final EditText Description = (EditText) v;
                    String value = Description.getText().toString();

                    if (value.compareTo("") != 0) {
                        String description = "";
                        if (Description.getTag() != null) {
                            description = Description.getTag().toString();
                        }
                    }
                    results.get(position).put("description", value);
                }
            }
        });
        /*viewHolder.spType.setTag(position);
        if (results.get(position).get("type") != null) {
            viewHolder.spType.setSelection(Integer.parseInt(results.get(position).get("type")));
        } else viewHolder.spType.setSelection(0);
        viewHolder.spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {
                if (selectedItemPosition == 0) {
                    viewHolder.etMark.setVisibility(View.GONE);
                    viewHolder.cbMark.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.etMark.setVisibility(View.VISIBLE);
                    viewHolder.cbMark.setVisibility(View.GONE);
                }
                results.get((Integer) parent.getTag()).put("type", String.valueOf(selectedItemPosition));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/
        viewHolder.cbMark.setTag(position);
        viewHolder.cbMark.setVisibility(View.GONE);
        if (results.get(position).get("mark").equals("1")) {
            viewHolder.cbMark.setChecked(true);
        } else viewHolder.cbMark.setChecked(false);
        viewHolder.cbMark.setOnCheckedChangeListener(checkChangeListener);
        /*if (results.get(position).get("description") != null) {
            viewHolder.etDescription.setText(results.get(position).get("description"));
        } else viewHolder.etDescription.setText("");

        if (results.get(position).get("mark") != null *//*&& results.get(position).get("type") != "0"*//*) {
            viewHolder.cbMark.setVisibility(View.GONE);
            viewHolder.etMark.setVisibility(View.VISIBLE);
            viewHolder.etMark.setText(results.get(position).get("mark"));
        } else {
            viewHolder.cbMark.setVisibility(View.VISIBLE);
            viewHolder.etMark.setVisibility(View.GONE);
            if (results.get(position).get("mark").equals("0")) {
                viewHolder.cbMark.setChecked(false);
            } else viewHolder.cbMark.setChecked(true);
        }*/

        return v;
    }

    CompoundButton.OnCheckedChangeListener checkChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
                results.get((Integer) compoundButton.getTag()).put("mark", "1");
                notifyDataSetChanged();
                //viewHolder.etMark.setText(results.get(pos).get("mark"));
            } else {
                results.get((Integer) compoundButton.getTag()).put("mark", "0");
                notifyDataSetChanged();
                //viewHolder.etMark.setText(results.get(pos).get("mark"));
            }
        }
    };
}

