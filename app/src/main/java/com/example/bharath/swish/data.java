package com.example.bharath.swish;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Bharath on 6/18/2016.
 */

public class data extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "profile";
    private static final int DATABASE_VERSION = 1;
    private static final String CALL = "call";
    private static final String MESSAGE = "message";

    private static final String cid = "id";

    //For calling
    private static final String cn1 = "cn1";
    private static final String cp1 = "cp1";
    private static final String cn2 = "cn2";
    private static final String cp2 = "cp2";
    private static final String cn3 = "cn3";
    private static final String cp3 = "cp3";
    private static final String cn4 = "cn4";
    private static final String cp4 = "cp4";


    //For messaging..

    private static final String mid = "id";
    private static final String mn1 = "mn1";
    private static final String mp1 = "mp1";
    private static final String mn2 = "mn2";
    private static final String mp2 = "mp2";
    private static final String mn3 = "mn3";
    private static final String mp3 = "mp3";
    private static final String mn4 = "mn4";
    private static final String mp4 = "mp4";


    //For table..

    private static final String CREATE_CALL="CREATE TABLE "+CALL+"("+cid+" INTEGER PRIMARY KEY AUTOINCREMENT, "+cn1+" TEXT, "+cp1+" TEXT, "+cn2+" TEXT, "+cp2+" TEXT, "+cn3+" TEXT, "+cp3+" TEXT, "+cn4+" TEXT, "+cp4+" TEXT "+")";
    private static final String CREATE_MESSAGE="CREATE TABLE "+MESSAGE+"("+mid+" INTEGER PRIMARY KEY AUTOINCREMENT, "+mn1+" TEXT, "+mp1+" TEXT, "+mn2+" TEXT, "+mp2+" TEXT, "+mn3+" TEXT, "+mp3+" TEXT, "+mn4+" TEXT, "+mp4+" TEXT "+")";



    public data(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_CALL);
        db.execSQL(CREATE_MESSAGE);
        Log.d("DB", "DB created..");

    }

    public void addCall(){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(cn1,"Bharath");
        cv.put(cp1,"+919524899989");
        cv.put(cn2,"Robert");
        cv.put(cp2,"+919788477735");
        cv.put(cn3,"Barbieee");
        cv.put(cp3,"+918056882955");
        cv.put(cn4,"Pangali");
        cv.put(cp4,"+919487256689");
        db.insert(CALL, null, cv);
        Log.d("Inserted:", "DB values inserted..");
        db.close();
    }

    public void addMessage(){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(mn1,"Appa");
        cv.put(mp1,"+919894662672");
        cv.put(mn2,"Amma");
        cv.put(mp2,"+919363249959");
        cv.put(mn3,"Bhai");
        cv.put(mp3,"+918903419661");
        cv.put(mn4,"iMaapla");
        cv.put(mp4,"+919894522683");
        db.insert(MESSAGE, null, cv);
        Log.d("Inserted:","DB values inserted..");
        db.close();
    }

    public void getCall(){

        String selectQuery = "SELECT  * FROM " + CALL;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String []StringArray=new String[8];
        int i=0;
        if (cursor.moveToFirst()) {
            do {
                int id=Integer.parseInt(cursor.getString(0));
                StringArray[i++]=cursor.getString(1);
                StringArray[i++]=cursor.getString(2);
                StringArray[i++]=cursor.getString(3);
                StringArray[i++]=cursor.getString(4);
                StringArray[i++]=cursor.getString(5);
                StringArray[i++]=cursor.getString(6);
                StringArray[i++]=cursor.getString(7);
                StringArray[i++]=cursor.getString(8);

            } while (cursor.moveToNext());
        }

    }

    public void getMessage(){

        String selectQuery = "SELECT  * FROM " + MESSAGE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String []StringArray=new String[8];
        int i=0;
        if (cursor.moveToFirst()) {
            do {
                int id=Integer.parseInt(cursor.getString(0));
                StringArray[i++]=cursor.getString(1);
                StringArray[i++]=cursor.getString(2);
                StringArray[i++]=cursor.getString(3);
                StringArray[i++]=cursor.getString(4);
                StringArray[i++]=cursor.getString(5);
                StringArray[i++]=cursor.getString(6);
                StringArray[i++]=cursor.getString(7);
                StringArray[i++]=cursor.getString(8);

            } while (cursor.moveToNext());
        }

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + CALL);
        db.execSQL("DROP TABLE IF EXISTS " + MESSAGE);
        // Create tables again
        onCreate(db);

    }
}

