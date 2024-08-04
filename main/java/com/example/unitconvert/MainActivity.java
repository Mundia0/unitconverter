package com.example.unitconvert;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinnerUnitType, spinnerFromUnit, spinnerToUnit;
    private EditText editTextFromValue;
    private TextView textViewResult;

    private final String[] units = {"Length", "Weight", "Temperature", "Area", "Volume"};
    private final String[][] unitValues = {
            {"Millimeter", "Centimeter", "Meter", "Kilometer", "Inch", "Foot", "Yard", "Mile"},
            {"Milligram", "Gram", "Kilogram", "Ounce", "Pound"},
            {"Celsius", "Fahrenheit", "Kelvin"},
            {"Square Meter", "Square Kilometer", "Square Foot", "Square Yard", "Acre"},
            {"Milliliter", "Liter", "Fluid Ounce", "Cup", "Pint", "Quart", "Gallon"}
    };

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerUnitType = findViewById(R.id.spinnerUnitType);
        spinnerFromUnit = findViewById(R.id.spinnerFromUnit);
        spinnerToUnit = findViewById(R.id.spinnerToUnit);
        editTextFromValue = findViewById(R.id.editTextFromValue);
        textViewResult = findViewById(R.id.textViewResult);
        Button buttonConvert = findViewById(R.id.buttonConvert);
        Button buttonReset = findViewById(R.id.buttonReset);

        // Set up the spinners
        ArrayAdapter<String> unitTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, units);
        unitTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnitType.setAdapter(unitTypeAdapter);

        spinnerUnitType.setOnItemSelectedListener(this);

        buttonConvert.setOnClickListener(v -> performConversion());

        buttonReset.setOnClickListener(v -> resetFields());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Update the unit options based on the selected category
        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, unitValues[position]);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFromUnit.setAdapter(unitAdapter);
        spinnerToUnit.setAdapter(unitAdapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }

    @SuppressLint("SetTextI18n")
    private void performConversion() {
        int unitTypeIndex = spinnerUnitType.getSelectedItemPosition();
        int fromUnitIndex = spinnerFromUnit.getSelectedItemPosition();
        int toUnitIndex = spinnerToUnit.getSelectedItemPosition();
        double fromValue = Double.parseDouble(editTextFromValue.getText().toString());

        double result = convertValue(unitTypeIndex, fromUnitIndex, toUnitIndex, fromValue);
        textViewResult.setText("Result: " + result);
    }

    private double convertValue(int unitTypeIndex, int fromUnitIndex, int toUnitIndex, double fromValue) {
        switch (unitTypeIndex) {
            case 0: // Length
                return convertLength(fromUnitIndex, toUnitIndex, fromValue);
            case 1: // Weight
                return convertWeight(fromUnitIndex, toUnitIndex, fromValue);
            case 2: // Temperature
                return convertTemperature(fromUnitIndex, toUnitIndex, fromValue);
            case 3: // Area
                return convertArea(fromUnitIndex, toUnitIndex, fromValue);
            case 4: // Volume
                return convertVolume(fromUnitIndex, toUnitIndex, fromValue);
            default:
                return 0;
        }
    }

    private double convertLength(int fromUnitIndex, int toUnitIndex, double fromValue) {
        double[] conversionFactors = {1, 0.1, 0.001, 1e-6, 0.0393701, 0.00328084, 0.00109361, 6.2137e-7}; // to meters
        return fromValue * conversionFactors[fromUnitIndex] / conversionFactors[toUnitIndex];
    }

    private double convertWeight(int fromUnitIndex, int toUnitIndex, double fromValue) {
        double[] conversionFactors = {1e-6, 1e-3, 1, 0.035274, 0.00220462}; // to kilograms
        return fromValue * conversionFactors[fromUnitIndex] / conversionFactors[toUnitIndex];
    }

    private double convertTemperature(int fromUnitIndex, int toUnitIndex, double fromValue) {
        if (fromUnitIndex == toUnitIndex) return fromValue;
        if (fromUnitIndex == 0 && toUnitIndex == 1) return (fromValue * 9 / 5) + 32; // Celsius to Fahrenheit
        if (fromUnitIndex == 0 && toUnitIndex == 2) return fromValue + 273.15; // Celsius to Kelvin
        if (fromUnitIndex == 1 && toUnitIndex == 0) return (fromValue - 32) * 5 / 9; // Fahrenheit to Celsius
        if (fromUnitIndex == 1 && toUnitIndex == 2) return ((fromValue - 32) * 5 / 9) + 273.15; // Fahrenheit to Kelvin
        if (fromUnitIndex == 2 && toUnitIndex == 0) return fromValue - 273.15; // Kelvin to Celsius
        if (fromUnitIndex == 2 && toUnitIndex == 1) return ((fromValue - 273.15) * 9 / 5) + 32; // Kelvin to Fahrenheit
        return 0;
    }

    private double convertArea(int fromUnitIndex, int toUnitIndex, double fromValue) {
        double[] conversionFactors = {1, 1e-6, 10.7639, 1.19599, 2.471e-4}; // to square meters
        return fromValue * conversionFactors[fromUnitIndex] / conversionFactors[toUnitIndex];
    }

    private double convertVolume(int fromUnitIndex, int toUnitIndex, double fromValue) {
        double[] conversionFactors = {1e-6, 0.001, 3.3814e-5, 0.000236588, 0.000473176, 0.000946353, 0.00378541}; // to cubic meters
        return fromValue * conversionFactors[fromUnitIndex] / conversionFactors[toUnitIndex];
    }

    @SuppressLint("SetTextI18n")
    private void resetFields() {
        spinnerUnitType.setSelection(0);
        spinnerFromUnit.setSelection(0);
        spinnerToUnit.setSelection(0);
        editTextFromValue.setText("");
        textViewResult.setText("Result: ");
    }
}
