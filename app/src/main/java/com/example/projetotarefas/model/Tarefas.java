package com.example.projetotarefas.model;

public class Tarefas {
    private int id;
    private String descricao;
    private String dataInicio;   // formato: "yyyy-MM-dd"
    private String prazoFinal;   // formato: "yyyy-MM-dd"
    private boolean importante;  // true = estrela marcada
    private boolean concluida;   // true = tarefa feita

    public Tarefas(int id, String descricao, String dataInicio, String prazoFinal, boolean importante, boolean concluida) {
        this.id = id;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.prazoFinal = prazoFinal;
        this.importante = importante;
        this.concluida = concluida;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getDataInicio() { return dataInicio; }
    public void setDataInicio(String dataInicio) { this.dataInicio = dataInicio; }

    public String getPrazoFinal() { return prazoFinal; }
    public void setPrazoFinal(String prazoFinal) { this.prazoFinal = prazoFinal; }

    public boolean isImportante() { return importante; }
    public void setImportante(boolean importante) { this.importante = importante; }

    public boolean isConcluida() { return concluida; }
    public void setConcluida(boolean concluida) { this.concluida = concluida; }
}
