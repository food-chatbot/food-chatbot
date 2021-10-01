package com.example.chatbot;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {

    private View view;

    GridView gridView;
    ArrayList<Food> list;
    FoodListAdater adapter = null;

    private DBHelper helper;
    private SQLiteDatabase db;

    private Cursor cursor;

    int image_id;

    //OnTimePickerSetListener onTimePickerSetListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.gallery, container, false);

        gridView = (GridView) view.findViewById(R.id.gallery_gridView);
        list = new ArrayList<>();
        adapter = new FoodListAdater(getActivity(), R.layout.gallery_items, list);
        gridView.setAdapter(adapter);

        helper = new DBHelper(getActivity().getApplicationContext());
        db = helper.getWritableDatabase();

        //sqlite에서 이미지 받아오는 코드
        String sql = "select * from DiaryData"; //나중에 고치기
        cursor = db.rawQuery(sql,null);
        list.clear();
        while (cursor.moveToNext()){
            image_id = cursor.getInt(0);
            byte[] image = cursor.getBlob(3);

            list.add(new Food(image_id, image));
        }
        adapter.notifyDataSetChanged();

        gridView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //onTimePickerSetListener.onTimePickerSet(image_id);
                Bitmap bitmap = adapter.getBitmap(position);
                Intent intent = new Intent(getActivity(), GalleryView.class);
                intent.putExtra("bitmap", bitmap);
                startActivity(intent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        /*
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onTimePickerSetListener.onTimePickerSet(image_id);
            }
        });
        */

        return view;
    }
    /*
    public interface OnTimePickerSetListener{
        void onTimePickerSet(int id);
    }


    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof OnTimePickerSetListener){
            onTimePickerSetListener = (OnTimePickerSetListener) context;
        } else{
            throw new RuntimeException(context.toString()
                    + " must implement OnTimePickerSetListener");
        }
    }

    @Override
    public void onDetach(){
        super.onDetach();
        onTimePickerSetListener = null;
    }
    */
}
