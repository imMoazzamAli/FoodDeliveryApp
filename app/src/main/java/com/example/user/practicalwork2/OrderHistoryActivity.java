package com.example.user.practicalwork2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.user.practicalwork2.Adapters.OrderHistoryAdapter;
import com.example.user.practicalwork2.Models.ModelFinalOrder;
import com.example.user.practicalwork2.Models.ModelUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class OrderHistoryActivity extends AppCompatActivity {

    TextView txtHistoryDate, txtHistoryTime, txtHistoryQuantity, txtHistoryName, txtHistoryPrice, txtHistoryTotal;
    ListView lvOrderHistory;

    ArrayAdapter adapter;

    ModelUser modelUser;
    ModelFinalOrder mfo;

    ArrayList<ModelFinalOrder> modelFinalOrders;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbReferenceOrder;
    DatabaseReference dbReferenceUser;

    String userName, userEmail, currentUserEmail;
    int[] priceArray;
    int[] finalPriceArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        /*LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = layoutInflater.inflate(R.layout.activity_order_history, null, false);
        drawer.addView(convertView);
*/
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

        lvOrderHistory = findViewById(R.id.lvOrderHistory);

        modelFinalOrders = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        dbReferenceOrder = firebaseDatabase.getReference("ORDER");
        dbReferenceUser = firebaseDatabase.getReference("USERS");

        //Query query= dbReferenceOrder.orderByChild("orderUserName");

        getUser();
        onItemClick();

    }

    public void getUser() {
        dbReferenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot userData : dataSnapshot.getChildren()) {
                    modelUser = userData.getValue(ModelUser.class);

                    userEmail = modelUser.getUSER_mEMAIL();
                    currentUserEmail = firebaseAuth.getCurrentUser().getEmail();

                    if (userEmail.equals(currentUserEmail)) {
                        userName = modelUser.getUSER_NAME();
                        //Toast.makeText(OrderHistoryActivity.this, "" + userName, Toast.LENGTH_SHORT).show();

                        ProgressDialog progressDialog= new ProgressDialog(OrderHistoryActivity.this);
                        progressDialog.setMessage("Please Wait");
                        progressDialog.setCancelable(false);
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();

                        getAllOrders();

                        progressDialog.dismiss();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getAllOrders() {
        dbReferenceOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot orderData : dataSnapshot.getChildren()) {

                    ModelFinalOrder modelFinalOrder = orderData.getValue(ModelFinalOrder.class);

                    //adding only those values whose user is logged in which is compared in function "getUser()"
                    if (userName.equals(modelFinalOrder.getOrderUserName())) {
                        modelFinalOrders.add(modelFinalOrder);

                    }
                }

                //removing the repeated items  and showing only final prices here
                ArrayList<ModelFinalOrder> temp1 = modelFinalOrders;
                ArrayList<ModelFinalOrder> temp2 = modelFinalOrders;
                priceArray = new int[modelFinalOrders.size()];

                for (int i = 0; i < modelFinalOrders.size(); i++) {

                    String date1 = temp1.get(i).getOrderDate();
                    String time1 = temp1.get(i).getOrderTime();

                    priceArray[i] = temp1.get(i).getOrderPrice();

                    for (int j = i + 1; j < modelFinalOrders.size(); j = j) {
                        String date2 = temp2.get(j).getOrderDate();
                        String time2 = temp2.get(j).getOrderTime();

                        //index j will not change if values are same i.e. j=j
                        if (date1.equals(date2) && time1.equals(time2)) {
                            priceArray[i] = priceArray[i] + temp2.get(j).getOrderPrice();
                            modelFinalOrders.remove(j);
                        }
                        //update index j if values not equal i.e. j++
                        else if (!date1.equals(date2) || !time1.equals(time2)) {
                            j++;
                        }
                    }
                }


                Collections.reverse(Arrays.asList(priceArray));

                int j = 0;
                finalPriceArray = new int[priceArray.length];

                for (int i = modelFinalOrders.size() - 1; i >= 0; i--) {

                    finalPriceArray[j] = priceArray[i];
                    //Toast.makeText(OrderHistoryActivity.this, "index: " + j + " value:" + finalPriceArray[j], Toast.LENGTH_SHORT).show();
                    j++;
                }
                //pass the whole array which contains prices for each removed items
                adapter = new OrderHistoryAdapter(OrderHistoryActivity.this, modelFinalOrders, finalPriceArray);
                lvOrderHistory.setAdapter(adapter);

                sortList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void sortList() {
        Collections.sort(modelFinalOrders, new Comparator<ModelFinalOrder>() {
            @Override
            public int compare(ModelFinalOrder obj1, ModelFinalOrder obj2) {

                //first sorting w.r.t. date and then w.r.t time after if statement
                //comparing 2nd object with 1st for descending order list
                int date = obj2.getOrderDate().compareTo(obj1.getOrderDate());
                if (date != 0) {
                    return date;
                }
                //sorting w.r.t time after checking date in if statement
                //comparing 2nd object with 1st for descending order list
                return obj2.getOrderTime().compareTo(obj1.getOrderTime());
            }

        });
        //notify adapter for changed data
        adapter.notifyDataSetChanged();
    }

    public void onItemClick() {
        lvOrderHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                mfo = (ModelFinalOrder) adapterView.getItemAtPosition(position);

                Intent intent = new Intent(OrderHistoryActivity.this, OrderHistoryMoreActivity.class);
                intent.putExtra("short_order", mfo);
                intent.putExtra("final_price", finalPriceArray[position]);
                startActivity(intent);

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
