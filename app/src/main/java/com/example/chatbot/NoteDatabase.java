package com.example.chatbot;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class NoteDatabase {


    private static final String TAG = "NoteDatabase";
    private static NoteDatabase database;

    private Context context;
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    private NoteDatabase(Context context){
        this.context = context;
    }

    public static NoteDatabase getInstance(Context context){
        if(database == null){
            database = new NoteDatabase(context);
        }

        return database;
    }

    // 리스트 표시할 때 현재 위치 나타냄.
    public Cursor rawQuery(String SQL){

        Cursor c1 = null;
        try{
            c1 = db.rawQuery(SQL,null);
        } catch (Exception ex){
            //Log.e(TAG,"Exception in rawQuery",ex);
            return null;
        }

        return c1;
    }

    public boolean execSQL(String SQL) {

        try {
            Log.d(TAG, "SQL : " + SQL);
            db.execSQL(SQL);
        } catch(Exception ex) {
            Log.e(TAG, "Exception in execSQL", ex);
            return false;
        }
        return true;
    }

    //데이터베이스 열기
    public boolean open(){

        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();

        return true;
    }

    //데이터베이스 닫기
    public void close(){
        db.close();
        database = null;
    }

}