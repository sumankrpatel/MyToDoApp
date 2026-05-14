package com.example.myfirstapp;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Task> tasks;
    private TaskAdapter adapter;
    private EditText editTextTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTask = findViewById(R.id.editTextTask);
        ListView listViewTasks = findViewById(R.id.listViewTasks);
        Button buttonAdd = findViewById(R.id.buttonAdd);

        tasks = new ArrayList<>();
        adapter = new TaskAdapter();
        listViewTasks.setAdapter(adapter);

        buttonAdd.setOnClickListener(v -> {
            String taskText = editTextTask.getText().toString().trim();
            if (!taskText.isEmpty()) {
                tasks.add(new Task(taskText));
                adapter.notifyDataSetChanged();
                editTextTask.setText("");
            } else {
                Toast.makeText(MainActivity.this, R.string.msg_empty, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class TaskAdapter extends ArrayAdapter<Task> {
        public TaskAdapter() {
            super(MainActivity.this, R.layout.item_task, tasks);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_task, parent, false);
            }

            Task task = tasks.get(position);

            TextView textViewTask = convertView.findViewById(R.id.textViewTask);
            CheckBox checkBoxDone = convertView.findViewById(R.id.checkBoxDone);
            ImageButton buttonDelete = convertView.findViewById(R.id.buttonDelete);

            textViewTask.setText(task.getText());
            
            // Set checkbox state without triggering listener
            checkBoxDone.setOnCheckedChangeListener(null);
            checkBoxDone.setChecked(task.isCompleted());

            // Apply strike-through if completed
            updateTaskText(textViewTask, task.isCompleted());

            checkBoxDone.setOnCheckedChangeListener((buttonView, isChecked) -> {
                task.setCompleted(isChecked);
                updateTaskText(textViewTask, isChecked);
            });

            buttonDelete.setOnClickListener(v -> {
                tasks.remove(position);
                notifyDataSetChanged();
                Toast.makeText(MainActivity.this, R.string.msg_removed, Toast.LENGTH_SHORT).show();
            });

            return convertView;
        }

        private void updateTaskText(TextView textView, boolean isCompleted) {
            if (isCompleted) {
                textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                textView.setAlpha(0.5f);
            } else {
                textView.setPaintFlags(textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                textView.setAlpha(1.0f);
            }
        }
    }
}
