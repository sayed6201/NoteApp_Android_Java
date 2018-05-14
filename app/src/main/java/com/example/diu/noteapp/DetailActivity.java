package com.example.diu.noteapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteStatement;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.io.IOException;
import java.util.HashSet;

import static com.example.diu.noteapp.MainActivity.*;
import static com.example.diu.noteapp.ObjectSerializer.serialize;

public class DetailActivity extends AppCompatActivity {

    EditText editText;
    EditText titleEditText;
    int noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        editText = findViewById(R.id.editText);
        titleEditText = findViewById(R.id.titleEditText);

        Intent intent = getIntent();
        noteId = intent.getIntExtra("noteId",-1);

        //assuring that the enterence is through MainActivity
        if(noteId != -1) {
            editText.setText( MainActivity.note.get(noteId));
            titleEditText.setText(MainActivity.noteTitle.get(noteId));
        }else {
            MainActivity.noteTitle.add("");
            MainActivity.note.add("");
            noteId = MainActivity.note.size() - 1;
            MainActivity.noteArrayAdapter.notifyDataSetChanged();


        }

            //saving Title As it is typed
            titleEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    MainActivity.noteTitle.set(noteId,String.valueOf(charSequence));
                    MainActivity.noteArrayAdapter.notifyDataSetChanged();

//                    HashSet<String> setTitle = new HashSet<>(MainActivity.noteTitle);
                    try {
                        MainActivity.sharedPreferences.edit().putString("noteTitle", serialize(MainActivity.noteTitle)).apply();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
//                    MainActivity.noteTitle.set(noteId,editable.toString());
//                    MainActivity.noteArrayAdapter.notifyDataSetChanged();

                }
            });

            //saving note as it is typed
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    MainActivity.note.set(noteId, String.valueOf(charSequence));

//                    HashSet<String> setNote = new HashSet<>(MainActivity.note);
                    try {
                        MainActivity.sharedPreferences.edit().putString("note", serialize(MainActivity.note)).apply();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void afterTextChanged(Editable editable) {
//                    MainActivity.note.set(noteId, editable.toString());

                }
            });




    }
}
