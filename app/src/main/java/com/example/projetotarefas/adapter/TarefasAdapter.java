package com.example.projetotarefas.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetotarefas.R;
import com.example.projetotarefas.model.Tarefas;
import com.example.projetotarefas.utils.DataUtils;

import java.util.List;

public class TarefasAdapter extends RecyclerView.Adapter<TarefasAdapter.TarefaViewHolder> {

    private List<Tarefas> listaTarefas;
    private final Context context;
    private final OnTarefaClickListener listener;

    public interface OnTarefaClickListener {
        void onEditar(Tarefas tarefa);
        void onExcluir(Tarefas tarefa);
        void onMarcarConcluida(Tarefas tarefa);
    }

    public TarefasAdapter(Context context, List<Tarefas> lista, OnTarefaClickListener listener) {
        this.context = context;
        this.listaTarefas = lista;
        this.listener = listener;
        DataUtils.ordenarTarefasPorImportanciaEData(this.listaTarefas);
    }

    public void atualizarLista(List<Tarefas> novaLista) {
        this.listaTarefas = novaLista;
        DataUtils.ordenarTarefasPorImportanciaEData(this.listaTarefas);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TarefaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.item_tarefa, parent, false);
        return new TarefaViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull TarefaViewHolder holder, int position) {
        Tarefas tarefa = listaTarefas.get(position);

        holder.txtDescricao.setText(tarefa.getDescricao());
        holder.txtPrazo.setText("Prazo: " + tarefa.getPrazoFinal());

        long diasRestantes = DataUtils.calcularDiasRestantes(tarefa.getPrazoFinal());
        holder.txtDiasRestantes.setText("Faltam: " + diasRestantes + " dias");

        holder.txtImportante.setText(tarefa.isImportante() ? "★ Importante" : "");

        holder.btnConcluir.setText(tarefa.isConcluida() ? "Desfazer" : "Concluir");

        holder.btnEditar.setOnClickListener(v -> listener.onEditar(tarefa));
        holder.btnExcluir.setOnClickListener(v -> listener.onExcluir(tarefa));
        holder.btnConcluir.setOnClickListener(v -> listener.onMarcarConcluida(tarefa));
    }

    @Override
    public int getItemCount() {
        return listaTarefas.size();
    }

    static class TarefaViewHolder extends RecyclerView.ViewHolder {
        TextView txtDescricao, txtPrazo, txtDiasRestantes, txtImportante;
        Button btnConcluir;
        ImageButton btnEditar, btnExcluir;

        public TarefaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDescricao = itemView.findViewById(R.id.txtDescricao);
            txtPrazo = itemView.findViewById(R.id.txtPrazo);
            txtDiasRestantes = itemView.findViewById(R.id.txtDiasRestantes);
            txtImportante = itemView.findViewById(R.id.txtImportante);
            btnConcluir = itemView.findViewById(R.id.btnConcluir);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnExcluir = itemView.findViewById(R.id.btnExcluir);
        }
    }
}
