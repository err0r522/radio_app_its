package com.example.jradio_app.services;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.app.Service;
import android.os.IBinder;
import android.widget.Toast;

public class PlayerService extends Service {
    public static final String SERVICE_ACTION = " com.example.jradio_app.services.PlayerService";
    final String audioUrl256 = "http://xn--80aibovgem6j.xn--p1ai:8000/live256";
    final String audioUrl128 = "http://xn--80aibovgem6j.xn--p1ai:8000/live128";
    MediaPlayer mediaPlayer;
    boolean isPlaying = false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        Toast.makeText(this, "Служба создана", Toast.LENGTH_SHORT).show();
    }


    public void makePercentToast(int percent){
        Toast.makeText(this, percent, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(isPlaying) stopSelf();
        isPlaying = true;
        mediaPlayer.setOnBufferingUpdateListener((mp, percent) -> {
            if(percent % 10 == 0){
                makePercentToast(percent);
            }
        });

        mediaPlayer.setOnPreparedListener(mp -> mediaPlayer.start());

        try {
            mediaPlayer.setDataSource(audioUrl256);
            mediaPlayer.prepareAsync();

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.release();
        Toast.makeText(this, "Служба остановлена", Toast.LENGTH_SHORT).show();
    }
}