package com.example.chatbot;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;

import java.util.Calendar;

public class DiaryView extends AppCompatActivity {

    Toolbar toolbar;
    DBHelper dbHelper;
    SQLiteDatabase sqlitedb;
    Integer postID = -1;
    Integer newDate = 0;
    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_edit);

        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        
        Calendar cal = Calendar.getInstance();
        int cYear = cal.get(Calendar.YEAR);
        int cMonth = cal.get(Calendar.MONTH);
        int cDay = cal.get(Calendar.DAY_OF_MONTH);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼
        getSupportActionBar().setTitle(cYear+"년 "+(cMonth+1)+"월 "+cDay+"일");





        //일기가 있다면
        if(postID>0){
            dbHelper = new DBHelper(this);
            //loadDiary();    일기를 불러옴옴
       }



    }


    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_toolbar_diary_edit, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){

            case R.id.action_edit:  //수정 버튼 누르면 diary_write로 넘어가게
                Intent intent = new Intent(getApplicationContext(),DiaryViewEdit.class);
                startActivity(intent);
                break;
            case R.id.action_delete:

        }
        return super.onOptionsItemSelected(item);
    }
}
