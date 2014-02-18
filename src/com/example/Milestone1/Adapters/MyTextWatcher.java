package com.example.Milestone1.Adapters;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by Руслан on 06.01.14.
 */
public abstract class MyTextWatcher implements TextWatcher {
    private final int position;

    public MyTextWatcher(final int position) {
        this.position = position;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        beforeTextChanged(s, start, count, after, position);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        onTextChanged(s, start, before, count, position);
    }

    @Override
    public void afterTextChanged(Editable s) {
        afterTextChanged(s, position);
    }

    public abstract void afterTextChanged(Editable s, int position);

    public abstract void onTextChanged(CharSequence s, int start, int before, int count, int position);

    public abstract void beforeTextChanged(CharSequence s, int start, int count, int after, int position);

}
