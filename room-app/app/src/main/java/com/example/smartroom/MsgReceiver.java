package com.example.smartroom;

import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class MsgReceiver extends Thread {
    private static final String TAG = "MsgReceiver";
    private final BluetoothSocket btSocket;
    String msg;

    public MsgReceiver(BluetoothSocket btSocket) {
        this.btSocket = btSocket;
    }

    @Override
    public void run() {
        while(true) {
            try {
                Log.d(TAG, "1");
                InputStream iStr = btSocket.getInputStream();
                Log.d(TAG, "2");
                InputStreamReader isr = new InputStreamReader(iStr, StandardCharsets.UTF_8);
                Log.d(TAG, "3");
                BufferedReader br = new BufferedReader(isr);
                Log.d(TAG, "4");
                br.lines().forEach(line -> Log.i(TAG, line));
                Log.d(TAG, "5");
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
    }
}
