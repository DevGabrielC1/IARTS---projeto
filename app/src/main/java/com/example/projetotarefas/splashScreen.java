package com.example.projetotarefas;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.ImageDecoder;
import android.graphics.drawable.AnimatedImageDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Window;
import android.widget.ImageView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.example.projetotarefas.telaLogin;

public class splashScreen extends AppCompatActivity {

    private ImageView ivSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ivSplash = findViewById(R.id.ivSplash);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            try {
                ImageDecoder.Source source = ImageDecoder.createSource(getResources(), R.drawable.full_final);
                @SuppressLint("WrongThread") Drawable drawable = ImageDecoder.decodeDrawable(source);
                if (drawable instanceof AnimatedImageDrawable) {
                    AnimatedImageDrawable animatedDrawable = (AnimatedImageDrawable) drawable;
                    ivSplash.setImageDrawable(animatedDrawable);
                    animatedDrawable.start();
                } else {
                    ivSplash.setImageDrawable(drawable); // fallback
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Fallback: exibe imagem estática
            ivSplash.setImageResource(R.drawable.full_final);
        }

        // Delay para ir à tela principal
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(splashScreen.this, telaLogin.class));
            finish();
        }, 3000); // 3 segundos
    }
}