package com.example.bharath.swish;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class sPhoto extends AppCompatActivity {


    android.hardware.Camera camera;
    Handler photo=null;
    int iD,time;
    Runnable rr;
    final CountDownTimer[] cT = new CountDownTimer[1];


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_photo);



        Intent i=getIntent();
        iD=Integer.parseInt(i.getStringExtra("iD").toString());
        time=Integer.parseInt(i.getStringExtra("time").toString());
        Log.d("Time:",""+time);


        final SurfaceView sv;
        sv=(SurfaceView)findViewById(R.id.surfaceView);


        camera= android.hardware.Camera.open(iD);

        photo=new Handler();
        final Handler mHandler=new Handler();

        rr=new Runnable() {
            @Override
            public void run() {
                camera.takePicture(null,null,jpegCallBack);
            }
        };

        final Runnable mRunnalbe=new Runnable() {
            @Override
            public void run() {


                try{

                    android.hardware.Camera.Parameters params = camera.getParameters();
                    params.setFocusMode(android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                    camera.setParameters(params);
                    camera.setPreviewDisplay(sv.getHolder());
                    camera.setDisplayOrientation(90);
                    camera.startPreview();

                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        };






        mHandler.postDelayed(mRunnalbe, 2000);
        countDown();

    }


    public void countDown(){


        Log.d("Time","Function");


        cT[0] =new CountDownTimer(time,1000) {
            @Override
            public void onTick(final long millisUntilFinished) {


                long t=millisUntilFinished/1000;
                Toast.makeText(getApplicationContext(),"Keep smiling for "+t+"sec..",Toast.LENGTH_SHORT).show();
                Log.d("Time",""+t);

            }

            @Override
            public void onFinish() {

                photo.postDelayed(rr,0);

            }
        }.start();


    }

    final android.hardware.Camera.PictureCallback jpegCallBack=new android.hardware.Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, android.hardware.Camera camera) {

            Toast.makeText(getApplicationContext(), "Photo taken baby..", Toast.LENGTH_SHORT).show();

            File pictureFile = getOutputMediaFile();
            Log.d("Time", "getting ouputmedia..");
            if (pictureFile == null) {
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                Log.d("MyCameraApp", "writing photo...");
                fos.close();
            } catch (FileNotFoundException e) {

            } catch (IOException e) {
            }

        }
    };





    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "Swish");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
            else{
                Log.d("MyCameraApp", "success");
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }

}
