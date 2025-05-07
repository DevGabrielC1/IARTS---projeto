package com.example.projetotarefas;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class telaTarefas extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private TarefaPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configurar abas
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        pagerAdapter = new TarefaPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(position == 0 ? "To Do" : "Done")
        ).attach();

        // FAB "+"
        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(view -> {
            // Mostrar BottomSheet com lista de tarefas ou formulário de adicionar
            TarefaBottomSheet dialog = new TarefaBottomSheet();
            dialog.show(getSupportFragmentManager(), "TarefaBottomSheet");
        });
    }
}