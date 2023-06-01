package com.mics.jradio_app.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mics.jradio_app.databinding.FragmentHomeBinding;
import com.mics.jradio_app.services.PlayerService;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private Context context;
    private BroadcastReceiver reciever;
    static boolean firstServiceStart = true;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        context = getContext();

        assert context != null;

        //final ToggleButton playerButton = binding.playerButtonToggle;
        final ImageView playerButton = binding.playerImage;

        /*playerButton.setText("Играть");
        playerButton.setTextOff("Играть");
        playerButton.setTextOn("Пауза");
        playerButton.setChecked(PlayerService.getIsPlaying());*/

        playerButton.setActivated(PlayerService.getIsPlaying());

        /* reciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                playerButton.setChecked(intent.getBooleanExtra("status", false));
            }
        }; */

        playerButton.setOnClickListener(v -> {
            playerButton.setActivated(!playerButton.isActivated());

            if(firstServiceStart){
                firstServiceStart = false;
                firstStart(context);
            }

            Intent i = new Intent(context, PlayerService.class);
            i.putExtra("t", 1);

            context.startService(i);

        });

        PlayerService.setStatusChangeListener(playerButton::setActivated);

        // LocalBroadcastManager.getInstance(context).registerReceiver(reciever, new IntentFilter("play_status_change"));



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        // LocalBroadcastManager.getInstance(context).unregisterReceiver(reciever);
    }

    public void firstStart(Context ctx){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            PlayerService.createPlayer(ctx);

            Intent i = new Intent(ctx, PlayerService.class);
            i.putExtra("t", 0);

            ctx.startForegroundService(i);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        PlayerService.unsetStatusChangeListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(binding != null){
            PlayerService.setStatusChangeListener(binding.playerImage::setActivated);
            binding.playerImage.setActivated(PlayerService.getIsPlaying());
        }

    }
}