package com.example.najihah_electricbillestimator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper db;

    Spinner spinnerMonth, spinnerRebate;
    EditText editTextUnit;
    TextView textTotalCharges, textFinalCost;
    Button btnCalculate, btnInstructions, btnHistory, btnAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Electricity Estimator");

        // Initialize views
        spinnerMonth = findViewById(R.id.spinnerMonth);
        spinnerRebate = findViewById(R.id.spinnerRebate);
        editTextUnit = findViewById(R.id.editTextUnit);
        textTotalCharges = findViewById(R.id.textTotalCharges);
        textFinalCost = findViewById(R.id.textFinalCost);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnInstructions = findViewById(R.id.btnInstructions);
        btnHistory = findViewById(R.id.btnHistory);
        btnAbout = findViewById(R.id.btnAbout);

        // Initialize database helper
        db = new DatabaseHelper(this);

        // Set month spinner values
        String[] months = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, months);
        spinnerMonth.setAdapter(monthAdapter);

        // Set rebate spinner values
        String[] rebates = {"0%", "1%", "2%", "3%", "4%", "5%"};
        ArrayAdapter<String> rebateAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, rebates);
        spinnerRebate.setAdapter(rebateAdapter);

        // Button listeners
        btnCalculate.setOnClickListener(v -> calculateBill());

        btnInstructions.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, InstructionActivity.class);
            startActivity(intent);
        });

        btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        btnAbout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        });
    }

    private void calculateBill() {
        String unitStr = editTextUnit.getText().toString();

        if (unitStr.isEmpty()) {
            Toast.makeText(this, "Please enter electricity usage.", Toast.LENGTH_SHORT).show();
            return;
        }

        int units;
        try {
            units = Integer.parseInt(unitStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number entered.", Toast.LENGTH_SHORT).show();
            return;
        }

        double totalCharges;

        if (units <= 200) {
            totalCharges = units * 0.218;
        } else if (units <= 300) {
            totalCharges = (200 * 0.218) + ((units - 200) * 0.334);
        } else if (units <= 600) {
            totalCharges = (200 * 0.218) + (100 * 0.334) + ((units - 300) * 0.516);
        } else {
            totalCharges = (200 * 0.218) + (100 * 0.334) + (300 * 0.516) + ((units - 600) * 0.546);
        }

        String selectedRebate = spinnerRebate.getSelectedItem().toString().replace("%", "");
        double rebate = Double.parseDouble(selectedRebate);
        double finalCost = totalCharges - (totalCharges * rebate / 100);

        // Display results
        textTotalCharges.setText(String.format("Total Charges: RM %.2f", totalCharges));
        textFinalCost.setText(String.format("Final Cost After Rebate: RM %.2f", finalCost));

        // Save to database
        String selectedMonth = spinnerMonth.getSelectedItem().toString();

        boolean inserted = db.insertData(
                selectedMonth,
                units,
                totalCharges,
                rebate,
                finalCost
        );

        if (inserted) {
            Toast.makeText(this, "Data saved!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to save data.", Toast.LENGTH_SHORT).show();
        }
    }
}


