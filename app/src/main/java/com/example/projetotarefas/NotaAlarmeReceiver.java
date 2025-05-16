package com.example.projetotarefas;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

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

        createNotificationChannel(context);

        // Construindo a notificação
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification) // ícone da notificação, substitua se necessário
                .setContentTitle("Lembrete de Tarefa")
                .setContentText(titulo)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);  // som, vibração e luz padrão

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // Checa permissão para notificações no Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) !=
                    android.content.pm.PackageManager.PERMISSION_GRANTED) {
                Log.e("debug", "Permissão para notificações negada!");
                return; // Não tentar mostrar notificação sem permissão
            }
        }

        int notificationId = (int) System.currentTimeMillis();
        notificationManager.notify(notificationId, builder.build());
        Log.i("debug", "Notificação enviada com sucesso! ID = " + notificationId);
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Canal de Tarefas",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Notificações de lembrete de tarefas");

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
                Log.i("debug", "Canal criado ou já existente");
            } else {
                Log.e("debug", "NotificationManager é null");
            }
        }
    }
}
