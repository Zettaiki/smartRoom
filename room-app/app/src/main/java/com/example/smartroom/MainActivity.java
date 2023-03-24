package com.example.smartroom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;

import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";
    private BluetoothSocket btSocket;
    private SeekBar blindsBar;
    private Button lightBtn;
    private Button manualCtrl;
    protected int isManOn = 0;
    protected int isLightOn = 0;
    protected int blindValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);
                return;
            }
        }

        BluetoothManager bluetoothManager = getSystemService(BluetoothManager.class);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        Set<BluetoothDevice> connectedDevices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : connectedDevices) {
            if (device.getType() == BluetoothDevice.DEVICE_TYPE_CLASSIC) {
                if(device.getName().equals("RoomController")) {
                    ConnectThread connectThread = new ConnectThread(device);
                    connectThread.start();
                    try {
                        connectThread.join();
                    } catch (InterruptedException e) {
                        Log.e(TAG, e.toString());
                    }
                    btSocket = connectThread.getBtSocket();
                }
            }
        }

        this.manualCtrl = (Button) findViewById(R.id.btn_control);
        this.lightBtn = (Button) findViewById(R.id.btn_light);
        this.blindsBar = (SeekBar) findViewById(R.id.seek_blinds);

        this.manualCtrl.setOnClickListener(v -> {
            if(this.manualCtrl.getText().toString().equals("off")) {
                this.isManOn = 1;
                new MsgSender(btSocket, generateMsg()).start();
                this.manualCtrl.setText("on");
            } else {
                this.isManOn = 0;
                new MsgSender(btSocket, generateMsg()).start();
                this.manualCtrl.setText("off");
            }
        });

        this.lightBtn.setOnClickListener(v -> {
            if(this.lightBtn.getText().toString().equals("off")) {
                this.isLightOn = 1;
                new MsgSender(btSocket, generateMsg()).start();
                this.lightBtn.setText("on");
            } else {
                this.isLightOn = 0;
                new MsgSender(btSocket, generateMsg()).start();
                this.lightBtn.setText("off");
            }
        });

        this.blindsBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                updateBlindValue(seekBar.getProgress());
                new MsgSender(btSocket, generateMsg()).start();
            }
        });

        new MsgSender(btSocket, generateMsg()).start();
    }

    protected String generateMsg() {
        return  "control=" + this.isManOn +
                " light=" + this.isLightOn +
                " blinds=" + this.blindValue;
    }

    protected void updateBlindValue(int seekBarValue) {
        this.blindValue = seekBarValue;
    }
}