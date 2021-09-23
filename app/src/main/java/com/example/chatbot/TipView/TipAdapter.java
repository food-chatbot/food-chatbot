package com.example.chatbot.TipView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.chatbot.R;

import java.util.ArrayList;

public class TipAdapter extends PagerAdapter {

    LayoutInflater inflater;

    public TipAdapter(LayoutInflater inflater) {

        //전달 받은 LayoutInflater를 멤버변수로 전달
        this.inflater=inflater;

    }

    //보통 보여줘야하는 이미지 배열 데이터의 길이를 리턴

    @Override

    public int getCount() {

        return 4; //이미지 개수 리턴

    }


    @Override

    public Object instantiateItem(ViewGroup container, int position) {

        View view=null;

        //새로운 View 객체를 Layoutinflater를 이용해서 생성

        view= inflater.inflate(R.layout.tip_viewpager, null);


        //만들어진 View안에 있는 ImageView 객체 참조
        //위에서 inflated 되어 만들어진 view로부터 findViewById()를 해야 하는 것에 주의.

        ImageView img= (ImageView)view.findViewById(R.id.img_viewpager_childimage);


        //ImageView에 현재 position 번째에 해당하는 이미지를 보여주기 위한 작업
        //현재 position에 해당하는 이미지를 setting

        img.setImageResource(R.drawable.hfood1+position);

        //ViewPager에 만들 낸 View 추가

        container.addView(view);
        //Image가 세팅된 View를 리턴

        return view;

    }



    //화면에 보이지 않은 View는파쾨를 해서 메모리를 관리함.
    //첫번째 파라미터 : ViewPager
    //두번째 파라미터 : 파괴될 View의 인덱스(가장 처음부터 0,1,2,3...)
    //세번째 파라미터 : 파괴될 객체(더 이상 보이지 않은 View 객체)

    @Override

    public void destroyItem(ViewGroup container, int position, Object object) {

        //ViewPager에서 보이지 않는 View는 제거

        //세번째 파라미터가 View 객체 이지만 데이터 타입이 Object여서 형변환 실시

        container.removeView((View)object);


    }



    //instantiateItem() 메소드에서 리턴된 Ojbect가 View가  맞는지 확인하는 메소드

    @Override

    public boolean isViewFromObject(View v, Object obj) {

        return v==obj;

    }


}
