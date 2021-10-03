package com.example.chatbot;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.BreakIterator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class DiaryViewEdit extends AppCompatActivity {

    Toolbar toolbar;
    Button btnwrite;
    DBHelper mDBHelper;
    private SQLiteDatabase db;
    View action_add;
    EditText diary_write_et;

    private Cursor cursor;

    ImageView imageView;
    private final int GET_GALLERY_IMAGE = 200;

    Bitmap bitmap = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_write);

        diary_write_et = findViewById(R.id.diary_wirte_et);
        btnwrite = findViewById(R.id.btnWrite); // 다이어리 저장버튼
        mDBHelper = new DBHelper(this);
        db = mDBHelper.getWritableDatabase();
        imageView = findViewById(R.id.imageView);

        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼
        getSupportActionBar().setTitle("");

        setInit();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });
    }


    private void setInit()
    {
        action_add = findViewById(R.id.action_add); //다이어리 추가버튼

        Intent intent = getIntent();
        int cYear = intent.getIntExtra("cYear", 0);
        int cMonth = intent.getIntExtra("cMonth", 0);
        int cDay = intent.getIntExtra("cDay", 0);
        String write_edit = intent.getStringExtra("write-edit");
        if(write_edit.equals("write"))
            btnwrite.setText("작성하기");
        else
            btnwrite.setText("수정하기");


        String YMD = (cYear+"년 "+(cMonth+1)+"월 "+cDay+"일").toString();

        String sql = "select content from DiaryData where reporting_date = '" + YMD + "'";
        cursor = db.rawQuery(sql, null);

        while(cursor.moveToNext()){
            String diary_content = cursor.getString(0);
            diary_write_et.setText(diary_content);
        }

        if(btnwrite.getText().equals("작성하기") && diary_write_et.length() != 0) //이미 다이어리를 작성한 날이면 작성하기 버튼 사라짐.
            btnwrite.setVisibility(View.GONE);


        btnwrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnwrite.getText().equals("작성하기")) { //작성하기 버튼일 때.
                    if(imageViewToByte(bitmap) != null){
                        SQLiteStatement p = db.compileStatement("insert into DiaryData (reporting_date, content, img_file) VALUES(?, ?, ?)");
                        p.bindString(1, YMD);
                        p.bindString(2, diary_write_et.getText().toString());
                        p.bindBlob(3, imageViewToByte(bitmap));
                        p.execute();
                    } else{
                        db.execSQL("insert into DiaryData(reporting_date, content) values('" + YMD + "','" + diary_write_et.getText().toString() + "')");
                    }
                } else{
                    db.execSQL("update DiaryData set content = '" + diary_write_et.getText() + "' where reporting_date = '" + YMD + "'"); //수정하기 버튼일 때. 수정하기 버튼일 때 + 버튼 오른쪽에 사진이 보이게 하려면 여기도 SQliteStatement를 써야함.
                }
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GET_GALLERY_IMAGE && resultCode==RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();

            try{
                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }
    }

    private byte[] imageViewToByte(Bitmap bitmap){
        byte[] byteArray = null;
        if(bitmap != null){
            //Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray = stream.toByteArray();
        }
        return byteArray;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id){

            case android.R.id.home : //뒤로가기
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}

