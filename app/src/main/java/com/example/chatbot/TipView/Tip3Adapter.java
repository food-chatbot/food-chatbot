package com.example.chatbot.TipView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.chatbot.R;

import java.util.ArrayList;

public class Tip3Adapter extends PagerAdapter {

    LayoutInflater inflater;

    public Tip3Adapter(LayoutInflater inflater) {

        this.inflater=inflater;

    }


    @Override

    public int getCount() {

        return 5; //이미지 개수 리턴

    }


    @Override

    public Object instantiateItem(ViewGroup container, int position) {

        View view=null;


        view= inflater.inflate(R.layout.tip_viewpager, null);



        ImageView img= (ImageView)view.findViewById(R.id.img_viewpager_childimage);


        img.setImageResource(R.drawable.obes_prevent_1+position);

        container.addView(view);


        return view;

    }

    @Override

    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((View)object);


    }

    @Override

    public boolean isViewFromObject(View v, Object obj) {

        return v==obj;

    }


}
