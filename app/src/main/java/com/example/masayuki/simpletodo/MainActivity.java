package com.example.masayuki.simpletodo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> items;
    //Model to the view.
    ArrayAdapter<String> itemsAdapter;
    //instance of list view itself
    ListView lvItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readItems();
        //this: reference to main activity
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(itemsAdapter);

        //mock data
        //items.add("First item");
        //items.add("Second item");

        setUpListViewListener();

    }

    public void onAddItem(View v) {
        //reference to edit text
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        //get the value of the string
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);

        //user can clear the next item
        etNewItem.setText("");

        writeItems();

        //Toast: brief notification to the user
        Toast.makeText(getApplicationContext(), "Item added to list", Toast.LENGTH_SHORT).show();
    }

    //private because we will be calling this directly
    private void setUpListViewListener(){
        //Never displayed to User. Only used for debugging
        //first argument is tag.
        //Where can I see this?
        Log.i("MainActivity", "Setting up listener on list view");

        //all that will be envoked at the beginning
        //is this first line. Everything inside will not
        //be called. Only when a long click occurs.
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            //since we are returning this, we are returning true
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("MainActivity", "Item removed from list: " + i);
                //remove the item
                //removing the list
                //i means position
                items.remove(i);

                //we also let the adaptor know
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });
    }

    private File getDataFile(){
        return new File(getFilesDir(), "todo.txt");
    }

    //Helps maintain the information in the app across multiple
    //runs
    private void readItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        }
        catch (IOException e){
            Log.e("MainActivity", "Error reading file", e);
            //make sures we dont have null pointer exception
            items = new ArrayList<>();
        }
    }

    private void writeItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        }
        catch (IOException e){
            Log.e("MainActivity", "Error writing file", e);
        }
    }
}
