package com.example.chatbot;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;

import java.sql.Blob;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    String sql;

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "chatbotDB.db";
    private static final String TAG = "DBHelper";
    private SQLiteDatabase db;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        //데이터베이스가 생성이 될 때 호출출
        //데이터베이스 -> 테이블 -> 컬럼 -> 값

       // 다이어리 테이블
        db.execSQL("CREATE TABLE DiaryData (post_id INTEGER PRIMARY KEY AUTOINCREMENT, reporting_date CHAR(10) NOT NULL, content TEXT, img_file BYTE)");

        // 투두 테이블
        sql = "create table diary_todo ("
                + " _id integer NOT NULL PRIMARY KEY AUTOINCREMENT, "
                + "  TODO TEXT DEFAULT '', "
                + "date INTEGER NOT NULL "
                + ")";

        db.execSQL(sql);

        //투두 테이블 인덱스
        String CREATE_INDEX_SQL = "create index diary_todo _IDX ON diary_todo" + "("
                + "_id"
                + ")";

        //레시피 테이블
        db.execSQL("CREATE TABLE recipe(id INTEGER PRIMARY KEY AUTOINCREMENT, rcp_nm TEXT, rcp_parts_dtls TEXT, manual TEXT)");

    }

    //SELECT 문 (할일 목록 조회)
    public ArrayList<DiaryData> getDiaryData() {
        ArrayList<DiaryData> diaryDatas = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM DiaryData ORDER BY reporting_date DESC", null);

        if(cursor.getCount() != 0){
            //조회온 데이터가 있을때 내부 수행
            while(cursor.moveToNext()){

                int post_id = cursor.getInt(cursor.getColumnIndex("post_id"));
                String reporting_date = cursor.getString(cursor.getColumnIndex("reporting_date"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                byte[] img_file = cursor.getBlob(cursor.getColumnIndex("img_file"));

                DiaryData diaryData = new DiaryData();
                diaryData.setPost_id(post_id);
                diaryData.setContent(content);
                diaryData.setReporting_date(reporting_date);
                diaryData.setImg(img_file);
            }
       }
        cursor.close();

        return diaryDatas;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE DiaryData;");
        db.execSQL("DROP TABLE diary_todo");
        db.execSQL("DROP TABLE recipe");
        this.onCreate(db);
    }

    //INSERT문 (할일 목록을 DB에 넣는다.)
    //img_file의 타입???
    public void InsertDiary(String _reporting_date, String _content, byte[] _img_file){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO DiaryData (reporting_date, content, img_file) VALUES('"+ _reporting_date + "','" + _content + "' , '" + _img_file + "');");
    }

    //diary_update문 (할일 목록 수정 한다.)
    public void updateDiary(String _reporting_date, String _content, byte[] _img_file, Integer _post_id){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE DiaryData SET content='" + _content + "', reporting_date='" + _reporting_date +  "', img_file='" + _img_file + "' WHERE post_id='" + _post_id + "'");
    }

    // DELETE 문(Diary를 제거한다.)
    public void deleteDiary(int _post_id){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM DiaryData WHERE post_id '" + _post_id + "'");
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

}