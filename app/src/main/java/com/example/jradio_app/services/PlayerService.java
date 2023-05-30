package com.example.jradio_app.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
//import android.media.AudioManager;
//import android.media.MediaPlayer;
import android.app.Service;
//import android.os.AsyncTask;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.jradio_app.ui.home.HomeFragment;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;

//import java.io.IOException;

public class PlayerService extends Service {
    public static final String SERVICE_ACTION = " com.example.jradio_app.services.PlayerService";
    final String audioUrl256 = "http://xn--80aibovgem6j.xn--p1ai:8000/live256";
    final String audioUrl128 = "http://xn--80aibovgem6j.xn--p1ai:8000/live128";

    final MediaItem item256 = MediaItem.fromUri(audioUrl256);
    final MediaItem item128 = MediaItem.fromUri(audioUrl128);
    // static MediaPlayer mediaPlayer;
    static ExoPlayer mediaPlayer = null;
    static PlayerNotificationManager notificationManager = null;
    static Notification playerNotification = null;
    static boolean wf = false;

    static boolean isStarted = false;
    static boolean isPrepared = false;
    static boolean isPlaying = false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void createPlayer(Context ctx){
        mediaPlayer = new ExoPlayer.Builder(ctx).build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            if (notificationManager == null) {
                createNotificationManager(ctx);
            }

            notificationManager.setPlayer(mediaPlayer);
        }
    }

    public static void createNotificationManager(Context ctx){
        PlayerNotificationManager.MediaDescriptionAdapter adapter = new PlayerNotificationManager.MediaDescriptionAdapter() {

            @Override
            public CharSequence getCurrentContentTitle(Player player) {
                return "Юное Радио";
            }

            @Nullable
            @Override
            public PendingIntent createCurrentContentIntent(Player player) {
                return null;
            }

            @Nullable
            @Override
            public CharSequence getCurrentContentText(Player player) {
                return "Слушайте Юное Радио!";
            }

            @Nullable
            @Override
            public Bitmap getCurrentLargeIcon(Player player, PlayerNotificationManager.BitmapCallback callback) {
                return null;
            }

        };


        PlayerNotificationManager.Builder playerNotificationManagerBuilder = new PlayerNotificationManager.Builder(ctx, 1, "player_channel");
        playerNotificationManagerBuilder.setMediaDescriptionAdapter(adapter);
        playerNotificationManagerBuilder.setNotificationListener(new PlayerNotificationManager.NotificationListener() {
            @Override
            public void onNotificationPosted(int notificationId, Notification notification, boolean ongoing) {
                PlayerNotificationManager.NotificationListener.super.onNotificationPosted(notificationId, notification, ongoing);
                playerNotification = notification;
            }
        });

        mediaPlayer.addListener(new Player.Listener() {
            @Override
            public void onIsPlayingChanged(boolean pl) {
                Player.Listener.super.onIsPlayingChanged(pl);
                isPlaying = pl;
                if(!isPlaying) mediaPlayer.stop();

                Intent i = new Intent(ctx, HomeFragment.class);
                i.putExtra("t", 1);

                ctx.startActivity(i);

            }
        });

        notificationManager = playerNotificationManagerBuilder.build();
        notificationManager.setPlayer(mediaPlayer);

    }

    @Override
    public void onCreate() {
        //mediaPlayer = new MediaPlayer();


        // mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        // Toast.makeText(this, "Служба создана", Toast.LENGTH_SHORT).show();

        if (mediaPlayer == null){
            createPlayer(this);
        }
    }


    public void makePercentToast(int percent){
        Toast.makeText(this, percent, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && playerNotification != null && !wf) {
            startForeground(1, playerNotification);
            wf = true;
        }

        int extra = intent.getIntExtra("t", 0);
        switch (extra){
            case 1:
                if(!isStarted){
                    isStarted = true;
                    playerPrepare();
                } else {
                    pauseTask(extra);
                }
                break;
            case 2:
                stopSelf();
        }

        return START_STICKY;
    }

    public void pauseTask(int task){
        if(isPrepared){
            if(isPlaying){
                mediaPlayer.stop();
            } else {
                mediaPlayer.play();
            }
        }
    }
    @Override
    public void onDestroy() {
        wf = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(true);
        }
        playerNotification = null;

        notificationManager.setPlayer(null);
        mediaPlayer.release();
        mediaPlayer = null;
        isStarted = false;
        isPrepared = false;
        isPlaying = false;
        // Toast.makeText(this, "Служба остановлена", Toast.LENGTH_SHORT).show();

    }

    public void playerPrepare() {
        mediaPlayer.setMediaItem(item256);
        mediaPlayer.prepare();
        mediaPlayer.play();
        isPrepared = true;
        isPlaying = true;
    }

    public static boolean getIsPlaying(){
        return isPlaying;
    }

    public static boolean getIsPrepared(){
        return isPrepared;
    }
}