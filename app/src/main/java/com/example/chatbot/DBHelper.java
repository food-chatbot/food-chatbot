package com.example.chatbot;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    String sql;

    public DBHelper(Context context) {
        super(context, "chatbotDB", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        // 다이어리 테이블
        sql = "CREATE TABLE diary_posts (post_id INTEGER PRIMARY KEY AUTOINCREMENT,";
        sql += "reporting_date INTEGER NOT NULL, weather INTEGER, content TEXT, img_file BLOB";

        db.execSQL(sql);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE diary_posts;");
        this.onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}

