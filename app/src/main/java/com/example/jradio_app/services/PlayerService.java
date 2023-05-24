package com.example.jradio_app.services;

import android.content.Intent;
//import android.media.AudioManager;
//import android.media.MediaPlayer;
import android.app.Service;
//import android.os.AsyncTask;
import android.os.IBinder;
import android.widget.Toast;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;

//import java.io.IOException;

public class PlayerService extends Service {
    public static final String SERVICE_ACTION = " com.example.jradio_app.services.PlayerService";
    final String audioUrl256 = "http://xn--80aibovgem6j.xn--p1ai:8000/live256";
    final String audioUrl128 = "http://xn--80aibovgem6j.xn--p1ai:8000/live128";

    final MediaItem item256 = MediaItem.fromUri(audioUrl256);
    final MediaItem item128 = MediaItem.fromUri(audioUrl128);
    // static MediaPlayer mediaPlayer;
    static ExoPlayer mediaPlayer;
    boolean isStarted = false;
    boolean isPrepared = false;
    boolean isPlaying = false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        //mediaPlayer = new MediaPlayer();
        mediaPlayer = new ExoPlayer.Builder(this).build();

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
                isPlaying = !isPlaying;
            } else {
                stopSelf();
            }
        }
    }
    @Override
    public void onDestroy() {
        mediaPlayer.release();
        // Toast.makeText(this, "Служба остановлена", Toast.LENGTH_SHORT).show();
    }

    public void playerPrepare() {
        mediaPlayer.setMediaItem(item256);
        mediaPlayer.prepare();
        mediaPlayer.play();
        isPrepared = true;
        isPlaying = true;
    }
}