package com.example.najihah_electricbillestimator;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity {

    EditText editMonth, editUnit;
    Button btnUpdate;
    DatabaseHelper db;
    int billId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        setTitle("Edit Bill");

        editMonth = findViewById(R.id.editMonth);
        editUnit = findViewById(R.id.editUnit);
        btnUpdate = findViewById(R.id.btnUpdate);

        db = new DatabaseHelper(this);

        // Dapatkan BILL_ID dari intent
        billId = getIntent().getIntExtra("BILL_ID", -1);

        if (billId == -1) {
            Toast.makeText(this, "Invalid bill ID", Toast.LENGTH_SHORT).show();
            finish();
        }

        loadData(billId);

        btnUpdate.setOnClickListener(view -> updateBill());
    }

    private void loadData(int id) {
        Cursor cursor = db.getDataById(id);
        if (cursor != null && cursor.moveToFirst()) {
            String month = cursor.getString(cursor.getColumnIndexOrThrow("MONTH"));
            int unit = cursor.getInt(cursor.getColumnIndexOrThrow("UNIT"));

            editMonth.setText(month);
            editUnit.setText(String.valueOf(unit));
        } else {
            Toast.makeText(this, "Failed to load data", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void updateBill() {
        String newMonth = editMonth.getText().toString().trim();
        String unitStr = editUnit.getText().toString().trim();

        if (newMonth.isEmpty() || unitStr.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int newUnit = Integer.parseInt(unitStr);

        // Logic to calculate total, rebate, final
        double total = newUnit * 0.218; // contoh kadar unit
        double rebate = (newUnit <= 200) ? total * 0.05 : 0;
        double finalCost = total - rebate;

        boolean updated = db.updateBill(billId, newMonth, newUnit, total, rebate, finalCost);

        if (updated) {
            Toast.makeText(this, "Bill updated successfully", Toast.LENGTH_SHORT).show();
            finish(); // Kembali ke HistoryActivity
        } else {
            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
        }
    }
}
