package com.example.chatbot;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GalleryView extends AppCompatActivity {

    int image_id;
    ImageView galleryView_imageView;

    private DBHelper helper;
    private SQLiteDatabase db;

    private Cursor cursor;
    byte[] image;

    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_view);

        galleryView_imageView = findViewById(R.id.galleryView_imageView);
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼
        getSupportActionBar().setTitle("");

        helper = new DBHelper(getApplicationContext());
        db = helper.getWritableDatabase();

        Intent getIntent = getIntent();
        image_id = getIntent.getIntExtra("image_id", 0);

        String sql = "select img_file from DiaryData where post_id = " + image_id; //나중에 고치기
        cursor = db.rawQuery(sql,null);
        while (cursor.moveToNext()) {
            image = cursor.getBlob(0);
        }

        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        galleryView_imageView.setImageBitmap(bitmap);

    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();

        switch(id){
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
