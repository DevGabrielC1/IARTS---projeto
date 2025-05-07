package com.example.projetotarefas.utils;

import com.example.projetotarefas.model.Tarefas;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DataUtils {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static long calcularDiasRestantes(String dataFinalStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dataFinal = sdf.parse(dataFinalStr);
            Date hoje = new Date();

            long diffMillis = dataFinal.getTime() - hoje.getTime();
            return TimeUnit.DAYS.convert(diffMillis, TimeUnit.MILLISECONDS);

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static void ordenarTarefasPorImportanciaEData(List<Tarefas> tarefas) {
        Collections.sort(tarefas, new Comparator<Tarefas>() {
            @Override
            public int compare(Tarefas t1, Tarefas t2) {
                // Primeiro: comparar importância (importante = true vem primeiro)
                if (t1.isImportante() != t2.isImportante()) {
                    return t1.isImportante() ? -1 : 1;
                }

                // Segundo: comparar data de prazo final
                try {
                    Date data1 = sdf.parse(t1.getPrazoFinal());
                    Date data2 = sdf.parse(t2.getPrazoFinal());
                    return data1.compareTo(data2); // mais próxima vem antes
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });
    }


}