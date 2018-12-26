package com.example.user.practicalwork2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.practicalwork2.Adapters.CartOrderAdapter;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CartOrderActivity extends AppCompatActivity implements View.OnClickListener {

    static CartOrderActivity cartOrderActivity;
    public TextView tvEmptyCart;
    Button btnCheckOut;
    public ListView lvCartOrder;
    EditText etEnterCode;

    public RelativeLayout rlAll;

    ModelCompanies mc1;

    int[] quantityArray;
    String[] nameArray;
    int[] priceArray;

    //two list passed from different activities
    private ArrayList<ModelPreOrder> cartList1;
    private ArrayList<ModelFinalOrder> cartList2;
    private ArrayList<ModelFinalOrder> finalOrdersList;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbReferenceUser;
    DatabaseReference dbReferenceOrder;

    ModelUser modelUser;
    private String userName = "userName";
    private String userEmail = "userEmail";
    private String userNumber = "userNumber";
    String currentUserEmail = "currentUserEmail";
    String orderDate;
    String orderTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_order);

        cartOrderActivity = this;

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

        tvEmptyCart = findViewById(R.id.tvEmptyCart);
        lvCartOrder = findViewById(R.id.lvCartOrder);
        btnCheckOut = findViewById(R.id.btnCheckOut);
        //etEnterCode = findViewById(R.id.etEnterCode);

        rlAll = findViewById(R.id.rlAll);

        cartList1 = new ArrayList<>();
        finalOrdersList = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        dbReferenceUser = firebaseDatabase.getReference("USERS");
        dbReferenceOrder = firebaseDatabase.getReference("ORDER");

        Intent intent = getIntent();
        //two lists received form different activities either from ExpandMenuActivity(cartList) or ConfirmOrderActivity(cartList1)
        cartList1 = (ArrayList<ModelPreOrder>) intent.getSerializableExtra("cartList");
        cartList2 = (ArrayList<ModelFinalOrder>) intent.getSerializableExtra("cartList2");
        mc1 = (ModelCompanies) intent.getSerializableExtra("companies");

        if (!cartList1.isEmpty()) {
            quantityArray = new int[cartList1.size()];
            nameArray = new String[cartList1.size()];
            priceArray = new int[cartList1.size()];
        }

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("k:mm:ss");
        Date date = new Date();

        orderDate = dateFormat.format(date);
        orderTime = timeFormat.format(date);

        btnCheckOut.setOnClickListener(this);

        retrieveUserData();
        //showCartListData();
    }

    public static CartOrderActivity getInstance() {
        return cartOrderActivity;
    }

    private void retrieveUserData() {

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
                            showCartListData();
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
                            showCartListData();
                            break;
                        }
                        if (i == dataSnapshot.getChildrenCount()) {
                            userName = modelUser.getUSER_NAME();
                            userEmail = modelUser.getUSER_mEMAIL();
                            userNumber = modelUser.getUSER_PHONE();
                            showCartListData();
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showCartListData() {

        if (cartList1.isEmpty()) {
            tvEmptyCart.setVisibility(View.VISIBLE);

        } else {

            rlAll.setVisibility(View.VISIBLE);
            //btnCheckOut.setVisibility(View.VISIBLE);
            showOrderListData();
        }

    }

    public void showOrderListData() {

        ProgressDialog progressDialog = new ProgressDialog(CartOrderActivity.this);

        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        int finalPrice = 0;
        int orderPrice = 0;
        int quantity = 0;

        for (int i = 0; i < cartList1.size(); i++) {
            finalPrice = finalPrice + cartList1.get(i).getPrice();
            //textViewShowOrder.setText("$ " + finalPrice);
        }

        ArrayList<ModelPreOrder> temp1 = cartList1;
        ArrayList<ModelPreOrder> temp2 = cartList1;

        for (int i = 0; i < cartList1.size(); i++) {

            String name1 = temp1.get(i).getTitle();
            orderPrice = temp1.get(i).getPrice();
            quantity = 1;

            for (int j = i + 1; j < cartList1.size(); j = j) {
                String name2 = temp2.get(j).getTitle();

                if (name1.equals(name2)) {
                    quantity++;
                    orderPrice = orderPrice + temp2.get(j).getPrice();

                    cartList1.remove(j);

                } else if (!name1.equals(name2)) {
                    j++;
                }
            }

            quantityArray[i] = quantity;
            nameArray[i] = name1;
            priceArray[i] = orderPrice;
        }

        for (int k = 0; k < cartList1.size(); k++) {
            finalOrdersList.add(new ModelFinalOrder(quantityArray[k], nameArray[k], priceArray[k],
                    orderDate, orderTime, mc1.getCompanyTitle(), mc1.getCompanyImageUrl(), userName, userEmail, userNumber));
            //Toast.makeText(this, "At Index: " + k + " " + " Quantity: " + quantityArray[k] + " Name: " + nameArray[k] + " Price: " + priceArray[k], Toast.LENGTH_LONG).show();
        }

        ListAdapter listAdapter = new CartOrderAdapter(this, finalOrdersList);
        lvCartOrder.setAdapter(listAdapter);

        progressDialog.dismiss();
    }

    public void checkOutOrder() {
        /*final ProgressDialog progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Saving...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();*/

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure to confirm your order!");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for (int k = 0; k < cartList1.size(); k++) {
                    //Toast.makeText(ConfirmOrderActivity.this, "At Index: " + k + " " + " Quantity: " + quantityArray[k] + " Name: " + nameArray[k] + " Price: " + priceArray[k], Toast.LENGTH_LONG).show();
                    String id = dbReferenceOrder.push().getKey();
                    //ModelFinalOrder modelFinalOrdera = new ModelFinalOrder(finalOrdersList.get(k).getOrderQuantity(), finalOrdersList.get(k).getOrderName(), finalOrdersList.get(k).getOrderPrice(), userName);
                    ModelFinalOrder modelFinalOrder = new ModelFinalOrder(quantityArray[k], nameArray[k], priceArray[k],
                            orderDate, orderTime, mc1.getCompanyTitle(), mc1.getCompanyImageUrl(), userName, userEmail, userNumber);

                    dbReferenceOrder.child(id).setValue(modelFinalOrder);

                }
                //progressDialog.dismiss();

                finish();
                FancyToast.makeText(CartOrderActivity.this, "Order Is Done.", Toast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                //Toast.makeText(ConfirmOrderActivity.this, "Order is done...", Toast.LENGTH_LONG).show();
                startActivity(new Intent(CartOrderActivity.this, CompaniesActivity.class));

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btnCheckOut) {
            checkOutOrder();
        }

    }
}
