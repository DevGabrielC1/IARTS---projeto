package com.example.projetotarefas;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.database.Cursor;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetotarefas.model.Database;
import com.example.projetotarefas.model.Tarefas;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        taskData=new ArrayList<>();
        taskAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        fabAddTask = findViewById(R.id.fab_add_task);
        dbHelper = new Database(this);
        loadTasksFromSQLite(taskData);
        recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotasAdapter(
                this, taskData);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new NotasAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(int position) {
                Intent intent = new Intent(MainActivity.this, EditarNotas.class);
                intent.putExtra("task", taskData.get(position).getNomeTarefa());
                startActivity(intent);
                //Toast.makeText(MainActivity.this, "Edit clicked at position " + position, Toast.LENGTH_SHORT).show();
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

        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddNotaActivity.class);
                startActivity(intent);
            }
        });

    }
    public void loadTasksFromSQLite(List<Tarefas> data) {
        // Assuming you have a SQLiteOpenHelper instance named dbHelper
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+ContratoTarefa.EntradaTarefa.NOME_TABELA, null);

        while (cursor.moveToNext()) {
            @SuppressLint("Range") String taskName = cursor.getString(cursor.getColumnIndex(ContratoTarefa.EntradaTarefa.COLUNA_TAREFA));
            @SuppressLint("Range") String taskDate = cursor.getString(cursor.getColumnIndex(ContratoTarefa.EntradaTarefa.COLUNA_DATA_LIMITE));
            @SuppressLint("Range") String taskTime = cursor.getString(cursor.getColumnIndex(ContratoTarefa.EntradaTarefa.COLUNA_HORA_LIMITE));
            @SuppressLint("Range") String notes = cursor.getString(cursor.getColumnIndex(ContratoTarefa.EntradaTarefa.COLUNA_OBSERVACOES));

            data.add(new Tarefas(taskName, taskDate, taskTime, notes));
            //Toast.makeText(this, "added", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        db.close();
    }
    public void markTaskAsComplete(int position) {
        String task = taskData.get(position).getNomeTarefa();
        //Toast.makeText(this, task, Toast.LENGTH_SHORT).show();
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