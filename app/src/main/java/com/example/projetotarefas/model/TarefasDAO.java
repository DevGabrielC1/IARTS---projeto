package com.example.projetotarefas.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class TarefasDAO {

    private Database dbHelper;

    public TarefasDAO(Context context) {
        this.dbHelper = new Database(context);
    }

    // Inserir nova tarefa
    public long inserir(Tarefas tarefa) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put("descricao", tarefa.getDescricao());
        valores.put("data_inicio", tarefa.getDataInicio());
        valores.put("prazo_final", tarefa.getPrazoFinal());
        valores.put("importancia", tarefa.isImportante() ? 1 : 0);

        return db.insert("tarefas", null, valores);
    }

    // Atualizar tarefa
    public int atualizar(Tarefas tarefa) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put("descricao", tarefa.getDescricao());
        valores.put("data_inicio", tarefa.getDataInicio());
        valores.put("prazo_final", tarefa.getPrazoFinal());
        valores.put("importancia", tarefa.isImportante() ? 1 : 0);

        return db.update("tarefas", valores, "id = ?", new String[]{String.valueOf(tarefa.getId())});
    }

    // Deletar tarefa
    public int deletar(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete("tarefas", "id = ?", new String[]{String.valueOf(id)});
    }

    // Buscar todas as tarefas
    public List<Tarefas> listar() {
        List<Tarefas> lista = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM tarefas", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao"));
                String dataInicio = cursor.getString(cursor.getColumnIndexOrThrow("data_inicio"));
                String prazoFinal = cursor.getString(cursor.getColumnIndexOrThrow("prazo_final"));
                boolean importante = cursor.getInt(cursor.getColumnIndexOrThrow("importancia")) == 1;

                Tarefas tarefa = new Tarefas(id, descricao, dataInicio, prazoFinal, importante);
                lista.add(tarefa);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return lista;
    }

    // Buscar uma tarefa por ID
    public Tarefas buscarPorId(int idBusca) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM tarefas WHERE id = ?", new String[]{String.valueOf(idBusca)});

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao"));
            String dataInicio = cursor.getString(cursor.getColumnIndexOrThrow("data_inicio"));
            String prazoFinal = cursor.getString(cursor.getColumnIndexOrThrow("prazo_final"));
            boolean importante = cursor.getInt(cursor.getColumnIndexOrThrow("importancia")) == 1;

            cursor.close();
            return new Tarefas(id, descricao, dataInicio, prazoFinal, importante);
        }

        cursor.close();
        return null;
    }
}
