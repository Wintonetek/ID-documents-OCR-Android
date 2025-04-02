package com.kernal.passportreader.sdk.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.kernal.passportreader.myapplication.R;

import java.util.ArrayList;
import java.util.List;


public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.MyViewHolder> {
    private List<ResultData> mResultDatas;
    private Context context;

    private UpDataCallback upDataCallback;

    public ResultAdapter(Context context,UpDataCallback upDataCallback) {
        this.context = context;
        this.upDataCallback = upDataCallback;
    }


    public void setResultDatas(List<ResultData> mResultDatas) {
        this.mResultDatas = mResultDatas;
        notifyDataSetChanged();
    }


    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(View.inflate(this.context, R.layout.result_item, null));
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, @SuppressLint("RecyclerView") int position) {
        ResultData resultData = mResultDatas.get(position);
        System.out.println("resultData"+ resultData.getKey()+"--"+resultData.getValue());
        myViewHolder.key.setText(resultData.getKey());
        myViewHolder.value.setText(resultData.getValue());
        switch (resultData.getIconType()) {
            case 1:
                //不显示
                myViewHolder.icon.setVisibility(View.GONE);
                myViewHolder.value.setEnabled(true);
                break;
            case 2:
                // ocr mrz
                myViewHolder.icon.setVisibility(View.VISIBLE);
                myViewHolder.icon.setImageResource(R.mipmap.mrz);
                myViewHolder.value.setEnabled(true);
                break;
            case 3:
                //viz
                myViewHolder.icon.setVisibility(View.VISIBLE);
                myViewHolder.icon.setImageResource(R.mipmap.viz);
                myViewHolder.value.setEnabled(true);
                break;
            case 4:
                //芯片
                myViewHolder.icon.setVisibility(View.VISIBLE);
                myViewHolder.icon.setImageResource(R.mipmap.chip);
                myViewHolder.value.setEnabled(false);
                break;
        }
        myViewHolder.value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (upDataCallback!=null){
                    resultData.setValue(s.toString());
                    upDataCallback.updata(position,resultData);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.mResultDatas.size();
    }

    @Override
    public void onViewRecycled(@NonNull MyViewHolder holder) {
        super.onViewRecycled(holder);
        holder.itemView.setTag(null);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView key;
        EditText value;
        ImageView icon;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            key = itemView.findViewById(R.id.key);
            value = itemView.findViewById(R.id.value);
            icon = itemView.findViewById(R.id.icon);
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams((int) (itemView.getResources().getDisplayMetrics().widthPixels * 0.9), ConstraintLayout.LayoutParams.WRAP_CONTENT);
            itemView.setLayoutParams(layoutParams);

        }
    }
    public interface UpDataCallback{
        void updata(int position,ResultData resultData);
    }
}
