package com.example.bharath.swish;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class Bluetooth extends Service {


    InputStream mmInputStream;
    BluetoothSocket socket = null;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    int counter;
    volatile boolean stopWorker;

    final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    public Bluetooth() {
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Connect();
        Toast.makeText(getApplicationContext(),"Started bluetooth",Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }

    public void Connect(){
        BluetoothDevice device=null;
        for(BluetoothDevice t : mBluetoothAdapter.getBondedDevices()){
            if(t.getName().toString().equals("vengatesh")){
                device=t;
                Log.d("Found:","Gokul");
                break;
            }
        }
        UUID SERIAL_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); // bluetooth serial port service



        try {
            socket = device.createRfcommSocketToServiceRecord(SERIAL_UUID);
        } catch (Exception e) {
            Log.e("", "Error creating socket");}

        try {
            socket.connect();
            Log.e("Status:","Connected");
        } catch (IOException e) {
            Log.e("",e.getMessage());
            try {
                Log.e("Status:","trying fallback...");

                socket =(BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(device,1);
                socket.connect();

                Log.e("Status:","Connected");
            }
            catch (Exception e2) {
                Log.e("Status:", "Couldn't establish Bluetooth connection!");
            }
        }

        beginListenForData();

    }

    void beginListenForData()
    {
        final Handler handler = new Handler();
        final byte delimiter = 10; //This is the ASCII code for a newline character

        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable()
        {
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopWorker)
                {
                    try
                    {
                        mmInputStream = socket.getInputStream();
                        int bytesAvailable = mmInputStream.available();
                        if(bytesAvailable > 0)
                        {
                            byte[] packetBytes = new byte[bytesAvailable];
                            mmInputStream.read(packetBytes);
                            for(int i=0;i<bytesAvailable;i++)
                            {
                                byte b = packetBytes[i];
                                if(b == delimiter)
                                {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;

                                    handler.post(new Runnable()
                                    {
                                        public void run()
                                        {
                                            //Toast.makeText(getApplicationContext(),"Letter:"+data,Toast.LENGTH_SHORT).show();
                                            Intent ii=new Intent();
                                            ii.setAction(BLUETOOTH_SERVICE);
                                            ii.putExtra("bt",data);
                                            sendBroadcast(ii);
                                        }
                                    });
                                }
                                else
                                {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    }
                    catch (IOException ex)
                    {
                        stopWorker = true;
                    }
                }
            }
        });

        workerThread.start();
    }
}




