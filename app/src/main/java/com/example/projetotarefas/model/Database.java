package com.example.projetotarefas.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "taskmanager.db";
    private static final String TABELA_TAREFAS = "tarefas";

    private static final String SQL_CREATE_TAREFAS =
            "CREATE TABLE " + TABELA_TAREFAS + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "descricao TEXT NOT NULL, " +
                    "data_inicio TEXT NOT NULL, " +
                    "prazo_final TEXT NOT NULL, " +
                    "importancia INTEGER NOT NULL CHECK(importancia IN (0,1)), " +
                    "concluida INTEGER NOT NULL CHECK(concluida IN (0,1)))"; // Adicionada a coluna "concluida"

    private static final String SQL_DELETE_TAREFAS = "DROP TABLE IF EXISTS " + TABELA_TAREFAS;

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
    public long inserirTarefa(Tarefas tarefa) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("descricao", tarefa.getDescricao());
        valores.put("data_inicio", tarefa.getDataInicio());
        valores.put("prazo_final", tarefa.getPrazoFinal());
        valores.put("importancia", tarefa.isImportante() ? 1 : 0);
        valores.put("concluida", tarefa.isConcluida() ? 1 : 0); // Gerenciando o campo de conclusão
        return db.insert(TABELA_TAREFAS, null, valores);
    }

    // Atualizar tarefa
    public int atualizarTarefa(Tarefas tarefa) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("descricao", tarefa.getDescricao());
        valores.put("data_inicio", tarefa.getDataInicio());
        valores.put("prazo_final", tarefa.getPrazoFinal());
        valores.put("importancia", tarefa.isImportante() ? 1 : 0);
        valores.put("concluida", tarefa.isConcluida() ? 1 : 0); // Atualizando o campo de conclusão
        return db.update(TABELA_TAREFAS, valores, "id = ?", new String[]{String.valueOf(tarefa.getId())});
    }

    // Deletar tarefa
    public int deletarTarefa(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABELA_TAREFAS, "id = ?", new String[]{String.valueOf(id)});
    }

    // Buscar todas as tarefas
    public List<Tarefas> buscarTodasTarefas() {
        List<Tarefas> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABELA_TAREFAS, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao"));
                String dataInicio = cursor.getString(cursor.getColumnIndexOrThrow("data_inicio"));
                String prazoFinal = cursor.getString(cursor.getColumnIndexOrThrow("prazo_final"));
                boolean importante = cursor.getInt(cursor.getColumnIndexOrThrow("importancia")) == 1;
                boolean concluida = cursor.getInt(cursor.getColumnIndexOrThrow("concluida")) == 1;

                Tarefas tarefa = new Tarefas(id, descricao, dataInicio, prazoFinal, importante, concluida);
                lista.add(tarefa);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return lista;
    }

    // Buscar tarefas concluídas
    public List<Tarefas> buscarTarefasConcluidas() {
        List<Tarefas> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABELA_TAREFAS + " WHERE concluida = 1", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao"));
                String dataInicio = cursor.getString(cursor.getColumnIndexOrThrow("data_inicio"));
                String prazoFinal = cursor.getString(cursor.getColumnIndexOrThrow("prazo_final"));
                boolean importante = cursor.getInt(cursor.getColumnIndexOrThrow("importancia")) == 1;
                boolean concluida = cursor.getInt(cursor.getColumnIndexOrThrow("concluida")) == 1;

                Tarefas tarefa = new Tarefas(id, descricao, dataInicio, prazoFinal, importante, concluida);
                lista.add(tarefa);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return lista;
    }

    // Buscar tarefas não concluídas
    public List<Tarefas> buscarTarefasNaoConcluidas() {
        List<Tarefas> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABELA_TAREFAS + " WHERE concluida = 0", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao"));
                String dataInicio = cursor.getString(cursor.getColumnIndexOrThrow("data_inicio"));
                String prazoFinal = cursor.getString(cursor.getColumnIndexOrThrow("prazo_final"));
                boolean importante = cursor.getInt(cursor.getColumnIndexOrThrow("importancia")) == 1;
                boolean concluida = cursor.getInt(cursor.getColumnIndexOrThrow("concluida")) == 1;

                Tarefas tarefa = new Tarefas(id, descricao, dataInicio, prazoFinal, importante, concluida);
                lista.add(tarefa);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return lista;
    }
}
