package com.example.chatbot.TipView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.chatbot.R;

public class TipFragment extends Fragment {

    private View view;
    ImageView tip1, tip2, tip3, tip4, tip5;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tip, container, false);

        tip1 = view.findViewById(R.id.tip1);
        tip2 = view.findViewById(R.id.tip2);
        tip3 = view.findViewById(R.id.tip3);
        tip4 = view.findViewById(R.id.tip4);
        tip5 = view.findViewById(R.id.tip5);

        tip1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), TipActivity.class);
                startActivity(intent);

            }
        });

        tip2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Tip2Activity.class);
                startActivity(intent);
            }
        });

        tip3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Tip3Activity.class);
                startActivity(intent);
            }
        });

        tip4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Tip4Activity.class);
                startActivity(intent);
            }
        });

        tip5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Tip5Activity.class);
                startActivity(intent);
            }
        });

        return view;
    }



}
