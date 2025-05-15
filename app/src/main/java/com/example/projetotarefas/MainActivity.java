package com.example.projetotarefas;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetotarefas.model.Database;
import com.example.projetotarefas.model.Tarefas;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ScrollView taskScrollView;
    private ArrayAdapter<String> taskAdapter;
    private Database dbHelper;
    private List<Tarefas> taskData;
    FloatingActionButton fabAddTask;
    RecyclerView recyclerView;
    private NotasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        taskData = new ArrayList<>();
        taskAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
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
                Intent intent = new Intent(MainActivity.this, EditarNotas.class);
                intent.putExtra("task", taskData.get(position).getNomeTarefa());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(int position) {
                markTaskAsComplete(position);
                taskData.remove(position);
                Toast.makeText(MainActivity.this, "Nota Excluída", Toast.LENGTH_SHORT).show();
                adapter.notifyItemRemoved(position);
            }

            @Override
            public void onCheckboxClick(int position) {
                markTaskAsComplete(position);
                taskData.remove(position);
                Toast.makeText(MainActivity.this, "Nota Concluída", Toast.LENGTH_SHORT).show();
                adapter.notifyItemRemoved(position);
            }
        });

        // Aqui foi feita a alteração para abrir o modal
        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateTaskModal modal = new CreateTaskModal();
                modal.show(getSupportFragmentManager(), "CreateTaskModal");
            }
        });
    }

    public void loadTasksFromSQLite(List<Tarefas> data) {
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
        db.delete(ContratoTarefa.EntradaTarefa.NOME_TABELA,
                ContratoTarefa.EntradaTarefa.COLUNA_TAREFA + " = ?", new String[]{task});
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
