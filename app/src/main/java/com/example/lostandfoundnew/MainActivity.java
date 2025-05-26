package com.example.lostandfoundnew;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnCreate = findViewById(R.id.btnCreateAdvert);
        Button btnShowItems = findViewById(R.id.btnShowItems);
        Button btnShowMap = findViewById(R.id.btnShowMap);

        btnCreate.setOnClickListener(v -> startActivity(new Intent(this, CreateAdvertActivity.class)));
        btnShowItems.setOnClickListener(v -> startActivity(new Intent(this, ItemListActivity.class)));
        btnShowMap.setOnClickListener(v -> startActivity(new Intent(this, MapActivity.class)));
    }
}