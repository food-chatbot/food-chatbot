package com.example.chatbot.TipView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.chatbot.R;
import com.rd.PageIndicatorView;


public class Tip3Activity extends AppCompatActivity {

    ViewPager pager;
    Toolbar toolbar;
    TextView tbarText;
    PageIndicatorView pg;


    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.tip_view);

        toolbar = findViewById(R.id.toolBar);
        tbarText = (TextView)toolbar.findViewById(R.id.tbarText);
        tbarText.setText("비만 예방");
        tbarText.setTextColor(Color.rgb(0,0,0));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pg = findViewById(R.id.page_indicator_view);


        pager= (ViewPager)findViewById(R.id.tipViewPager);


        Tip3Adapter adapter= new Tip3Adapter(getLayoutInflater());


        //ViewPager에 Adapter 설정

        pg.setViewPager(pager);
        pager.setAdapter(adapter);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {

            case android.R.id.home: //뒤로가기
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}