package com.example.projetotarefas;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;

import com.example.projetotarefas.telaTarefas;

public class telaLogin extends AppCompatActivity {

    public EditText userInfo;
    public EditText password;
    public TextView usuarioinvalido;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Log.i("Debug", "minha msg");

        userInfo = findViewById(R.id.editLogin);
        password = findViewById(R.id.editTextTextPassword);
        usuarioinvalido = findViewById(R.id.usuarioInvalido);

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void login(View view){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String senhaPadrao = sharedPreferences.getString("senha_padrao", "admin");

        if(
                userInfo.getText().toString().equals("admin")
                        && password.getText().toString().equals(senhaPadrao)
        ){
            Intent intent = new Intent(telaLogin.this, telaTarefas.class);
            intent.putExtra("usuario", userInfo.getText().toString());

            startActivity(intent);
        }else{
            usuarioinvalido.setVisibility(View.VISIBLE);
        }
    }
}