// Classe que representa uma Tarefa
public class Tarefas {
    private String nomeTarefa;
    private String prioridade;
    private String categoria;
    private String dataLimite;
    private String horaLimite;
    private boolean concluida;

    // Construtor
    public Tarefas(String nomeTarefa, String prioridade, String categoria, String dataLimite, String horaLimite, String string, boolean concluida) {
        this.nomeTarefa = nomeTarefa;
        this.prioridade = prioridade;
        this.categoria = categoria;
        this.dataLimite = dataLimite;
        this.horaLimite = horaLimite;
        this.concluida = concluida;
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

    public boolean isConcluida() {
        return concluida;
    }

    public void setConcluida(boolean concluida) {
        this.concluida = concluida;
    }
}