package com.example.user.practicalwork2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.practicalwork2.Adapters.OrderHistoryMoreAdapter;
import com.example.user.practicalwork2.Models.ModelFinalOrder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrderHistoryMoreActivity extends AppCompatActivity {

    TextView txtHistoryMoreTitle, txtHistoryMoreOrderPrice;
    ImageView imgHistoryMore;

    ListView lvHistoryMore;

    ModelFinalOrder mfo;
    ArrayList<ModelFinalOrder> modelFinalOrders;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbReferenceOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_more);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(getLayoutInflater().inflate(R.layout.tool_bar, null),
                new ActionBar.LayoutParams(
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.MATCH_PARENT,
                        Gravity.CENTER));

        actionBar.setDisplayHomeAsUpEnabled(true);

        txtHistoryMoreTitle = findViewById(R.id.txtHistoryMoreTitle);
        txtHistoryMoreOrderPrice = findViewById(R.id.txtHistoryMoreOrderPrice);
        imgHistoryMore = findViewById(R.id.imgHistoryMore);

        lvHistoryMore = findViewById(R.id.lvHistoryMore);

        firebaseDatabase = FirebaseDatabase.getInstance();
        dbReferenceOrder = firebaseDatabase.getReference("ORDER");

        modelFinalOrders = new ArrayList<>();

        Intent intent = getIntent();
        mfo = (ModelFinalOrder) intent.getSerializableExtra("short_order");

        int totalPrice = intent.getIntExtra("final_price", 0);

        Picasso.get().load(mfo.getOrderCompanyUrl()).into(imgHistoryMore);
        txtHistoryMoreTitle.setText(mfo.getOrderCompanyName());

        txtHistoryMoreOrderPrice.setText("$ " + totalPrice);

        showListData();

    }

    public void showListData() {

        dbReferenceOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot orderData : dataSnapshot.getChildren()) {

                    ModelFinalOrder modelFinalOrder = orderData.getValue(ModelFinalOrder.class);

                    String datePrevious = mfo.getOrderDate();
                    String timePrevious = mfo.getOrderTime();

                    String dateCurrent= modelFinalOrder.getOrderDate();
                    String timeCurrent= modelFinalOrder.getOrderTime();

                    if (datePrevious.equals(dateCurrent) && timePrevious.equals(timeCurrent)) {

                        modelFinalOrders.add(modelFinalOrder);
                    } else {
                        //Toast.makeText(OrderHistoryMoreActivity.this, "Not matched", Toast.LENGTH_SHORT).show();
                    }
                }

                ArrayAdapter adapter = new OrderHistoryMoreAdapter(OrderHistoryMoreActivity.this, modelFinalOrders);
                lvHistoryMore.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
