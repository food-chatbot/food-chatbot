package com.example.chatbot;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;


public class DiaryFragment extends Fragment {
    private static final String TAG = "DiaryFragment";

    private View view;
    private Button btnDiaryAddTodo;
    CalendarView calendarView;
    TextView date_tv, diary_tv;
    LinearLayout preDiary;

    RecyclerView recyclerView;
    NoteAdapter adapter;
    Context context;

    public int todoDate;
    LinearLayout diaryTodoWriteLayout;
    EditText todoWrite;
    Button todoSave;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.diary, container, false);
        setHasOptionsMenu(true);

        calendarView = view.findViewById(R.id.calendarView);
        date_tv = view.findViewById(R.id.date_tv);
        diary_tv = view.findViewById(R.id.diary_tv);
        preDiary = view.findViewById(R.id.preDiary);

        initUI(view);

        Calendar cal = Calendar.getInstance();
        int cYear = cal.get(Calendar.YEAR);
        int cMonth = cal.get(Calendar.MONTH);
        int cDay = cal.get(Calendar.DAY_OF_MONTH);

        todoDate = Integer.parseInt(String.valueOf(cYear) + String.valueOf(cMonth+1) + String.valueOf(cDay));

        loadNoteListData(todoDate);

        date_tv.setText(cYear+"년 "+(cMonth+1)+"월 "+cDay+"일"); //오늘 날짜 date_tv에 출력

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() // 날짜 선택 이벤트
        {
            @Override

            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
            {
                String date = year + "년 " + (month + 1) + "월 " + dayOfMonth + "일";
                date_tv.setText(date); // 선택한 날짜로 설정

                String selectedYear = String.valueOf(year);
                String selectedMonth = String.valueOf(month+1);
                String selectedDay = String.valueOf(dayOfMonth);
                todoDate = Integer.parseInt(selectedYear + selectedMonth + selectedDay);
                loadNoteListData(todoDate);

            }
        });


        preDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),DiaryView.class);

                startActivity(intent);

            }
        });

        btnDiaryAddTodo = view.findViewById(R.id.btnDiaryAddTodo); // 투두 추가 버튼
        diaryTodoWriteLayout = view.findViewById(R.id.diaryTodoWriteLayout); // 투두 내용 적는 레이아웃
        todoWrite = view.findViewById(R.id.todoWrite); // 투두 내용 적는 에딧텍스트
        todoSave = view.findViewById(R.id.todoSave); // 투두 저장 버튼

        // 투두 추가 버튼 누르면 입력칸 나옴
        btnDiaryAddTodo.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                diaryTodoWriteLayout.setVisibility(v.VISIBLE);
                todoWrite.setVisibility(v.VISIBLE);
                todoSave.setVisibility(v.VISIBLE);
            }
        });

        // 투두 저장 버튼 클릭하면
        todoSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToDo();
                diaryTodoWriteLayout.setVisibility(v.GONE);
                todoWrite.setVisibility(v.GONE);
                todoSave.setVisibility(v.GONE);
                adapter.notifyItemInserted(adapter.getItemCount());
                loadNoteListData(todoDate);
            }
        });

        return view;
    }

    private void initUI(View view){
        recyclerView = view.findViewById(R.id.todoRecyclerview); // diary.xml에서 만든 TodoRecyclerview 연결

        // 리니어레이아웃메니저를 이용하여 리니어레이아웃에 recyclerView를 붙임. (todo_item들을 세로로 정렬하는 역할을 함.)
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // 어댑터 연결
        adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);
    }

    public int loadNoteListData(int todoDate){
        String sql = "select _id, TODO, checktodo from diary_todo where date = " + todoDate; //+ " order by _id desc";

        int recordCount = -1;
        NoteDatabase database = NoteDatabase.getInstance(context);

        if (database != null){
            // cursor에 rawQuery문 저장
            Cursor outCursor = database.rawQuery(sql);

            if(outCursor!=null){
                recordCount = outCursor.getCount();

                //id, TODO가 담길 배열 생성
                ArrayList<Note> items = new ArrayList<>();

                //하나하나 추가
                for(int i=0;i<recordCount;i++){
                    outCursor.moveToNext();

                    int _id = outCursor.getInt(0);
                    String todo = outCursor.getString(1);
                    int checktodo = outCursor.getInt(2);
                    items.add(new Note(_id, todo, checktodo));
                }
                outCursor.close();

                //어댑터 연걸, 데이터셋 변경
                adapter.setItems(items);
                adapter.notifyDataSetChanged();

            }
        }
        return recordCount;
    }

    private void saveToDo(){

        // 투두 입력칸에 적힌 글 가져오기
        String todo = todoWrite.getText().toString();

        // 투두 테이블에 값 추가
        String sql = "insert into diary_todo(TODO, date, checktodo) values('" + todo + "', " + todoDate + ", 0);";

        NoteDatabase database = NoteDatabase.getInstance(context);
        database.execSQL(sql);

        todoWrite.setText("");
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