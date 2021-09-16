package com.example.chatbot;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DiaryEditFragment extends Fragment {

    private View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.diary_edit, container, false);
        setHasOptionsMenu(true);


        return view;
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) { // 다이어리 에디트 뷰 툴바 메뉴
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_toolbar_diary_edit, menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) { // 다이어리 수정, 삭제 메뉴 클릭 시 실행
        switch(item.getItemId()){
            case R.id.action_edit:
                Toast.makeText(getActivity(), "수정", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_delete:
                Toast.makeText(getActivity(), "삭제", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
