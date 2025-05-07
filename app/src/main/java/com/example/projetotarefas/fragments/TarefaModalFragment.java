package com.example.projetotarefas.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.projetotarefas.R;
import com.example.projetotarefas.model.Tarefas;
import com.example.projetotarefas.model.TarefasDAO;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;

public class TarefaModalFragment extends Fragment {

    private TextInputEditText etDataInicio, etDataFinal;
    private ImageView ivImportancia;
    private Button btnSalvar;
    private TarefasDAO tarefasDAO;
    private boolean isImportante = false;

    public TarefaModalFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Infla o layout
        return inflater.inflate(R.layout., container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializar elementos
        etDataInicio = view.findViewById(R.id.etDataInicio);
        etDataFinal = view.findViewById(R.id.etDataFinal);
        ivImportancia = view.findViewById(R.id.ivImportancia);
        btnSalvar = view.findViewById(R.id.btnSalvar);

        // Inicializar DAO
        tarefasDAO = new TarefasDAO(getContext());

        // Configurar Data Picker para Data Início
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        final MaterialDatePicker<Long> datePicker = builder.build();

        etDataInicio.setOnClickListener(v -> datePicker.show(getChildFragmentManager(), "start_date_picker"));
        datePicker.addOnPositiveButtonClickListener(selection -> {
            etDataInicio.setText(datePicker.getHeaderText());
        });

        // Configurar Data Picker para Data Prazo Final
        etDataFinal.setOnClickListener(v -> {
            MaterialDatePicker<Long> datePickerFinal = builder.build();
            datePickerFinal.show(getChildFragmentManager(), "end_date_picker");
            datePickerFinal.addOnPositiveButtonClickListener(selection -> {
                etDataFinal.setText(datePickerFinal.getHeaderText());
            });
        });

        // Alternar estado de importância
        ivImportancia.setOnClickListener(v -> {
            isImportante = !isImportante;
            ivImportancia.setImageResource(isImportante ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline);
        });

        // Configurar o botão salvar
        btnSalvar.setOnClickListener(v -> salvarTarefa());
    }

    private void salvarTarefa() {
        // Coletar dados do formulário
        String descricao = "Nova Tarefa"; // Definir conforme necessário
        String dataInicio = etDataInicio.getText().toString().trim();
        String prazoFinal = etDataFinal.getText().toString().trim();

        if (descricao.isEmpty() || dataInicio.isEmpty() || prazoFinal.isEmpty()) {
            // Mostrar mensagem de erro se faltar algum dado
            return;
        }

        Tarefas tarefa = new Tarefas(0, descricao, dataInicio, prazoFinal, isImportante);
        tarefasDAO.inserir(tarefa);

        dismiss();
    }
}
