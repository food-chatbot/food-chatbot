package com.example.chatbot;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Calendar;
import java.util.Date;


public class DiaryFragment extends Fragment {

    private View view;
    private Button btnDiaryAddTodo;
    LinearLayout diaryTodoLayout;
    CalendarView calendarView;
    TextView date_tv, diary_tv;
    LinearLayout preDiary;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.diary, container, false);
        setHasOptionsMenu(true);

        calendarView = view.findViewById(R.id.calendarView);
        date_tv = view.findViewById(R.id.date_tv);
        diary_tv = view.findViewById(R.id.diary_tv);
        preDiary = view.findViewById(R.id.preDiary);

        Calendar cal = Calendar.getInstance();
        int cYear = cal.get(Calendar.YEAR);
        int cMonth = cal.get(Calendar.MONTH);
        int cDay = cal.get(Calendar.DAY_OF_MONTH);

        date_tv.setText(cYear+"년 "+(cMonth+1)+"월 "+cDay+"일"); //오늘 날짜 date_tv에 출력

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() // 날짜 선택 이벤트
        {
            @Override

            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
            {
                String date = year + "년 " + (month + 1) + "월 " + dayOfMonth + "일";
                date_tv.setText(date); // 선택한 날짜로 설정

            }
        });


        preDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),DiaryView.class);

                startActivity(intent);

            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) { // 다이어리 프래그먼트의 toolbar를 menu_toolbar_diary로 지정.
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_toolbar_diary, menu);
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) { // 다이어리 추가 버튼을 눌렀을 때
        switch(item.getItemId()){
            case R.id.action_add:
                Toast.makeText(getActivity(), "추가 버튼", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(),DiaryViewEdit.class);
                startActivity(intent);
                break;

            case R.id.action_delete:
                Toast.makeText(getActivity(), "삭제버튼", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}