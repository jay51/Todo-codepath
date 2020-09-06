package com.example.simpletodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static String ITEM_KEY = "item_key";
    public static String ITEM_POSITION = "item_positon";
    public static int REQUEST_CODE = 200;

    List<String> items;
    Button bntAdd;
    EditText edItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bntAdd = findViewById(R.id.btnAdd);
        edItem = findViewById(R.id.edItem);
        rvItems = findViewById(R.id.rvItems);

        // laodItems from file into items array
        loadItems();

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                items.remove(position);
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                // create new activity
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                // pass the data being edited to new activity
                i.putExtra(ITEM_POSITION, position);
                i.putExtra(ITEM_KEY, items.get(position));
                // display the activity
                // request code is just a random number to
                startActivityForResult(i, REQUEST_CODE);

            }
        };

        itemsAdapter = new ItemsAdapter(items, onLongClickListener, onClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        bntAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String todoItem = edItem.getText().toString();
                if(todoItem != null && !todoItem.isEmpty()) {
                    // add item to model
                    items.add(todoItem);
                    // notify adapter that an item is inserted
                    itemsAdapter.notifyItemInserted(items.size() -1);
                    edItem.setText("");
                    Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                    saveItems();
                } else {
                    Toast.makeText(getApplicationContext(), "Please add a todo", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // retrieve data
            String itemText = data.getStringExtra(ITEM_KEY);
            int position = data.getExtras().getInt(ITEM_POSITION);

            if( itemText != null && !itemText.isEmpty()) {
                items.set(position, itemText);
                // notify the adapter
                itemsAdapter.notifyItemChanged(position);
                saveItems();
                Toast.makeText(getApplicationContext(), "Todo has changed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Todo can't be empty", Toast.LENGTH_SHORT).show();
            }
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            Log.w("MainActivity", "Unknow call to onActivityResutl");
        }
    }


    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }

    // Read data file
    private void loadItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e){
            Log.e("IOException", "could not read file");
            items = new ArrayList<>();
        }
    }


    // Write to file
    private void saveItems() {

        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e){
            Log.e("IOException", "could not write to file");
        }
    }
}
