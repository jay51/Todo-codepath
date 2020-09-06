package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    EditText edText;
    Button btnSave;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        edText = findViewById(R.id.edText);
        btnSave = findViewById(R.id.btnSave);
        getSupportActionBar().setTitle("Edit Item");

        edText.setText(getIntent().getStringExtra(MainActivity.ITEM_KEY));
        position = getIntent().getExtras().getInt(MainActivity.ITEM_POSITION);

        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // create an intent to go back with the result
                Intent intent = new Intent();
                // pass the data (results of editin)
                intent.putExtra(MainActivity.ITEM_KEY, edText.getText().toString());
                intent.putExtra(MainActivity.ITEM_POSITION, position);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}