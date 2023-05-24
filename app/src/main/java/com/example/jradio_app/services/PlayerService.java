package com.example.jradio_app.services;

import android.app.PendingIntent;
import android.content.Intent;
//import android.media.AudioManager;
//import android.media.MediaPlayer;
import android.app.Service;
//import android.os.AsyncTask;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

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
    static ExoPlayer mediaPlayer;
    PlayerNotificationManager notificationManager;
    static boolean isStarted = false;
    static boolean isPrepared = false;
    static boolean isPlaying = false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        //mediaPlayer = new MediaPlayer();
        mediaPlayer = new ExoPlayer.Builder(this).build();
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
                return "<Media sub-Title>";
            }

            @Nullable
            @Override
            public Bitmap getCurrentLargeIcon(Player player, PlayerNotificationManager.BitmapCallback callback) {
                return null;
            }
        };
        PlayerNotificationManager.Builder playerNotificationManagerBuilder = new PlayerNotificationManager.Builder(this, 1, "player_channel");
        playerNotificationManagerBuilder.setMediaDescriptionAdapter(adapter);
        notificationManager = playerNotificationManagerBuilder.build();
        notificationManager.setPlayer(mediaPlayer);

        mediaPlayer.addListener(new Player.Listener() {
            @Override
            public void onIsPlayingChanged(boolean pl) {
                Player.Listener.super.onIsPlayingChanged(pl);
                isPlaying = pl;
            }
        });


        // mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        // Toast.makeText(this, "Служба создана", Toast.LENGTH_SHORT).show();
    }


    public void makePercentToast(int percent){
        Toast.makeText(this, percent, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int extra = intent.getIntExtra("t", 0);
        if(extra == 1 && !isStarted) {
            stopSelf();
        } else
        if(isStarted) {
            pauseTask(extra);
        } else {
            isStarted = true;
            playerPrepare();
        }


        return START_STICKY;
    }

    public void pauseTask(int task){
        if(isPrepared){
            if(task == 0){
                if(isPlaying){
                    mediaPlayer.pause();
                } else {
                    mediaPlayer.play();
                }
            } else {
                stopSelf();
            }
        }
    }
    @Override
    public void onDestroy() {
        notificationManager.setPlayer(null);
        mediaPlayer.release();
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