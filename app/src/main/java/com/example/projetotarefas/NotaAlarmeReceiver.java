package com.example.projetotarefas;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class NotaAlarmeReceiver extends BroadcastReceiver {

    public static final String CHANNEL_ID = "channel_tarefa_01";
    public static final String EXTRA_TASK_TITLE = "task_title";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("debug", "Recebendo alarme...");

        String titulo = intent.getStringExtra(EXTRA_TASK_TITLE);
        if (titulo == null || titulo.isEmpty()) {
            titulo = "Você tem uma tarefa agendada!";
        }

        // Cria canal de notificação se necessário
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Canal de Tarefas",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Notificações de lembrete de tarefas");
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
                Log.i("debug", "Canal criado");
            }
        }

        // Cria notificação
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification) // ou use outro ícone que tiver
                .setContentTitle("Lembrete de Tarefa")
                .setContentText(titulo)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManager nfm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (nfm != null) {
            nfm.notify((int) System.currentTimeMillis(), builder.build());
            Log.i("debug", "Notificação enviada com sucesso!");
        } else {
            Log.e("debug", "NotificationManager é null");
        }
    }
}
