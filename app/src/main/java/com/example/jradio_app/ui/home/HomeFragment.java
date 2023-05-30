package com.example.jradio_app.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.jradio_app.databinding.FragmentHomeBinding;
import com.example.jradio_app.services.PlayerService;

import java.util.concurrent.atomic.AtomicBoolean;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private Context context;
    private BroadcastReceiver reciever;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        context = getContext();

        assert context != null;

        final ToggleButton playerButton = binding.playerButtonToggle;
        playerButton.setTextOff("Играть");
        playerButton.setTextOn("Пауза");

        reciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                playerButton.setChecked(intent.getBooleanExtra("status", false));
            }
        };

        playerButton.setOnClickListener(v -> {

            Intent i = new Intent(context, PlayerService.class);
            i.putExtra("t", 1);

            context.startService(i);

        });


        LocalBroadcastManager.getInstance(context).registerReceiver(reciever, new IntentFilter("play_status_change"));



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        LocalBroadcastManager.getInstance(context).unregisterReceiver(reciever);
    }

}