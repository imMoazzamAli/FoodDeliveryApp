package com.example.user.practicalwork2.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.practicalwork2.R;


public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.RecycleViewHolder> {

    private String[] data;

    public RecycleAdapter(String[] d) {
        data = d;
    }

    @NonNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_companies, parent, false);
        return new RecycleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewHolder holder, int position) {
        String title = data[position];
        holder.txtTitle.setText(title);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class RecycleViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView txtTitle;

        public RecycleViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.customTitle);
        }
    }
}
