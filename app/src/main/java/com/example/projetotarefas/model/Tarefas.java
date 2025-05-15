package com.example.projetotarefas.model;

// Classe que representa uma Tarefa
public class Tarefas {
    private String nomeTarefa;
    private String prioridade;
    private String categoria;
    private String dataLimite;
    private String horaLimite;

    private String observacoes;

    // Construtor
    public Tarefas(String nomeTarefa, String dataLimite, String horaLimite, String observacoes) {
        this.nomeTarefa = nomeTarefa;
        this.dataLimite = dataLimite;
        this.horaLimite = horaLimite;
        this.observacoes = observacoes;
    }

    public String getNomeTarefa() {
        return nomeTarefa;
    }

    public void setNomeTarefa(String nomeTarefa) {
        this.nomeTarefa = nomeTarefa;
    }

    public String getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(String prioridade) {
        this.prioridade = prioridade;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDataLimite() {
        return dataLimite;
    }

    public void setDataLimite(String dataLimite) {
        this.dataLimite = dataLimite;
    }

    public String getHoraLimite() {
        return horaLimite;
    }

    public void setHoraLimite(String horaLimite) {
        this.horaLimite = horaLimite;
    }

    public void setObservacoes(String observacoes){
        this.observacoes = observacoes;
    }

    public String getObservacoes(){
        return observacoes;
    }


}