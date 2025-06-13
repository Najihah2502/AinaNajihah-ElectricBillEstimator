package com.example.najihah_electricbillestimator;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    ListView listView;
    DatabaseHelper db;
    ArrayList<String> listItems;
    ArrayList<Integer> ids; // Track IDs for each bill

    @Override
    protected void onResume() {
        super.onResume();
        listItems.clear();
        ids.clear();
        loadData(); // Reload updated list when returning from other screens
    }

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

        // Short click = View details
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            int id = ids.get(i);
            Intent intent = new Intent(HistoryActivity.this, DetailsActivity.class);
            intent.putExtra("BILL_ID", id);
            startActivity(intent);
        });

        // Long click = Edit or Delete
        listView.setOnItemLongClickListener((adapterView, view, position, id) -> {
            int selectedId = ids.get(position);
            String selectedItem = listItems.get(position);

            new android.app.AlertDialog.Builder(HistoryActivity.this)
                    .setTitle("Select Option")
                    .setMessage("What do you want to do with:\n" + selectedItem + "?")
                    .setPositiveButton("Edit", (dialogInterface, i1) -> {
                        Intent editIntent = new Intent(HistoryActivity.this, EditActivity.class);
                        editIntent.putExtra("BILL_ID", selectedId);
                        startActivity(editIntent);
                    })
                    .setNegativeButton("Delete", (dialogInterface, i1) -> {
                        boolean deleted = db.deleteBill(selectedId);
                        if (deleted) {
                            Toast.makeText(HistoryActivity.this, "Bill deleted.", Toast.LENGTH_SHORT).show();
                            listItems.clear();
                            ids.clear();
                            loadData();
                        } else {
                            Toast.makeText(HistoryActivity.this, "Failed to delete.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNeutralButton("Cancel", null)
                    .show();

            return true;
        });
    }

    // Load all bill data from the database into the ListView
    private void loadData() {
        Cursor cursor = db.getAllData();
        if (cursor == null || cursor.getCount() == 0) {
            Toast.makeText(this, "No data found.", Toast.LENGTH_SHORT).show();
            return;
        }

        listItems.clear();
        ids.clear();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0); // ID column
            String month = cursor.getString(1); // MONTH column
            double finalCost = cursor.getDouble(5); // FINAL_COST column

            listItems.add(month + " - RM " + String.format("%.2f", finalCost));
            ids.add(id);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);
    }
}
