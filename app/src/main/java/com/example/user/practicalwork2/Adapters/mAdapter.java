package com.example.user.practicalwork2.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.practicalwork2.Models.ModelExpandAble;
import com.example.user.practicalwork2.R;
import com.jaychang.srv.SimpleCell;
import com.jaychang.srv.SimpleViewHolder;

public class mAdapter extends SimpleCell<ModelExpandAble, mAdapter.ViewHolder> {

    public mAdapter(@NonNull ModelExpandAble item) {
        super(item);
    }

    @Override
    protected int getLayoutRes() {
        //The child element
        return R.layout.simple_model;
    }

    @NonNull
    @Override
    protected mAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, @NonNull View cellView) {
        return new mAdapter.ViewHolder(cellView);
    }

    @Override
    protected void onBindViewHolder(@NonNull mAdapter.ViewHolder viewHolder, int position, @NonNull Context context, Object payload) {
        viewHolder.titleTxt.setText(getItem().getmName());
        viewHolder.descTxt.setText(getItem().getmDescription());
        viewHolder.img.setImageResource(R.drawable.ic_delete_black_24dp);
    }

    static class ViewHolder extends SimpleViewHolder {
        TextView titleTxt, descTxt;
        ImageView img;

        ViewHolder(View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.nameTxt);
            descTxt = itemView.findViewById(R.id.descTxt);
            img = itemView.findViewById(R.id.galaxyImageview);

        }
    }

}
