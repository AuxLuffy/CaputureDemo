package com.example.camera.util;

import android.content.Context;
import android.media.AudioManager;

import static android.content.Context.AUDIO_SERVICE;

public class AudioUtil {
    public static void setAudioManage(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
        audioManager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, 0, 0);
        audioManager.setStreamVolume(AudioManager.STREAM_DTMF, 0, 0);
        audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 0, 0);
        audioManager.setStreamVolume(AudioManager.STREAM_RING, 0, 0);
    }

    public static void mutePhone(Context context) {
        //mute phone
        AudioManager audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
        audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
    }

    public static void openPhoneVol(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    try {
                        wait(500);
                    } catch (InterruptedException e) {
                    } finally {
                        //unmute the phone
                        AudioManager audioManager1 = (AudioManager) context.getSystemService(AUDIO_SERVICE);
                        audioManager1.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    }
                }
            }
        }).start();
    }
}
