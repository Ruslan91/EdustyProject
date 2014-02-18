package com.example.Milestone1.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.Milestone1.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Руслан on 13.12.13.
 */
public class EditMarksJournalAdapter extends BaseAdapter {
    private final Activity activity;
    private ArrayList<Map<String, String>> results;
    private Context ctx;
    private int pos;

    LayoutInflater lInflater;
    private ViewGroup par;

    public EditMarksJournalAdapter(Activity a, ArrayList<Map<String, String>> data) {

        results = data;
        activity = a;
        lInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        notifyDataSetChanged();

    }

    public static class ViewHolder {
        public TextView tvTitle;
        public EditText etDescription;
        public EditText etMark;
        public Spinner spType;
        public CheckBox cbMark;
        public int ref;
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
        par = parent;
        View v = view;
        if (v == null) {
            v = lInflater.inflate(R.layout.marks_edit_list_item, null);

            viewHolder = new ViewHolder();
            viewHolder.cbMark = (CheckBox) v.findViewById(R.id.cbMark);
            viewHolder.tvTitle = (TextView) v.findViewById(R.id.tvTitle);
            viewHolder.etMark = (EditText) v.findViewById(R.id.etMark);
            viewHolder.spType = (Spinner) v.findViewById(R.id.spType);
            viewHolder.etDescription = (EditText) v.findViewById(R.id.etDescription);

/*            viewHolder.etMark.setTag(results.get(position).get("mark"));
            viewHolder.etDescription.setTag(results.get(position).get("description"));
            viewHolder.cbMark.setTag(results.get(position).get("mark"));
            viewHolder.spType.setTag(results.get(position).get("type"));*/
            // viewHolder.cbMark.setOnCheckedChangeListener(checkChangeListener);
            /*if (viewHolder.spType.getSelectedItemPosition() == 0) {
                viewHolder.etMark.setVisibility(View.GONE);
                if (Integer.parseInt(results.get(viewHolder.ref).get("mark")) == 1) {
                    viewHolder.cbMark.setChecked(true);
                } else viewHolder.cbMark.setChecked(false);
            } else viewHolder.etMark.setVisibility(View.VISIBLE);*/


            v.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) v.getTag();

        }
        //viewHolder.cbMark.setId(position);
        //viewHolder.tvTitle.setId(position);
        viewHolder.tvTitle.setText(results.get(position).get("title"));
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



        /*viewHolder.etMark.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable editable) {
                results.get((Integer) viewHolder.etMark.getTag()).put("mark", editable.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }
        });*/

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

        viewHolder.spType.setTag(position);
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
        });
        viewHolder.cbMark.setTag(position);
        if (results.get(position).get("mark").equals("1")) {
            viewHolder.cbMark.setChecked(true);
        } else viewHolder.cbMark.setChecked(false);
        viewHolder.cbMark.setOnCheckedChangeListener(checkChangeListener);
        /*viewHolder.etMark.addTextChangedListener(new MyTextWatcher(position) {
            @Override
            public void afterTextChanged(Editable s, int position) {
                results.get(position).put("mark", s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count, int position) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after, int position) {

            }
        });*/
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
