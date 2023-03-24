package com.example.smartroom;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.OutputStream;
import java.nio.charset.Charset;

public class MsgSender extends Thread {
    private static final String TAG = "MsgSender";
    private final BluetoothSocket btSocket;
    private String msg;

    public MsgSender(BluetoothSocket btSocket, String msg) {
        this.btSocket = btSocket;
        this.msg = msg;
    }

    @Override
    public void run() {
        try {
            byte[] buffMsg = msg.getBytes(Charset.defaultCharset());
            OutputStream oStr = btSocket.getOutputStream();
            oStr.write(buffMsg);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }
}
