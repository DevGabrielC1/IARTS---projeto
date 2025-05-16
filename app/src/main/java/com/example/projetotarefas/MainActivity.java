package com.example.projetotarefas;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetotarefas.model.Database;
import com.example.projetotarefas.model.Tarefas;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1001;

    private ScrollView taskScrollView;
    private Database dbHelper;
    private List<Tarefas> taskData;
    private FloatingActionButton fabAddTask;
    private RecyclerView recyclerView;
    private NotasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Pedido de permissão para notificações no Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        PERMISSION_REQUEST_CODE);
            }
        }

        taskData = new ArrayList<>();
        fabAddTask = findViewById(R.id.fab_add_task);
        dbHelper = new Database(this);

        loadTasksFromSQLite(taskData);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotasAdapter(this, taskData);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new NotasAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(int position) {
                Tarefas tarefaSelecionada = taskData.get(position);
                EditarNotas editModal = new EditarNotas(tarefaSelecionada);

                editModal.setOnTaskEditedListener(() -> {
                    loadTasksFromSQLite(taskData);
                    adapter.notifyDataSetChanged();
                });

                editModal.show(getSupportFragmentManager(), "EditTaskModal");
            }

            @Override
            public void onDeleteClick(int position) {
                ConfirmarExclusao confirmDialog = new ConfirmarExclusao();

                confirmDialog.setConfirmarDeleteListener(() -> {
                    markTaskAsComplete(position);
                    taskData.remove(position);
                    adapter.notifyItemRemoved(position);
                    Toast.makeText(MainActivity.this, "Nota Excluída", Toast.LENGTH_SHORT).show();
                });

                confirmDialog.show(getSupportFragmentManager(), "ConfirmDeleteDialog");
            }

            @Override
            public void onCheckboxClick(int position) {
                markTaskAsComplete(position);
                taskData.remove(position);
                adapter.notifyItemRemoved(position);
                Toast.makeText(MainActivity.this, "Nota Concluída", Toast.LENGTH_SHORT).show();
            }
        });

        fabAddTask.setOnClickListener(view -> {
            CreateTaskModal modal = new CreateTaskModal();

            modal.setOnTaskCreatedListener(novaTarefa -> {
                taskData.add(novaTarefa);
                adapter.notifyItemInserted(taskData.size() - 1);
            });

            modal.show(getSupportFragmentManager(), "CreateTaskModal");
        });
    }

    // Resposta do pedido de permissão
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permissão para notificações concedida", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permissão para notificações negada. Notificações podem não funcionar.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void loadTasksFromSQLite(List<Tarefas> data) {
        data.clear();  // Evita duplicatas
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + ContratoTarefa.EntradaTarefa.NOME_TABELA, null);

        while (cursor.moveToNext()) {
            @SuppressLint("Range") String taskName = cursor.getString(cursor.getColumnIndex(ContratoTarefa.EntradaTarefa.COLUNA_TAREFA));
            @SuppressLint("Range") String taskDate = cursor.getString(cursor.getColumnIndex(ContratoTarefa.EntradaTarefa.COLUNA_DATA_LIMITE));
            @SuppressLint("Range") String taskTime = cursor.getString(cursor.getColumnIndex(ContratoTarefa.EntradaTarefa.COLUNA_HORA_LIMITE));
            @SuppressLint("Range") String notes = cursor.getString(cursor.getColumnIndex(ContratoTarefa.EntradaTarefa.COLUNA_OBSERVACOES));

            data.add(new Tarefas(taskName, taskDate, taskTime, notes));
        }

        cursor.close();
        db.close();
    }

    public void markTaskAsComplete(int position) {
        String task = taskData.get(position).getNomeTarefa();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ContratoTarefa.EntradaTarefa.COLUNA_CONCLUIDA, 1);

        // Marca como concluído ou deleta
        db.delete(ContratoTarefa.EntradaTarefa.NOME_TABELA,
                ContratoTarefa.EntradaTarefa.COLUNA_TAREFA + " = ?", new String[]{task});

        db.close();
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
