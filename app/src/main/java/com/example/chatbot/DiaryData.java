package com.example.chatbot;

import android.graphics.Bitmap;

public class DiaryData {

    Integer id = 0;
    Integer reporting_date = 0;
    Integer weather = 0;
    String content = "";
    Bitmap img = null;

    DiaryData() {}

    DiaryData(Integer id, Integer date, Integer weather, String content, Bitmap img){

        this.id = id;
        this. reporting_date = date;
        this. weather = weather;
        this.content = content;
        this.img = img;
    }
}
