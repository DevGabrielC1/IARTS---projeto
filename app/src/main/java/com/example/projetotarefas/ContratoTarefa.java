package com.example.projetotarefas;

import android.provider.BaseColumns;


public class ContratoTarefa {

    // Construtor privado para evitar instâncias
    private ContratoTarefa() {
    }

    // Classe interna que define os nomes da tabela e das colunas
    public static final class EntradaTarefa implements BaseColumns {
        public static final String NOME_TABELA = "tarefas";
        public static final String COLUNA_TAREFA = "tarefa";
        public static final String COLUNA_CATEGORIA = "categoria";
        public static final String COLUNA_PRIORIDADE = "prioridade";
        public static final String COLUNA_OBSERVACOES = "observacoes";
        public static final String COLUNA_DATA_LIMITE = "data_limite";
        public static final String COLUNA_HORA_LIMITE = "hora_limite";
        public static final String COLUNA_CONCLUIDA = "concluida";
    }
}