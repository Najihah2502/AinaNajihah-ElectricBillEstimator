package com.example.najihah_electricbillestimator;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    ListView listView;
    DatabaseHelper db;
    ArrayList<String> listItems;
    ArrayList<Integer> ids; // to track IDs for detail page

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setTitle("History");

        listView = findViewById(R.id.listViewBills);
        db = new DatabaseHelper(this);

        listItems = new ArrayList<>();
        ids = new ArrayList<>();

        loadData();

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            int id = ids.get(i);
            Intent intent = new Intent(HistoryActivity.this, DetailsActivity.class);
            intent.putExtra("BILL_ID", id);
            startActivity(intent);
        });
    }

    private void loadData() {
        Cursor cursor = db.getAllData();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data found.", Toast.LENGTH_SHORT).show();
            return;
        }

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String month = cursor.getString(1);
            double finalCost = cursor.getDouble(5);

            listItems.add(month + " - RM " + String.format("%.2f", finalCost));
            ids.add(id);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);
    }
}
