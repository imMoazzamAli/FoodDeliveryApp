package com.example.user.practicalwork2.Adapters;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.practicalwork2.ExpandMenuActivity;
import com.example.user.practicalwork2.Models.ModelMenuDrinks;
import com.example.user.practicalwork2.Models.ModelPreOrder;
import com.example.user.practicalwork2.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MenuDrinksAdapter extends ArrayAdapter<ModelMenuDrinks> {

    int total = 0;

    int[] arrayQuantityDrink;

    List<ModelMenuDrinks> modelMenuDrinksList;
    ArrayList<ModelPreOrder> drinkList;

    Activity context;
    View view;
    LayoutInflater inflater;

    Button btnOrder;

    public MenuDrinksAdapter(Activity context, List<ModelMenuDrinks> modelMenuDrinksList) {
        super(context, '0', modelMenuDrinksList);

        this.context = context;
        this.modelMenuDrinksList = modelMenuDrinksList;
        this.btnOrder = btnOrder;
        drinkList = new ArrayList<>();

        arrayQuantityDrink = new int[modelMenuDrinksList.size() + 1];

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LocalBroadcastManager.getInstance(context).registerReceiver(showArrayQuantity, new IntentFilter("array_intent"));
    }

    public BroadcastReceiver showArrayQuantity = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            //arrayQuantity = intent.getIntArrayExtra("array");
            String name = intent.getStringExtra("name");

            for (int i = 0; i < modelMenuDrinksList.size(); i++) {
                if (name.equals(modelMenuDrinksList.get(i).getDrinkName())) {
                    arrayQuantityDrink[i]--;

                    TextView textView = ExpandMenuActivity.getInstance().lvDrinks.getChildAt(i).findViewById(R.id.tvQuantity);
                    textView.setText(arrayQuantityDrink[i] + "");
                }
            }
        }
    };

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            view = inflater.inflate(R.layout.row_expand, null);
        }

        final TextView title = view.findViewById(R.id.txtExpandTitle);
        TextView description = view.findViewById(R.id.dd);
        final TextView price = view.findViewById(R.id.txtExpandPrice);

        final ModelMenuDrinks modelMenuDrinks = modelMenuDrinksList.get(position);

        title.setText(modelMenuDrinks.getDrinkName());
        description.setText(modelMenuDrinks.getDrinkDescription());
        price.setText("$" + modelMenuDrinks.getDrinkPrice());

        final int singlePriceDrink = modelMenuDrinksList.get(position).getDrinkPrice();

        final TextView textViewQuantity = view.findViewById(R.id.tvQuantity);

        Button btnExpand = view.findViewById(R.id.btnExpand);
        btnExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                total = total + singlePriceDrink;

                ModelMenuDrinks obj = modelMenuDrinksList.get(position);
                drinkList.add(new ModelPreOrder(obj.getDrinkPrice(), obj.getDrinkName(), obj.getDrinkDescription(), obj.getDrinkcompanyName()));

                Intent intent = new Intent("price_intent");
                intent.putExtra("price", singlePriceDrink);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                Intent in = new Intent("drink_intent");
                in.putExtra("drink_list", drinkList);
                LocalBroadcastManager.getInstance(context).sendBroadcast(in);


                for (int i = 0; i < modelMenuDrinksList.size(); i++) {

                    if (i == position) {
                        arrayQuantityDrink[i] = arrayQuantityDrink[i] + 1;
                        textViewQuantity.setText(arrayQuantityDrink[i] + "");
                    }
                }

            }
        });


        Button btnExpandMinus = view.findViewById(R.id.btnExpandMinus);
        btnExpandMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<ModelPreOrder> drinkList = ExpandMenuActivity.getInstance().drinkList;

                if (!drinkList.isEmpty()) {
                    for (int i = 0; i < drinkList.size(); i = i) {
                        if (drinkList.get(i).getTitle().equals(modelMenuDrinksList.get(position).getDrinkName())) {
                            ExpandMenuActivity.getInstance().finalPrice = ExpandMenuActivity.getInstance().finalPrice - drinkList.get(i).getPrice();
                            drinkList.remove(i);
                            break;
                        } else {
                            i++;
                        }
                    }
                }

                ExpandMenuActivity.getInstance().orderText.setText(ExpandMenuActivity.getInstance().finalPrice + " $");


                for (int i = 0; i < modelMenuDrinksList.size(); i++) {

                    if (i == position) {
                        if (arrayQuantityDrink[i] != 0) {
                            arrayQuantityDrink[i] = arrayQuantityDrink[i] - 1;
                            textViewQuantity.setText(arrayQuantityDrink[i] + "");
                        }

                    }
                }

            }
        });

        return view;
    }
}
