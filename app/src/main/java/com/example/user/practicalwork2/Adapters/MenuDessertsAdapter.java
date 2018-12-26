package com.example.user.practicalwork2.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.practicalwork2.ExpandMenuActivity;
import com.example.user.practicalwork2.Models.ModelMenuDesserts;
import com.example.user.practicalwork2.Models.ModelMenuDrinks;
import com.example.user.practicalwork2.Models.ModelPreOrder;
import com.example.user.practicalwork2.R;

import java.util.ArrayList;

public class MenuDessertsAdapter extends ArrayAdapter<ModelMenuDesserts> {

    int total = 0;

    ArrayList<ModelMenuDesserts> modelMenuDessertsList;
    ArrayList<ModelPreOrder> dessertList;

    View view;
    Context context;
    LayoutInflater inflater;

    int[] arrayQuantity;

    public MenuDessertsAdapter(@NonNull Context context, ArrayList<ModelMenuDesserts> modelMenuDessertsList) {
        super(context, '0', modelMenuDessertsList);

        this.modelMenuDessertsList = modelMenuDessertsList;
        this.context = context;

        dessertList = new ArrayList<>();
        arrayQuantity = new int[modelMenuDessertsList.size()];

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            view = inflater.inflate(R.layout.row_expand, null);
        }

        final TextView title = view.findViewById(R.id.txtExpandTitle);
        TextView description = view.findViewById(R.id.dd);
        final TextView price = view.findViewById(R.id.txtExpandPrice);

        ModelMenuDesserts modelMenuDesserts = modelMenuDessertsList.get(position);


        title.setText(modelMenuDesserts.getDessertName());
        description.setText(modelMenuDesserts.getDessertDescription());
        price.setText("$" + modelMenuDesserts.getDessertPrice());

        final int singlePriceDrink = modelMenuDessertsList.get(position).getDessertPrice();

        final TextView textViewQuantity = view.findViewById(R.id.tvQuantity);

        Button btnExpand = view.findViewById(R.id.btnExpand);

        btnExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                total = total + singlePriceDrink;

                ModelMenuDesserts obj = modelMenuDessertsList.get(position);
                dessertList.add(new ModelPreOrder(obj.getDessertPrice(), obj.getDessertName(), obj.getDessertDescription(),
                        obj.getDessertcompanyName()));

                Intent intent = new Intent("price_intent");
                intent.putExtra("price", singlePriceDrink);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                Intent in = new Intent("dessert_intent");
                in.putExtra("dessert_list", dessertList);
                LocalBroadcastManager.getInstance(context).sendBroadcast(in);

                for (int i = 0; i < modelMenuDessertsList.size(); i++) {

                    if (i == position) {
                        arrayQuantity[i] = arrayQuantity[i] + 1;
                        textViewQuantity.setText(arrayQuantity[i] + "");

                    }
                }

            }
        });


        Button btnExpandMinus = view.findViewById(R.id.btnExpandMinus);
        btnExpandMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<ModelPreOrder> dessertList = ExpandMenuActivity.getInstance().dessertList;

                if (!dessertList.isEmpty()) {
                    for (int i = 0; i < dessertList.size(); i = i) {
                        if (dessertList.get(i).getTitle().equals(modelMenuDessertsList.get(position).getDessertName())) {
                            ExpandMenuActivity.getInstance().finalPrice = ExpandMenuActivity.getInstance().finalPrice - dessertList.get(i).getPrice();
                            dessertList.remove(i);
                            break;
                        } else {
                            i++;
                        }
                    }
                }

                ExpandMenuActivity.getInstance().orderText.setText(ExpandMenuActivity.getInstance().finalPrice + " $");


                for (int i = 0; i < modelMenuDessertsList.size(); i++) {

                    if (i == position) {
                        if (arrayQuantity[i] != 0) {
                            arrayQuantity[i] = arrayQuantity[i] - 1;
                            textViewQuantity.setText(arrayQuantity[i] + "");
                        }

                    }
                }

            }
        });


        return view;
    }
}
