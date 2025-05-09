package com.example.projetotarefas;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projetotarefas.model.Database;

import java.util.Calendar;
import java.util.Locale;

public class AddNotaActivity extends AppCompatActivity {

    private TextView selectedDateTextView;
    private TextView selectedTimeTextView;
    private EditText taskEditText;
    private Spinner categorySpinner;
    private Spinner prioritySpinner;
    private EditText notesEditText;

    private Database dbHelper;

    private Calendar calendar;
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        selectedDateTextView = findViewById(R.id.selected_date_text_view);
        selectedTimeTextView = findViewById(R.id.selected_time_text_view);
        taskEditText = findViewById(R.id.task_edit_text);
        categorySpinner = findViewById(R.id.category_spinner);
        prioritySpinner = findViewById(R.id.priority_spinner);
        notesEditText = findViewById(R.id.notes_edit_text);
        Button selectDateButton = findViewById(R.id.button_select_due_date);
        Button selectTimeButton = findViewById(R.id.button_select_due_time);
        Button addTaskButton = findViewById(R.id.button_add_task);

        calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);

        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(
                this, R.array.categories_array, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        ArrayAdapter<CharSequence> priorityAdapter = ArrayAdapter.createFromResource(
                this, R.array.priorities_array, android.R.layout.simple_spinner_item);
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(priorityAdapter);

        updateDateAndTimeTextViews();

        selectDateButton.setOnClickListener(v -> showDatePickerDialog());
        selectTimeButton.setOnClickListener(v -> showTimePickerDialog());

        addTaskButton.setOnClickListener(v -> {
            addTask();
            Intent intent = new Intent(AddNotaActivity.this, MainActivity.class);
            startActivity(intent);
        });

        dbHelper = new Database(this);
    }

    private void updateDateAndTimeTextViews() {
        String dateString = String.format(Locale.getDefault(), "%02d/%02d/%d", mDay, mMonth + 1, mYear);
        selectedDateTextView.setText(dateString);

        String timeString = String.format(Locale.getDefault(), "%02d:%02d", mHour, mMinute);
        selectedTimeTextView.setText(timeString);
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    mYear = year;
                    mMonth = month;
                    mDay = dayOfMonth;
                    updateDateAndTimeTextViews();
                },
                mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    mHour = hourOfDay;
                    mMinute = minute;
                    updateDateAndTimeTextViews();
                },
                mHour, mMinute, true);
        timePickerDialog.show();
    }

    private void addTask() {
        String task = taskEditText.getText().toString().trim();
        String category = categorySpinner.getSelectedItem().toString();
        String priority = prioritySpinner.getSelectedItem().toString();
        String notes = notesEditText.getText().toString().trim();
        String dueDate = selectedDateTextView.getText().toString().trim();
        String dueTime = selectedTimeTextView.getText().toString().trim();

        if (task.isEmpty()) {
            Toast.makeText(this, "Digite uma tarefa", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ContratoTarefa.EntradaTarefa.COLUNA_TAREFA, task);
        values.put(ContratoTarefa.EntradaTarefa.COLUNA_CATEGORIA, category);
        values.put(ContratoTarefa.EntradaTarefa.COLUNA_PRIORIDADE, priority);
        values.put(ContratoTarefa.EntradaTarefa.COLUNA_OBSERVACOES, notes);
        values.put(ContratoTarefa.EntradaTarefa.COLUNA_DATA_LIMITE, dueDate);
        values.put(ContratoTarefa.EntradaTarefa.COLUNA_HORA_LIMITE, dueTime);
        values.put(ContratoTarefa.EntradaTarefa.COLUNA_CONCLUIDA, 0);

        long newRowId = db.insert(ContratoTarefa.EntradaTarefa.NOME_TABELA, null, values);
        db.close();

        if (newRowId == -1) {
            Toast.makeText(this, "Erro ao adicionar tarefa", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Tarefa adicionada com sucesso", Toast.LENGTH_SHORT).show();

        // Agenda o alarme com notificação
        Calendar alarmCalendar = Calendar.getInstance();
        alarmCalendar.set(mYear, mMonth, mDay, mHour, mMinute, 0);

        Intent alarmIntent = new Intent(this, NotaAlarmeReceiver.class);
        alarmIntent.putExtra("task_title", task);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, (int) newRowId, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    alarmCalendar.getTimeInMillis(),
                    pendingIntent
            );
            Log.d("AlarmDebug", "Alarme agendado para: " + alarmCalendar.getTime());
        }
    }
}
