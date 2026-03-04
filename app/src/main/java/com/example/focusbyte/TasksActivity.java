package com.example.focusbyte;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class TasksActivity extends AppCompatActivity {
    ArrayList<String> taskNames = new ArrayList<>();
    ArrayList<Integer> taskMinutes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        EditText etName = findViewById(R.id.etTaskName);
        EditText etMin = findViewById(R.id.etTaskMinutes);
        ListView lv = findViewById(R.id.listView);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, taskNames);
        lv.setAdapter(adapter);

        findViewById(R.id.btnAdd).setOnClickListener(v -> {
            String name = etName.getText().toString();
            String minStr = etMin.getText().toString();
            if(!name.isEmpty() && !minStr.isEmpty()) {
                taskNames.add(name + " (" + minStr + " mins)");
                taskMinutes.add(Integer.parseInt(minStr));
                adapter.notifyDataSetChanged();
                etName.setText(""); etMin.setText("");
            }
        });

        // Click karne se Timer par jana (Connection)
        lv.setOnItemClickListener((p, v, pos, id) -> {
            Intent intent = new Intent(this, TimerActivity.class);
            intent.putExtra("TASK_NAME", taskNames.get(pos).split(" ")[0]);
            intent.putExtra("MINUTES", taskMinutes.get(pos));
            startActivity(intent);
        });

        // Long press se delete
        lv.setOnItemLongClickListener((p, v, pos, id) -> {
            taskNames.remove(pos);
            taskMinutes.remove(pos);
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "Task Deleted", Toast.LENGTH_SHORT).show();
            return true;
        });
    }
}