package com.android.qz.menuexample;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static android.widget.AdapterView.*;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    String chosenLanguage = "";

    ListView listView;

    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter arrayAdapter;
    static Set<String> set;

    private void showAlert() {
        final SharedPreferences sharedPreferences = this.getSharedPreferences("com.android.qz.menuexample", Context.MODE_PRIVATE);

        chosenLanguage = sharedPreferences.getString("language", "");

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Which language do you want ? ")
                .setMessage("Are you going to use Spanish or English?")
                .setPositiveButton("English", new DialogInterface.OnClickListener(
                ){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sharedPreferences.edit().putString("language","english").apply();
                        textView.setText("english");
                    }
                })
                .setNegativeButton("Spanish", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sharedPreferences.edit().putString("language","spanish").apply();
                        textView.setText("spanish");
                    }
                })
                .show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textView = (TextView) findViewById(R.id.textView);

        listView = (ListView) findViewById(R.id.listView);

        SharedPreferences sharedPreferences = getSharedPreferences(Constant.PACKAGE_NAME, Context.MODE_PRIVATE);

        set = sharedPreferences.getStringSet(Constant.NOTES, null);


//        previously there is no note
        if (set == null) {
            //initiate the set
            set = new HashSet<String>();

            notes.add(Constant.EXAMPLE_NOTE);

            set.addAll(notes);

            sharedPreferences.edit().putStringSet("note", set).apply();
        } else {
            notes.clear();

            notes.addAll(set);
        }

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), Edit_Your_Note.class);
                intent.putExtra("noteId", position);
                startActivity(intent);
            }
        });

        if ("".equals(chosenLanguage)) {
//            showAlert();
        } else {
            textView.setText(chosenLanguage);
        }

        listView.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure")
                        .setMessage("Do you want to delete the note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                notes.remove(i);
                                refreshNotes();
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
                return true;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.NEW) {
            //showAlert();

            notes.add("");

            refreshNotes();

            //redirect to the edit_your_note activity
            Intent i = new Intent(getApplicationContext(),Edit_Your_Note.class);

            i.putExtra("noteId", notes.size()-1);

            startActivity(i);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshNotes() {
        //reset the set
        if (set == null) {
            set = new HashSet<>();
        } else {
            set.clear();
        }

        //put the the notes into storage
        set.addAll(notes);

        SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences(Constant.PACKAGE_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(Constant.NOTES);
        sharedPreferences.edit().putStringSet(Constant.NOTES, set).apply();
        arrayAdapter.notifyDataSetChanged();
    }
}
