package com.example.projetotarefas;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.projetotarefas.fragments.DoneFragment;
import com.example.projetotarefas.fragments.ToDoFragment;

public class TarefaPagerAdapter extends FragmentStateAdapter {
    public TarefaPagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    @Override
    public Fragment createFragment(int position) {
        return position == 0 ? new ToDoFragment() : new DoneFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

