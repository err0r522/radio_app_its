package com.example.jradio_app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.jradio_app.databinding.ActivityMainBinding;
import com.example.jradio_app.services.PlayerService;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_media, R.id.navigation_home, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        channelsCreator(binding.getRoot().getContext());
    }

    public void channelsCreator(Context ctx){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            NotificationManager manager = getSystemService(NotificationManager.class);
            NotificationChannel player_channel = new NotificationChannel(
                    "player_channel", "Проигрыватель", NotificationManager.IMPORTANCE_LOW
            );
            player_channel.setDescription("Уведомления проигрывателя");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                player_channel.setBlockable(false);
            }
            manager.createNotificationChannel(player_channel);

            PlayerService.createPlayer(ctx);

            Intent i = new Intent(ctx, PlayerService.class);
            i.putExtra("t", 0);

            startForegroundService(i);

        }


    }

}