package com.example.chatbot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class FoodListAdater extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Food> foodsList;

    public FoodListAdater(Context context, int layout, ArrayList<Food> foodsList) {
        this.context = context;
        this.layout = layout;
        this.foodsList = foodsList;
    }

    @Override
    public int getCount() {
        return foodsList.size();
    }

    @Override
    public Object getItem(int position) {
        return foodsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder{
        ImageView imageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder = new ViewHolder();

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.imageView = (ImageView) row.findViewById(R.id.gallery_imageView);
            row.setTag(holder);
        }else{
            holder = (ViewHolder) row.getTag();
        }

        Bitmap bitmap = getBitmap(position);

        /*
        Food food = foodsList.get(position);

        byte[] foodImage = food.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(foodImage, 0, foodImage.length);
        */
        holder.imageView.setImageBitmap(bitmap);

        return null;
    }

    public Bitmap getBitmap(int position){
        Food food = foodsList.get(position);

        byte[] foodImage = food.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(foodImage, 0, foodImage.length);

        return bitmap;
    }
}
