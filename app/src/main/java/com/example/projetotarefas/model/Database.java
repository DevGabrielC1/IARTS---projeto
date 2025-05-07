
package com.example.projetotarefas.model;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.projetotarefas.ContratoTarefa;

// Classe auxiliar para gerenciar o banco de dados de tarefas
public class Database extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "lista_tarefas.db";
    private static final int VERSAO_BANCO = 1;

    // Comando SQL para criar a tabela de tarefas
    private static final String SQL_CRIAR_TABELA_TAREFAS =
            "CREATE TABLE " + ContratoTarefa.EntradaTarefa.NOME_TABELA + " (" +
                    ContratoTarefa.EntradaTarefa._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ContratoTarefa.EntradaTarefa.COLUNA_TAREFA + " TEXT NOT NULL, " +
                    ContratoTarefa.EntradaTarefa.COLUNA_CATEGORIA + " TEXT, " +
                    ContratoTarefa.EntradaTarefa.COLUNA_PRIORIDADE + " TEXT, " +
                    ContratoTarefa.EntradaTarefa.COLUNA_OBSERVACOES + " TEXT, " +
                    ContratoTarefa.EntradaTarefa.COLUNA_DATA_LIMITE + " TEXT, " +
                    ContratoTarefa.EntradaTarefa.COLUNA_HORA_LIMITE + " TEXT, " +
                    ContratoTarefa.EntradaTarefa.COLUNA_CONCLUIDA + " INTEGER DEFAULT 0);";

    public Database(Context context) {
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CRIAR_TABELA_TAREFAS);
    }

    // Retorna todas as tarefas do banco de dados
    public Cursor buscarTodasTarefas() {
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
    public void onUpgrade(SQLiteDatabase db, int versaoAntiga, int versaoNova) {

    }
}
