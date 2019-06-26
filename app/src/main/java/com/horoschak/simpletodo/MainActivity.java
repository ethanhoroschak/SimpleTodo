// Ethan Horoschak 2019
package com.horoschak.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

//import android.os.FileUtils;

public class MainActivity extends AppCompatActivity {
    // Declare objects
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    //Constants:
    // a numeric code to identify the edit activity
    public static final int EDIT_REQUEST_CODE = 20;
    // keys used for passing data between activities
    public static final String ITEM_TEXT = "itemText";
    public static final String ITEM_POSITION = "itemPosition";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItems = (ListView) findViewById(R.id.lvItems);
        //Load items into app
        readItems();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);

        //items.add("First todo item");
        //items.add("Second todo item");

        setupListViewListener();
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        //Update file
        writeItems();
        //Clear text in edittext
        etNewItem.setText("");
        // display a notification to the user
        Toast.makeText(getApplicationContext(), "Item added to list", Toast.LENGTH_SHORT).show();
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // remove the item in the list at the index given by position
                items.remove(position);
                // notify the adapter that the underlying dataset changed
                itemsAdapter.notifyDataSetChanged();
                //Update file
                writeItems();
                Log.i("MainActivity", "Removed item " + position);
                //Display notification
                Toast.makeText(getApplicationContext(), "Item removed from list", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        //Regular click listener for editing
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                // put "extras" into the bundle for access in the edit activity
                i.putExtra(ITEM_TEXT, items.get(position));
                i.putExtra(ITEM_POSITION, position);
                // brings up the edit activity with the expectation of a result
                startActivityForResult(i, EDIT_REQUEST_CODE);
            }
        });
    }

    //Get the datafile
    private File getDataFile() {
        return new File(getFilesDir(), "todo.txt");
    }

    //Load data into app
    private void readItems() {
        try {
            items = new ArrayList<String>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            e.printStackTrace();
            items = new ArrayList<>();
        }
    }

    //Save data into text file
    private void writeItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE) {
            // extract updated item value from result extras
            String updatedItem = data.getExtras().getString(ITEM_TEXT);
            // get the position of the item which was edited
            int position = data.getExtras().getInt(ITEM_POSITION, 0);
            // update the model with the new item text at the edited position
            items.set(position, updatedItem);
            itemsAdapter.notifyDataSetChanged();
            // Store the updated items back to disk
            writeItems();
            Toast.makeText(this, "Item updated", Toast.LENGTH_SHORT).show();
        }
    }
}
