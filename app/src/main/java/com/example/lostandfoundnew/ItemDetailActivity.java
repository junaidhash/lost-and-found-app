package com.example.lostandfoundnew;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;

public class ItemDetailActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        dbHelper = new DatabaseHelper(this);

        // Get intent data
        int itemId = getIntent().getIntExtra("ITEM_ID", -1);
        String name = getIntent().getStringExtra("NAME");
        String phone = getIntent().getStringExtra("PHONE");
        String description = getIntent().getStringExtra("DESCRIPTION");
        String date = getIntent().getStringExtra("DATE");
        String location = getIntent().getStringExtra("LOCATION");
        String postType = getIntent().getStringExtra("POST_TYPE");

        // Initialize views
        TextView tvName = findViewById(R.id.tvName);
        Chip chipPostType = findViewById(R.id.chipPostType);
        TextView tvPhone = findViewById(R.id.tvPhone);
        TextView tvDate = findViewById(R.id.tvDate);
        TextView tvLocation = findViewById(R.id.tvLocation);
        TextView tvDescription = findViewById(R.id.tvDescription);
        MaterialButton btnRemove = findViewById(R.id.btnRemove);

        // Set data
        tvName.setText(name);
        chipPostType.setText(postType);
        tvPhone.setText(phone);
        tvDate.setText(date);
        tvLocation.setText(location);
        tvDescription.setText(description);

        // Remove button click
        btnRemove.setOnClickListener(v -> deleteItem(itemId));
    }

    private void deleteItem(int itemId) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Removal")
                .setMessage("Are you sure you want to remove this item?")
                .setPositiveButton("Remove", (dialog, which) -> {
                    if (dbHelper.deleteItem(itemId)) {
                        Toast.makeText(this, "Item removed", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}