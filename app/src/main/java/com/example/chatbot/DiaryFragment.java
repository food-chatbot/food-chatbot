package com.example.chatbot;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;



public class DiaryFragment extends Fragment {

    private View view;
    private Button btnDiaryAddTodo;
    LinearLayout diaryTodoLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.diary, container, false);
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) { // 다이어리 프래그먼트의 toolbar를 menu_toolbar_diary로 지정.
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_toolbar_diary, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) { // 다이어리 추가 버튼을 눌렀을 때
        switch(item.getItemId()){
            case R.id.action_add:
                Toast.makeText(getActivity(), "추가 버튼", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
