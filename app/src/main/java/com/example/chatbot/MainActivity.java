package com.example.chatbot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.chatbot.TipView.TipFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 파이어베이스 관련 작업들

    private FragmentManager fm;
    private FragmentTransaction ft;
    private TipFragment tipfragment;
    private DiaryFragment diaryfragment;
    private GalleryFragment galleryfragment;
    private ChatFragment chatfragment;

    public static NoteDatabase noteDatabase = null;

    int mBeginner = 0; // 네비바 메뉴에 따라 툴바를 변경하기 위한 변수.

    private String TAG = "MainActivity";
    private long mBackWait = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.naviView);
        navigationView.setNavigationItemSelectedListener(this);

        tipfragment = new TipFragment();
        diaryfragment = new DiaryFragment();
        galleryfragment = new GalleryFragment();
        chatfragment = new ChatFragment();

        openDatabase();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.hamburger); // 뒤로가기 버튼의 이미지를 햄버거바로 설정

        getSupportActionBar().setTitle("챗봇");

    }

    //toolbar에 메뉴 생성

    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();

        if(mBeginner==0){ // 네비바에서 챗봇메뉴 (홈메뉴)가 눌리면
            toolbar.getMenu().clear(); // 툴바 내용 안 보이게.
            menuInflater.inflate(R.menu.menu_toolbar_chat, menu);
        } else { // 네비바에서 그 외의 메뉴가 눌리면
            toolbar.getMenu().clear();
        }

        return true;
    }

    //드로어레이아웃 열리게 하는 함수
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //네비바 메뉴들 누를 때
    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();
        if(id == R.id.nav_home) { // 채팅화면
            setFrag(0);
        } else if (id == R.id.nav_tip){ // 팁화면
            setFrag(1);
        } else if (id == R.id.nav_diary){ // 다이어리화면
            setFrag(2);
        } else if (id == R.id.nav_gallery){ // 사진첩화면
            setFrag(3);
        } else if(id == R.id.nav_logout){ // 로그아웃
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(MainActivity.this, "로그아웃", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_Withdrawal){ // 회원탈퇴
            user.delete().addOnCompleteListener(new OnCompleteListener<Void>(){
                @Override
                public void onComplete(@NonNull Task<Void> task){
                    if (task.isSuccessful()){
                        Log.d(TAG, "User account deleted.");
                        Toast.makeText(MainActivity.this, "회원탈퇴", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    // 프래그먼트들 화면 전환 함수
    private void setFrag(int n){
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        switch(n) {
            case 0:
                getSupportActionBar().setTitle("챗봇");
                mBeginner = 0;
                invalidateOptionsMenu(); // onCreateOptionsMenu 호출하는 함수
                ft.replace(R.id.main_frame, chatfragment);
                ft.commit();
                break;
            case 1:
                getSupportActionBar().setTitle("건강tip");
                mBeginner = 1;
                invalidateOptionsMenu();
                ft.replace(R.id.main_frame, tipfragment);
                ft.commit();
                break;
            case 2:
                getSupportActionBar().setTitle("다이어리");
                mBeginner = 1;
                invalidateOptionsMenu();
                ft.replace(R.id.main_frame, diaryfragment);
                ft.commit();
                break;
            case 3:
                getSupportActionBar().setTitle("사진첩");
                mBeginner = 1;
                invalidateOptionsMenu();
                ft.replace(R.id.main_frame, galleryfragment);
                ft.commit();
                break;
        }
    }

    public void openDatabase() {
        // open database
        if (noteDatabase != null) {
            noteDatabase.close();
            noteDatabase = null;
        }

        noteDatabase = NoteDatabase.getInstance(this);
        boolean isOpen = noteDatabase.open();
        if (isOpen) {
            Log.d(TAG, "Note database is open.");
        } else {
            Log.d(TAG, "Note database is not open.");
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (noteDatabase != null) {
            noteDatabase.close();
            noteDatabase = null;
        }
    }

    // MainActivity에서 뒤로가기 두 번 누르면 종료
    @Override
    public void onBackPressed(){
        if(System.currentTimeMillis() - mBackWait > 2000){
            mBackWait = System.currentTimeMillis();
            Toast.makeText(this, "\'뒤로\' 버튼을 한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        } else{
            ActivityCompat.finishAffinity(this);
            System.exit(0);
        }
    }

}