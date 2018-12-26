package com.example.user.practicalwork2;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.practicalwork2.Adapters.MenuDessertsAdapter;
import com.example.user.practicalwork2.Adapters.MenuDrinksAdapter;
import com.example.user.practicalwork2.Adapters.MenuStarterAdapter;
import com.example.user.practicalwork2.Models.ModelCompanies;
import com.example.user.practicalwork2.Models.ModelMenuDrinks;
import com.example.user.practicalwork2.Models.ModelMainMenu;
import com.example.user.practicalwork2.Models.ModelMenuDesserts;
import com.example.user.practicalwork2.Models.ModelMenuStarter;
import com.example.user.practicalwork2.Models.ModelPreOrder;
import com.example.user.practicalwork2.Models.ModelUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ExpandMenuActivity extends AppCompatActivity implements View.OnClickListener {


    RecyclerView recyclerViewMy;

    public ArrayList<ModelPreOrder> starterList;
    public ArrayList<ModelPreOrder> drinkList;
    public ArrayList<ModelPreOrder> dessertList;
    public ArrayList<ModelPreOrder> finalList;

    public int finalPrice = 0;

    ModelCompanies mc;

    ArrayList<ModelMenuStarter> listStarterData;
    ArrayList<ModelMenuDrinks> listDrinksData;
    ArrayList<ModelMenuDesserts> listDessertData;

    ListAdapter adapterStarter;
    ListAdapter adapterDrinks;
    ListAdapter adapterDesserts;

    TextView textViewPlusStarterNoodle, textViewPlusDrinksNoodle, textViewPlusDesserts;
    TextView customTitle, customDescription, customStatus;

    public TextView orderText;
    Button btnOrder;

    ImageView imgCompanies;

    public ListView lvStarter;
    public ListView lvDrinks;
    ListView lvDesserts;

    RelativeLayout relativeLayoutStarter;
    RelativeLayout relativeLayoutDrinks;
    RelativeLayout relativeLayoutDesserts;

    ArrayList<String> titlesExpand;
    ArrayList<String> descriptionsExpand;
    ArrayList<String> priceExpand;

    ArrayList<String> titlesDrinks;
    ArrayList<String> descriptionsDrinks;
    ArrayList<String> priceDrinks;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbReferenceMainMenu;
    DatabaseReference dbReferenceStarter;
    DatabaseReference dbReferenceDrinks;
    DatabaseReference dbReferenceDesserts;
    DatabaseReference dbReferenceUser;

    public ArrayList<ModelPreOrder> cartList;
    ListView cartListView;

    static ExpandMenuActivity expActivity;

    Toolbar toolbarExpandMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expand_menu);

        /*LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_expand_menu, null, false);
        drawer.addView(contentView, 0);*/

        toolbarExpandMenu = findViewById(R.id.toolbarExpandMenu);
        setSupportActionBar(toolbarExpandMenu);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(getLayoutInflater().inflate(R.layout.tool_bar, null),
                new ActionBar.LayoutParams(
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.MATCH_PARENT,
                        Gravity.CENTER
                )
        );
        actionBar.setDisplayHomeAsUpEnabled(true);

        expActivity = this;

        recyclerViewMy = findViewById(R.id.recycleViewMy);

        firebaseDatabase = FirebaseDatabase.getInstance();
        dbReferenceMainMenu = firebaseDatabase.getReference("COMPANYMENU");
        dbReferenceStarter = firebaseDatabase.getReference("Starter");
        dbReferenceDrinks = firebaseDatabase.getReference("DRINKS");
        dbReferenceDesserts = firebaseDatabase.getReference("Desserts");
        dbReferenceUser = firebaseDatabase.getReference("USERS");
        //btnExpand = (Button) findViewById(R.id.btnExpand);
        btnOrder = (Button) findViewById(R.id.btnOrder);

        orderText = (TextView) findViewById(R.id.ordertxt);

        textViewPlusStarterNoodle = (TextView) findViewById(R.id.plusStarterNoodle);
        textViewPlusDrinksNoodle = (TextView) findViewById(R.id.plusDrinksNoodle);
        textViewPlusDesserts = (TextView) findViewById(R.id.plusDesserts);

        customTitle = findViewById(R.id.customTitle);
        customDescription = findViewById(R.id.customDescription);
        customStatus = findViewById(R.id.customStatus);

        imgCompanies = findViewById(R.id.imgCompanies);

        relativeLayoutStarter = (RelativeLayout) findViewById(R.id.relativeLayoutStarter);
        relativeLayoutDrinks = (RelativeLayout) findViewById(R.id.relativeLayoutDrinks);
        relativeLayoutDesserts = (RelativeLayout) findViewById(R.id.relativeLayoutDesserts);

        lvStarter = findViewById(R.id.lvStarter);
        lvDrinks = findViewById(R.id.lvDrinks);
        lvDesserts = findViewById(R.id.lvDesserts);

        listStarterData = new ArrayList<>();
        listDrinksData = new ArrayList<>();
        listDessertData = new ArrayList<>();

        finalList = new ArrayList<>();

        relativeLayoutStarter.setOnClickListener(this);
        relativeLayoutDrinks.setOnClickListener(this);
        relativeLayoutDesserts.setOnClickListener(this);

        btnOrder.setOnClickListener(this);

        Intent i = getIntent();
        mc = (ModelCompanies) i.getSerializableExtra("companies");

        Picasso.get().load(mc.getCompanyImageUrl()).into(imgCompanies);
        customTitle.setText(mc.getCompanyTitle());
        customDescription.setText(mc.getCompanyDetail());
        customStatus.setText(mc.getCompanyStatus());

        starterList = new ArrayList<>();
        drinkList = new ArrayList<>();
        dessertList = new ArrayList<>();

        LocalBroadcastManager.getInstance(this).registerReceiver(showFinalPrice, new IntentFilter("price_intent"));

        LocalBroadcastManager.getInstance(this).registerReceiver(showDrinkList, new IntentFilter("drink_intent"));
        LocalBroadcastManager.getInstance(this).registerReceiver(showStarterList, new IntentFilter("starter_intent"));
        LocalBroadcastManager.getInstance(this).registerReceiver(showDessertList, new IntentFilter("dessert_intent"));
        getMainMenu();
        //getSubMenu();
        //starter();
        //drinks();

        cartList = new ArrayList<ModelPreOrder>();

        getWindow().setStatusBarColor(getColor(R.color.colorPurpleGiven));

        ImageView imgCartExpandMenu = findViewById(R.id.imgCartExpandMenu);

        imgCartExpandMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (starterList.isEmpty() && drinkList.isEmpty()) {

                    intent = new Intent(ExpandMenuActivity.this, CartOrderActivity.class);
                    intent.putExtra("cartList", cartList);
                    startActivity(intent);

                } else {
                    cartList.addAll(starterList);
                    cartList.addAll(drinkList);

                    //Toast.makeText(ExpandMenuActivity.this, "Length: " + cartList.size(), Toast.LENGTH_SHORT).show();
                    intent = new Intent(ExpandMenuActivity.this, CartOrderActivity.class);
                    intent.putExtra("cartList", cartList);
                    intent.putExtra("companies", mc);
                    startActivity(intent);

                    cartList.clear();

                }
            }
        });

    }

    public static ExpandMenuActivity getInstance() {
        return expActivity;
    }

    public void getMainMenu() {

        Intent intent = getIntent();
        mc = (ModelCompanies) intent.getSerializableExtra("companies");

        dbReferenceMainMenu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot menuSnapshot : dataSnapshot.getChildren()) {

                    ModelMainMenu dataMenu = menuSnapshot.getValue(ModelMainMenu.class);

                    String companyTitle = dataMenu.getMmcompanyTitle();

                    if (companyTitle.equals(mc.getCompanyTitle())) {
                        //Toast.makeText(ExpandMenuActivity.this, "" + data.getMmName(), Toast.LENGTH_SHORT).show();

                        if (dataMenu.getMmName().equals("Starter")) {
                            //Toast.makeText(ExpandMenuActivity.this, "In the starter...", Toast.LENGTH_SHORT).show();
                            relativeLayoutStarter.setVisibility(View.VISIBLE);
                            showListStarter();

                        }
                        if (dataMenu.getMmName().equals("Drinks")) {
                            //Toast.makeText(ExpandMenuActivity.this, "In the drinks...", Toast.LENGTH_SHORT).show();
                            relativeLayoutDrinks.setVisibility(View.VISIBLE);
                            showListDrinks();
                        }
                        if (dataMenu.getMmName().equals("Desserts")) {
                            //Toast.makeText(ExpandMenuActivity.this, "In the Desserts...", Toast.LENGTH_SHORT).show();
                            relativeLayoutDesserts.setVisibility(View.VISIBLE);
                            showListDesserts();
                        }

                    }

                    //Toast.makeText(ExpandMenuActivity.this, "Out side IF: " + data.getMmName(), Toast.LENGTH_SHORT).show();
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
            finalPrice = price + finalPrice;
            orderText.setText(finalPrice + " $");
            //Toast.makeText(context, "FinalPrice: " + finalPrice, Toast.LENGTH_SHORT).show();
        }
    };

    public BroadcastReceiver showStarterList = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            starterList = (ArrayList<ModelPreOrder>) intent.getSerializableExtra("starter_list");
        }
    };

    public BroadcastReceiver showDrinkList = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            drinkList = (ArrayList<ModelPreOrder>) intent.getSerializableExtra("drink_list");
        }
    };
    public BroadcastReceiver showDessertList = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            dessertList = (ArrayList<ModelPreOrder>) intent.getSerializableExtra("dessert_list");
        }
    };

    public void showListStarter() {

        dbReferenceStarter.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot starterSnapshot : dataSnapshot.getChildren()) {
                    //ModelMenuStarter dataStarter = starterSnapshot.getValue(ModelMenuStarter.class);
                    //String name = dataStarter.getStartercompanyName();
                    ModelMenuStarter dataStarter = starterSnapshot.getValue(ModelMenuStarter.class);

                    String name = dataStarter.getStartercompanyName();

                    if (name.equals(mc.getCompanyTitle())) {
                        /*Toast.makeText(ExpandMenuActivity.this,
                                "Values: " + dataStarter.getStarterName() + ", " + dataStarter.getStarterDescription() + ", " + dataStarter.getStarterPrice(),
                                Toast.LENGTH_LONG).show();*/
                        listStarterData.add(dataStarter);

                        adapterStarter = new MenuStarterAdapter(ExpandMenuActivity.this, listStarterData);
                        lvStarter.setAdapter(adapterStarter);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void showListDrinks() {

        dbReferenceDrinks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot drinksSnapshot : dataSnapshot.getChildren()) {
                    ModelMenuDrinks drinksData = drinksSnapshot.getValue(ModelMenuDrinks.class);

                    String name = drinksData.getDrinkcompanyName();
                    if (name.equals(mc.getCompanyTitle())) {
                        listDrinksData.add(drinksData);

                        adapterDrinks = new MenuDrinksAdapter(ExpandMenuActivity.this, listDrinksData);
                        lvDrinks.setAdapter(adapterDrinks);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void showListDesserts() {

        dbReferenceDesserts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dessertSnapshot : dataSnapshot.getChildren()) {
                    ModelMenuDesserts modelMenuDesserts = dessertSnapshot.getValue(ModelMenuDesserts.class);

                    String name = modelMenuDesserts.getDessertcompanyName();
                    if (name.equals(mc.getCompanyTitle())) {

                        listDessertData.add(modelMenuDesserts);

                        adapterDesserts = new MenuDessertsAdapter(ExpandMenuActivity.this, listDessertData);
                        lvDesserts.setAdapter(adapterDesserts);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void starterList() {
        titlesExpand = new ArrayList<>();
        descriptionsExpand = new ArrayList<>();
        priceExpand = new ArrayList<>();

        titlesExpand.add("Chicken soup");
        titlesExpand.add("Chicken Chowmein");
        titlesExpand.add("Beef Noodles");

        descriptionsExpand.add("Served with all salad, sauce and bread");
        descriptionsExpand.add("Served with all salad, sauce and bread and coke");
        descriptionsExpand.add("Any thing here");

        priceExpand.add("16");
        priceExpand.add("10");
        priceExpand.add("9");


        /*final ListAdapter listAdapter = new MenuStarterAdapter(this, '0', titlesExpand, descriptionsExpand, priceExpand, orderText
                , btnOrder);*/
        lvStarter = (ListView) findViewById(R.id.lvStarter);
        //listStarter.setAdapter(listAdapter);

    }

    public void drinks() {

        titlesDrinks = new ArrayList<>();
        descriptionsDrinks = new ArrayList<>();
        priceDrinks = new ArrayList<>();

        titlesDrinks.add("Coke");
        titlesDrinks.add("Sprite");
        titlesDrinks.add("Dew");
        titlesDrinks.add("Fanta");

        descriptionsDrinks.add("1 Ltr.");
        descriptionsDrinks.add("2.5 Ltr.");
        descriptionsDrinks.add("1/2 Ltr.");
        descriptionsDrinks.add("1 Ltr.");

        priceDrinks.add("10");
        priceDrinks.add("9");
        priceDrinks.add("8");
        priceDrinks.add("11");

        /*final ListAdapter listAdapter1 = new MenuStarterAdapter(this, '0', titlesDrinks, descriptionsDrinks, priceDrinks, orderText
                , btnOrder);*/

        lvDrinks = (ListView) findViewById(R.id.lvDrinks);
        //listDrinks.setAdapter(listAdapter1);

    }

    public void popupForNumberLayout() {

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {

            AlertDialog.Builder builder = new AlertDialog.Builder(ExpandMenuActivity.this);
            builder.setTitle("Enter Following Field.");

            LayoutInflater inflater = ExpandMenuActivity.this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.row_final_number, null, false);
            builder.setView(dialogView);

            final EditText etPhoneNumber = dialogView.findViewById(R.id.etNumber);

            builder.setPositiveButton("OK", null);
            builder.setNegativeButton("Cancel", null);
            //builder.setNeutralButton("Center", null);

            final AlertDialog alertDialog = builder.create();
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();

            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final String number = etPhoneNumber.getText().toString();

                    if (TextUtils.isEmpty(number)) {
                        etPhoneNumber.setError("Field required");

                    } else {
                        //save data in Firebase DB

                        //saving number here if already not exist
                        dbReferenceUser.addListenerForSingleValueEvent(new ValueEventListener() {
                            int i = 0;

                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                    ModelUser modelUser = userSnapshot.getValue(ModelUser.class);
                                    i++;

                                    String userNumber = modelUser.getUSER_PHONE();
                                    if (userNumber.equals(number)) {
                                        break;
                                    }

                                    if (i == dataSnapshot.getChildrenCount()) {
                                        String id = dbReferenceUser.push().getKey();
                                        ModelUser modelUser1 = new ModelUser(id, "", number,
                                                "", "", "normal");
                                        dbReferenceUser.child(id).setValue(modelUser1);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                        Intent intent = new Intent(ExpandMenuActivity.this, ConfirmOrderActivity.class);
                        intent.putExtra("final_list", finalList);
                        intent.putExtra("companies1", mc);
                        intent.putExtra("number", number);
                        startActivity(intent);
                        finish();

                    }

                }
            });

            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Cancel the dialog and return to screen
                    alertDialog.cancel();
                }
            });

        } else {

            finish();
            Intent intent = new Intent(ExpandMenuActivity.this, ConfirmOrderActivity.class);
            intent.putExtra("final_list", finalList);
            intent.putExtra("companies1", mc);
            //intent.putExtra("number", number);
            startActivity(intent);
        }

    }

    @Override
    public void onClick(View view) {

        long id = view.getId();

        if (id == R.id.relativeLayoutStarter && lvStarter.getVisibility() == View.GONE) {
            //textViewPlusDrinksNoodle.setText("+");
            //relativeLayoutDrinks.animate().translationY(250);

            textViewPlusStarterNoodle.setText("");
            relativeLayoutStarter.setBackgroundColor(getColor(R.color.colorWhite));
            lvStarter.setVisibility(View.VISIBLE);

        } else if (id == R.id.relativeLayoutStarter && lvStarter.getVisibility() == View.VISIBLE) {
            textViewPlusStarterNoodle.setText("+");
            //relativeLayoutDrinks.animate().translationY(0);
            relativeLayoutStarter.setBackground(getDrawable(R.drawable.img_background_menu_main));
            lvStarter.setVisibility(View.GONE);
        }

        if (id == R.id.relativeLayoutDrinks && lvDrinks.getVisibility() == View.GONE) {
            lvDrinks.setVisibility(View.VISIBLE);
            textViewPlusDrinksNoodle.setText("");
            relativeLayoutDrinks.setBackgroundColor(getColor(R.color.colorWhite));

            //relativeLayoutStarter.animate().translationY(0);
            //textViewPlusStarterNoodle.setText("+");
            //listStarter.setVisibility(View.GONE);
        } else if (id == R.id.relativeLayoutDrinks && lvDrinks.getVisibility() == View.VISIBLE) {

            lvDrinks.setVisibility(View.GONE);
            textViewPlusDrinksNoodle.setText("+");
            relativeLayoutDrinks.setBackground(getDrawable(R.drawable.img_background_menu_main));
        }

        if (id == R.id.relativeLayoutDesserts && lvDesserts.getVisibility() == View.GONE) {
            lvDesserts.setVisibility(View.VISIBLE);
            textViewPlusDesserts.setText("");
            relativeLayoutDesserts.setBackgroundColor(getColor(R.color.colorWhite));


        } else if (id == R.id.relativeLayoutDesserts && lvDesserts.getVisibility() == View.VISIBLE) {
            lvDesserts.setVisibility(View.GONE);
            textViewPlusDesserts.setText("+");
            relativeLayoutDesserts.setBackground(getDrawable(R.drawable.img_background_menu_main));

        }


        if (id == R.id.btnOrder) {
            //Toast.makeText(ExpandMenuActivity.this, "Hi Starter:" + starterList + "TTTTTTTTT" + drinkList, Toast.LENGTH_SHORT).show();

            if (starterList.isEmpty() && drinkList.isEmpty()) {
                Toast.makeText(this, "Some data is required", Toast.LENGTH_SHORT).show();

            } else {
                finalList.addAll(starterList);
                finalList.addAll(drinkList);

                popupForNumberLayout();
            }
        }

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
        //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        //startActivity(new Intent(ExpandMenuActivity.this, CompaniesActivity.class));

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
