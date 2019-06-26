package com.horoschak.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class EditItemActivity extends AppCompatActivity {
    //item description
    EditText etItemText;
    //item position
    int position;

    //Constants:
    // a numeric code to identify the edit activity
    public static final int EDIT_REQUEST_CODE = 20;
    // keys used for passing data between activities
    public static final String ITEM_TEXT = "itemText";
    public static final String ITEM_POSITION = "itemPosition";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        //Find edittext object
        etItemText = (EditText) findViewById(R.id.etItemText);
        // set the text field's content
        etItemText.setText(getIntent().getStringExtra(ITEM_TEXT));
        position = getIntent().getIntExtra(ITEM_POSITION, 0);
        // set the title bar to reflect the purpose of the view
        getSupportActionBar().setTitle("Edit Item");
    }

    public void onSaveItem(View v) {
        Intent data = new Intent();
        // Pass updated item text and original position
        data.putExtra(ITEM_TEXT, etItemText.getText().toString());
        data.putExtra(ITEM_POSITION, position);
        // set result code and bundle data for response
        setResult(RESULT_OK, data);
        finish();
    }
}
