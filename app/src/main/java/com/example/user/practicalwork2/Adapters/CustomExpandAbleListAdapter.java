package com.example.user.practicalwork2.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.practicalwork2.ExpandAbleActivity;
import com.example.user.practicalwork2.ExpandMenuActivity;
import com.example.user.practicalwork2.Models.ModelExpandAble;
import com.example.user.practicalwork2.Models.ModelMenuStarter;
import com.example.user.practicalwork2.Models.ModelPreOrder;
import com.example.user.practicalwork2.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CustomExpandAbleListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listDataHeader;
    private HashMap<String, List<ModelExpandAble>> listHashMap;


    private ArrayList<ModelExpandAble> listData;

    private int[] arrayQuantity;

    private int[][] multi;


    public CustomExpandAbleListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<ModelExpandAble>> listHashMap) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listHashMap = listHashMap;

        listData = new ArrayList<>();
        arrayQuantity = new int[listDataHeader.size() + listHashMap.size()];

        multi = new int[listDataHeader.size()][listDataHeader.size()];

    }

    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return listHashMap.get(listDataHeader.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return listDataHeader.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return listHashMap.get(listDataHeader.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {

        //String to get group i.e. Starter, Drinks, Desserts etc
        String titleGroup = (String) getGroup(i);

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_expanable_list_group, null);
        }

        //showing it in textView in defined layout
        TextView tvTitle = view.findViewById(R.id.tvGroupHeader);
        tvTitle.setText(titleGroup);

        return view;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean b, View view, ViewGroup viewGroup) {

        final ModelExpandAble modelExpandAble = (ModelExpandAble) getChild(groupPosition, childPosition);
        //String title = (String) getChild(i, i1);
        //String description = (String) getChild(i, i1);
//        String title = (String) getChild(i, i1);

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_expanable_list_item, null);
        }

        final TextView tvTitle = view.findViewById(R.id.txtExpandTitle);
        final TextView tvDescription = view.findViewById(R.id.dd);
        TextView tvPrice = view.findViewById(R.id.txtExpandPrice);

        tvTitle.setText(modelExpandAble.getmName());
        tvDescription.setText(modelExpandAble.getmDescription());
        tvPrice.setText(modelExpandAble.getmPrice() + "");

        final int singlePrice = modelExpandAble.getmPrice();

        final TextView textViewQuantity = view.findViewById(R.id.tvQuantity);

        Button btnPlus = view.findViewById(R.id.btnExpand);
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final int random = new Random().nextInt(61) + 20;

                ModelExpandAble obj = modelExpandAble;
                listData.add(new ModelExpandAble(random, obj.getmPrice(), obj.getmName(), obj.getmDescription(), obj.getmCompanyName(), obj.getMmName()));

                //Toast.makeText(context, "Title: " + listData.get(childPosition).getTitle(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent("price_intent");
                intent.putExtra("price", singlePrice);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                Intent starterIntent = new Intent("list_intent");
                starterIntent.putExtra("list_data", listData);
                LocalBroadcastManager.getInstance(context).sendBroadcast(starterIntent);

                for (int i = 0; i <= groupPosition; i++) {
                    for (int j = 0; j <= childPosition; j++) {
                        if (i == groupPosition && j == childPosition) {
                            multi[i][j] = multi[i][j] + 1;
                            textViewQuantity.setText(multi[i][j] + "");
                        }
                    }
                }
            }
        });

        Button btnMinus = view.findViewById(R.id.btnExpandMinus);
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<ModelExpandAble> listReceiving1 = ExpandAbleActivity.getInstance().listReceiving;

                if (!listReceiving1.isEmpty()) {
                    for (int i = 0; i < listReceiving1.size(); i = i) {

                        String rowValueName = modelExpandAble.getmName();
//                        String listValueName = listReceiving1.get(i).getmName();

                        if (ExpandAbleActivity.getInstance().listReceiving.get(i).getmName().equals(rowValueName)) {
                            ExpandAbleActivity.getInstance().finalPrice = ExpandAbleActivity.getInstance().finalPrice - listReceiving1.get(i).getmPrice();
                            listReceiving1.remove(i);
                            break;

                        } else {
                            i++;
                        }
                    }

                }

                ExpandAbleActivity.getInstance().orderText.setText(ExpandAbleActivity.getInstance().finalPrice + " $");

                for (int i = 0; i <= groupPosition; i++) {
                    for (int j = 0; j <= childPosition; j++) {
                        if (i == groupPosition && j == childPosition) {
                            if (multi[i][j] != 0) {
                                multi[i][j] = multi[i][j] - 1;
                                textViewQuantity.setText(multi[i][j] + "");
                            }
                        }
                    }
                }


            }
        });

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

/*
    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);

        notifyDataSetChanged();
    }*/

}
