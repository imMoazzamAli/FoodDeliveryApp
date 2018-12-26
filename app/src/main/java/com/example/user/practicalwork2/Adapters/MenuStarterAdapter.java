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
import android.widget.BaseAdapter;
import android.widget.Button;

import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.practicalwork2.CartOrderActivity;
import com.example.user.practicalwork2.ExpandAbleActivity;
import com.example.user.practicalwork2.ExpandMenuActivity;
import com.example.user.practicalwork2.Models.ModelMenuStarter;
import com.example.user.practicalwork2.Models.ModelPreOrder;
import com.example.user.practicalwork2.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MenuStarterAdapter extends ArrayAdapter<ModelMenuStarter> {

    public String name = "";

    private ArrayList<ModelPreOrder> starterList;
    private List<ModelMenuStarter> modelMenuStarterList;

    private Activity context;
    LayoutInflater inflater;
    View view;

    int quantity = 0;

    public int[] arrayQuantity;

    static MenuStarterAdapter menuStarterAdapter;

    public MenuStarterAdapter(Activity context, List<ModelMenuStarter> modelMenuStarterList) {

        super(context, '0', modelMenuStarterList);

        this.context = context;
        this.modelMenuStarterList = modelMenuStarterList;

        starterList = new ArrayList<ModelPreOrder>();
        arrayQuantity = new int[modelMenuStarterList.size() + modelMenuStarterList.size() + 1];

        menuStarterAdapter = this;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);


        LocalBroadcastManager.getInstance(context).registerReceiver(showArrayQuantity, new IntentFilter("array_intent"));
    }

    public static MenuStarterAdapter getInstance() {
        return menuStarterAdapter;
    }

    //receiving intent from cartOrderAdapter to change quantity array on base of name at specific position
    public BroadcastReceiver showArrayQuantity = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            //arrayQuantity = intent.getIntArrayExtra("array");
            name = intent.getStringExtra("name");

            for (int i = 0; i < modelMenuStarterList.size(); i++) {
                if (name.equals(modelMenuStarterList.get(i).getStarterName())) {
                    arrayQuantity[i]--;

                    TextView textView = ExpandMenuActivity.getInstance().lvStarter.getChildAt(i).findViewById(R.id.tvQuantity);
                    textView.setText(arrayQuantity[i] + "");

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

        ModelMenuStarter modelMenuStarter = modelMenuStarterList.get(position);

        title.setText(modelMenuStarter.getStarterName());
        description.setText(modelMenuStarter.getStarterDescription());
        price.setText("$" + modelMenuStarter.getStarterPrice());

        final int singlePrice = modelMenuStarterList.get(position).getStarterPrice();

        final TextView textViewQuantity = view.findViewById(R.id.tvQuantity);

        final Button btnExpand = (Button) view.findViewById(R.id.btnExpand);
        btnExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ModelMenuStarter obj = modelMenuStarterList.get(position);
                starterList.add(new ModelPreOrder(obj.getStarterPrice(), obj.getStarterName(), obj.getStarterDescription(), obj.getStartercompanyName()));

                Intent intent = new Intent("price_intent");
                intent.putExtra("price", singlePrice);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                Intent starterIntent = new Intent("starter_intent");
                starterIntent.putExtra("starter_list", starterList);
                LocalBroadcastManager.getInstance(context).sendBroadcast(starterIntent);

                for (int i = 0; i < modelMenuStarterList.size(); i++) {

                    if (i == position) {
                        arrayQuantity[i] = arrayQuantity[i] + 1;
                        textViewQuantity.setText(arrayQuantity[i] + "");
                    }
                }
            }
        });

        final Button btnExpandMinus = view.findViewById(R.id.btnExpandMinus);
        btnExpandMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<ModelPreOrder> starterList = ExpandMenuActivity.getInstance().starterList;

                if (!starterList.isEmpty()) {

                    for (int i = 0; i < starterList.size(); i = i) {
                        if (starterList.get(i).getTitle().equals(modelMenuStarterList.get(position).getStarterName())) {
                            ExpandMenuActivity.getInstance().finalPrice = ExpandMenuActivity.getInstance().finalPrice - starterList.get(i).getPrice();
                            starterList.remove(i);
                            break;
                        } else {
                            i++;
                        }
                    }
                }

                ExpandMenuActivity.getInstance().orderText.setText(ExpandMenuActivity.getInstance().finalPrice + " $");

                for (int i = 0; i < modelMenuStarterList.size(); i++) {

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
