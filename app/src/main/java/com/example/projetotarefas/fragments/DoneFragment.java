package com.example.projetotarefas.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetotarefas.R;
import com.example.projetotarefas.adapter.TarefasAdapter;
import com.example.projetotarefas.model.Tarefas;
import com.example.projetotarefas.model.TarefasDAO;

import java.util.ArrayList;
import java.util.List;

public class DoneFragment extends Fragment {

    private RecyclerView recyclerView;
    private TarefasAdapter adapter;
    private TarefasDAO dao;

    public DoneFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_done, container, false);

        recyclerView = view.findViewById(R.id.recyclerDone);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dao = new TarefasDAO(getContext());
        List<Tarefas> tarefas = filtrarConcluidas(dao.listar());

        adapter = new TarefasAdapter(getContext(), tarefas, new TarefasAdapter.OnTarefaClickListener() {
            @Override
            public void onEditar(Tarefas tarefa) {
                // TODO: abrir modal de edição
            }

            @Override
            public void onExcluir(Tarefas tarefa) {
                dao.deletar(tarefa.getId());
                atualizarLista();
            }

            @Override
            public void onMarcarConcluida(Tarefas tarefa) {
                tarefa.setConcluida(false);
                dao.atualizar(tarefa);
                atualizarLista();
            }
        });

        recyclerView.setAdapter(adapter);
        return view;
    }

    private List<Tarefas> filtrarConcluidas(List<Tarefas> todas) {
        List<Tarefas> resultado = new ArrayList<>();
        for (Tarefas t : todas) {
            if (t.isConcluida()) resultado.add(t);
        }
        return resultado;
    }

    private void atualizarLista() {
        adapter.atualizarLista(filtrarConcluidas(dao.listar()));
    }
}
