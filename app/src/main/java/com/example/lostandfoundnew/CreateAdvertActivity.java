package com.example.lostandfoundnew;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.lostandfoundnew.DatabaseHelper;
import com.example.lostandfoundnew.R;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CreateAdvertActivity extends AppCompatActivity {
    private static final int REQUEST_CHECK_SETTINGS = 1002;
    private EditText etName, etPhone, etDescription, etDate, etLocation;
    private RadioGroup rgPostType;
    private DatabaseHelper dbHelper;
    private FusedLocationProviderClient fusedLocationClient;
    private double latitude = 0;
    private double longitude = 0;

    // Activity result launchers
    private final ActivityResultLauncher<Intent> autocompleteLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Place place = Autocomplete.getPlaceFromIntent(data);
                        etLocation.setText(place.getAddress());
                        LatLng latLng = place.getLatLng();
                        if (latLng != null) {
                            latitude = latLng.latitude;
                            longitude = latLng.longitude;
                        }
                    }
                } else if (result.getResultCode() == AutocompleteActivity.RESULT_ERROR) {
                    Status status = Autocomplete.getStatusFromIntent(result.getData());
                    Toast.makeText(this, "Error: " + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<String> locationPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    fetchCurrentLocation();
                } else {
                    Toast.makeText(this, R.string.location_permission_required, Toast.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<IntentSenderRequest> locationSettingsLauncher =
            registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK) {
                            fetchCurrentLocation();
                        } else {
                            Toast.makeText(this, "Location services required", Toast.LENGTH_SHORT).show();
                        }
                    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert);

        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        initializeViews();
        setupDateField();
        setupLocationButtons();
        setupSaveButton();
    }


    private void initializeViews() {
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etDescription = findViewById(R.id.etDescription);
        etDate = findViewById(R.id.etDate);
        etLocation = findViewById(R.id.etLocation);
        rgPostType = findViewById(R.id.rgPostType);
        dbHelper = new DatabaseHelper(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void setupDateField() {
        etDate.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date()));
    }

    private void setupLocationButtons() {
        Button btnAutoComplete = findViewById(R.id.btnAutoComplete);
        Button btnCurrentLocation = findViewById(R.id.btnCurrentLocation);

        btnAutoComplete.setOnClickListener(v -> launchPlacesAutocomplete());
        btnCurrentLocation.setOnClickListener(v -> checkLocationPermission());
    }

    private void setupSaveButton() {
        findViewById(R.id.btnSave).setOnClickListener(v -> saveItem());
    }

    private void launchPlacesAutocomplete() {
        List<Place.Field> fields = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG);
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN,
                fields
        ).build(this);
        autocompleteLauncher.launch(intent);
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fetchCurrentLocation();
        } else {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void fetchCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            LocationRequest locationRequest = new LocationRequest.Builder(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    10000 // 10 seconds
            ).build();

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            SettingsClient client = LocationServices.getSettingsClient(this);
            Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

            task.addOnSuccessListener(this, response -> {
                fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                        .addOnSuccessListener(location -> {
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                etLocation.setText(R.string.current_location);
                            } else {
                                // Fallback to last known location
                                fusedLocationClient.getLastLocation()
                                        .addOnSuccessListener(lastLocation -> {
                                            if (lastLocation != null) {
                                                latitude = lastLocation.getLatitude();
                                                longitude = lastLocation.getLongitude();
                                                etLocation.setText(R.string.current_location);
                                            } else {
                                                Toast.makeText(this,
                                                        "Enable location services",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
            }).addOnFailureListener(e -> {
                if (e instanceof ResolvableApiException) {
                    try {
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        IntentSenderRequest request = new IntentSenderRequest.Builder(
                                resolvable.getResolution()
                        ).build();
                        locationSettingsLauncher.launch(request);
                    } catch (Exception sendEx) {
                        Toast.makeText(this, "Error showing location settings", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void saveItem() {
        String postType = ((RadioButton) findViewById(rgPostType.getCheckedRadioButtonId())).getText().toString();
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String location = etLocation.getText().toString().trim();

        if (validateInputs(name, phone, description, location)) {
            if (dbHelper.insertItem(postType, name, phone, description, date, location, latitude, longitude)) {
                Toast.makeText(this, R.string.item_saved, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private boolean validateInputs(String name, String phone, String description, String location) {
        if (name.isEmpty() || phone.isEmpty() || description.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, R.string.all_fields_required, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (latitude == 0 && longitude == 0) {
            Toast.makeText(this, R.string.invalid_location, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    
}