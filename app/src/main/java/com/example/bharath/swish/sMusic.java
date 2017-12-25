package com.example.bharath.swish;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;

public class sMusic extends AppCompatActivity {


    LST header, footer;
    Animation move1, move2;
    private MediaPlayer mMediaPlayer;
    BtReceiver btrec;
    Handler mHandler = null;
    int songIndex=0;
    ImageView pp;
    View selection;
    Drawable dd;
    int length;
    HashMap<String, String> song = new HashMap<String, String>();
    String []StringArray=new String[1000];
    final HashMap<String, String> action = new HashMap<String, String>();
    final int[] i = {10};
    int childIndex=0;
    boolean playState=true;
    RelativeLayout rel;
    CardView v0;
    ListView songs;
    TextView selectedSong;
    LinearLayout footerLayout, headerLayout;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_music);

        header = new LST(this);
        footer = new LST(this);
        header.setLetterSpacing(10);
        footer.setLetterSpacing(10);
        header.setTextColor(Color.parseColor("#000000"));
        footer.setTextColor(Color.parseColor("#000000"));
        header.setTextSize(25);
        footer.setTextSize(25);
        header.setText("###music###");
        footer.setText("Recognising....");
        footerLayout = (LinearLayout) findViewById(R.id.footerLayout);
        headerLayout = (LinearLayout) findViewById(R.id.headerLayout);
        rel = (RelativeLayout) findViewById(R.id.rel);
        headerLayout.addView(header);
        footerLayout.addView(footer);
        selectedSong=(TextView)findViewById(R.id.selectedSong);
        songs=(ListView)findViewById(R.id.songs);


        //getting the music from SD card....
        StringArray=getMusic();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, StringArray);
        songs.setAdapter(adapter);

        //Setting the cardviews...
        mMediaPlayer=new MediaPlayer();

        v0 = (CardView) findViewById(R.id.view0);

        v0.setElevation(10);



        btrec = new BtReceiver();
        IntentFilter iff = new IntentFilter();
        iff.addAction(Bluetooth.BLUETOOTH_SERVICE);
        registerReceiver(btrec, iff);

        //Starting timer...
        mHandler = new Handler();
        timer();


        animateText();

        selection=v0.getChildAt(childIndex);
        dd=selection.getBackground();

        highlight();
        nextSong();


    }

    public void nextSong(){
        selectedSong.setText("Current Song:"+StringArray[songIndex]);
    }

    public void highlight(){
        selection=v0.getChildAt(childIndex);
        pp=(ImageView)v0.getChildAt(1);
        dd=selection.getBackground();
        selection.setBackgroundColor(Color.parseColor("#BDBDBD"));
    }

    public void animateText() {


        move1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move);
        move2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move1);
        move1.setDuration(1000);
        footer.startAnimation(move1);


        move1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                move2.setDuration(700);
                footer.startAnimation(move2);
                footer.setLetterSpacing(i[0]);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {


            }
        });


        move2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                move1.setDuration(1000);
                i[0] = 10;
                footer.setLetterSpacing(i[0]);
                footer.startAnimation(move1);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    public void timer() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (true) {
                    try {
                        Thread.sleep(1000);
                        mHandler.post(new Runnable() {

                            @Override
                            public void run() {

                                i[0] += 5;

                            }
                        });
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
            }
        }).start();


    }

    public String[] getMusic(){


        Cursor mCursor;
        String[] STAR = { "*" };
        Uri allsongsuri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        mCursor = getContentResolver().query(allsongsuri, STAR, selection, null, null);

        int count = mCursor.getCount();
        Log.d("Count:",""+count);

        String[] songs = new String[count];
        int i = 0;
        if (mCursor.moveToFirst()) {
            do {
                String t,path;
                t = mCursor.getString(mCursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                path=mCursor.getString(mCursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                song.put(t,path);
                if(t!=null){
                    songs[i]=t;
                    i++;
                }
            } while (mCursor.moveToNext());
        }

        mCursor.close();

        return songs;

    }

    private void playSong(String path) throws IllegalArgumentException,
            IllegalStateException, IOException {



            Log.d("Playing...", "" + path);

            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
            Log.d("Playing...", "playing....");


    }


    public void changePlayState(){

        if(!playState){
            pp.setImageResource(R.drawable.pause);
            footer.setText("playing baby...");
        }
        else{
            pp.setImageResource(R.drawable.play);
            mMediaPlayer.pause();
            footer.setText("paused baby...");
        }

    }


    private class BtReceiver extends BroadcastReceiver {

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onReceive(Context context, Intent intent) {

            String s = intent.getStringExtra("bt");
            s=s.trim();
            Toast.makeText(getApplicationContext(), "Display:" + s, Toast.LENGTH_SHORT).show();
            int val=Integer.parseInt(s);
            if(val==0){

                childIndex=(childIndex+1)%3;
                selection.setBackground(dd);
                highlight();

            }

            else if(val==1){
                switch (childIndex){
                    case 0:
                        songIndex--;
                        nextSong();
                        try {
                            playSong(song.get(StringArray[songIndex]));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        length=0;
                        changePlayState();
                        break;
                    case 1:
                            try {
                                playSong(song.get(StringArray[songIndex]));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        playState=!playState;
                        changePlayState();
                        break;
                    case 2:
                        songIndex++;
                        nextSong();
                        try {
                            playSong(song.get(StringArray[songIndex]));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        changePlayState();
                        length=0;
                        break;
                    default:
                        Toast.makeText(getApplicationContext(),"LOLLL",Toast.LENGTH_SHORT).show();
                        break;
                }
            }

        }
    }

}
