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

public class EditarNotas extends BottomSheetDialogFragment {

    private TextView selectedDateTextView;
    private TextView selectedTimeTextView;
    private EditText notesEditText;
    private TextView taskTitleView;

    private Database dbHelper;
    private Calendar calendar;
    private int mYear, mMonth, mDay, mHour, mMinute;

    private Tarefas tarefa;

    public EditarNotas(Tarefas tarefa) {
        this.tarefa = tarefa;
    }

    public interface OnTaskEditedListener {
        void onTaskEdited();
    }

    private OnTaskEditedListener callback;

    public void setOnTaskEditedListener(OnTaskEditedListener listener) {
        this.callback = listener;
    }

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_task_modal, container, false);

        selectedDateTextView = view.findViewById(R.id.button_select_due_date_add);
        selectedTimeTextView = view.findViewById(R.id.button_select_due_time_add);
        notesEditText = view.findViewById(R.id.notes_edit_text_add);
        taskTitleView = view.findViewById(R.id.task_edit_text_add);
        Button selectDateButton = view.findViewById(R.id.button_select_due_date_add);
        Button selectTimeButton = view.findViewById(R.id.button_select_due_time_add);
        Button editTaskButton = view.findViewById(R.id.button_add_task_add);

        calendar = Calendar.getInstance();

        dbHelper = new Database(requireContext());

        taskTitleView.setEnabled(false); // título não editável
        taskTitleView.setText(tarefa.getNomeTarefa());
        notesEditText.setText(tarefa.getObservacoes());

        setInitialDateAndTime(tarefa.getDataLimite(), tarefa.getHoraLimite());
        updateDateAndTimeTextViews();

        selectDateButton.setOnClickListener(v -> showDatePickerDialog());
        selectTimeButton.setOnClickListener(v -> showTimePickerDialog());

        editTaskButton.setText("Editar");
        editTaskButton.setOnClickListener(v -> {
            editTask();
            if (callback != null) {
                callback.onTaskEdited();
            }
            dismiss();
        });

        return view;
    }

    private void setInitialDateAndTime(String date, String time) {
        String[] dateParts = date.split("/");
        mDay = Integer.parseInt(dateParts[0]);
        mMonth = Integer.parseInt(dateParts[1]) - 1;
        mYear = Integer.parseInt(dateParts[2]);

        String[] timeParts = time.split(":");
        mHour = Integer.parseInt(timeParts[0]);
        mMinute = Integer.parseInt(timeParts[1]);
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

    private void editTask() {
        String notes = notesEditText.getText().toString().trim();
        String dueDate = selectedDateTextView.getText().toString().trim();
        String dueTime = selectedTimeTextView.getText().toString().trim();

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ContratoTarefa.EntradaTarefa.COLUNA_OBSERVACOES, notes);
        values.put(ContratoTarefa.EntradaTarefa.COLUNA_DATA_LIMITE, dueDate);
        values.put(ContratoTarefa.EntradaTarefa.COLUNA_HORA_LIMITE, dueTime);
        db.update(ContratoTarefa.EntradaTarefa.NOME_TABELA, values,
                ContratoTarefa.EntradaTarefa.COLUNA_TAREFA + " = ?", new String[]{tarefa.getNomeTarefa()});
        db.close();

        Calendar alarmCalendar = Calendar.getInstance();
        alarmCalendar.set(mYear, mMonth, mDay, mHour, mMinute, 0);

        Intent alarmIntent = new Intent(requireContext(), NotaAlarmeReceiver.class);
        alarmIntent.putExtra("task_title", tarefa.getNomeTarefa());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                requireContext(), tarefa.getNomeTarefa().hashCode(), alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), pendingIntent);
        }

        Toast.makeText(requireContext(), "Tarefa editada", Toast.LENGTH_SHORT).show();
    }
}
