package com.example.chatbot;

import android.graphics.Bitmap;

public class DiaryData {

    Integer post_id = 0;
    String reporting_date;
    //Integer weather = 0;
    String content = "";
    byte[] img = null;      //데이터형??

    DiaryData(){

        this.post_id = post_id;
        this. reporting_date = reporting_date;
        //this. weather = weather;
        this.content = content;
        this.img = img;
    }

    public Integer getPost_id() {
        return post_id;
    }

    public void setPost_id(Integer post_id) {
        this.post_id = post_id;
    }

    public String getReporting_date(){
        return reporting_date;
    }

    public void setReporting_date(String reporting_date){
        this.reporting_date = reporting_date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }


}
