package com.example.user.practicalwork2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.practicalwork2.Adapters.ConfirmOrderAdapter;
import com.example.user.practicalwork2.Models.ModelCompanies;
import com.example.user.practicalwork2.Models.ModelFinalOrder;
import com.example.user.practicalwork2.Models.ModelPreOrder;
import com.example.user.practicalwork2.Models.ModelUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ConfirmOrderActivity extends AppCompatActivity implements View.OnClickListener {

    private int finalPrice = 0;
    private int quantity = 1;

    TextView textViewShowOrder;
    TextView textViewFinalQuantity;

    Button btnOrderCreditCard, btnOrderPayPal, btnOrderCash;

    ImageView imgCompanies;
    TextView txtConfirmTitle;

    ArrayList<String> FinalQuantity;
    ArrayList<String> FinalTitle;
    ArrayList<String> FinalPrice;

    ArrayList<String> testPrice;

    ModelCompanies mc1;

    public ArrayList<ModelPreOrder> preOrderList;

    int orderPrice = 0;
    public ArrayList<ModelFinalOrder> finalOrdersList;

    ListView listFinalOrder;

    int quantityArray[];
    String nameArray[];
    int priceArray[];

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference dbReferenceOrder;
    DatabaseReference dbReferenceUser;

    ModelUser modelUser;
    private String userName = "userName";
    private String userEmail = "userEmail";
    private String userNumber = "userNumber";
    String currentUserEmail = "currentUserEmail";
    String orderDate;
    String orderTime;

    public Boolean statusConfirmActivity = false;

    static ConfirmOrderActivity confirmActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

/*
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_confirm_order, null, false);
        drawer.addView(contentView, 0);
        //drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
*/

        confirmActivity = this;

        getWindow().setStatusBarColor(getColor(R.color.colorPurpleGiven));

        Toolbar toolbar = findViewById(R.id.toolbarConfirmActivity);
        setSupportActionBar(toolbar);

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

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        dbReferenceOrder = firebaseDatabase.getReference("ORDER");
        dbReferenceUser = firebaseDatabase.getReference("USERS");

        textViewShowOrder = (TextView) findViewById(R.id.txtShowOrder);
        textViewFinalQuantity = findViewById(R.id.txtFinalQuantity);

        btnOrderCreditCard = findViewById(R.id.btnOrderCreditCard);
        btnOrderPayPal = findViewById(R.id.btnOrderPayPal);
        btnOrderCash = findViewById(R.id.btnOrderCash);

        imgCompanies = findViewById(R.id.imgConfirmCompanies);
        txtConfirmTitle = findViewById(R.id.txtConfirmTitle);

        listFinalOrder = (ListView) findViewById(R.id.listFinalOrder);

        preOrderList = new ArrayList<>();
        finalOrdersList = new ArrayList<>();

        final Intent intent = getIntent();
        preOrderList = (ArrayList<ModelPreOrder>) intent.getSerializableExtra("final_list");
        mc1 = (ModelCompanies) intent.getSerializableExtra("companies1");
        userNumber = intent.getStringExtra("number");

        Picasso.get().load(mc1.getCompanyImageUrl()).into(imgCompanies);
        txtConfirmTitle.setText(mc1.getCompanyTitle());

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("k:mm:ss");

        Date date = new Date();

        orderDate = dateFormat.format(date);
        orderTime = timeFormat.format(date);

        //Toast.makeText(this, "" + orderDate + "hello: " + orderTime, Toast.LENGTH_LONG).show();

        quantityArray = new int[preOrderList.size()];
        nameArray = new String[preOrderList.size()];
        priceArray = new int[preOrderList.size()];

        btnOrderCash.setOnClickListener(this);

        ImageView imgCartConfirmActivity = findViewById(R.id.imgCartConfirmActivity);
        imgCartConfirmActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent;
                if (finalOrdersList.isEmpty()) {
                    intent = new Intent(ConfirmOrderActivity.this, CartOrderActivity.class);
                    startActivity(intent);
                } else {

                    //Toast.makeText(ExpandMenuActivity.this, "Length: " + cartList.size(), Toast.LENGTH_SHORT).show();
                    intent = new Intent(ConfirmOrderActivity.this, CartOrderActivity.class);
                    intent.putExtra("cartList2", finalOrdersList);
                    statusConfirmActivity = true;
                    intent.putExtra("companies", mc1);
                    startActivity(intent);

                }
            }
        });

        dbReferenceUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    currentUserEmail = firebaseAuth.getCurrentUser().getEmail();
                    for (DataSnapshot userData : dataSnapshot.getChildren()) {
                        modelUser = userData.getValue(ModelUser.class);

                        String mUserEmail = modelUser.getUSER_mEMAIL();
                        //userEmail = modelUser.getUSER_mEMAIL();

                        if (mUserEmail.equals(currentUserEmail)) {
                            userName = modelUser.getUSER_NAME();
                            userEmail = modelUser.getUSER_mEMAIL();
                            userNumber = modelUser.getUSER_PHONE();
                            showOrderListData();
                        }
                    }

                } else {
                    int i = 0;
                    for (DataSnapshot userSnashot : dataSnapshot.getChildren()) {
                        i++;
                        modelUser = userSnashot.getValue(ModelUser.class);
                        String mUserNumber = modelUser.getUSER_PHONE();

                        if (userNumber.equals(mUserNumber)) {
                            userName = modelUser.getUSER_NAME();
                            userEmail = modelUser.getUSER_mEMAIL();
                            userNumber = modelUser.getUSER_PHONE();
                            showOrderListData();
                            break;
                        }
                        if (i == dataSnapshot.getChildrenCount()) {
                            userName = modelUser.getUSER_NAME();
                            userEmail = modelUser.getUSER_mEMAIL();
                            userNumber = modelUser.getUSER_PHONE();
                            showOrderListData();
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public static ConfirmOrderActivity getInstance() {
        return confirmActivity;
    }

    public void showOrderListData() {

        ProgressDialog progressDialog = new ProgressDialog(ConfirmOrderActivity.this);

        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        for (int i = 0; i < preOrderList.size(); i++) {
            finalPrice = finalPrice + preOrderList.get(i).getPrice();
            textViewShowOrder.setText("$ " + finalPrice);
        }

        ArrayList<ModelPreOrder> temp1 = preOrderList;
        ArrayList<ModelPreOrder> temp2 = preOrderList;

        for (int i = 0; i < preOrderList.size(); i++) {

            String name1 = temp1.get(i).getTitle();
            orderPrice = temp1.get(i).getPrice();
            quantity = 1;

            for (int j = i + 1; j < preOrderList.size(); j = j) {
                String name2 = temp2.get(j).getTitle();

                if (name1.equals(name2)) {
                    quantity++;
                    orderPrice = orderPrice + temp2.get(j).getPrice();

                    preOrderList.remove(j);

                } else if (!name1.equals(name2)) {
                    j++;
                }
            }
            quantityArray[i] = quantity;
            nameArray[i] = name1;
            priceArray[i] = orderPrice;
        }

        for (int k = 0; k < preOrderList.size(); k++) {
            finalOrdersList.add(new ModelFinalOrder(quantityArray[k], nameArray[k], priceArray[k],
                    orderDate, orderTime, mc1.getCompanyTitle(), mc1.getCompanyImageUrl(), userName, userEmail, userNumber));
            //Toast.makeText(this, "At Index: " + k + " " + " Quantity: " + quantityArray[k] + " Name: " + nameArray[k] + " Price: " + priceArray[k], Toast.LENGTH_LONG).show();
        }

        ListAdapter listAdapter = new ConfirmOrderAdapter(this, finalOrdersList);
        listFinalOrder = (ListView) findViewById(R.id.listFinalOrder);
        listFinalOrder.setAdapter(listAdapter);

        progressDialog.dismiss();

    }

    public void saveOrderData() {
/*
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
*/

        AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmOrderActivity.this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure to confirm your order!");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                for (int k = 0; k < preOrderList.size(); k++) {
                    //Toast.makeText(ConfirmOrderActivity.this, "At Index: " + k + " " + " Quantity: " + quantityArray[k] + " Name: " + nameArray[k] + " Price: " + priceArray[k], Toast.LENGTH_LONG).show();
                    String id = dbReferenceOrder.push().getKey();
//          ModelFinalOrder modelFinalOrdera = new ModelFinalOrder(finalOrdersList.get(k).getOrderQuantity(), finalOrdersList.get(k).getOrderName(), finalOrdersList.get(k).getOrderPrice(), userName);
                    ModelFinalOrder modelFinalOrder = new ModelFinalOrder(quantityArray[k], nameArray[k], priceArray[k],
                            orderDate, orderTime, mc1.getCompanyTitle(), mc1.getCompanyImageUrl(), userName, userEmail, userNumber);

                    dbReferenceOrder.child(id).setValue(modelFinalOrder);

                }
                // progressDialog.dismiss();

                finish();
                FancyToast.makeText(ConfirmOrderActivity.this, "Order Is Done.", Toast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                //Toast.makeText(ConfirmOrderActivity.this, "Order is done...", Toast.LENGTH_LONG).show();
                startActivity(new Intent(ConfirmOrderActivity.this, CompaniesActivity.class));
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {

        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setMessage("Are you sure to move to main menu?");

        b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
                //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                //startActivity(new Intent(ConfirmOrderActivity.this, CompaniesActivity.class));
            }
        });

        b.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alertDialog = b.create();
        alertDialog.show();

        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        finish();
        //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        //startActivity(new Intent(ConfirmOrderActivity.this, CompaniesActivity.class));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btnOrderCash) {
            saveOrderData();
        }

    }
}
