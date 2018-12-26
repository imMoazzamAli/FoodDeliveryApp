package com.example.user.practicalwork2.Adapters;

import android.app.Activity;
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

public class ConfirmOrderAdapter extends ArrayAdapter<ModelFinalOrder> {

    private ArrayList<String> priceFinal;

    private ArrayList<ModelFinalOrder> modelFinalOrders;
    private LayoutInflater inflater;
    View view;

    public ConfirmOrderAdapter(Activity context, ArrayList<ModelFinalOrder> modelFinalOrders) {

        super(context, '0', modelFinalOrders);

        this.priceFinal = priceFinal;
        this.modelFinalOrders = modelFinalOrders;

        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        inflater = LayoutInflater.from(getContext());
        view = inflater.inflate(R.layout.row_final, null);

        /*final String singlePrices = priceFinal.get(position);
        TextView price = (TextView) view.findViewById(R.id.txtFinalPrice);
        price.setText(singlePrices);*/

        TextView price = view.findViewById(R.id.txtFinalPrice);
        TextView title = view.findViewById(R.id.txtFinalTitle);
        TextView quantity = view.findViewById(R.id.txtFinalQuantity);

        quantity.setText(modelFinalOrders.get(position).getOrderQuantity() + "");
        price.setText("$" + modelFinalOrders.get(position).getOrderPrice());
        title.setText(modelFinalOrders.get(position).getOrderName());

        return view;
    }

}
