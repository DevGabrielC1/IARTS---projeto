package com.example.projetotarefas.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "taskmanager.db";

    private static final String TABELA_TAREFAS = "tarefas";

    private static final String SQL_CREATE_TAREFAS = "CREATE TABLE tarefas (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "descricao TEXT NOT NULL, " +
            "data_inicio TEXT NOT NULL, " +
            "prazo_final TEXT NOT NULL, " +
            "importancia INTEGER NOT NULL CHECK(importancia IN (0,1)))";  // agora é booleano

    private static final String SQL_DELETE_TAREFAS = "DROP TABLE IF EXISTS tarefas";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(SQL_CREATE_TAREFAS);
            Log.i("Debug", "Banco de dados de tarefas criado com sucesso");
        } catch (SQLException e) {
            Log.e("Debug", "Erro ao criar banco: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TAREFAS);
        onCreate(db);
    }

    // Inserir nova tarefa
    public long inserirTarefa(String descricao, String dataInicio, String prazoFinal, int importancia) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("descricao", descricao);
        valores.put("data_inicio", dataInicio);
        valores.put("prazo_final", prazoFinal);
        valores.put("importancia", importancia); // 0 ou 1

        return db.insert(TABELA_TAREFAS, null, valores);
    }

    // Atualizar uma tarefa existente
    public int atualizarTarefa(int id, String descricao, String dataInicio, String prazoFinal, int importancia) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("descricao", descricao);
        valores.put("data_inicio", dataInicio);
        valores.put("prazo_final", prazoFinal);
        valores.put("importancia", importancia); // 0 ou 1

        return db.update(TABELA_TAREFAS, valores, "id = ?", new String[]{String.valueOf(id)});
    }

    // Deletar uma tarefa pelo ID
    public int deletarTarefa(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABELA_TAREFAS, "id = ?", new String[]{String.valueOf(id)});
    }

    // Buscar todas as tarefas
    public List<HashMap<String, String>> buscarTarefas() {
        List<HashMap<String, String>> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABELA_TAREFAS, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> tarefa = new HashMap<>();
                tarefa.put("id", String.valueOf(cursor.getInt(0)));
                tarefa.put("descricao", cursor.getString(1));
                tarefa.put("data_inicio", cursor.getString(2));
                tarefa.put("prazo_final", cursor.getString(3));
                tarefa.put("importancia", cursor.getInt(4) == 1 ? "Sim" : "Não");

                lista.add(tarefa);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }
}
