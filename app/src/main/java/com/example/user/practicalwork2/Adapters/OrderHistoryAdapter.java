package com.example.user.practicalwork2.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.view.menu.ActionMenuItem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.practicalwork2.Models.ModelFinalOrder;
import com.example.user.practicalwork2.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class OrderHistoryAdapter extends ArrayAdapter<ModelFinalOrder> {

    private ArrayList<ModelFinalOrder> modelFinalOrders;

    int[] priceArray;

    LayoutInflater inflater;
    Activity context;
    View view;

    public OrderHistoryAdapter(@NonNull Context context, ArrayList<ModelFinalOrder> modelFinalOrders, int[] priceArray) {

        super(context, '0', modelFinalOrders);
        this.modelFinalOrders = modelFinalOrders;


        this.priceArray = priceArray;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        inflater = LayoutInflater.from(getContext());
        view = inflater.inflate(R.layout.order_history_row, null);

        TextView txtHistoryName = view.findViewById(R.id.txtHistoryName);
        TextView txtHistoryDate = view.findViewById(R.id.txtHistoryDate);
        TextView txtHistoryTime = view.findViewById(R.id.txtHistoryTime);
        ImageView imgHistoryCompany = view.findViewById(R.id.imgHistoryCompany);
        TextView txtHistoryTotal = view.findViewById(R.id.txtHistoryTotal);

        txtHistoryName.setText(modelFinalOrders.get(position).getOrderCompanyName());
        txtHistoryDate.setText(modelFinalOrders.get(position).getOrderDate());
        txtHistoryTime.setText(modelFinalOrders.get(position).getOrderTime());
        Picasso.get().load(modelFinalOrders.get(position).getOrderCompanyUrl()).into(imgHistoryCompany);
        //txtHistoryTotal.setText(" $% " + priceTotal);

        txtHistoryTotal.setText(" $ " + priceArray[position]);

        return view;
    }
}
