package com.example.chatbot;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    String sql;

    private static final String TAG = "DBHelper";
    private SQLiteDatabase db;

    public DBHelper(Context context) {
        super(context, "chatbotDB", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        // 다이어리 테이블
        sql = "CREATE TABLE diary_posts (post_id INTEGER PRIMARY KEY AUTOINCREMENT,";
        sql += "reporting_date INTEGER NOT NULL, weather INTEGER, content TEXT, img_file BLOB)";

        db.execSQL(sql);

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

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE diary_posts;");
        db.execSQL("DROP TABLE diary_todo");
        db.execSQL("DROP TABLE recipe");
        this.onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

}