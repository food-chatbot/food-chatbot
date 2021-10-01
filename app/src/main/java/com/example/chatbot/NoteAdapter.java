package com.example.chatbot;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder>{
    public static final String TAG = "NoteAdapter";

    ArrayList<Note> items = new ArrayList<Note>();

    //todo_item.xml을 인플레이션
    @NonNull
    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflate = LayoutInflater.from(parent.getContext());
        View itemView = inflate.inflate(R.layout.todo_item, parent, false);

        Log.d(TAG, "onCreate");
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.ViewHolder holder, int position) {

        Log.d(TAG, "onBind");
        Note item = items.get(position);
        holder.setItem(item);
        holder.setLayout();

        int checktodo = holder.setchecktodo(item);

        holder.setCheck(checktodo);
        
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout layoutTodoItem; //투두 아이템 레이아웃
        Button btnDiaryCheckTodo; //투두 체크 버튼
        TextView tVDiaryTodo; // 투두 내용 텍스트뷰
        Button btnDiaryEditTodo; // 투두 수정 버튼
        Button btnDiaryDeleteTodo; // 투두 삭제 버튼
        EditText eTDiaryTodoEdit; // 투두 수정할 때 에딧텍스트
        Button btnDiaryEditTodoComplition; // 투두 수정 완료 버튼
        String tVtemp; // 투두 텍스트뷰의 내용을 잠시 저장하는 변수
        String eTtemp; // 투두 수정할 때 에딧텍스트뷰의 내용을 잠시 저장하는 변수
        CardView todoCardView; // 카드뷰
        int checktodo;

        public ViewHolder(View itemView){
            super(itemView);
            layoutTodoItem = itemView.findViewById(R.id.layoutTodoItem);
            btnDiaryCheckTodo = itemView.findViewById(R.id.btnDiaryCheckTodo);
            tVDiaryTodo = itemView.findViewById(R.id.tVDiaryTodo);
            btnDiaryEditTodo = itemView.findViewById(R.id.btnDiaryEditTodo);
            btnDiaryDeleteTodo = itemView.findViewById(R.id.btnDiaryDeleteTodo);
            eTDiaryTodoEdit = itemView.findViewById(R.id.eTDiaryTodoEdit);
            btnDiaryEditTodoComplition = itemView.findViewById(R.id.btnDiaryEditTodoComplition);
            todoCardView = itemView.findViewById(R.id.todoCardView);


            btnDiaryCheckTodo.setOnClickListener(new View.OnClickListener() {
                Context context;

                @Override
                public void onClick(View v) {
                    if(btnDiaryCheckTodo.isSelected()){
                        String todotext = (String)tVDiaryTodo.getText();
                        String sql = "update diary_todo set checktodo = 0 where TODO = '" + todotext + "'";
                        NoteDatabase database = NoteDatabase.getInstance(context);
                        database.execSQL(sql);

                        btnDiaryCheckTodo.setSelected(false);
                        todoCardView.setCardBackgroundColor(Color.WHITE);
                        tVDiaryTodo.setPaintFlags(0);

                    } else{
                        String todotext = (String)tVDiaryTodo.getText();
                        String sql = "update diary_todo set checktodo = 1 where TODO = '" + todotext + "'";
                        NoteDatabase database = NoteDatabase.getInstance(context);
                        database.execSQL(sql);

                        btnDiaryCheckTodo.setSelected(true);
                        todoCardView.setCardBackgroundColor(Color.rgb(0xFF, 0xCC, 0xB2));
                        tVDiaryTodo.setPaintFlags(tVDiaryTodo.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                    }

                }
            });



            // 투두 수정 버튼
            btnDiaryEditTodo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tVtemp = (String)tVDiaryTodo.getText();

                    btnDiaryCheckTodo.setVisibility(v.GONE);
                    tVDiaryTodo.setVisibility(v.GONE);
                    btnDiaryEditTodo.setVisibility(v.GONE);
                    btnDiaryDeleteTodo.setVisibility(v.GONE);
                    eTDiaryTodoEdit.setVisibility(v.VISIBLE);
                    btnDiaryEditTodoComplition.setVisibility(v.VISIBLE);

                    eTDiaryTodoEdit.setText(tVtemp);

                }
            });

            // 투두 수정 후 확인 누르면 db에 저장
            btnDiaryEditTodoComplition.setOnClickListener(new View.OnClickListener() {

                Context context;

                @Override
                public void onClick(View v) {
                    eTtemp = eTDiaryTodoEdit.getText().toString();
                    String sql = "update diary_todo set TODO = '" + eTtemp + "' where TODO = '" + tVtemp + "'";

                    NoteDatabase database = NoteDatabase.getInstance(context);
                    database.execSQL(sql);

                    int position = getAdapterPosition();

                    if(btnDiaryCheckTodo.isSelected()){
                        checktodo = 1;
                    } else{
                        checktodo = 0;
                    }
                    Note note = new Note(position, eTtemp, checktodo);
                    items.set(position, note);
                    notifyItemChanged(position);

                    //tVDiaryTodo.setText(eTtemp);

                    eTDiaryTodoEdit.setVisibility(v.GONE);
                    btnDiaryEditTodoComplition.setVisibility(v.GONE);
                    btnDiaryCheckTodo.setVisibility(v.VISIBLE);
                    tVDiaryTodo.setVisibility(v.VISIBLE);
                    btnDiaryEditTodo.setVisibility(v.VISIBLE);
                    btnDiaryDeleteTodo.setVisibility(v.VISIBLE);
                }
            });

            // 투두 삭제 버튼
            btnDiaryDeleteTodo.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    String TODO = (String) tVDiaryTodo.getText();
                    deleteToDo(TODO);


                    // 삭제한 거 바로 반영
                    int position = getAdapterPosition();
                    //if(position != RecyclerView.NO_POSITION){
                        items.remove(position);
                        notifyItemRemoved(position);
                        //notifyItemRangeChanged(position, items.size());
                    //}
                    Toast.makeText(v.getContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();

                }

                Context context;

                private void deleteToDo(String TODO){ //데이터베이스 값 삭제하는 메서드.
                    String sql = "delete from diary_todo where TODO = '" + TODO + "'";
                    NoteDatabase database = NoteDatabase.getInstance(context);
                    database.execSQL(sql);

                }
            });

        }

        // 투두 입력한 것을 투두 내용 텍스트뷰에 넣는 메서드.
        public void setItem(Note item){
            tVDiaryTodo.setText(item.getTodo());
        }

        // 아이템들을 담은 리니어레이아웃을 보여주는 메서드.
        public void setLayout(){
            layoutTodoItem.setVisibility(View.VISIBLE);
        }

        public void setCheck(int checktodo){
            if(checktodo == 1){
                btnDiaryCheckTodo.setSelected(true);
                todoCardView.setCardBackgroundColor(Color.rgb(0xFF, 0xCC, 0xB2));
                tVDiaryTodo.setPaintFlags(tVDiaryTodo.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else{
                btnDiaryCheckTodo.setSelected(false);
                todoCardView.setCardBackgroundColor(Color.WHITE);
                tVDiaryTodo.setPaintFlags(0);
            }
        }

        public int setchecktodo(Note item){
         return item.getChecktodo();
        }

    }

    //배열의 아이템을 가리키는 메서드.
    public void setItems(ArrayList<Note> items) {
        this.items = items;
    }

}