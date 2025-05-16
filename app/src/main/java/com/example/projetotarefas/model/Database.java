
package com.example.projetotarefas.model;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.projetotarefas.ContratoTarefa;

// Classe auxiliar para gerenciar o banco de dados de tarefas
public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tasklist.db";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TASK_TABLE =
            "CREATE TABLE " + ContratoTarefa.EntradaTarefa.NOME_TABELA + " (" +
                    ContratoTarefa.EntradaTarefa._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ContratoTarefa.EntradaTarefa.COLUNA_TAREFA + " TEXT NOT NULL, " +
                    ContratoTarefa.EntradaTarefa.COLUNA_OBSERVACOES+ " TEXT, " +
                    ContratoTarefa.EntradaTarefa.COLUNA_DATA_LIMITE + " TEXT, " +
                    ContratoTarefa.EntradaTarefa.COLUNA_HORA_LIMITE + " TEXT, " +
                    ContratoTarefa.EntradaTarefa.COLUNA_CONCLUIDA + " INTEGER DEFAULT 0);";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TASK_TABLE);
    }
    public Cursor getAllTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(
                ContratoTarefa.EntradaTarefa.NOME_TABELA,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

