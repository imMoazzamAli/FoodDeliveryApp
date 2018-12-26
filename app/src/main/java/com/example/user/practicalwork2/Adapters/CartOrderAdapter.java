package com.example.user.practicalwork2.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.user.practicalwork2.CartOrderActivity;
import com.example.user.practicalwork2.ConfirmOrderActivity;
import com.example.user.practicalwork2.ExpandMenuActivity;
import com.example.user.practicalwork2.Models.ModelFinalOrder;
import com.example.user.practicalwork2.Models.ModelPreOrder;
import com.example.user.practicalwork2.R;

import java.util.ArrayList;

public class CartOrderAdapter extends ArrayAdapter<ModelFinalOrder> {

    ImageView imgEdit, imgDelete;

    private ArrayList<ModelFinalOrder> modelFinalOrders;
    private LayoutInflater inflater;
    View view;
    Context context;

    public CartOrderAdapter(@NonNull Context context, ArrayList<ModelFinalOrder> modelFinalOrders) {
        super(context, '0', modelFinalOrders);

        this.modelFinalOrders = modelFinalOrders;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        inflater = LayoutInflater.from(getContext());
        view = inflater.inflate(R.layout.row_cart_orders, null);


        /*final String singlePrices = priceFinal.get(position);
        TextView price = (TextView) view.findViewById(R.id.txtFinalPrice);
        price.setText(singlePrices);*/

        TextView price = view.findViewById(R.id.txtFinalPrice);
        TextView title = view.findViewById(R.id.txtFinalTitle);
        TextView quantity = view.findViewById(R.id.txtFinalQuantity);

        quantity.setText(modelFinalOrders.get(position).getOrderQuantity() + "");
        price.setText("$" + modelFinalOrders.get(position).getOrderPrice());
        title.setText(modelFinalOrders.get(position).getOrderName());


        imgEdit = view.findViewById(R.id.imgEdit);
        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) getContext()).finish();
            }
        });

        imgDelete = view.findViewById(R.id.imgDelete);
        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<ModelPreOrder> starterList = ExpandMenuActivity.getInstance().starterList;
                ArrayList<ModelPreOrder> drinkList = ExpandMenuActivity.getInstance().drinkList;
                ArrayList<ModelPreOrder> dessertList = ExpandMenuActivity.getInstance().dessertList;

                //int[] arrayQuantityStarter = MenuStarterAdapter.getInstance().arrayQuantityStarter;

                if (!starterList.isEmpty()) {

                    for (int i = 0; i < starterList.size(); i = i) {
                        if (starterList.get(i).getTitle().equals(modelFinalOrders.get(position).getOrderName())) {
                            ExpandMenuActivity.getInstance().finalPrice = ExpandMenuActivity.getInstance().finalPrice - starterList.get(i).getPrice();
                            starterList.remove(i);

                            String name = modelFinalOrders.get(position).getOrderName();
                            Intent intent = new Intent("array_intent");
                            intent.putExtra("name", name);
                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                        } else {
                            i++;
                        }
                    }
                }
                if (!drinkList.isEmpty()) {
                    for (int i = 0; i < drinkList.size(); i = i) {
                        if (drinkList.get(i).getTitle().equals(modelFinalOrders.get(position).getOrderName())) {
                            ExpandMenuActivity.getInstance().finalPrice = ExpandMenuActivity.getInstance().finalPrice - drinkList.get(i).getPrice();
                            drinkList.remove(i);

                            //Passing intent to MenuStarterAdapter for updation of quantity array on bases of name
                            String name = modelFinalOrders.get(position).getOrderName();
                            Intent intent = new Intent("array_intent");
                            intent.putExtra("name", name);
                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                        } else {
                            i++;
                        }
                    }
                }

                if (!dessertList.isEmpty()) {
                    for (int i = 0; i < drinkList.size(); i = i) {
                        if (drinkList.get(i).getTitle().equals(modelFinalOrders.get(position).getOrderName())) {
                            ExpandMenuActivity.getInstance().finalPrice = ExpandMenuActivity.getInstance().finalPrice - drinkList.get(i).getPrice();
                            drinkList.remove(i);


                        } else {
                            i++;
                        }
                    }
                }

                /*ArrayList<ModelFinalOrder> finalList = ConfirmOrderActivity.getInstance().finalOrdersList;
                if (ConfirmOrderActivity.getInstance().statusConfirmActivity) {

                    for (int i = 0; i < finalList.size(); i++) {
                        if (finalList.get(i).getOrderName().equals(modelFinalOrders.get(i).getOrderName())) {
                            finalList.remove(i);
                            notifyDataSetChanged();
                        }
                    }

                }*/

                ExpandMenuActivity.getInstance().orderText.setText(ExpandMenuActivity.getInstance().finalPrice + " $");
                //MenuStarterAdapter.getInstance().textViewQuantity.setText(MenuStarterAdapter.getInstance().quantity + "");

                modelFinalOrders.remove(position);
                notifyDataSetChanged();

                if (modelFinalOrders.isEmpty()) {
                    CartOrderActivity.getInstance().rlAll.setVisibility(View.INVISIBLE);
                    CartOrderActivity.getInstance().tvEmptyCart.setVisibility(View.VISIBLE);
                }
            }
        });

        return view;
    }
}
