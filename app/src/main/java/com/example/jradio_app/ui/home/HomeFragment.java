package com.example.jradio_app.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.jradio_app.databinding.FragmentHomeBinding;
import com.example.jradio_app.services.PlayerService;

import java.util.concurrent.atomic.AtomicBoolean;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final Context context = getContext();



        final ToggleButton playerButton = binding.playerButtonToggle;
        playerButton.setTextOff("Играть");
        playerButton.setTextOn("Пауза");

        playerButton.setOnClickListener(v -> {
            assert context != null;
            Intent i = new Intent(context, PlayerService.class);
            i.putExtra("t", 0);

            context.startService(i);

        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}