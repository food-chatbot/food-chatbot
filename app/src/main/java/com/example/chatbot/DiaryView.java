package com.example.chatbot;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;

import java.io.ByteArrayInputStream;
import java.util.Calendar;

public class DiaryView extends AppCompatActivity {

    Toolbar toolbar;
    DBHelper dbHelper;
    SQLiteDatabase sqlitedb;
    Integer postID = -1;
    Integer newDate = 0;
    Intent intent;
    ImageView diaryImg;
    EditText diary_et;

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


        diaryImg = findViewById(R.id.diary_img);
        diary_et = findViewById(R.id.diary_et);

        //newDate = intent.getIntExtra("newDate",0);
        //postID = intent.getIntExtra("postID",-1);

        //일기가 있다면
        if(postID>0){
            dbHelper = new DBHelper(this);
            loadDiary();    //일기를 불러옴
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

            case android.R.id.home : //뒤로가기
                finish();
                return true;

            case R.id.action_edit:  //수정 버튼 누르면 diary_write로 넘어가게
                Intent intent = new Intent(getApplicationContext(),DiaryViewEdit.class);
                startActivity(intent);
                break;

            case R.id.action_delete:

                if(postID>0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("삭제").setMessage("해당 일기를 삭제하시겠습니까?");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteDiary();
                            Toast.makeText(getApplicationContext(),"일기가 삭제되었습니다.",Toast.LENGTH_SHORT);
                            finish();
                            Intent intent = new Intent(getApplicationContext(),DiaryFragment.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    builder.setNegativeButton("취소", null);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }else{
                    Toast.makeText(getApplicationContext(),"삭제할 일기가 없습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadDiary(){  //DB에서 다이어리 가져오기
        Integer weather;
        byte[] image;
        Bitmap bitmap;
        Cursor cursor;

        sqlitedb = dbHelper.getReadableDatabase();

        cursor = sqlitedb.rawQuery("SELECT * FROM diary_posts WHERE post_id = $postID", null);

        if(cursor.moveToFirst()){
             diary_et.setText(cursor.getString(cursor.getColumnIndex("content")));

             try{
                 image = cursor.getBlob(cursor.getColumnIndex("img_file"));
                 bitmap = BitmapFactory.decodeByteArray(image,0,image.length);
                 diaryImg.setImageBitmap(bitmap);

             }catch (NullPointerException n){
                 Toast.makeText(getApplicationContext(),"저장된 사진이 없습니다.",Toast.LENGTH_SHORT);
             }
        }
        cursor.close();
        sqlitedb.close();
    }

    //다이어리 삭제
    public void deleteDiary(){
        sqlitedb = dbHelper.getWritableDatabase();
        sqlitedb.execSQL("DELETE FROM diary_posts WHERE post_id = $postID");
    }
}
