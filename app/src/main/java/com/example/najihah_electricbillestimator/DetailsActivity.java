package com.example.najihah_electricbillestimator;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class DetailsActivity extends AppCompatActivity {

    TextView textMonth, textUnit, textTotal, textRebate, textFinal;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setTitle("Bill Details");

        textMonth = findViewById(R.id.textMonth);
        textUnit = findViewById(R.id.textUnit);
        textTotal = findViewById(R.id.textTotal);
        textRebate = findViewById(R.id.textRebate);
        textFinal = findViewById(R.id.textFinal);

        db = new DatabaseHelper(this);

        int billId = getIntent().getIntExtra("BILL_ID", -1);
        if (billId != -1) {
            loadBillDetails(billId);
        } else {
            Toast.makeText(this, "No bill data found.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadBillDetails(int id) {
        Cursor cursor = db.getDataById(id);
        if (cursor.moveToFirst()) {
            String month = cursor.getString(1);
            int unit = cursor.getInt(2);
            double total = cursor.getDouble(3);
            double rebate = cursor.getDouble(4);
            double finalCost = cursor.getDouble(5);

            textMonth.setText("Month: " + month);
            textUnit.setText("Units Used: " + unit + " kWh");
            textTotal.setText("Total Charges: RM " + String.format("%.2f", total));
            textRebate.setText("Rebate: " + String.format("%.0f", rebate) + "%");
            textFinal.setText("Final Cost: RM " + String.format("%.2f", finalCost));
        }
    }
}