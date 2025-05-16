package com.example.projetotarefas;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class ConfirmarExclusao extends DialogFragment {

    public interface ConfirmDeleteListener {
        void onConfirm();
    }

    private ConfirmDeleteListener listener;

    public void setConfirmarDeleteListener(ConfirmDeleteListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.delete_task_modal, null);

        Button btnCancelar = view.findViewById(R.id.buttonCancel);
        Button btnExcluir = view.findViewById(R.id.buttonConfirm);

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(view)
                .create();

        btnCancelar.setOnClickListener(v -> dialog.dismiss());

        btnExcluir.setOnClickListener(v -> {
            if (listener != null) listener.onConfirm();
            dialog.dismiss();
        });

        return dialog;
    }
}
