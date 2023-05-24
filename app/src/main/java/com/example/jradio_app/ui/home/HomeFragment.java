package com.example.jradio_app.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.jradio_app.databinding.FragmentHomeBinding;
import com.example.jradio_app.services.PlayerService;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final Context context = getContext();

        ToggleButton playerButton = binding.playerButtonToggle;
        playerButton.setTextOff("Играть");
        playerButton.setTextOn("Пауза");

        if(PlayerService.getIsPlaying()){
            playerButton.setChecked(true);
        }

        playerButton.setOnClickListener(v -> {
            if(PlayerService.getIsPrepared() || !PlayerService.getIsPlaying()) {
                assert context != null;
                Intent i = new Intent(context, PlayerService.class);
                i.putExtra("t", 0);
                context.startService(i);
            }
        });

        final Button playerButton2 = binding.playerButtonToggle2;

        playerButton2.setOnClickListener(v -> {
            assert context != null;
            Intent  i = new Intent(context, PlayerService.class);
            i.putExtra("t", 1);
            context.startService(i);

            playerButton.setChecked(false);

        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}