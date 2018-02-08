package com.android.qz.menuexample;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashSet;

public class Edit_Your_Note extends AppCompatActivity implements TextWatcher {

    EditText editText;

    int noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__your__note);

        editText = (EditText) findViewById(R.id.editText);

        Intent intent = getIntent();

        noteId = intent.getIntExtra("noteId",-1);

        if (noteId != -1) {
            editText.setText(MainActivity.notes.get(noteId));
        }
        editText.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        MainActivity.notes.set(noteId, String.valueOf(charSequence));
        MainActivity.arrayAdapter.notifyDataSetChanged();

        if (MainActivity.set == null) {
            MainActivity.set = new HashSet<>();
        } else {
            MainActivity.set.clear();
        }

        SharedPreferences sharedPreferences = this.getSharedPreferences(Constant.PACKAGE_NAME, Context.MODE_PRIVATE);
        MainActivity.set.addAll(MainActivity.notes);
        sharedPreferences.edit().remove(Constant.NOTES);
        sharedPreferences.edit().putStringSet(Constant.NOTES, MainActivity.set).apply();
        MainActivity.arrayAdapter.notifyDataSetChanged();

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
