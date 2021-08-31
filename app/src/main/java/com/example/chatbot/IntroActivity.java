package com.example.chatbot;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.ActionBar;

public class IntroActivity extends MainActivity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro); //splash를 띄울 xml화면

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                startActivity(new Intent(IntroActivity.this, MainActivity.class));

                finish();
            }
        },5000); //splash를 몇초간 띄울것인가 2000=2초
    }
}
