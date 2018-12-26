package com.example.user.practicalwork2.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.user.practicalwork2.Models.ModelFinalOrder;
import com.example.user.practicalwork2.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class OrderHistoryMoreAdapter extends ArrayAdapter<ModelFinalOrder> {

    ArrayList<ModelFinalOrder> modelFinalOrders;

    LayoutInflater inflater;
    Activity context;
    View view;

    public OrderHistoryMoreAdapter(Activity context, ArrayList<ModelFinalOrder> modelFinalOrders) {
        super(context, 0, modelFinalOrders);

        this.context = context;
        this.modelFinalOrders = modelFinalOrders;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        inflater = LayoutInflater.from(getContext());
        view = inflater.inflate(R.layout.order_history_more_row, null);


        TextView quantity = view.findViewById(R.id.txtFinalQuantity);
        TextView title = view.findViewById(R.id.txtFinalTitle);
        TextView price = view.findViewById(R.id.txtFinalPrice);

        quantity.setText("" + modelFinalOrders.get(position).getOrderQuantity());
        title.setText(modelFinalOrders.get(position).getOrderName());
        price.setText("$ " + modelFinalOrders.get(position).getOrderPrice());

        return view;
    }
}
