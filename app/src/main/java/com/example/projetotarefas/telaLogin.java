package com.example.projetotarefas;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.preference.PreferenceManager;

public class telaLogin extends AppCompatActivity {

    public EditText userInfo;
    public EditText password;
    public TextView usuarioinvalido;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.i("Debug", "minha msg");

        userInfo = findViewById(R.id.editLogin);
        password = findViewById(R.id.editTextTextPassword);
        usuarioinvalido = findViewById(R.id.usuarioInvalido);

        // 🔽 Recupera nome salvo e preenche no campo de login
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String usuarioSalvo = sharedPreferences.getString("usuario_salvo", "");
        userInfo.setText(usuarioSalvo); // Preenche automaticamente

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void login(View view) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String senhaPadrao = sharedPreferences.getString("senha_padrao", "admin");

        if (
                userInfo.getText().toString().equals("admin") &&
                        password.getText().toString().equals(senhaPadrao)
        ) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("usuario_salvo", userInfo.getText().toString());
            editor.apply();

            Intent intent = new Intent(telaLogin.this, MainActivity.class);
            intent.putExtra("usuario", userInfo.getText().toString());

            startActivity(intent);
            finish();
        } else {
            usuarioinvalido.setVisibility(View.VISIBLE);
        }
    }
    private void entrarNaMain() {
        Intent intent = new Intent(telaLogin.this, MainActivity.class);
        startActivity(intent);
    }
}