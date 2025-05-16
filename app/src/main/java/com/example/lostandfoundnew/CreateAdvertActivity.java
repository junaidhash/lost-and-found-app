package com.example.lostandfoundnew;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateAdvertActivity extends AppCompatActivity {
    private EditText etName, etPhone, etDescription, etDate, etLocation;
    private RadioGroup rgPostType;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert);

        // Initialize views
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etDescription = findViewById(R.id.etDescription);
        etDate = findViewById(R.id.etDate);
        etLocation = findViewById(R.id.etLocation);
        rgPostType = findViewById(R.id.rgPostType);
        dbHelper = new DatabaseHelper(this);

        // Save button click
        findViewById(R.id.btnSave).setOnClickListener(v -> saveItem());
    }

    private void saveItem() {
        String postType = ((RadioButton) findViewById(rgPostType.getCheckedRadioButtonId())).getText().toString();
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String location = etLocation.getText().toString().trim();

        // Validate fields
        if (name.isEmpty() || phone.isEmpty() || description.isEmpty() || date.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save to database
        if (dbHelper.insertItem(postType, name, phone, description, date, location)) {
            Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}