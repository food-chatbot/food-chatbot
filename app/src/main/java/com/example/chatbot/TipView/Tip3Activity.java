package com.example.chatbot.TipView;

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
    PageIndicatorView pg;


    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.tip_view);

        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("비만 예방");

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
    //onClick속성이 지정된 View를 클릭했을때 자동으로 호출되는 메소드

    public void onClick(View v){

        int position = 1;

        switch( v.getId() ){

            case R.id.btnPrevious://이전버튼 클릭



                position=pager.getCurrentItem();//현재 보여지는 아이템의 위치를 리턴



                //현재 위치(position)에서 -1 을 해서 이전 position으로 변경

                //이전 Item으로 현재의 아이템 변경 설정(가장 처음이면 더이상 이동하지 않음)

                //첫번째 파라미터: 설정할 현재 위치

                //두번째 파라미터: 변경할 때 부드럽게 이동하는가? false면 팍팍 바뀜

                pager.setCurrentItem(position-1,true);



                break;



            case R.id.btnNext://다음버튼 클릭



                position=pager.getCurrentItem();//현재 보여지는 아이템의 위치를 리턴



                //현재 위치(position)에서 +1 을 해서 다음 position으로 변경

                //다음 Item으로 현재의 아이템 변경 설정(가장 마지막이면 더이상 이동하지 않음)

                //첫번째 파라미터: 설정할 현재 위치

                //두번째 파라미터: 변경할 때 부드럽게 이동하는가? false면 팍팍 바뀜
                pager.setCurrentItem(position+1,true);



                break;

        }



    }

}