package com.example.user.practicalwork2.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.practicalwork2.Models.Galaxy;
import com.example.user.practicalwork2.R;
import com.jaychang.srv.SimpleCell;
import com.jaychang.srv.SimpleViewHolder;

public class GalaxyCell extends SimpleCell<Galaxy, GalaxyCell.ViewHolder> {

    public GalaxyCell(@NonNull Galaxy item) {
        super(item);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.simple_model;
    }

    /*
    - Return a ViewHolder instance
     */
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(ViewGroup parent, View cellView) {
        return new ViewHolder(cellView);
    }

    /*
    - Bind data to widgets in our viewholder.
     */
    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull Context context, Object o) {
        viewHolder.titleTxt.setText(getItem().getName());
        viewHolder.descTxt.setText(getItem().getDescription());
        viewHolder.img.setImageResource(getItem().getImage());
    }

    /**
     * - Our ViewHolder class.
     * - Inner static class.
     * Define your view holder, which must extend SimpleViewHolder.
     */
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
