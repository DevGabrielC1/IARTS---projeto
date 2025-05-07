package com.example.projetotarefas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class TarefaBottomSheet extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate o layout do BottomSheet
        return inflater.inflate(R.layout.fragment_tarefa_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Configurar os elementos do BottomSheet, como botões e campos de texto
        Button btnSalvar = view.findViewById(R.id.btnSalvar);
        btnSalvar.setOnClickListener(v -> {
            // Ação para salvar a tarefa ou realizar a operação desejada
            dismiss();  // Fecha o BottomSheet
        });
    }
}
