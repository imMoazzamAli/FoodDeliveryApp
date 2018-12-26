package com.example.user.practicalwork2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.practicalwork2.Adapters.CustomExpandAbleListAdapter;
import com.example.user.practicalwork2.Models.ModelCompanies;
import com.example.user.practicalwork2.Models.ModelExpandAble;
import com.example.user.practicalwork2.Models.ModelMainMenu;
import com.example.user.practicalwork2.Models.ModelMenuDesserts;
import com.example.user.practicalwork2.Models.ModelMenuDrinks;
import com.example.user.practicalwork2.Models.ModelMenuStarter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandAbleActivity extends DrawerActivity implements View.OnClickListener {

    static ExpandAbleActivity expActivity;

    public TextView orderText;
    public ExpandableListView lvExpand;

    ModelCompanies mc;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbReferenceMainMenu;
    DatabaseReference dbReferenceStarter;
    DatabaseReference dbReferenceDrinks;
    DatabaseReference dbReferenceDesserts;

    DatabaseReference dbReferenceStarter1;

    private ArrayList<String> listDataHeader;

    ArrayList<ModelMenuStarter> listStarterData;
    ArrayList<ModelMenuDrinks> listDrinksData;
    ArrayList<ModelMenuDesserts> listDessertData;

    ArrayList<ModelExpandAble> listStarterData1;
    ArrayList<ModelExpandAble> listDrinkData1;

    private HashMap<String, List<ModelMenuStarter>> listHash;
    private HashMap<String, List<ModelExpandAble>> listHash1;
    CustomExpandAbleListAdapter adapter;

    public ArrayList<ModelExpandAble> listReceiving;
    public int finalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_expand_able);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.activity_expand_able, null, false);
        drawer.addView(convertView);

        expActivity = this;

        firebaseDatabase = FirebaseDatabase.getInstance();
        dbReferenceMainMenu = firebaseDatabase.getReference("COMPANYMENU");
        dbReferenceStarter = firebaseDatabase.getReference("Starter");
        dbReferenceDrinks = firebaseDatabase.getReference("DRINKS");
        dbReferenceDesserts = firebaseDatabase.getReference("Desserts");

        dbReferenceStarter1 = firebaseDatabase.getReference("Starter1");

        listStarterData = new ArrayList<>();
        listDrinksData = new ArrayList<>();
        listDessertData = new ArrayList<>();

        listStarterData1 = new ArrayList<>();
        listDrinkData1 = new ArrayList<>();

        listReceiving = new ArrayList<ModelExpandAble>();

        orderText = (TextView) findViewById(R.id.ordertxt);
        lvExpand = findViewById(R.id.lvExpand);
        //lvExpand.setIndicatorBounds(lvExpand.getRight() - 40, lvExpand.getWidth());

        LocalBroadcastManager.getInstance(this).registerReceiver(showFinalPrice, new IntentFilter("price_intent"));

        LocalBroadcastManager.getInstance(this).registerReceiver(showListData, new IntentFilter("list_intent"));

        getMenuHere();

        /*lvExpand.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {
                lvExpand.setSelection(i);
            }
        });*/


//        listView.setSelectedGroup(groupPosition);
//        mExpandableList.expandGroup(groupPosition);
//        mExpandableList.setSelectionFromTop(groupPosition, 0);
//        ListView.setSelection(position)


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(expActivity, "onResultToast", Toast.LENGTH_SHORT).show();

        lvExpand.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {
                lvExpand.setSelection(i);
            }
        });

    }

    public static ExpandAbleActivity getInstance() {
        return expActivity;
    }

    public void getMenuHere() {

        Intent intent = getIntent();
        mc = (ModelCompanies) intent.getSerializableExtra("companies");

        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();
        listHash1 = new HashMap<>();

        dbReferenceMainMenu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot menuSnapshot : dataSnapshot.getChildren()) {

                    ModelMainMenu dataMenu = menuSnapshot.getValue(ModelMainMenu.class);
                    String companyTitle = dataMenu.getMmcompanyTitle();

                    if (companyTitle.equals(mc.getCompanyTitle())) {
                        listDataHeader.add(dataMenu.getMmName());
                    }
                }

                getStarter1ListData();
                adapter = new CustomExpandAbleListAdapter(ExpandAbleActivity.this, listDataHeader, listHash1);
                lvExpand.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void getStarter1ListData() {
        dbReferenceStarter1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot starter1Snapshot : dataSnapshot.getChildren()) {

                    ModelExpandAble modelExpandAble = starter1Snapshot.getValue(ModelExpandAble.class);

                    String companyName = modelExpandAble.getmCompanyName();
                    String mmName = modelExpandAble.getMmName();

                    if (companyName.equals(mc.getCompanyTitle()) && mmName.equals("Starter")) {
                        listStarterData1.add(modelExpandAble);
                    }

                    if (companyName.equals(mc.getCompanyTitle()) && mmName.equals("Drinks")) {
                        listDrinkData1.add(modelExpandAble);
                    }

                }
                for (int i = 0; i < listDataHeader.size(); i++) {

                    if (listDataHeader.get(i).equals("Starter")) {
                        listHash1.put(listDataHeader.get(i), listStarterData1);
                    }

                    if (listDataHeader.get(i).equals("Drinks")) {
                        listHash1.put(listDataHeader.get(i), listDrinkData1);
                    }
                    if (listDataHeader.get(i).equals("Desserts")) {

                    }
                    if (listDataHeader.get(0).equals("Starter1")) {

                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public BroadcastReceiver showFinalPrice = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int price = intent.getIntExtra("price", 0);

            finalPrice = finalPrice + price;

            orderText.setText(finalPrice + " $");
        }
    };

    public BroadcastReceiver showListData = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            listReceiving = (ArrayList<ModelExpandAble>) intent.getSerializableExtra("list_data");
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {

    }
}
