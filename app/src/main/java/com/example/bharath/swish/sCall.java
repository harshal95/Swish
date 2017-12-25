package com.example.bharath.swish;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class sCall extends AppCompatActivity {

    LST header, footer;
    Animation move1, move2;
    BtReceiver btrec;
    Handler mHandler = null;
    float h, w, x, y;
    final HashMap<String, String> action = new HashMap<String, String>();
    String buffer;
    final int[] i = {10};
    int boundFlag = 0, index = 0, bFLAG = 0, actionFlag = 0, state;
    RelativeLayout rel;
    private WindowManager wm;
    private LinearLayout ll;
    private Button yes, no;
    CardView v0, v1, v2, v3;
    TextView show;
    LinearLayout footerLayout, headerLayout;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.scall);

        //Setting the header and footer....
        header = new LST(sCall.this);
        footer = new LST(sCall.this);
        header.setLetterSpacing(10);
        footer.setLetterSpacing(10);
        header.setTextColor(Color.parseColor("#000000"));
        footer.setTextColor(Color.parseColor("#000000"));
        header.setTextSize(25);
        footer.setTextSize(25);
        header.setText("###call###");
        footer.setText("Recognising....");
        footerLayout = (LinearLayout) findViewById(R.id.footerLayout);
        headerLayout = (LinearLayout) findViewById(R.id.headerLayout);
        rel = (RelativeLayout) findViewById(R.id.rel);
        headerLayout.addView(header);
        footerLayout.addView(footer);

        //Setting the cardviews...

        v0 = (CardView) findViewById(R.id.view0);
        v1 = (CardView) findViewById(R.id.view1);
        v2 = (CardView) findViewById(R.id.view2);
        v3 = (CardView) findViewById(R.id.view3);

        v0.setElevation(10);
        v1.setElevation(10);
        v2.setElevation(10);
        v3.setElevation(10);

        btrec = new BtReceiver();
        IntentFilter iff = new IntentFilter();
        iff.addAction(Bluetooth.BLUETOOTH_SERVICE);
        registerReceiver(btrec, iff);

        //Starting timer...
        mHandler = new Handler();
        timer();


        animateText();

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
                                h = v3.getHeight();
                                w = v3.getWidth();
                                x = v3.getX();
                                y = v3.getY();
                                if (boundFlag == 0) {
                                    bound(x, y + 55, w + 55, h);
                                    boundFlag++;
                                }
                            }
                        });
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
            }
        }).start();


    }


    public void bound(float tx, float ty, float tw, float th) {
        Paint paint = new Paint();
        int myColour = Color.argb(255, 0, 0, 0);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setColor(myColour);
        Bitmap bg = Bitmap.createBitmap(rel.getWidth(), rel.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bg);
        canvas.drawRect(tx, ty, tx + tw, ty + th, paint);

        ImageView iV = new ImageView(this);
        iV.setImageBitmap(bg);

        rel.addView(iV);
        index = rel.indexOfChild(iV);
        Log.d("Index:", "" + index);


        if (bFLAG == 0) {

            /*bFLAG++;
            Intent ii = new Intent(this, Bluetooth.class);
            ii.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startService(ii);*/

        }

    }


    private class BtReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String s = intent.getStringExtra("bt");
            Toast.makeText(getApplicationContext(), "Display:" + s, Toast.LENGTH_SHORT).show();
            if (buffer == null) {
                buffer = "";
                buffer += s;
                buffer = buffer.trim();
                footer.setText("Recognised..." + buffer);
                Log.d("Index_delete:", "" + index);
                if (actionFlag == 0) {
                    rel.removeViewAt(index);
                    checkDraw(buffer);
                } else if (actionFlag == 1) {
                    confirmActivity(buffer);
                    footer.setText("Recognising...");
                    buffer = null;
                }
                return;
            } else if (buffer.length() == 2) {
                buffer = null;
            } else {
                buffer += s;
                buffer = buffer.trim();
                if (actionFlag == 0) {
                    rel.removeViewAt(index);
                    checkDraw(buffer);
                }
                String t = action.get(buffer);
                Toast.makeText(getApplicationContext(), "Action baby:" + t, Toast.LENGTH_LONG).show();
                footer.setText("Recognising...");
                buffer = null;
            }

        }
    }

    public void confirmActivity(String t) {
        int x = Integer.parseInt(t);
        if (x == 1) {

            if (state == 0) {

                String posted_by = "+919894662672";

                String uri = "tel:" + posted_by.trim();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);

            }

            else if(state==1){

                String posted_by = "+919092435064";

                String uri = "tel:" + posted_by.trim();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);


            }

            else if(state==10){

                String posted_by = "+918526410903";

                String uri = "tel:" + posted_by.trim();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);


            }

            else {

                String posted_by = "+918056882955";

                String uri = "tel:" + posted_by.trim();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);


            }


        }
        else{
            Toast.makeText(getApplicationContext(),"Poda venna..."+state,Toast.LENGTH_SHORT).show();
        }
        wm.removeView(ll);
        v0.setAlpha((float) 1);
        v1.setAlpha((float) 1);
        v2.setAlpha((float) 1);
        v3.setAlpha((float) 1);
        actionFlag=0;
    }

    public void confirm(String t) {

        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        ll = new LinearLayout(this);
        show = new TextView(this);
        yes = new Button(this);
        no = new Button(this);

        //Text
        ViewGroup.LayoutParams textp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        show.setLayoutParams(textp);
        show.setTextSize(32);
        show.setGravity(Gravity.CENTER);
        show.setTextColor(Color.parseColor("#FFFFFF"));
        show.setText("Call " + t + "?");

        //Button Yes...
        ViewGroup.LayoutParams pyes = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        yes.setText("Yes(1)");
        yes.setLayoutParams(pyes);

        //Button No...
        ViewGroup.LayoutParams pno = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        no.setText("No(0)");
        no.setLayoutParams(pno);

        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ll.setBackgroundColor(Color.parseColor("#212121"));
        ll.setLayoutParams(llp);
        ll.setPadding(5,5,5,5);
        WindowManager.LayoutParams p = new WindowManager.LayoutParams(1000, 500, WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        p.x = 0;
        p.y = 0;
        p.gravity = Gravity.CENTER | Gravity.CENTER;
        ll.setOrientation(LinearLayout.VERTICAL);

        //Blurring bggggg
        v0.setAlpha((float) 0.3);
        v1.setAlpha((float) 0.3);
        v2.setAlpha((float) 0.3);
        v3.setAlpha((float) 0.3);


        ll.addView(show);
        ll.addView(yes);
        ll.addView(no);
        wm.addView(ll, p);

    }


    public void checkDraw(String xx){

        Log.d("Status:", "" + xx);
        int length=xx.length();
        if(length==1){
            int t=Integer.parseInt(xx);
            if(t==0){

                h= v0.getHeight();
                w = v0.getWidth();
                x=v0.getX();
                y=v0.getY();
                bound(x,y-10,w+45,(2*h)+90);

            }
            else{

                h= v2.getHeight();
                w = v2.getWidth();
                x=v2.getX();
                y=v2.getY();
                bound(x,y+35,w+55,(2*h)+90);


            }
        }
        else{

            int yy=Integer.parseInt(xx);
            switch (yy){
                case 00:
                    h= v0.getHeight();
                    w = v0.getWidth();
                    x=v0.getX();
                    y=v0.getY();
                    bound(x,y-10,w+55,h);
                    actionFlag=1;
                    state=yy;
                    confirm("Dad");
                    break;
                case 01:
                    h= v1.getHeight();
                    w = v1.getWidth();
                    x=v1.getX();
                    y=v1.getY();
                    bound(x,y+15,w+55,h);
                    actionFlag=1;
                    state=yy;
                    confirm("Mom");
                    break;
                case 10:
                    h= v2.getHeight();
                    w = v2.getWidth();
                    x=v2.getX();
                    y=v2.getY();
                    bound(x,y+35,w+55,h);
                    actionFlag=1;
                    state=yy;
                    confirm("Robert");
                    break;
                case 11:
                    h= v3.getHeight();
                    w = v3.getWidth();
                    x=v3.getX();
                    y=v3.getY();
                    bound(x,y+35,w+55,h+15);
                    actionFlag=1;
                    state=yy;
                    confirm("Barbieee");
                    break;
            }


        }

    }




}
