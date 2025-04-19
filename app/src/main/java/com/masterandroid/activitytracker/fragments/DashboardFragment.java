package com.masterandroid.activitytracker.fragments;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.masterandroid.activitytracker.R;

public class DashboardFragment extends Fragment implements SensorEventListener{

    // Sensor-related
    private SensorManager sensorManager;
    private Sensor stepSensor;
    private int totalSteps = 0;

    // UI elements
    private TextView stepCountText;
    private TextView locationText;

    // Location services
    private FusedLocationProviderClient fusedLocationClient;

    // SharedPreferences to read settings
    private SharedPreferences sharedPrefs;



    public DashboardFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // Link UI elements
        stepCountText = view.findViewById(R.id.step_count);
        locationText = view.findViewById(R.id.location);

        // Initialize step counter sensor
        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        // Initialize location provider
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        // Load user preferences
        sharedPrefs = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();

        // Register step sensor
        if (stepSensor != null) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
        }

        // Check if location tracking is enabled in settings
        boolean locationEnabled = sharedPrefs.getBoolean("location_enabled", true);
        if (locationEnabled) {
            requestLocation();
        } else {
            locationText.setText("Location: Disabled");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister step sensor to save battery
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // When step count updates
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            totalSteps = (int) event.values[0];
            stepCountText.setText("Steps: " + totalSteps);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // You can leave this empty for now unless you want to respond to accuracy changes
    }

    private void requestLocation() {
        // Check location permission
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Ask for permission
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
            return;
        }

        // Get last known location
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        locationText.setText("Location: " + location.getLatitude() + ", " + location.getLongitude());
                    } else {
                        locationText.setText("Location: Not available");
                    }
                });
    }
}