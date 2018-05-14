package com.example.diu.noteapp;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.HashSet;

import static com.example.diu.noteapp.ObjectSerializer.serialize;

public class MainActivity extends AppCompatActivity {

    ListView noteListView;
    static ArrayList<String> note;
    static ArrayList<String> noteTitle;
    static ArrayAdapter<String> noteArrayAdapter;

    static SharedPreferences sharedPreferences;



    //menu code goes here
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.addNote){

            Intent intent = new Intent(getApplicationContext(),DetailActivity.class);
            startActivity(intent);

            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteListView = findViewById(R.id.noteListView);

        //adding note ArrayList to ListView through ArrayAdapter
        note = new ArrayList<String>();
        noteTitle = new ArrayList<String>();

        sharedPreferences = this.getApplicationContext().getSharedPreferences("com.example.diu.noteapp", Context.MODE_PRIVATE);

//        HashSet<String> setNote = (HashSet<String>) sharedPreferences.getStringSet("notes",null);
//
//        HashSet<String> setTitle = (HashSet<String>) sharedPreferences.getStringSet("noteTitle",null);

        try {
            noteTitle = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("noteTitle", serialize(new ArrayList<String>())));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            note = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("note", serialize(new ArrayList<String>())));
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (note.isEmpty()){
            note.add("Write Text Here");
        }

        if (noteTitle.isEmpty()){
            noteTitle.add("Example Note");
        }

        noteArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,noteTitle);

        noteListView.setAdapter(noteArrayAdapter);




        //SETTING OnItemClickListner to ListView items
        noteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),DetailActivity.class);
                intent.putExtra("noteId",i);
                startActivity(intent);

            }
        });

        //long press deletes the note
        noteListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                final int itemToDelete = i;
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are You sure ?")
                        .setMessage("Do you Want to delete this note ?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                note.remove(itemToDelete);
                                noteTitle.remove(itemToDelete);
                                noteArrayAdapter.notifyDataSetChanged();

//                                HashSet<String> setNote = new HashSet<>(MainActivity.note);
//                                sharedPreferences.edit().putStringSet("notes",setNote).apply();
//                                HashSet<String> setTitle = new HashSet<>(MainActivity.noteTitle);
//                                sharedPreferences.edit().putStringSet("noteTitle",setTitle).apply();
                                try {

                                    sharedPreferences.edit().putString("note",ObjectSerializer.serialize(note)).apply();
                                    sharedPreferences.edit().putString("noteTitle", ObjectSerializer.serialize(noteTitle)).apply();

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
                return true;
            }
        });

    }
}
