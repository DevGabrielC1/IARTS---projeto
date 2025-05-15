package com.example.projetotarefas;

import static android.content.Context.ALARM_SERVICE;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.projetotarefas.model.Database;
import com.example.projetotarefas.model.Tarefas;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;
import java.util.Locale;

public class CreateTaskModal extends BottomSheetDialogFragment {

    private TextView selectedDateTextView;
    private TextView selectedTimeTextView;
    private EditText taskEditText;
    private EditText notesEditText;
    private Database dbHelper;
    private Calendar calendar;
    private int mYear, mMonth, mDay, mHour, mMinute;

    public interface OnTaskCreatedListener {
        void onTaskCreated(Tarefas novaTarefa);
    }

    private OnTaskCreatedListener callback;

    public void setOnTaskCreatedListener(OnTaskCreatedListener listener) {
        this.callback = listener;
    }

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_task_modal, container, false);

        selectedDateTextView = view.findViewById(R.id.button_select_due_date_add);
        selectedTimeTextView = view.findViewById(R.id.button_select_due_time_add);
        taskEditText = view.findViewById(R.id.task_edit_text_add);
        notesEditText = view.findViewById(R.id.notes_edit_text_add);
        Button selectDateButton = view.findViewById(R.id.button_select_due_date_add);
        Button selectTimeButton = view.findViewById(R.id.button_select_due_time_add);
        Button addTaskButton = view.findViewById(R.id.button_add_task_add);

        calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);

        dbHelper = new Database(requireContext());
        updateDateAndTimeTextViews();

        selectDateButton.setOnClickListener(v -> showDatePickerDialog());
        selectTimeButton.setOnClickListener(v -> showTimePickerDialog());

        addTaskButton.setOnClickListener(v -> {
            Tarefas nova = addTask();
            if (nova != null && callback != null) {
                callback.onTaskCreated(nova);
            }
            dismiss(); // Fecha o modal
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        View bottomSheet = getView();
        if (bottomSheet != null) {
            ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
            int screenHeight = (int) (getResources().getDisplayMetrics().heightPixels * 0.3);
            layoutParams.height = screenHeight;
            bottomSheet.setLayoutParams(layoutParams);
        }
    }

    private void updateDateAndTimeTextViews() {
        String dateString = String.format(Locale.getDefault(), "%02d/%02d/%d", mDay, mMonth + 1, mYear);
        selectedDateTextView.setText(dateString);

        String timeString = String.format(Locale.getDefault(), "%02d:%02d", mHour, mMinute);
        selectedTimeTextView.setText(timeString);
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
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
                requireContext(),
                (view, hourOfDay, minute) -> {
                    mHour = hourOfDay;
                    mMinute = minute;
                    updateDateAndTimeTextViews();
                },
                mHour, mMinute, true);
        timePickerDialog.show();
    }

    private Tarefas addTask() {
        String task = taskEditText.getText().toString().trim();
        String notes = notesEditText.getText().toString().trim();
        String dueDate = selectedDateTextView.getText().toString().trim();
        String dueTime = selectedTimeTextView.getText().toString().trim();

        if (task.isEmpty()) {
            Toast.makeText(requireContext(), "Digite uma tarefa", Toast.LENGTH_SHORT).show();
            return null;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ContratoTarefa.EntradaTarefa.COLUNA_TAREFA, task);
        values.put(ContratoTarefa.EntradaTarefa.COLUNA_OBSERVACOES, notes);
        values.put(ContratoTarefa.EntradaTarefa.COLUNA_DATA_LIMITE, dueDate);
        values.put(ContratoTarefa.EntradaTarefa.COLUNA_HORA_LIMITE, dueTime);
        values.put(ContratoTarefa.EntradaTarefa.COLUNA_CONCLUIDA, 0);

        long newRowId = db.insert(ContratoTarefa.EntradaTarefa.NOME_TABELA, null, values);
        db.close();

        if (newRowId == -1) {
            Toast.makeText(requireContext(), "Erro ao adicionar tarefa", Toast.LENGTH_SHORT).show();
            return null;
        }

        Toast.makeText(requireContext(), "Tarefa adicionada", Toast.LENGTH_SHORT).show();

        // Agendar notificação
        Calendar alarmCalendar = Calendar.getInstance();
        alarmCalendar.set(mYear, mMonth, mDay, mHour, mMinute, 0);

        Intent alarmIntent = new Intent(requireContext(), NotaAlarmeReceiver.class);
        alarmIntent.putExtra("task_title", task);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                requireContext(), (int) newRowId, alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    alarmCalendar.getTimeInMillis(),
                    pendingIntent
            );
            Log.d("AlarmDebug", "Alarme agendado para: " + alarmCalendar.getTime());
        }

        return new Tarefas(task, dueDate, dueTime, notes);
    }
}
